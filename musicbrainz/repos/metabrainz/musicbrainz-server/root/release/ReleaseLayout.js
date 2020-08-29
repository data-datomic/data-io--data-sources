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
import ReleaseSidebar from '../layout/components/sidebar/ReleaseSidebar';
import {reduceArtistCredit}
  from '../static/scripts/common/immutable-entities';

import ReleaseHeader from './ReleaseHeader';

type Props = {
  +$c: CatalystContextT,
  +children: React.Node,
  +entity: ReleaseT,
  +fullWidth?: boolean,
  +page: string,
  +title?: string,
};

const ReleaseLayout = ({
  $c,
  children,
  entity: release,
  fullWidth,
  page,
  title,
}: Props): React.Element<typeof Layout> => {
  const mainTitle = texp.l('Release “{name}” by {artist}', {
    artist: reduceArtistCredit(release.artistCredit),
    name: release.name,
  });
  return (
    <Layout $c={$c} title={title ? hyphenateTitle(mainTitle, title) : mainTitle}>
      <div id="content">
        <ReleaseHeader page={page} release={release} />
        {children}
      </div>
      {fullWidth ? null : <ReleaseSidebar $c={$c} release={release} />}
    </Layout>
  );
};

export default ReleaseLayout;
