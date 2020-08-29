from __future__ import print_function

import logging
import os
import sys

import click
from brainzutils import cache, ratelimit
from flask import current_app
from flask.cli import FlaskGroup

import db
import db.data
import db.dump
import db.dump_manage
import db.exceptions
import db.stats
import db.user
import webserver

ADMIN_SQL_DIR = os.path.join(os.path.dirname(os.path.realpath(__file__)), 'admin', 'sql')

cli = FlaskGroup(add_default_commands=False, create_app=webserver.create_app_flaskgroup)

logging.basicConfig(level=logging.INFO)


@cli.command(name='init_db')
@click.option("--force", "-f", is_flag=True, help="Drop existing database and user.")
@click.argument("archive", type=click.Path(exists=True), required=False)
@click.option("--skip-create-db", "-s", is_flag=True, help="Skip database creation step.")
def init_db(archive, force, skip_create_db=False):
    """Initialize database and import data.

    This process involves several steps:
    1. Table structure is created.
    2. Data is imported from the archive if it is specified.
    3. Primary keys and foreign keys are created.
    4. Indexes are created.

    Data dump needs to be a .tar.xz archive produced by export command.

    More information about populating a PostgreSQL database efficiently can be
    found at http://www.postgresql.org/docs/current/static/populate.html.
    """

    db.init_db_engine(current_app.config['POSTGRES_ADMIN_URI'])
    if force:
        res = db.run_sql_script_without_transaction(os.path.join(ADMIN_SQL_DIR, 'drop_db.sql'))
        if not res:
            raise Exception('Failed to drop existing database and user! Exit code: %i' % res)

    if not skip_create_db:
        current_app.logger.info('Creating user and a database...')
        res = db.run_sql_script_without_transaction(os.path.join(ADMIN_SQL_DIR, 'create_db.sql'))
        if not res:
            raise Exception('Failed to create new database and user! Exit code: %i' % res)

    current_app.logger.info('Creating database extensions...')
    db.init_db_engine(current_app.config['POSTGRES_ADMIN_AB_URI'])
    res = db.run_sql_script_without_transaction(os.path.join(ADMIN_SQL_DIR, 'create_extensions.sql'))

    db.init_db_engine(current_app.config['SQLALCHEMY_DATABASE_URI'])

    current_app.logger.info('Creating types...')
    db.run_sql_script(os.path.join(ADMIN_SQL_DIR, 'create_types.sql'))

    current_app.logger.info('Creating tables...')
    db.run_sql_script(os.path.join(ADMIN_SQL_DIR, 'create_tables.sql'))

    if archive:
        current_app.logger.info('Importing data...')
        db.dump.import_dump(archive)
    else:
        current_app.logger.info('Skipping data importing.')
        current_app.logger.info('Loading fixtures...')
        current_app.logger.info('Models...')
        db.run_sql_script(os.path.join(ADMIN_SQL_DIR, 'create_models.sql'))

    current_app.logger.info('Creating primary and foreign keys...')
    db.run_sql_script(os.path.join(ADMIN_SQL_DIR, 'create_primary_keys.sql'))
    db.run_sql_script(os.path.join(ADMIN_SQL_DIR, 'create_foreign_keys.sql'))

    current_app.logger.info('Creating indexes...')
    db.run_sql_script(os.path.join(ADMIN_SQL_DIR, 'create_indexes.sql'))

    current_app.logger.info("Done!")


@cli.command(name='import_data')
@click.option("--drop-constraints", "-d", is_flag=True, help="Drop primary and foreign keys before importing.")
@click.argument("archive", type=click.Path(exists=True))
def import_data(archive, drop_constraints=False):
    """Imports data dump into the database."""
    if drop_constraints:
        current_app.logger.info('Dropping primary key and foreign key constraints...')
        db.run_sql_script(os.path.join(ADMIN_SQL_DIR, 'drop_foreign_keys.sql'))
        db.run_sql_script(os.path.join(ADMIN_SQL_DIR, 'drop_primary_keys.sql'))

    current_app.logger.info('Importing data...')
    db.dump.import_dump(archive)
    current_app.logger.info('Done!')

    if drop_constraints:
        current_app.logger.info('Creating primary key and foreign key constraints...')
        db.run_sql_script(os.path.join(ADMIN_SQL_DIR, 'create_primary_keys.sql'))
        db.run_sql_script(os.path.join(ADMIN_SQL_DIR, 'create_foreign_keys.sql'))


@cli.command(name='import_dataset_data')
@click.option("--drop-constraints", "-d", is_flag=True, help="Drop primary and foreign keys before importing.")
@click.argument("archive", type=click.Path(exists=True))
def import_dataset_data(archive, drop_constraints=False):
    """Imports dataset dump into the database."""

    if drop_constraints:
        current_app.logger.info('Dropping primary key and foreign key constraints...')
        db.run_sql_script(os.path.join(ADMIN_SQL_DIR, 'drop_foreign_keys.sql'))
        db.run_sql_script(os.path.join(ADMIN_SQL_DIR, 'drop_primary_keys.sql'))

    current_app.logger.info('Importing dataset data...')
    db.dump.import_datasets_dump(archive)
    current_app.logger.info('Done!')

    if drop_constraints:
        current_app.logger.info('Creating primary key and foreign key constraints...')
        db.run_sql_script(os.path.join(ADMIN_SQL_DIR, 'create_primary_keys.sql'))
        db.run_sql_script(os.path.join(ADMIN_SQL_DIR, 'create_foreign_keys.sql'))


@cli.command(name='compute_stats')
def compute_stats():
    """Compute outstanding hourly statistics."""
    import datetime
    import pytz
    db.stats.compute_stats(datetime.datetime.now(pytz.utc))


@cli.command(name='cache_stats')
def cache_stats():
    """Compute recent stats and add to cache."""
    db.stats.add_stats_to_cache()


@cli.command(name='clear_cache')
def clear_cache():
    """Clear the cache."""
    cache.flush_all()


@cli.command(name='add_admin')
@click.argument("username")
@click.option("--force", "-f", is_flag=True, help="Create user if doesn't exist.")
def add_admin(username, force=False):
    """Make user an admin."""
    try:
        db.user.set_admin(username, admin=True, force=force)
        click.echo("Made %s an admin." % username)
    except db.exceptions.DatabaseException as e:
        click.echo("Error: %s" % e, err=True)
        sys.exit(1)


@cli.command(name='remove_admin')
@click.argument("username")
def remove_admin(username):
    """Remove admin privileges from a user."""
    try:
        db.user.set_admin(username, admin=False)
        click.echo("Removed admin privileges from %s." % username)
    except db.exceptions.DatabaseException as e:
        click.echo("Error: %s" % e, err=True)
        sys.exit(1)


@cli.command(name='update_sequences')
def update_sequences():
    current_app.logger.info('Updating database sequences...')
    db.dump.update_sequences()
    current_app.logger.info('Done!')


@cli.group()
@click.pass_context
def highlevel(ctx):
    """Analyse highlevel results"""
    pass


@highlevel.command(name="list_failed_rows")
@click.option("--verbose", "-v", is_flag=True, help="Lists failed highlevel rows.")
def list_failed_rows(verbose):
    """ Displays the number of rows which do not contain highlevel metadata

    When run with -v, also output rowid, mbid, submission offset of each failed submission
    """

    try:
        rows = db.data.get_failed_highlevel_submissions()
        num_failed_rows = len(rows)
        click.echo("Number of highlevel rows that failed processing: %s" % num_failed_rows)
        if verbose:
            click.echo("rowid,mbid,submission_offset")
            for row in rows:
                click.echo("%s,%s,%s" % (row["id"], row["gid"], row["submission_offset"]))

    except db.exceptions.DatabaseException as e:
        click.echo("Error: %s" % e, err=True)
        sys.exit(1)


@highlevel.command(name="remove_failed_rows")
def remove_failed_rows():
    """ Deletes highlevel rows which do not have highlevel metadata"""
    try:
        click.echo("removing failed highlevel rows...")
        db.data.remove_failed_highlevel_submissions()
        click.echo("done")
    except db.exceptions.DatabaseException as e:
        click.echo("Error: %s" % e, err=True)
        sys.exit(1)


@cli.command(name='set_rate_limits')
@click.argument('per_ip', type=click.IntRange(1, None), required=False)
@click.argument('window_size', type=click.IntRange(1, None), required=False)
def set_rate_limits(per_ip, window_size):
    """Set rate limit parameters for the AcousticBrainz webserver. If no arguments
    are provided, print the current limits. To set limits, specify PER_IP and WINDOW_SIZE

    \b
    PER_IP: the number of requests allowed per IP address
    WINDOW_SIZE: the window in number of seconds for how long the limit is applied
    """

    current_limit_per_ip = cache.get(ratelimit.ratelimit_per_ip_key)
    current_limit_window = cache.get(ratelimit.ratelimit_window_key)

    current_app.logger.info("Current values:")
    if current_limit_per_ip is None and current_limit_window is None:
        current_app.logger.info("No values set, showing limit defaults")
        current_limit_per_ip = ratelimit.ratelimit_per_ip_default
        current_limit_window = ratelimit.ratelimit_window_default
    current_app.logger.info("Requests per IP: %s" % current_limit_per_ip)
    current_app.logger.info("Window size (s): %s" % current_limit_window)

    if per_ip is not None and window_size is not None:
        if per_ip / float(window_size) < 1:
            current_app.logger.info("Warning: Effective rate limit is less than 1 query per second")

        ratelimit.set_rate_limits(per_ip, per_ip, window_size)
        current_app.logger.info("New ratelimit parameters set:")
        current_app.logger.info("Requests per IP: %s" % per_ip)
        current_app.logger.info("Window size (s): %s" % window_size)


# Please keep additional sets of commands down there
cli.add_command(db.dump_manage.cli, name="dump")

if __name__ == '__main__':
    cli()
