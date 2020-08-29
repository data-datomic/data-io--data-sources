package MusicBrainz::Server::Controller::Role::Edit;
use MooseX::MethodAttributes::Role;
use MooseX::Role::Parameterized;

parameter 'form' => (
    isa => 'Str',
    required => 1
);

parameter 'edit_type' => (
    isa => 'Int',
    required => 1
);

parameter 'edit_arguments' => (
    isa => 'CodeRef',
    default => sub { sub { } }
);

role {
    my $params = shift;
    my %extra = @_;

    $extra{consumer}->name->config(
        action => {
            edit => { Chained => 'load', Edit => undef }
        },
        edit_edit_type => $params->edit_type
    );

    method 'edit' => sub {
        my ($self, $c) = @_;

        my $entity_name = $self->{entity_name};
        my $edit_entity = $c->stash->{ $entity_name };

        $c->stash->{template} = 'entity/edit.tt';

        return $self->edit_action($c,
            form        => $params->form,
            type        => $params->edit_type,
            item        => $edit_entity,
            edit_args   => { to_edit => $edit_entity },
            edit_rels   => 1,
            redirect    => sub {
                $c->response->redirect(
                    $c->uri_for_action($self->action_for('show'), [ $edit_entity->gid ]));
            },
            $params->edit_arguments->($self, $c, $edit_entity)
        );
    };
};

1;
