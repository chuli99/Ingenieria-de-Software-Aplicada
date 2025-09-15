import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { ExpensesService } from '../service/expenses.service';
import { IExpenses } from '../expenses.model';
import { ExpensesFormService } from './expenses-form.service';

import { ExpensesUpdateComponent } from './expenses-update.component';

describe('Expenses Management Update Component', () => {
  let comp: ExpensesUpdateComponent;
  let fixture: ComponentFixture<ExpensesUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let expensesFormService: ExpensesFormService;
  let expensesService: ExpensesService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [ExpensesUpdateComponent],
      providers: [
        provideHttpClient(),
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(ExpensesUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ExpensesUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    expensesFormService = TestBed.inject(ExpensesFormService);
    expensesService = TestBed.inject(ExpensesService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const expenses: IExpenses = { id: 456 };

      activatedRoute.data = of({ expenses });
      comp.ngOnInit();

      expect(comp.expenses).toEqual(expenses);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IExpenses>>();
      const expenses = { id: 123 };
      jest.spyOn(expensesFormService, 'getExpenses').mockReturnValue(expenses);
      jest.spyOn(expensesService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ expenses });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: expenses }));
      saveSubject.complete();

      // THEN
      expect(expensesFormService.getExpenses).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(expensesService.update).toHaveBeenCalledWith(expect.objectContaining(expenses));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IExpenses>>();
      const expenses = { id: 123 };
      jest.spyOn(expensesFormService, 'getExpenses').mockReturnValue({ id: null });
      jest.spyOn(expensesService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ expenses: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: expenses }));
      saveSubject.complete();

      // THEN
      expect(expensesFormService.getExpenses).toHaveBeenCalled();
      expect(expensesService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IExpenses>>();
      const expenses = { id: 123 };
      jest.spyOn(expensesService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ expenses });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(expensesService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
