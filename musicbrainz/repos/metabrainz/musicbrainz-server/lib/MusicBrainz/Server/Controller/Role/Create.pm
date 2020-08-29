package MusicBrainz::Server::Controller::Role::Create;
use MooseX::MethodAttributes::Role;
use MooseX::Role::Parameterized;
use aliased 'MusicBrainz::Server::WebService::JSONSerializer';

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

parameter 'path' => (
    isa => 'Str'
);

parameter 'dialog_template' => (
    isa => 'Str'
);

role {
    my $params = shift;
    my %extra = @_;

    my %attrs = (
        RequireAuth => undef,
        Edit        => undef
    );
    if ($params->path) {
        $attrs{Path}  = $params->path;
    }
    else {
        $attrs{Local} = undef;
    }

    $extra{consumer}->name->config(
        action => {
            create => \%attrs
        },
        create_edit_type => $params->edit_type
    );

    method 'create' => sub {
        my ($self, $c, %args) = @_;

        if ($params->dialog_template) {
            $c->stash( dialog_template => $params->dialog_template );
        }

        my $model = $self->config->{model};
        my $entity;

        $self->edit_action($c,
            form        => $params->form,
            type        => $params->edit_type,
            on_creation => sub {
                my $edit = shift;

                $entity = $c->model($model)->get_by_id($edit->entity_id);

                return unless $args{within_dialog};

                $c->stash( dialog_result => $c->json->encode(JSONSerializer->serialize_internal($c, $entity)) );

                # XXX Delete the "Thank you, your edit has been..." message
                # so it doesn't weirdly show up on the next page.
                delete $c->flash->{message};
            },
            redirect => sub {
                $c->response->redirect($c->uri_for_action(
                    $self->action_for('show'), [ $entity->gid ]));
            },
            no_redirect => $args{within_dialog},
            edit_rels   => 1,
            $params->edit_arguments->($self, $c)
        );
    };
};

1;
