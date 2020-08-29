package MusicBrainz::Server::Controller::Doc;
use Moose;

BEGIN { extends 'MusicBrainz::Server::Controller'; }

use DBDefs;
use HTTP::Status qw( HTTP_MOVED_PERMANENTLY );
use MusicBrainz::Server::Data::Utils qw( boolean_to_json );
use MusicBrainz::Server::Validation qw( is_guid );

sub show : Path('')
{
    my ($self, $c, @args) = @_;

    my $id = join '/', @args;
    $id =~ tr/ /_/;

    my $version = $c->model('WikiDocIndex')->get_page_version($id);
    my $page = $c->model('WikiDoc')->get_page($id, $version);

    if ($page && $page->canonical)
    {
        my ($path, $fragment) = split /\#/, $page->{canonical}, 2;
        $fragment = $fragment ? '#'.$fragment : '';

        $c->response->redirect($c->uri_for('/doc', $path).$fragment, HTTP_MOVED_PERMANENTLY);
        return;
    }

    if ($id =~ /^[^:]+:/i && $id !~ /^Category:/i) {
        $c->response->redirect(sprintf('http://%s/%s', DBDefs->WIKITRANS_SERVER, $id));
        $c->detach;
    }

    my %props = (
        id      => $id,
        page    => $page,
    );

    if ($page) {
        $c->stash(
            component_path  => 'doc/DocPage',
            component_props => \%props,
            current_view    => 'Node',
        );
    }    else {
        $c->response->status(404);
        $c->stash(
            component_path  => 'doc/DocError',
            component_props => \%props,
            current_view    => 'Node',
        );
    }
}

no Moose;
1;

=head1 COPYRIGHT

Copyright (C) 2009 Lukas Lalinsky
Copyright (C) 2018 MetaBrainz Foundation

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
