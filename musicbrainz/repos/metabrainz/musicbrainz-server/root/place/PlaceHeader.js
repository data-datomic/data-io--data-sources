/*
 * @flow
 * Copyright (C) 2018 MetaBrainz Foundation
 *
 * This file is part of MusicBrainz, the open internet music database,
 * and is licensed under the GPL version 2, or (at your option) any
 * later version: http://www.gnu.org/licenses/gpl-2.0.txt
 */

import * as React from 'react';

import EntityHeader from '../components/EntityHeader';

type Props = {
  page: string,
  place: PlaceT,
};

const PlaceHeader = ({
  place,
  page,
}: Props): React.Element<typeof EntityHeader> => (
  <EntityHeader
    entity={place}
    headerClass="placeheader"
    page={page}
    subHeading={place.typeName
      ? lp_attributes(place.typeName, 'place_type')
      : l('Place')}
  />
);

export default PlaceHeader;
