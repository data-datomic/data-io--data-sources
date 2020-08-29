/*
 * @flow
 * Copyright (C) 2020 MetaBrainz Foundation
 *
 * This file is part of MusicBrainz, the open internet music database,
 * and is licensed under the GPL version 2, or (at your option) any
 * later version: http://www.gnu.org/licenses/gpl-2.0.txt
 */

import * as React from 'react';

import sortByEntityName
  from '../static/scripts/common/utility/sortByEntityName';
import EnterEdit from '../components/EnterEdit';
import EnterEditNote from '../components/EnterEditNote';
import FieldErrors from '../components/FieldErrors';
import WorkList from '../components/list/WorkList';
import Layout from '../layout';

type Props = {
  +$c: CatalystContextT,
  +form: MergeFormT,
  +toMerge: $ReadOnlyArray<WorkT>,
};

const WorkMerge = ({
  $c,
  form,
  toMerge,
}: Props): React.Element<typeof Layout> => (
  <Layout $c={$c} fullWidth title={l('Merge works')}>
    <div id="content">
      <h1>{l('Merge works')}</h1>
      <p>
        {l(`You are about to merge all these works into a single one.
            Please select the work all others should be merged into:`)}
      </p>
      <form action={$c.req.uri} method="post">
        <WorkList
          $c={$c}
          mergeForm={form}
          works={sortByEntityName(toMerge)}
        />
        <FieldErrors field={form.field.target} />

        <EnterEditNote field={form.field.edit_note} />

        <EnterEdit form={form}>
          <button
            className="negative"
            name="submit"
            type="submit"
            value="cancel"
          >
            {l('Cancel')}
          </button>
        </EnterEdit>
      </form>
    </div>
  </Layout>
);

export default WorkMerge;
