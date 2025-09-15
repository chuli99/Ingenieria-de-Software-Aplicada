import dayjs from 'dayjs/esm';

export interface IExpenses {
  id: number;
  description?: string | null;
  amount?: number | null;
  date?: dayjs.Dayjs | null;
}

export type NewExpenses = Omit<IExpenses, 'id'> & { id: null };
