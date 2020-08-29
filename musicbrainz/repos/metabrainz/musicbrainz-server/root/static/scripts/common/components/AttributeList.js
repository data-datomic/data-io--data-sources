/*
 * @flow
 * Copyright (C) 2019 MetaBrainz Foundation
 *
 * This file is part of MusicBrainz, the open internet music database,
 * and is licensed under the GPL version 2, or (at your option) any
 * later version: http://www.gnu.org/licenses/gpl-2.0.txt
 */

import * as React from 'react';

import {bracketedText} from '../utility/bracketed';

type Props = {
  +entity: WorkT,
};

const AttributeList = ({entity}: Props): React.Element<'ul'> | null => (
  entity.attributes ? (
    <ul>
      {entity.attributes.map(attribute => (
        <li key={attribute.id}>
          {lp_attributes(
            attribute.value,
            'work_attribute_type_allowed_value',
          )}
          {' '}
          {bracketedText(lp_attributes(
            attribute.typeName,
            'work_attribute_type',
          ))}
        </li>
      ))}
    </ul>
  ) : null
);

export default AttributeList;
