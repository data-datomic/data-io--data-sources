package MusicBrainz::Server::Report::DuplicateArtists;
use Moose;

with 'MusicBrainz::Server::Report',
     'MusicBrainz::Server::Report::FilterForEditor';

sub _add_artist {
    my ($store, $name, $gid, $row) = @_;

    $name =~ s/[\p{Punctuation}]//g;
    $name =~ s/\bAND\b/&/g;

    my @words = sort $name =~ /(\w+)/g;
    my $key = "@words";

    return if exists $store->{$key}{$gid};
    $store->{$key}{$gid} = $row;
}

sub run
{
    my $self = shift;

    my %artists;
    my $sql = $self->sql;

    my $artists = $sql->select_list_of_hashes(
        q{SELECT artist.gid, artist.name, artist.sort_name,
                 musicbrainz_unaccent(artist.name) AS name_unac,
                 musicbrainz_unaccent(artist.sort_name) AS sort_name_unac,
                 artist.comment, artist.type, artist.id,
                 (artist.comment != '') AS has_comment
          FROM artist}
    );

    for my $r (@$artists) {
        _add_artist(\%artists, $r->{name_unac}, $r->{gid}, $r);
        _add_artist(\%artists, $r->{sort_name_unac}, $r->{gid}, $r);
    }

    my $aliases = $sql->select_list_of_hashes(
        "SELECT artist.gid, artist.id, artist.name, artist.sort_name,
                musicbrainz_unaccent(alias.name) AS alias,
                CASE
                  WHEN artist.comment = '' THEN
                      'alias: ' || musicbrainz_unaccent(alias.name)
                  ELSE artist.comment || ') (alias: ' || musicbrainz_unaccent(alias.name)
                END AS comment,
                (artist.comment != '') AS has_comment,
                artist.type
         FROM artist
         JOIN artist_alias alias ON alias.artist = artist.id"
    );

    for my $r (@$aliases) {
        _add_artist(\%artists, $r->{alias}, $r->{gid}, $r);
    }

    my $qualified_table = $self->qualified_table;
    $sql->do("DROP TABLE IF EXISTS $qualified_table");
    $sql->do("CREATE TABLE $qualified_table ( key TEXT NOT NULL, artist_id INT NOT NULL, alias TEXT )");

    while (my ($k, $v) = each %artists) {
        next unless keys(%$v) >= 2;
        my @dupes = values %$v;

        # Skip if all artists have comments
        next if (grep { $_->{has_comment} } @dupes) == @dupes;

        for my $dupe (values %$v) {
            $sql->do("INSERT INTO $qualified_table (key, artist_id, alias) VALUES (?, ?, ?)",
                     $k, $dupe->{id}, $dupe->{alias})
        }
    }
}

sub inflate_rows
{
    my ($self, $dupes) = @_;

    my $artists = $self->c->model('Artist')->get_by_ids(map { $_->{artist_id} } @$dupes);
    $self->c->model('ArtistType')->load(values %$artists);

    return [
        map +{
            %$_,
            artist => $artists->{ $_->{artist_id} }
        }, @$dupes
    ];
}

sub ordering { "key" }

sub load_filtered {
    my ($self, $editor_id, $limit, $offset) = @_;

    my $qualified_table = $self->qualified_table;
    my $ordering = $self->ordering;
    my ($rows, $total) = $self->c->model('Artist')->query_to_list_limited(
        "SELECT DISTINCT report.*
         FROM $qualified_table report
         WHERE (key) IN (
             SELECT key
             FROM $qualified_table
             JOIN editor_subscribe_artist esa ON esa.artist = artist_id
             WHERE esa.editor = ?
         ) ORDER BY $ordering",
        [$editor_id],
        $limit,
        $offset,
        sub { $_[1] },
    );

    $rows = $self->inflate_rows($rows);
    return ($rows, $total);
}

sub filter_sql { }

__PACKAGE__->meta->make_immutable;
no Moose;
1;

=head1 COPYRIGHT

Copyright (C) 2009 Lukas Lalinsky
Copyright (C) 2012 MetaBrainz Foundation

This program is free software; you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation; either version 2 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program; if not, write to the Free Software
Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.

=cut
