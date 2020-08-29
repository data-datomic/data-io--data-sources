package MusicBrainz::Server::Entity::CDStub;

use Moose;
use MusicBrainz::Server::Data::Utils qw( datetime_to_iso8601 );
use MusicBrainz::Server::Entity::Barcode;
use MusicBrainz::Server::Entity::Types;
use MusicBrainz::Server::Types qw( DateTime );

use namespace::autoclean;

extends 'MusicBrainz::Server::Entity';

sub entity_type { 'cdstub' }

has 'discid' => (
    is => 'rw',
    isa => 'Str'
);

has 'track_count' => (
    is => 'rw',
    isa => 'Int'
);

has 'leadout_offset' => (
    is => 'rw',
    isa => 'Int'
);

has 'track_offset' => (
    is => 'rw',
    isa => 'ArrayRef[Int]'
);

has 'title' => (
    is => 'rw',
    isa => 'Str'
);

has 'artist' => (
    is => 'rw',
    isa => 'Str'
);

has 'date_added' => (
    is => 'rw',
    isa => DateTime,
    coerce => 1
);

has 'last_modified' => (
    is => 'rw',
    isa => DateTime,
    coerce => 1
);

has 'lookup_count' => (
    is => 'rw',
    isa => 'Int'
);

has 'modify_count' => (
    is => 'rw',
    isa => 'Int'
);

has 'source' => (
    is => 'rw',
    isa => 'Int'
);

has 'barcode' => (
    is => 'rw',
    isa => 'Barcode',
    lazy => 1,
    default => sub { MusicBrainz::Server::Entity::Barcode->new() },
);

has 'comment' => (
    is => 'rw',
    isa => 'Str'
);

has 'tracks' => (
    is => 'rw',
    isa => 'ArrayRef[MusicBrainz::Server::Entity::CDStubTrack]',
    lazy => 1,
    default => sub { [] },
    traits => [ 'Array' ],
    handles => {
        all_tracks => 'elements',
        add_track => 'push',
        clear_tracks => 'clear'
    }
);

with 'MusicBrainz::Server::Entity::Role::TOC';

sub length {
    my $self = shift;

    return int(($self->leadout_offset / 75) * 1000);
}

# XXX This should be called automatically when loading tracks
sub update_track_lengths {
    my $self = shift;
    my $index = 0;
    my @offsets = @{$self->track_offset};
    push @offsets, $self->leadout_offset;
    foreach my $track (@{$self->tracks}) {
        $track->length(int((($offsets[$index + 1] - $offsets[$index]) / 75) * 1000));
        $index++;
    }
}

around TO_JSON => sub {
    my ($orig, $self) = @_;

    my $json = $self->$orig;
    $json->{artist} = $self->artist;
    $json->{barcode} = $self->barcode->format;
    $json->{comment} = $self->comment;
    $json->{date_added} = datetime_to_iso8601($self->date_added);
    $json->{discid} = $self->discid;
    $json->{last_modified} = datetime_to_iso8601($self->last_modified);
    $json->{lookup_count} = $self->lookup_count;
    $json->{modify_count} = $self->modify_count;
    $json->{title} = $self->title;
    $json->{toc} = $self->track_offset ? $self->toc : undef;
    $json->{track_count} = $self->track_count;

    return $json;
};

__PACKAGE__->meta->make_immutable;
no Moose;
1;

=head1 COPYRIGHT

Copyright (C) 2010 Robert Kaye

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
