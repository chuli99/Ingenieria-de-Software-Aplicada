import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IExpenses } from '../expenses.model';
import { ExpensesService } from '../service/expenses.service';

const expensesResolve = (route: ActivatedRouteSnapshot): Observable<null | IExpenses> => {
  const id = route.params.id;
  if (id) {
    return inject(ExpensesService)
      .find(id)
      .pipe(
        mergeMap((expenses: HttpResponse<IExpenses>) => {
          if (expenses.body) {
            return of(expenses.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default expensesResolve;
