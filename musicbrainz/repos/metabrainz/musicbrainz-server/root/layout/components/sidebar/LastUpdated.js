/*
 * @flow
 * Copyright (C) 2018 MetaBrainz Foundation
 *
 * This file is part of MusicBrainz, the open internet music database,
 * and is licensed under the GPL version 2, or (at your option) any
 * later version: http://www.gnu.org/licenses/gpl-2.0.txt
 */

import * as React from 'react';

import {CatalystContext} from '../../../context';
import formatUserDate from '../../../utility/formatUserDate';

type Props = {
  +entity: CoreEntityT,
};

const LastUpdated = ({entity}: Props): React.Element<'p'> => {
  const lastUpdated = entity.last_updated;
  return (
    <p className="lastupdate">
      {lastUpdated ? (
        <CatalystContext.Consumer>
          {$c => texp.l('Last updated on {date}', {
            date: formatUserDate($c, lastUpdated),
          })}
        </CatalystContext.Consumer>
      ) : (
        l('Last updated on an unknown date')
      )}
    </p>
  );
};

export default LastUpdated;
