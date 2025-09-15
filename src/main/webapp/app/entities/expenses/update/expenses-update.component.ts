import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IExpenses } from '../expenses.model';
import { ExpensesService } from '../service/expenses.service';
import { ExpensesFormGroup, ExpensesFormService } from './expenses-form.service';

@Component({
  standalone: true,
  selector: 'jhi-expenses-update',
  templateUrl: './expenses-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class ExpensesUpdateComponent implements OnInit {
  isSaving = false;
  expenses: IExpenses | null = null;

  protected expensesService = inject(ExpensesService);
  protected expensesFormService = inject(ExpensesFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: ExpensesFormGroup = this.expensesFormService.createExpensesFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ expenses }) => {
      this.expenses = expenses;
      if (expenses) {
        this.updateForm(expenses);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const expenses = this.expensesFormService.getExpenses(this.editForm);
    if (expenses.id !== null) {
      this.subscribeToSaveResponse(this.expensesService.update(expenses));
    } else {
      this.subscribeToSaveResponse(this.expensesService.create(expenses));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IExpenses>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(expenses: IExpenses): void {
    this.expenses = expenses;
    this.expensesFormService.resetForm(this.editForm, expenses);
  }
}
