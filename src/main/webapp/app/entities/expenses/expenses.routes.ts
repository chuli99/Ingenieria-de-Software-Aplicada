import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import ExpensesResolve from './route/expenses-routing-resolve.service';

const expensesRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/expenses.component').then(m => m.ExpensesComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/expenses-detail.component').then(m => m.ExpensesDetailComponent),
    resolve: {
      expenses: ExpensesResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/expenses-update.component').then(m => m.ExpensesUpdateComponent),
    resolve: {
      expenses: ExpensesResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/expenses-update.component').then(m => m.ExpensesUpdateComponent),
    resolve: {
      expenses: ExpensesResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default expensesRoute;
