import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IExpenses, NewExpenses } from '../expenses.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IExpenses for edit and NewExpensesFormGroupInput for create.
 */
type ExpensesFormGroupInput = IExpenses | PartialWithRequiredKeyOf<NewExpenses>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IExpenses | NewExpenses> = Omit<T, 'date'> & {
  date?: string | null;
};

type ExpensesFormRawValue = FormValueOf<IExpenses>;

type NewExpensesFormRawValue = FormValueOf<NewExpenses>;

type ExpensesFormDefaults = Pick<NewExpenses, 'id' | 'date'>;

type ExpensesFormGroupContent = {
  id: FormControl<ExpensesFormRawValue['id'] | NewExpenses['id']>;
  description: FormControl<ExpensesFormRawValue['description']>;
  amount: FormControl<ExpensesFormRawValue['amount']>;
  date: FormControl<ExpensesFormRawValue['date']>;
};

export type ExpensesFormGroup = FormGroup<ExpensesFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ExpensesFormService {
  createExpensesFormGroup(expenses: ExpensesFormGroupInput = { id: null }): ExpensesFormGroup {
    const expensesRawValue = this.convertExpensesToExpensesRawValue({
      ...this.getFormDefaults(),
      ...expenses,
    });
    return new FormGroup<ExpensesFormGroupContent>({
      id: new FormControl(
        { value: expensesRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      description: new FormControl(expensesRawValue.description, {
        validators: [Validators.required, Validators.maxLength(200)],
      }),
      amount: new FormControl(expensesRawValue.amount, {
        validators: [Validators.required],
      }),
      date: new FormControl(expensesRawValue.date, {
        validators: [Validators.required],
      }),
    });
  }

  getExpenses(form: ExpensesFormGroup): IExpenses | NewExpenses {
    return this.convertExpensesRawValueToExpenses(form.getRawValue() as ExpensesFormRawValue | NewExpensesFormRawValue);
  }

  resetForm(form: ExpensesFormGroup, expenses: ExpensesFormGroupInput): void {
    const expensesRawValue = this.convertExpensesToExpensesRawValue({ ...this.getFormDefaults(), ...expenses });
    form.reset(
      {
        ...expensesRawValue,
        id: { value: expensesRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): ExpensesFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      date: currentTime,
    };
  }

  private convertExpensesRawValueToExpenses(rawExpenses: ExpensesFormRawValue | NewExpensesFormRawValue): IExpenses | NewExpenses {
    return {
      ...rawExpenses,
      date: dayjs(rawExpenses.date, DATE_TIME_FORMAT),
    };
  }

  private convertExpensesToExpensesRawValue(
    expenses: IExpenses | (Partial<NewExpenses> & ExpensesFormDefaults),
  ): ExpensesFormRawValue | PartialWithRequiredKeyOf<NewExpensesFormRawValue> {
    return {
      ...expenses,
      date: expenses.date ? expenses.date.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
