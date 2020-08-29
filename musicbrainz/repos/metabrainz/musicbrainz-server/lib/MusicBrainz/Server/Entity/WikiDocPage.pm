package MusicBrainz::Server::Entity::WikiDocPage;

use Moose;

has 'version' => (
    is => 'rw',
    isa => 'Int'
);

has 'title' => (
    is => 'rw',
    isa => 'Str',
);

has 'hierarchy' => (
    is => 'rw',
    isa => 'ArrayRef',
);

has 'content' => (
    is => 'rw',
    isa => 'Str'
);

has 'canonical' => (
    is => 'rw',
    isa => 'Str',
);

sub TO_JSON {
    my ($self) = @_;

    return {
        content     => $self->content,
        hierarchy   => $self->hierarchy,
        title       => $self->title,
        version     => $self->version,
    };
}


__PACKAGE__->meta->make_immutable;
no Moose;
1;

=head1 COPYRIGHT

Copyright (C) 2009 Robert Kaye

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
