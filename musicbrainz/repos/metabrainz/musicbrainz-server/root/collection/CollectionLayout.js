/*
 * @flow
 * Copyright (C) 2018 MetaBrainz Foundation
 *
 * This file is part of MusicBrainz, the open internet music database,
 * and is licensed under the GPL version 2, or (at your option) any
 * later version: http://www.gnu.org/licenses/gpl-2.0.txt
 */

import * as React from 'react';

import Layout from '../layout';
import CollectionSidebar
  from '../layout/components/sidebar/CollectionSidebar';

import CollectionHeader from './CollectionHeader';

type Props = {
  +$c: CatalystContextT,
  +children: React.Node,
  +entity: CollectionT,
  +fullWidth?: boolean,
  +page: string,
  +title?: string,
};

const CollectionLayout = ({
  $c,
  children,
  entity: collection,
  fullWidth,
  page,
  title,
}: Props): React.Element<typeof Layout> => {
  const mainTitle = texp.l(
    'Collection “{collection}”',
    {collection: collection.name},
  );

  return (
    <Layout $c={$c} title={title ? hyphenateTitle(mainTitle, title) : mainTitle}>
      <div id="content">
        <CollectionHeader
          collection={collection}
          page={page}
          user={$c.user}
        />
        {children}
      </div>
      {fullWidth ? null : (
        <CollectionSidebar $c={$c} collection={collection} />
      )}
    </Layout>
  );
};

export default CollectionLayout;
