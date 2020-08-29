/*
 * @flow strict
 * Copyright (C) 2020 MetaBrainz Foundation
 *
 * This file is part of MusicBrainz, the open internet music database,
 * and is licensed under the GPL version 2, or (at your option) any
 * later version: http://www.gnu.org/licenses/gpl-2.0.txt
 */

import isGreyedOut from '../url/utility/isGreyedOut';

export default function isDisabledLink(
  relationshipOrLinkDatePeriod: {+ended: boolean, ...},
  entity: CoreEntityT,
): boolean {
  return entity.entityType === 'url' && (
    relationshipOrLinkDatePeriod.ended || isGreyedOut(entity.href_url)
  );
}
