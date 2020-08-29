package MusicBrainz::Server::Entity::Alias;
use MusicBrainz::Server::Data::Utils qw( boolean_to_json );

use Moose;

extends 'MusicBrainz::Server::Entity';
with 'MusicBrainz::Server::Entity::Role::Editable';
with 'MusicBrainz::Server::Entity::Role::DatePeriod';
with 'MusicBrainz::Server::Entity::Role::Type' => { model => 'AliasType' };

has 'name' => (
    is => 'rw',
    isa => 'Str'
);

has 'sort_name' => (
    is => 'rw',
    isa => 'Str'
);

has 'locale' => (
    is  => 'rw',
    isa => 'Str',
);

has 'primary_for_locale' => (
    isa => 'Bool',
    is => 'rw',
);

around TO_JSON => sub {
    my ($orig, $self) = @_;

    return {
        %{ $self->$orig },
        name => $self->name,
        locale => $self->locale,
        primary_for_locale => boolean_to_json($self->primary_for_locale),
        sort_name => $self->sort_name,
    };
};

__PACKAGE__->meta->make_immutable;
no Moose;
1;

=head1 COPYRIGHT

Copyright (C) 2010 MetaBrainz Foundation
Copyright (C) 2009 Lukas Lalinsky

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
