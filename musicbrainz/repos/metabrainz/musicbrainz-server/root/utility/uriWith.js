/*
 * @flow strict
 * Copyright (C) 2018 MetaBrainz Foundation
 *
 * This file is part of MusicBrainz, the open internet music database,
 * and is licensed under the GPL version 2, or (at your option) any
 * later version: http://www.gnu.org/licenses/gpl-2.0.txt
 */

import url from 'url';

export default function uriWith<T: {...}>(
  uriString: string,
  params: T,
): string {
  const u = url.parse(uriString, true);

  u.query = Object.assign(u.query, params);
  u.search = null;

  return url.format(u);
}
