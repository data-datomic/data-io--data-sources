package MusicBrainz::Server::Report::ArtistsThatMayBeGroups;
use Moose;

with 'MusicBrainz::Server::Report::ArtistReport',
     'MusicBrainz::Server::Report::FilterForEditor::ArtistID';

sub query {
    "SELECT DISTINCT ON (artist.id) artist.id AS artist_id,
       row_number() OVER (ORDER BY artist.name COLLATE musicbrainz, artist.id)
     FROM artist
     JOIN l_artist_artist ON l_artist_artist.entity1=artist.id
     JOIN link ON link.id=l_artist_artist.link
     JOIN link_type ON link_type.id=link.link_type
     WHERE (artist.type NOT IN (2, 5, 6) OR artist.type IS NULL)
       AND link_type.name IN ('collaboration', 'member of band', 'conductor position')"
}

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
