# Peoplework backend
The supplied Spring Boot module, original entities, repositories, converters, MapStruct mappers and 444 tests are retained. Java baseline is now 21. The entry point is public and the build no longer requires a private Maven repository or Git metadata.

New `com.h4h.employeeportal.portal` services expose the connected HR workspace under `/api`. Employees use the existing `core_employee` and related tables. Leave allowances and requests use `abs_absence` and `abs_absence_request`. New department, work profile, contract, activity, holiday and template tables are appended through Liquibase.

```sh
mvn clean package
java -jar employee-portal/target/employee-portal-0.0.1-SNAPSHOT.jar
```
See the root README for PostgreSQL, environment variables, the Vue development server and integration tests. After editing the frontend, use the root build script or Docker build to refresh the embedded website.
