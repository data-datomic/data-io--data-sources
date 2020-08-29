/*
 * @flow
 * Copyright (C) 2018 MetaBrainz Foundation
 *
 * This file is part of MusicBrainz, the open internet music database,
 * and is licensed under the GPL version 2, or (at your option) any
 * later version: http://www.gnu.org/licenses/gpl-2.0.txt
 */

import * as ReactDOMServer from 'react-dom/server';

import expand2react from './expand2react';

export default function expand2html(
  source: string,
  args?: ?{+[arg: string]: Expand2ReactInput},
): string {
  return ReactDOMServer.renderToStaticMarkup(
    expand2react(source, args),
  );
}
