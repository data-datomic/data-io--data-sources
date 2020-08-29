/*
 * @flow
 * Copyright (C) 2020 MetaBrainz Foundation
 *
 * This file is part of MusicBrainz, the open internet music database,
 * and is licensed under the GPL version 2, or (at your option) any
 * later version: http://www.gnu.org/licenses/gpl-2.0.txt
 */

import * as React from 'react';

import Layout from '../layout';

type Props = {
  +$c: CatalystContextT,
};

const CannotVote = ({$c}: Props): React.Element<typeof Layout> => (
  <Layout $c={$c} fullWidth title={l('Error Voting on Edits')}>
    <h1>{l('Error Voting on Edits')}</h1>
    <p>
      {l('You’re not currently allowed to vote, please read the banner.')}
    </p>
  </Layout>
);

export default CannotVote;
