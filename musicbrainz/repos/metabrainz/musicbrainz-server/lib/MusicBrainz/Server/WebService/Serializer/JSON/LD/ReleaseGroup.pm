package MusicBrainz::Server::WebService::Serializer::JSON::LD::ReleaseGroup;
use Moose;
use MusicBrainz::Server::WebService::Serializer::JSON::LD::Utils qw( serialize_entity list_or_single artwork );

use List::AllUtils qw( uniq );

extends 'MusicBrainz::Server::WebService::Serializer::JSON::LD';
with 'MusicBrainz::Server::WebService::Serializer::JSON::LD::Role::GID';
with 'MusicBrainz::Server::WebService::Serializer::JSON::LD::Role::Name';
with 'MusicBrainz::Server::WebService::Serializer::JSON::LD::Role::Aliases';

around serialize => sub {
    my ($orig, $self, $entity, $inc, $stash, $toplevel) = @_;
    my $ret = $self->$orig($entity, $inc, $stash, $toplevel);

    $ret->{'@type'} = 'MusicAlbum';

    if ($entity->cover_art) {
        $ret->{image} = artwork($entity->cover_art);
    }

    if ($stash->store($entity)->{releases}) {
        my $items = $stash->store($entity)->{releases}{items};
        my @releases = map { serialize_entity($_, $inc, $stash) } @$items;
        $ret->{albumRelease} = list_or_single(@releases);
    }

    if ($entity->artist_credit && scalar $entity->artist_credit->names) {
        $ret->{byArtist} = list_or_single(map { serialize_entity($_->artist, $inc, $stash) } @{ $entity->artist_credit->names });
    }
    $ret->{creditedTo} = $entity->artist_credit->name if $entity->artist_credit;

    if ($entity->primary_type && release_type($entity->primary_type)) {
        $ret->{albumReleaseType} = release_type($entity->primary_type);
    }

    my @production_types = uniq grep { defined } map { production_type($_) } grep { defined } $entity->all_secondary_types;
    if (scalar $entity->all_secondary_types == 0) {
	# XXX: We don't have a way to explicitly track studio album, so we'll
	# assume that studio albums are those without any secondary types.
        $ret->{albumProductionType} = 'http://schema.org/StudioAlbum';
    } elsif (@production_types) {
        $ret->{albumProductionType} = list_or_single(@production_types);
    }

    return $ret;
};

sub release_type {
    my ($primary_type) = @_;
    my %map = (
        1 => 'Album',
        2 => 'Single',
        3 => 'EP',
        12 => 'Broadcast'
    );

    my $name;
    if ($name = $map{$primary_type->id}) {
        return "http://schema.org/${name}Release";
    }
}

sub production_type {
    my ($secondary_type) = @_;
    my %map = (
        1 => 'Compilation',
        2 => 'Soundtrack',
        3 => 'SpokenWord',
        6 => 'Live',
        7 => 'Remix',
        8 => 'DJMix',
        9 => 'Mixtape',
        10 => 'Demo'
    );
    my $name;
    if ($name = $map{$secondary_type->id}) {
        return "http://schema.org/${name}Album";
    }
}

__PACKAGE__->meta->make_immutable;
no Moose;
1;

=head1 COPYRIGHT

Copyright (C) 2014 MetaBrainz Foundation

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

