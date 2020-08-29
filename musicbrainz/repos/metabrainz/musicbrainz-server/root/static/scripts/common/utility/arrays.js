/*
 * @flow strict
 * Copyright (C) 2019 MetaBrainz Foundation
 *
 * This file is part of MusicBrainz, the open internet music database,
 * and is licensed under the GPL version 2, or (at your option) any
 * later version: http://www.gnu.org/licenses/gpl-2.0.txt
 */

/*
 * Checks if two arrays are equal using the provided `isEqual` function
 * against each pair of items.
 */
export function arraysEqual<T>(
  a: $ReadOnlyArray<T>,
  b: $ReadOnlyArray<T>,
  isEqual: (T, T) => boolean,
): boolean {
  const length = a.length;
  if (length !== b.length) {
    return false;
  }
  for (let i = 0; i < length; i++) {
    if (!isEqual(a[i], b[i])) {
      return false;
    }
  }
  return true;
}

/*
 * Given a `destination` array that's already in sorted order according
 * to the provided `cmp` function, merges unique items from `source`
 * into `destination` while preserving the sorted order.
 */
export function mergeSortedArrayInto<T>(
  destination: Array<T>,
  source: $ReadOnlyArray<T>,
  cmp: (T, T) => number,
) {
  const length = source.length;
  for (let i = 0; i < length; i++) {
    const value = source[i];
    const [index, exists] = sortedIndexWith(
      destination,
      value,
      cmp,
    );
    if (!exists) {
      destination.splice(index, 0, value);
    }
  }
}

/*
 * Like Lodash's _.sortedIndexBy, but takes a comparator function
 * rather than an iteratee, and returns whether the item already
 * exists in the array.
 */
export function sortedIndexWith<T, U>(
  array: $ReadOnlyArray<T>,
  value: U,
  cmp: (T, U) => number,
): [number, boolean] {
  let low = 0;
  let high = array.length;
  let middle;
  let order;
  while (low < high) {
    middle = Math.floor((low + high) / 2);
    order = cmp(array[middle], value);
    if (order < 0) {
      low = middle + 1;
    } else {
      high = middle;
    }
  }
  if (middle !== undefined && high !== middle && high < array.length) {
    order = cmp(array[high], value);
  }
  return [high, order === 0];
}
