package MusicBrainz::Server::Report::EditorReport;
use Moose::Role;

with 'MusicBrainz::Server::Report::QueryReport';

around inflate_rows => sub {
    my $orig = shift;
    my $self = shift;

    my $items = $self->$orig(@_);

    my $editors = $self->c->model('Editor')->get_by_ids(
        map { $_->{id} } @$items
    );

    return [
        map +{
            %$_,
            editor => $editors->{ $_->{id} }
        }, @$items
    ];
};

1;

=head1 COPYRIGHT AND LICENSE

Copyright (C) 2019 MetaBrainz Foundation

This file is part of MusicBrainz, the open internet music database,
and is licensed under the GPL version 2, or (at your option) any
later version: http://www.gnu.org/licenses/gpl-2.0.txt

=cut
