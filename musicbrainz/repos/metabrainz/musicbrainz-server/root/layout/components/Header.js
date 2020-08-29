/*
 * @flow
 * Copyright (C) 2015 MetaBrainz Foundation
 *
 * This file is part of MusicBrainz, the open internet music database,
 * and is licensed under the GPL version 2, or (at your option) any
 * later version: http://www.gnu.org/licenses/gpl-2.0.txt
 */

import * as React from 'react';

import HeaderLogo from './HeaderLogo';
import TopMenu from './TopMenu';
import BottomMenu from './BottomMenu';

type Props = {
  +$c: CatalystContextT,
};

const Header = ({$c}: Props): React.Element<'div'> => (
  <div className="header">
    <a className="logo" href="/" title="MusicBrainz">
      <HeaderLogo />
    </a>
    <div className="right">
      <TopMenu $c={$c} />
      <BottomMenu $c={$c} />
    </div>
  </div>
);

export default Header;
