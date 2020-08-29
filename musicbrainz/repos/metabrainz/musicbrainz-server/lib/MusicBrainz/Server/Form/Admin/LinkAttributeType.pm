package MusicBrainz::Server::Form::Admin::LinkAttributeType;

use HTML::FormHandler::Moose;
use MusicBrainz::Server::Form::Utils qw( select_options_tree );
use MusicBrainz::Server::Constants qw( $INSTRUMENT_ROOT_ID );
use MusicBrainz::Server::Translation qw( l );

extends 'MusicBrainz::Server::Form';
with 'MusicBrainz::Server::Form::Role::Edit';

sub edit_field_names { qw( parent_id child_order name description ) }

has '+name' => ( default => 'linkattrtype' );

has_field 'parent_id' => (
    type => 'Select',
);

has_field 'child_order' => (
    type => '+MusicBrainz::Server::Form::Field::Integer',
    required => 1,
);

has_field 'name' => (
    type      => 'Text',
    required  => 1,
    maxlength => 255
);

has_field 'description' => (
    type => 'Text',
);

sub options_parent_id
{
    my ($self) = @_;
    return select_options_tree($self->ctx, $self->ctx->stash->{root}, accessor => 'name');
}

after validate => sub {
    my ($self) = @_;

    my $parent = $self->field('parent_id')->value ?
       $self->ctx->model('LinkAttributeType')->get_by_id($self->field('parent_id')->value) :
       undef;

    my $root = defined $parent ? ($parent->root_id // $parent->id) : 0;
    if ($root == $INSTRUMENT_ROOT_ID) {
        $self->field('parent_id')->add_error(l('Cannot add or edit instruments here; use the instrument editing forms instead.'));
    }
};

1;

=head1 COPYRIGHT

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
