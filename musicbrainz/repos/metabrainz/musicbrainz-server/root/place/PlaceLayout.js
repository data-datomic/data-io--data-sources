/*
 * @flow
 * Copyright (C) 2019 MetaBrainz Foundation
 *
 * This file is part of MusicBrainz, the open internet music database,
 * and is licensed under the GPL version 2, or (at your option) any
 * later version: http://www.gnu.org/licenses/gpl-2.0.txt
 */

import * as React from 'react';

import Layout from '../layout';
import PlaceSidebar from '../layout/components/sidebar/PlaceSidebar';

import PlaceHeader from './PlaceHeader';

type Props = {
  +$c: CatalystContextT,
  +children: React.Node,
  +entity: PlaceT,
  +fullWidth?: boolean,
  +page: string,
  +title?: string,
};

const PlaceLayout = ({
  $c,
  children,
  entity: place,
  fullWidth,
  page,
  title,
}: Props): React.Element<typeof Layout> => (
  <Layout
    $c={$c}
    title={title ? hyphenateTitle(place.name, title) : place.name}
  >
    <div id="content">
      <PlaceHeader page={page} place={place} />
      {children}
    </div>
    {fullWidth ? null : <PlaceSidebar $c={$c} place={place} />}
  </Layout>
);


export default PlaceLayout;
