from collections import defaultdict
from mbdata import models
from sqlalchemy.orm import joinedload
from brainzutils.musicbrainz_db import mb_session
from brainzutils.musicbrainz_db.utils import get_entities_by_gids
from brainzutils.musicbrainz_db.includes import check_includes
from brainzutils.musicbrainz_db.serialize import serialize_labels
from brainzutils.musicbrainz_db.helpers import get_relationship_info


def get_label_by_id(mbid, includes=None, unknown_entities_for_missing=False):
    """Get label with the MusicBrainz ID.

    Args:
        mbid (uuid): MBID(gid) of the label.
    Returns:
        Dictionary containing the label information.
    """
    if includes is None:
        includes = []

    return fetch_multiple_labels(
        [mbid],
        includes=includes,
        unknown_entities_for_missing=unknown_entities_for_missing,
    ).get(mbid)


def fetch_multiple_labels(mbids, includes=None, unknown_entities_for_missing=False):
    """Get info related to multiple labels using their MusicBrainz IDs.

    Args:
        mbids (list): List of MBIDs of labels.
        includes (list): List of information to be included.
    Returns:
        Dictionary containing info of multiple labels keyed by their mbid.
    """
    if includes is None:
        includes = []
    includes_data = defaultdict(dict)
    check_includes('label', includes)
    with mb_session() as db:
        query = db.query(models.Label).\
            options(joinedload("type")).\
            options(joinedload("area"))
        labels = get_entities_by_gids(
            query=query,
            entity_type='label',
            mbids=mbids,
            unknown_entities_for_missing=unknown_entities_for_missing,
        )
        label_ids = [label.id for label in labels.values()]

        if 'artist-rels' in includes:
            get_relationship_info(
                db=db,
                target_type='artist',
                source_type='label',
                source_entity_ids=label_ids,
                includes_data=includes_data,
            )

        if 'url-rels' in includes:
            get_relationship_info(
                db=db,
                target_type='url',
                source_type='label',
                source_entity_ids=label_ids,
                includes_data=includes_data,
            )

        for label in labels.values():
            includes_data[label.id]['type'] = label.type
            includes_data[label.id]['area'] = label.area

    if 'rating' in includes:
        for label in labels.values():
            includes_data[label.id]['rating'] = label.rating

    return {str(mbid): serialize_labels(labels[mbid], includes_data[labels[mbid].id]) for mbid in mbids}
