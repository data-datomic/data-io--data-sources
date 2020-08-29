/*
 * @flow
 * Copyright (C) 2018 MetaBrainz Foundation
 *
 * This file is part of MusicBrainz, the open internet music database,
 * and is licensed under the GPL version 2, or (at your option) any
 * later version: http://www.gnu.org/licenses/gpl-2.0.txt
 */

import * as React from 'react';

import PaginatedResults from '../../components/PaginatedResults';
import EntityLink from '../../static/scripts/common/components/EntityLink';
import loopParity from '../../utility/loopParity';
import type {ReportReleaseGroupT} from '../types';
import ArtistCreditLink
  from '../../static/scripts/common/components/ArtistCreditLink';

type Props = {
  +items: $ReadOnlyArray<ReportReleaseGroupT>,
  +pager: PagerT,
};

const ReleaseGroupList = ({
  items,
  pager,
}: Props): React.Element<typeof PaginatedResults> => {
  let currentKey = '';
  let lastKey = '';

  return (
    <PaginatedResults pager={pager}>
      <table className="tbl">
        <thead>
          <tr>
            <th>{l('Artist')}</th>
            <th>{l('Release Group')}</th>
            <th>{l('Type')}</th>
          </tr>
        </thead>
        <tbody>
          {items.map((item, index) => {
            if (item.key) {
              lastKey = currentKey;
              currentKey = item.key;
            }

            return (
              <>
                {item.key && (lastKey !== item.key) ? (
                  <tr className="subh">
                    <td colSpan="4" />
                  </tr>
                ) : null}
                <tr
                  className={loopParity(index)}
                  key={item.release_group_id}
                >
                  {item.release_group ? (
                    <>
                      <td>
                        <ArtistCreditLink
                          artistCredit={item.release_group.artistCredit}
                        />
                      </td>
                      <td>
                        <EntityLink entity={item.release_group} />
                      </td>
                      <td>
                        {item.release_group.l_type_name
                          ? item.release_group.typeName
                          : l('Unknown')}
                      </td>
                    </>
                  ) : (
                    <>
                      <td />
                      <td colSpan="2">
                        {l('This release group no longer exists.')}
                      </td>
                    </>
                  )}
                </tr>
              </>
            );
          })}
        </tbody>
      </table>
    </PaginatedResults>
  );
};

export default ReleaseGroupList;
