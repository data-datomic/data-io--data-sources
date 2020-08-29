/*
 * @flow strict
 * Copyright (C) 2018 MetaBrainz Foundation
 *
 * This file is part of MusicBrainz, the open internet music database,
 * and is licensed under the GPL version 2, or (at your option) any
 * later version: http://www.gnu.org/licenses/gpl-2.0.txt
 */

// NOTE: Don't convert to an ES module; this is used by root/server.js.
/* eslint-disable import/no-commonjs */

const parseCookie = require('../static/scripts/common/utility/parseCookie');

function getRequestCookie(
  req /*: CatalystRequestContextT */,
  name /*: string */,
  defaultValue /*: string */ = '',
) /*: string */ {
  const headers = req.headers;
  const cookie = (
    headers.cookie ||
    headers.Cookie ||
    headers.COOKIE
  );
  return parseCookie(cookie, name, defaultValue);
}

module.exports = getRequestCookie;
