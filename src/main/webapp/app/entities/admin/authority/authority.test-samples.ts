import { IAuthority, NewAuthority } from './authority.model';

export const sampleWithRequiredData: IAuthority = {
  name: '2a649460-f8c0-4279-b53b-d8f627838a42',
};

export const sampleWithPartialData: IAuthority = {
  name: 'bcb08457-8bf1-43b9-978c-de92b1db5b30',
};

export const sampleWithFullData: IAuthority = {
  name: '406ad974-373e-4b4c-a4a8-2bc494deafd7',
};

export const sampleWithNewData: NewAuthority = {
  name: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
