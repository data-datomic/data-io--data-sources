package MusicBrainz::Server::Controller::Role::Details;
use Moose::Role -traits => 'MooseX::MethodAttributes::Role::Meta::Role';

sub details : Chained('load') PathPart {
    my ($self, $c) = @_;

    my $entity = $c->stash->{$self->{entity_name}};

    $c->stash(
        component_path  => 'entity/Details.js',
        component_props => {entity => $entity},
        current_view    => 'Node',
    );
}

no Moose::Role;
1;
