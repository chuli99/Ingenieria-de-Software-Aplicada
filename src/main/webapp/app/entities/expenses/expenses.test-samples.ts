import dayjs from 'dayjs/esm';

import { IExpenses, NewExpenses } from './expenses.model';

export const sampleWithRequiredData: IExpenses = {
  id: 15670,
  description: 'splurge',
  amount: 29768.22,
  date: dayjs('2025-09-15T12:58'),
};

export const sampleWithPartialData: IExpenses = {
  id: 28563,
  description: 'lucky stitcher',
  amount: 1853.98,
  date: dayjs('2025-09-15T07:13'),
};

export const sampleWithFullData: IExpenses = {
  id: 2545,
  description: 'prance moment musty',
  amount: 30224.78,
  date: dayjs('2025-09-15T05:21'),
};

export const sampleWithNewData: NewExpenses = {
  description: 'obstruct so',
  amount: 15897.64,
  date: dayjs('2025-09-15T00:20'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
