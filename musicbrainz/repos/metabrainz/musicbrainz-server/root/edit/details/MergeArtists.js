/*
 * @flow
 * Copyright (C) 2020 MetaBrainz Foundation
 *
 * This file is part of MusicBrainz, the open internet music database,
 * and is licensed under the GPL version 2, or (at your option) any
 * later version: http://www.gnu.org/licenses/gpl-2.0.txt
 */

import * as React from 'react';

import ArtistList from '../../components/list/ArtistList';
import yesNo from '../../static/scripts/common/utility/yesNo';

type MergeArtistsEditT = {
  ...EditT,
  +display_data: {
    +new: ArtistT,
    +old: $ReadOnlyArray<ArtistT>,
    +rename: boolean,
  },
};

type Props = {
  +$c: CatalystContextT,
  +edit: MergeArtistsEditT,
};

const MergeArtists = ({$c, edit}: Props): React.Element<'table'> => (
  <table className="details merge-artists">
    <tr>
      <th>{l('Merge:')}</th>
      <td>
        <ArtistList $c={$c} artists={edit.display_data.old} showBeginEnd />
      </td>
    </tr>
    <tr>
      <th>{l('Into:')}</th>
      <td>
        <ArtistList $c={$c} artists={[edit.display_data.new]} showBeginEnd />
      </td>
    </tr>
    <tr className="rename-artist-credits">
      <th>{l('Rename artist and relationship credits')}</th>
      <td>{yesNo(edit.display_data.rename)}</td>
    </tr>
  </table>
);

export default MergeArtists;
