# Proyecto final Ingenieria de Software Aplicada

## Importar JDL:

- jhipster import-jdl expenses.jdl

## Para correr el proyecto (Con Docker Desktop iniciado):

- ./mvwn

# Tests

## Test de unidad

- Ruta: /src/test/java/com/jhipster/demo/expenses/service/ExpensesServiceTest.java
- ./mvnw test -Dtest=ExpensesServiceTest

## Tests individuales

Test para verificar que se crea correctamente un gasto

- ./mvnw test -Dtest=ExpensesServiceTest#shouldSaveExpenseSuccessfullyWhenValidData

Test para verificar que no se crea el gasto si la descripción es nula

- ./mvnw test -Dtest=ExpensesServiceTest#shouldNotSaveExpenseWhenDescriptionIsNull

Test para verificar que no se crea el gasto si la descripción está vacía

- ./mvnw test -Dtest=ExpensesServiceTest#shouldNotSaveExpenseWhenDescriptionIsEmpty

## Cypress

En carpeta src/test/javascript/cypress/e2e agregue archivo expenses-e2e-custom.cy.ts con tests e2e personalizados

Para ejecutar Cypress desde la raiz del proyecto:

- npx cypress open

## Deploy en Docker

1. Primero debo compilar el proyecto y generar el JAR

- ./mvnw clean package -DskipTests

2. Creamos el Dockerfile y le hacemos el build:

- docker build -t expenses:latest .

3. Para levantar el compose:

- docker compose up

Para tirarlo:

- docker-compose down

4. Para verificar el estado de los contenedores:

- docker ps

5. Para

### Logstasb

Ver logs de la aplicación desde la app como admin

### Kibana

Acceder a kibana:

- http://localhost:5601/status

## PWA (Ionic)

Link de repositorio:

- https://github.com/chuli99/Ingenieria-de-Software-Aplicada-Ionic.git

## Jenkins

Pasos para la ejecucion de Jenkins:  
1- Cree manualmente el Jenkinsfile en la raiz del proyecto (jhipster ci-dc no funcionaba)  
2- Modifique el POM con <image>XXXXX:latest</image>  
3- Ejecute:
docker container run -d --name jenkins -p 8090:8080 -p 50000:50000 jenkins/jenkins  
4- Luego:
docker exec -it jenkins bash
cd /var/jenkins_home/secrets/
cat initialAdminPassword
(PARA OBTENER CREDENCIALES)  
5- Acceder a:

- http://localhost:8090

6- Cree tareas con configuracion de la catedra  
7- Buildeamos el proyecto  
8- Agrego token de acceso en credentials por error.

### Docker Hub

Link de repositorio:

- https://hub.docker.com/repository/docker/chuli99/jenkinsaplicada/general

### Ejecutar imagen

Comando para descargar la imagen:

- docker pull chuli99/jenkinsaplicada:latest

Ejecutar la aplicacion:

- docker run -p 8080:8080 chuli99/jenkinsaplicada:latest
