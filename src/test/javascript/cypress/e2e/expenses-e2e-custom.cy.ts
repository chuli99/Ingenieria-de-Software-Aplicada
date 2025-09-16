import {
  usernameLoginSelector,
  passwordLoginSelector,
  submitLoginSelector,
  loginItemSelector,
  logoutItemSelector,
  accountMenuSelector,
} from '../support/commands';

import {
  entityTableSelector,
  entityCreateButtonSelector,
  entityCreateSaveButtonSelector,
  entityDeleteButtonSelector,
  entityConfirmDeleteButtonSelector,
} from '../support/entity';

describe('Expenses E2E Tests - Login, Create and Delete', () => {
  const expensesPageUrl = '/expenses';
  const adminUsername = 'admin';
  const adminPassword = 'admin';

  // Datos de prueba para crear un gasto
  const testExpense = {
    description: 'Gasto de prueba E2E',
    amount: '150.75',
    date: '2025-06-25T10:30',
  };

  let createdExpenseId: number | undefined;

  beforeEach(() => {
    cy.intercept('POST', '/api/authenticate').as('authenticateRequest');
    cy.intercept('GET', '/api/expenses+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/expenses').as('postEntityRequest');
    cy.intercept('DELETE', '/api/expenses/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    // Limpiar cualquier gasto creado durante las pruebas
    if (createdExpenseId) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/expenses/${createdExpenseId}`,
        failOnStatusCode: false,
      }).then(() => {
        createdExpenseId = undefined;
      });
    }
  });

  // TEST 1: LOGIN CON USUARIO ADMIN
  describe('Test 1: Login con usuario admin', () => {
    it('Hace login exitoso con usuario admin y contraseña admin', () => {
      // Visitar la página principal
      cy.visit('/');

      // Hacer clic en el botón de login usando el comando personalizado
      cy.clickOnLoginItem();

      // Verificar que aparece el formulario de login
      cy.get(usernameLoginSelector).should('be.visible');
      cy.get(passwordLoginSelector).should('be.visible');

      // Ingresar credenciales de admin
      cy.get(usernameLoginSelector).type(adminUsername);
      cy.get(passwordLoginSelector).type(adminPassword);

      // Hacer clic en submit
      cy.get(submitLoginSelector).click();

      // Esperar a que se complete la autenticación
      cy.wait('@authenticateRequest').then(({ response }) => {
        if (response) {
          expect(response.statusCode).to.equal(200);
        }
      });

      // Verificar que el login fue exitoso
      // El menú de cuenta debería estar visible después del login
      cy.get(accountMenuSelector).should('be.visible');

      // Verificar que el botón de logout está disponible
      cy.get(accountMenuSelector).click();
      cy.get(logoutItemSelector).should('be.visible');

      cy.log('Test 1 completado: Login exitoso con admin/admin');
    });
  });

  // TEST 2: CREAR UN GASTO
  describe('Test 2: Crear un gasto (Expenses)', () => {
    beforeEach(() => {
      // Hacer login antes de cada test
      cy.login(adminUsername, adminPassword);
    });

    it('Crea un nuevo gasto exitosamente', () => {
      // Navegar a la página de gastos
      cy.visit(expensesPageUrl);
      cy.wait('@entitiesRequest');

      // Hacer clic en el botón para crear nuevo gasto
      cy.get(entityCreateButtonSelector).click();

      // Verificar que estamos en la página de creación
      cy.url().should('match', new RegExp('/expenses/new$'));
      cy.getEntityCreateUpdateHeading('Expenses');

      // Llenar el formulario
      cy.get('[data-cy="description"]').type(testExpense.description).should('have.value', testExpense.description);

      cy.get('[data-cy="amount"]').type(testExpense.amount).should('have.value', testExpense.amount);

      cy.get('[data-cy="date"]').type(testExpense.date).blur().should('have.value', testExpense.date);

      // Guardar el gasto
      cy.get(entityCreateSaveButtonSelector).click();

      // Verificar que se creó exitosamente
      cy.wait('@postEntityRequest').then(({ response }) => {
        if (response) {
          expect(response.statusCode).to.equal(201);
          expect(response.body).to.have.property('id');
          expect(response.body.description).to.equal(testExpense.description);
          expect(response.body.amount).to.equal(parseFloat(testExpense.amount));

          // Guardar el ID para limpieza posterior
          createdExpenseId = response.body.id;
        }
      });

      // Verificar que volvemos a la lista de gastos
      cy.wait('@entitiesRequest');
      cy.url().should('match', new RegExp('/expenses(\\?.*)?$'));

      // Verificar que el gasto aparece en la tabla
      cy.get(entityTableSelector).should('contain', testExpense.description);

      cy.log('Test 2 completado: Gasto creado exitosamente');
    });
  });

  // TEST 3: ELIMINAR UN GASTO
  describe('Test 3: Eliminar un gasto', () => {
    beforeEach(() => {
      // Hacer login antes de cada test
      cy.login(adminUsername, adminPassword);

      // Crear un gasto para eliminar
      cy.authenticatedRequest({
        method: 'POST',
        url: '/api/expenses',
        body: {
          description: 'Gasto para eliminar E2E',
          amount: 99.99,
          date: new Date().toISOString(),
        },
      }).then(({ body }) => {
        createdExpenseId = body.id;
      });
    });

    it('Elimina un gasto exitosamente', () => {
      // Navegar a la página de gastos
      cy.visit(expensesPageUrl);
      cy.wait('@entitiesRequest');

      // Verificar que el gasto existe en la tabla
      cy.get(entityTableSelector).should('contain', 'Gasto para eliminar E2E');

      // Hacer clic en el botón de eliminar del último gasto
      cy.get(entityDeleteButtonSelector).last().click();

      // Verificar que aparece el diálogo de confirmación
      cy.getEntityDeleteDialogHeading('expenses').should('exist');

      // Confirmar la eliminación
      cy.get(entityConfirmDeleteButtonSelector).click();

      // Verificar que se eliminó exitosamente
      cy.wait('@deleteEntityRequest').then(({ response }) => {
        if (response) {
          expect(response.statusCode).to.equal(204);
        }
      });

      // Verificar que la lista se actualiza
      cy.wait('@entitiesRequest').then(({ response }) => {
        if (response) {
          expect(response.statusCode).to.equal(200);
        }
      });

      // Verificar que el gasto ya no aparece en la tabla
      cy.get('body').then($body => {
        if ($body.find(entityTableSelector).length > 0) {
          cy.get(entityTableSelector).should('not.contain', 'Gasto para eliminar E2E');
        }
      });

      // Marcar como eliminado para evitar limpieza en afterEach
      createdExpenseId = undefined;

      cy.log('Test 3 completado: Gasto eliminado exitosamente');
    });
  });

  // TEST 4 : COMBINACIÓN DE LOGIN + CREAR + ELIMINAR
  describe('Test Completo: Login + Crear + Eliminar', () => {
    it('Completa el flujo completo: login, crear y eliminar gasto', () => {
      // === PASO 1: LOGIN ===
      cy.visit('/');
      cy.clickOnLoginItem();
      cy.get(usernameLoginSelector).type(adminUsername);
      cy.get(passwordLoginSelector).type(adminPassword);
      cy.get(submitLoginSelector).click();
      cy.wait('@authenticateRequest');
      cy.get(accountMenuSelector).should('be.visible');

      cy.log('Paso 1: Login completado');

      // === PASO 2: CREAR GASTO ===
      cy.visit(expensesPageUrl);
      cy.wait('@entitiesRequest');
      cy.get(entityCreateButtonSelector).click();

      cy.get('[data-cy="description"]').type('Gasto completo E2E');
      cy.get('[data-cy="amount"]').type('200.00');
      cy.get('[data-cy="date"]').type('2025-06-25T15:00').blur();
      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        if (response) {
          expect(response.statusCode).to.equal(201);
          createdExpenseId = response.body.id;
        }
      });

      cy.wait('@entitiesRequest');
      cy.get(entityTableSelector).should('contain', 'Gasto completo E2E');

      cy.log('Paso 2: Gasto creado');

      // === PASO 3: ELIMINAR GASTO ===
      cy.get(entityDeleteButtonSelector).last().click();
      cy.getEntityDeleteDialogHeading('expenses').should('exist');
      cy.get(entityConfirmDeleteButtonSelector).click();

      cy.wait('@deleteEntityRequest').then(({ response }) => {
        if (response) {
          expect(response.statusCode).to.equal(204);
        }
      });

      cy.wait('@entitiesRequest');
      cy.log('Paso 3: Gasto eliminado');
      createdExpenseId = undefined;
    });
  });
});
