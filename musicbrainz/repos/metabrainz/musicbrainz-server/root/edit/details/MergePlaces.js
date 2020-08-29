/*
 * @flow
 * Copyright (C) 2020 MetaBrainz Foundation
 *
 * This file is part of MusicBrainz, the open internet music database,
 * and is licensed under the GPL version 2, or (at your option) any
 * later version: http://www.gnu.org/licenses/gpl-2.0.txt
 */

import * as React from 'react';

import PlaceList from '../../components/list/PlaceList';

type MergePlacesEditT = {
  ...EditT,
  +display_data: {
    +new: PlaceT,
    +old: $ReadOnlyArray<PlaceT>,
  },
};

type Props = {
  +$c: CatalystContextT,
  +edit: MergePlacesEditT,
};

const MergePlaces = ({$c, edit}: Props): React.Element<'table'> => (
  <table className="details merge-place">
    <tr>
      <th>{l('Merge:')}</th>
      <td>
        <PlaceList $c={$c} places={edit.display_data.old} />
      </td>
    </tr>
    <tr>
      <th>{l('Into:')}</th>
      <td>
        <PlaceList $c={$c} places={[edit.display_data.new]} />
      </td>
    </tr>
  </table>
);

export default MergePlaces;
