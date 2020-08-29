/*
 * @flow
 * Copyright (C) 2018 MetaBrainz Foundation
 *
 * This file is part of MusicBrainz, the open internet music database,
 * and is licensed under the GPL version 2, or (at your option) any
 * later version: http://www.gnu.org/licenses/gpl-2.0.txt
 */

import * as React from 'react';

type Props = {
  +className?: string,
  +label: string,
  +name?: string,
  +value?: string,
};

const FormSubmit = ({
  className,
  label,
  name,
  value,
}: Props): React.Element<'span'> => (
  <span className={'buttons' + (className ? ' ' + className : '')}>
    <button name={name} type="submit" value={value}>{label}</button>
  </span>
);

export default FormSubmit;
