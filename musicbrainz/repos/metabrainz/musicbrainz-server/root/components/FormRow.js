/*
 * @flow
 * Copyright (C) 2017 MetaBrainz Foundation
 *
 * This file is part of MusicBrainz, the open internet music database,
 * and is licensed under the GPL version 2, or (at your option) any
 * later version: http://www.gnu.org/licenses/gpl-2.0.txt
 */

import * as React from 'react';

type Props = {
  +children: React.Node,
  +hasNoLabel?: boolean,
};

const FormRow = ({
  children,
  hasNoLabel,
  ...props
}: Props): React.Element<'div'> => (
  <div className={'row' + (hasNoLabel ? ' no-label' : '')} {...props}>
    {children}
  </div>
);

export default FormRow;
