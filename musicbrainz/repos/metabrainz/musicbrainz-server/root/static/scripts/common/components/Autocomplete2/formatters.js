/*
 * @flow
 * Copyright (C) 2019 MetaBrainz Foundation
 *
 * This file is part of MusicBrainz, the open internet music database,
 * and is licensed under the GPL version 2, or (at your option) any
 * later version: http://www.gnu.org/licenses/gpl-2.0.txt
 */

import * as React from 'react';

import CountryAbbr from '../../../../../components/CountryAbbr';
import {commaOnlyListText} from '../../i18n/commaOnlyList';
import {unwrapNl} from '../../i18n';
import {addColonText} from '../../i18n/addColon';
import {reduceArtistCredit} from '../../immutable-entities';
import bracketed, {bracketedText} from '../../utility/bracketed';
import formatDate from '../../utility/formatDate';
import formatDatePeriod from '../../utility/formatDatePeriod';
import formatTrackLength from '../../utility/formatTrackLength';

import type {
  AutocompleteAreaT,
  AutocompleteEventT,
  AutocompleteInstrumentT,
  AutocompletePlaceT,
  AutocompleteRecordingT,
  AutocompleteReleaseT,
  AutocompleteReleaseGroupT,
  AutocompleteSeriesT,
  AutocompleteWorkT,
  Item,
} from './types';

const nonLatinRegExp = /[^\u0000-\u02ff\u1E00-\u1EFF\u2000-\u207F]/;

function isNonLatin(str) {
  return nonLatinRegExp.test(str);
}

function showExtraInfo(children, className = 'comment') {
  return (
    <span className={`autocomplete-${className}`}>
      {children}
    </span>
  );
}

function showBracketedTextInfo(comment) {
  return comment ? showExtraInfo(bracketedText(comment)) : null;
}

function showExtraInfoLine(children, className = 'comment') {
  return (
    <>
      <br />
      {showExtraInfo(children, className)}
    </>
  );
}

function formatGeneric(entity, extraInfo) {
  const name = unwrapNl<string>(entity.name);
  const info = [];

  if (entity.primaryAlias && entity.primaryAlias !== name) {
    info.push(entity.primaryAlias);
  }

  if (entity.entityType === 'artist' &&
      entity.sort_name &&
      entity.sort_name !== name &&
      !entity.primaryAlias &&
      isNonLatin(name)) {
    info.push(entity.sort_name);
  }

  if (entity.comment) {
    info.push(entity.comment);
  }

  if (extraInfo) {
    info.push.apply(info, extraInfo);
  }

  return (
    <>
      {name}
      {info.length ? (
        showExtraInfo(bracketedText(commaOnlyListText(info)))
      ) : null}
    </>
  );
}

function showLabeledTextList(label, items, className = 'comment') {
  return showExtraInfoLine(
    addColonText(label) + ' ' + commaOnlyListText(items),
    className,
  );
}

function showRelatedEntities(label, entities, className = 'comment') {
  if (entities && entities.hits > 0) {
    let toRender = entities.results;

    if (entities.hits > toRender.length) {
      toRender = toRender.concat(l('…'));
    }

    return showLabeledTextList(label, toRender, className);
  }
  return null;
}

const getName = item => item.name;

function pushContainmentInfo(area, extraInfo) {
  const containment = area.containment;
  if (containment && containment.length) {
    extraInfo.push(...containment.map(getName));
  }
}

function formatArea(area: AutocompleteAreaT) {
  const extraInfo = [];

  if (area.typeName) {
    extraInfo.push(lp_attributes(area.typeName, 'area_type'));
  }

  pushContainmentInfo(area, extraInfo);

  return (
    <>
      {area.name}
      {showBracketedTextInfo(area.comment)}
      {showExtraInfoLine(commaOnlyListText(extraInfo))}
    </>
  );
}

function formatEvent(event: AutocompleteEventT) {
  return (
    <>
      {formatGeneric(event)}

      {event.typeName ? (
        <>
          {' '}
          {showExtraInfo(
            bracketedText(lp_attributes(event.typeName, 'event_type')),
          )}
        </>
      ) : null}

      {event.begin_date || event.time ? showExtraInfoLine(
        (event.begin_date ? formatDatePeriod(event) + ' ' : '') +
        (event.time || ''),
      ) : null}

      {showRelatedEntities(
        l('Performers'),
        event.related_entities.performers,
      )}
      {showRelatedEntities(l('Location'), event.related_entities.places)}
    </>
  );
}

function formatInstrument(instrument: AutocompleteInstrumentT) {
  const extraInfo = [];

  if (instrument.typeName) {
    extraInfo.push(lp_attributes(instrument.typeName, 'instrument_type'));
  }

  let description = instrument.description;
  if (description) {
    // We want to strip html from the non-clickable description
    const div = document.createElement('div');
    div.innerHTML = l_instrument_descriptions(instrument.description);
    description = div.textContent;
  }

  return (
    <>
      {formatGeneric(instrument, extraInfo)}
      {description ? showExtraInfoLine(description) : null}
    </>
  );
}

function formatPlace(place: AutocompletePlaceT) {
  const extraInfo = [];

  if (place.typeName) {
    extraInfo.push(lp_attributes(place.typeName, 'place_type'));
  }

  const area = place.area;
  if (area) {
    extraInfo.push(area.name);
    pushContainmentInfo(area, extraInfo);
  }

  return (
    <>
      {formatGeneric(place)}
      {showExtraInfoLine(commaOnlyListText(extraInfo))}
    </>
  );
}

function formatRecording(recording: AutocompleteRecordingT) {
  const appearsOn = recording.appearsOn;

  return (
    <>
      {recording.video ? (
        <span className="video" title={l('This recording is a video')} />
      ) : null}

      {recording.name}

      {showBracketedTextInfo(recording.comment)}

      {recording.length ? (
        showExtraInfo(formatTrackLength(recording.length), 'length')
      ) : null}

      {showExtraInfoLine(texp.l('by {artist}', {artist: recording.artist}))}

      {appearsOn ? (
        appearsOn.hits > 0 ? (
          showRelatedEntities(l('appears on'), {
            hits: appearsOn.hits,
            results: appearsOn.results.map(getName),
          }, 'appears')
        ) : showExtraInfoLine(l('standalone recording'), 'appears')
      ) : null}

      {recording.isrcs && recording.isrcs.length ? showLabeledTextList(
        l('ISRCs'),
        recording.isrcs.map(isrc => isrc.isrc),
        'isrcs',
      ) : null}
    </>
  );
}

function formatReleaseEvent(event: ReleaseEventT) {
  const date = formatDate(event.date);
  const country = event.country;
  const countryDisplay = country ? <CountryAbbr country={country} /> : null;

  return (
    <React.Fragment key={(country ? country.id : '') + ',' + date}>
      <br />
      <span className="autocomplete-comment">
        {date}
        {date ? ' ' : ''}
        {date ? bracketed(countryDisplay) : countryDisplay}
      </span>
    </React.Fragment>
  );
}

function formatRelease(release: AutocompleteReleaseT) {
  const releaseLabelDisplay = [];

  if (release.labels) {
    const catNosByLabel = new Map<string, Array<string>>();

    for (const releaseLabel of release.labels) {
      const labelName = releaseLabel.label ? releaseLabel.label.name : '';

      let catNos = catNosByLabel.get(labelName);
      if (!catNos) {
        catNos = [];
        catNosByLabel.set(labelName, catNos);
      }

      if (releaseLabel.catalogNumber) {
        catNos.push(releaseLabel.catalogNumber);
      }
    }

    for (const [labelName, catNos] of catNosByLabel) {
      catNos.sort();

      const catNoDisplay = catNos.length ? (
        catNos.length > 2 ? (
          texp.l('{first_list_item} … {last_list_item}', {
            first_list_item: catNos[0],
            last_list_item: catNos[catNos.length - 1],
          })
        ) : commaOnlyListText(catNos)
      ) : null;

      releaseLabelDisplay.push(showExtraInfoLine(
        <>
          {labelName}
          {labelName ? ' ' + bracketedText(catNoDisplay) : catNoDisplay}
        </>,
      ));
    }
  }

  return (
    <>
      {formatGeneric(release)}
      {showExtraInfoLine(reduceArtistCredit(release.artistCredit))}
      {release.events ? release.events.map(formatReleaseEvent) : null}
      {releaseLabelDisplay}
      {release.barcode ? showExtraInfoLine(release.barcode) : ''}
    </>
  );
}

function formatReleaseGroup(releaseGroup: AutocompleteReleaseGroupT) {
  return (
    <>
      {releaseGroup.name}

      {showBracketedTextInfo(releaseGroup.firstReleaseDate)}

      {showBracketedTextInfo(releaseGroup.comment)}

      {releaseGroup.l_type_name ? (
        showExtraInfoLine(texp.l('{release_group_type} by {artist}', {
          artist: releaseGroup.artist,
          release_group_type: releaseGroup.l_type_name,
        }))
      ) : (
        showExtraInfoLine(texp.l('Release group by {artist}', {
          artist: releaseGroup.artist,
        }))
      )}
    </>
  );
}

function formatSeries(series: AutocompleteSeriesT) {
  return (
    <>
      {series.name}

      {showBracketedTextInfo(series.comment)}

      {series.type ? (
        showExtraInfo(
          bracketedText(lp_attributes(series.type.name, 'series_type')),
        )
      ) : null}
    </>
  );
}

function formatWork(work: AutocompleteWorkT) {
  const languages = work.languages;
  const typeName = work.typeName;

  return (
    <>
      {languages && languages.length ? (
        showExtraInfo(
          commaOnlyListText(
            languages.map(wl => l_languages(wl.language.name)),
          ),
          'language',
        )
      ) : null}

      {formatGeneric(work)}

      {typeName ? (
        showExtraInfoLine(
          addColonText(l('Type')) + ' ' +
          lp_attributes(typeName, 'work_type'),
        )
      ) : null}

      {work.artists ? (
        <>
          {showRelatedEntities(l('Writers'), work.artists.writers)}
          {showRelatedEntities(l('Artists'), work.artists.artists)}
        </>
      ) : null}
    </>
  );
}

export default function formatItem(item: Item): Expand2ReactOutput {
  if (!item.entityType) {
    return unwrapNl<string>(item.name);
  }

  switch (item.entityType) {
    case 'area':
      return formatArea(item);

    case 'event':
      return formatEvent(item);

    case 'instrument':
      return formatInstrument(item);

    case 'place':
      return formatPlace(item);

    case 'recording':
      return formatRecording(item);

    case 'release':
      return formatRelease(item);

    case 'release_group':
      return formatReleaseGroup(item);

    case 'series':
      return formatSeries(item);

    case 'work':
      return formatWork(item);

    default:
      return formatGeneric(item);
  }
}
