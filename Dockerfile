FROM node:24-alpine AS frontend
WORKDIR /frontend
COPY hr-portal/package*.json ./
RUN npm ci
COPY hr-portal/ ./
RUN npm run build

FROM maven:3.9-eclipse-temurin-21 AS backend
WORKDIR /backend
COPY employee-portal-internship/ ./
COPY --from=frontend /frontend/dist/ ./employee-portal/src/main/resources/static/
RUN mvn -B clean package

FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=backend /backend/employee-portal/target/employee-portal-0.0.1-SNAPSHOT.jar app.jar
ENV BIND_ADDRESS=0.0.0.0
EXPOSE 8648
USER 10001
ENTRYPOINT ["java","-jar","app.jar"]
