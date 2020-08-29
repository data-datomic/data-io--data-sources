/*
 * @flow strict
 * Copyright (C) 2019 MetaBrainz Foundation
 *
 * This file is part of MusicBrainz, the open internet music database,
 * and is licensed under the GPL version 2, or (at your option) any
 * later version: http://www.gnu.org/licenses/gpl-2.0.txt
 */

import * as React from 'react';

const HeaderLogo = (): React.Element<'img'> => (
  <img
    alt="MusicBrainz"
    className="logo"
    src={require('../../static/images/layout/header-logo.svg')}
  />
);

export default HeaderLogo;
