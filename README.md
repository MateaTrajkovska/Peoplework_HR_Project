# Peoplework — HR Management System

HR систем за управување со вработени, оддели, договори и отсуства, развиен како проект за предметот Напреден Веб Дизајн.

**Технологии:** Vue 3 + TypeScript · Spring Boot · PostgreSQL · Java 21

## Структура
peoplework/
├── employee-portal-internship/ # Backend (Spring Boot)
└── hr-portal/ # Frontend (Vue 3 + TypeScript)

## Функционалности

- Управување со вработени (профили, лични/работни/финансиски податоци)
- Оддели и договори
- Отсуства (годишен одмор, боледување, породилно/татковско, медицинско) со одобрување/одбивање
- Салда на денови и автоматска пресметка на годишен одмор
- Календар на одобрени отсуства
- Извештаи (CSV извоз)

## Стартување

### Потребно
- Java 21+
- Node.js 24
- PostgreSQL (или Docker)

### База на податоци
```sh
docker compose up -d --wait db
```

### Backend
```sh
cd employee-portal-internship
mvn spring-boot:run
```
Достапно на `http://localhost:8648`

### Frontend
```sh
cd hr-portal
npm ci
npm run dev
```
Достапно на `http://localhost:5173`

## Изработиле

Матеа Трајковска (232092), Лаура Атанасова (233267)
