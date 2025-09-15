import { IUser } from './user.model';

export const sampleWithRequiredData: IUser = {
  id: 4573,
  login: '-',
};

export const sampleWithPartialData: IUser = {
  id: 32304,
  login: '5kY@nXFr7',
};

export const sampleWithFullData: IUser = {
  id: 31074,
  login: 'h8TOFn',
};
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
