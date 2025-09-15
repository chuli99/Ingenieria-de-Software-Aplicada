import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IExpenses } from '../expenses.model';
import { ExpensesService } from '../service/expenses.service';

@Component({
  standalone: true,
  templateUrl: './expenses-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class ExpensesDeleteDialogComponent {
  expenses?: IExpenses;

  protected expensesService = inject(ExpensesService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.expensesService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
