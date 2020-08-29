package MusicBrainz::Server::Controller::Relationship::LinkAttributeType;
use Moose;
use MusicBrainz::Server::Constants qw(
    $EDIT_RELATIONSHIP_ADD_ATTRIBUTE
    $EDIT_RELATIONSHIP_REMOVE_LINK_ATTRIBUTE
    $EDIT_RELATIONSHIP_ATTRIBUTE
    $INSTRUMENT_ROOT_ID
);

use MusicBrainz::Server::Validation qw( is_guid );

with 'MusicBrainz::Server::Controller::Role::Load' => {
    model => 'LinkAttributeType',
    entity_name => 'link_attr_type',
};

BEGIN { extends 'MusicBrainz::Server::Controller' };

sub _load_tree
{
    my ($self, $c) = @_;

    my $tree = $c->model('LinkAttributeType')->get_tree(sub { return shift->root_id != $INSTRUMENT_ROOT_ID });
    $c->stash( root => $tree );
}

sub base : Chained('/') PathPart('relationship-attribute') CaptureArgs(0) { }

sub index : Path('/relationship-attributes') Args(0)
{
    my ($self, $c) = @_;

    $self->_load_tree($c);

    $c->stash(
        component_path  => 'relationship/linkattributetype/RelationshipAttributeTypesIndex',
        component_props => {root => $c->stash->{root}},
        current_view    => 'Node',
    );
}

sub create : Path('/relationship-attributes/create') Args(0) RequireAuth(relationship_editor)
{
    my ($self, $c) = @_;

    $self->_load_tree($c);
    my $form = $c->form( form => 'Admin::LinkAttributeType' );

    my $gid = $c->request->params->{parent};
    my $parent_link_attr_type = $c->model('LinkAttributeType')->get_by_gid($gid)
      if (is_guid($gid));

    $form->field('parent_id')->value($parent_link_attr_type->id)
        if $parent_link_attr_type;

    if ($c->form_posted_and_valid($form)) {
        $c->model('MB')->with_transaction(sub {
            $self->_insert_edit(
                $c, $form,
                edit_type => $EDIT_RELATIONSHIP_ADD_ATTRIBUTE,
                map { $_->name => $_->value } $form->edit_fields
            );
        });

        $c->response->redirect($c->uri_for_action('relationship/linkattributetype/index'));
        $c->detach;
    }
}

sub edit : Chained('load') RequireAuth(relationship_editor)
{
    my ($self, $c, $gid) = @_;

    my $link_attr_type = $c->stash->{link_attr_type};
    $c->detach('/error_403') if $link_attr_type->root_id == $INSTRUMENT_ROOT_ID;
    $self->_load_tree($c);

    my $form = $c->form( form => 'Admin::LinkAttributeType', init_object => $link_attr_type );

    if ($c->form_posted_and_valid($form)) {
        $c->model('MB')->with_transaction(sub {
            $self->_insert_edit(
                $c, $form,
                edit_type => $EDIT_RELATIONSHIP_ATTRIBUTE,
                entity_id => $link_attr_type->id,
                new => { map { $_->name => $_->value } $form->edit_fields },
                old => {
                    name => $link_attr_type->name,
                    description => $link_attr_type->description,
                    parent_id => $link_attr_type->parent_id,
                    child_order => $link_attr_type->child_order,
                }
            );
        });

        $c->response->redirect($c->uri_for_action('relationship/linkattributetype/index'));
        $c->detach;
    }
}

sub delete : Chained('load') RequireAuth(relationship_editor)
{
    my ($self, $c, $gid) = @_;

    my $link_attr_type = $c->stash->{link_attr_type};
    $c->detach('/error_403') if $link_attr_type->root_id == $INSTRUMENT_ROOT_ID;
    my $form = $c->form(
        form => 'Confirm'
    );

    if ($c->model('LinkAttributeType')->in_use($link_attr_type->id)) {
        $c->stash(
            component_path  => 'relationship/linkattributetype/RelationshipAttributeTypeInUse',
            component_props => {type => $link_attr_type},
            current_view    => 'Node',
        );
        $c->detach;
    }

    if ($c->form_posted_and_valid($form)) {
        $c->model('MB')->with_transaction(sub {
            $self->_insert_edit(
                $c, $form,
                edit_type => $EDIT_RELATIONSHIP_REMOVE_LINK_ATTRIBUTE,
                name => $link_attr_type->name,
                description => $link_attr_type->description,
                parent_id => $link_attr_type->parent_id,
                child_order => $link_attr_type->child_order,
                id => $link_attr_type->id
            );
        });

        $c->response->redirect($c->uri_for_action('relationship/linkattributetype/index'));
        $c->detach;
    }
}

1;

=head1 COPYRIGHT

Copyright (C) 2009 Lukas Lalinsky
Copyright (C) 2011 MetaBrainz Foundation

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
