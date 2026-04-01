# POS Transactions Automation

Веб‑приложение на **Java 17 + Spring Boot 3.2** для автоматизации обработки транзакций ПОС‑терминалов с интеграцией REST API МПЦ.

## Запуск (Windows)

1. Поднять PostgreSQL 15:

```bash
docker compose up -d
```

Если ты используешь установленный PostgreSQL через GUI (pgAdmin / DBeaver), убедись что база **pos** существует.
Пример SQL:

```sql
create database pos;
```

2. Запустить приложение:

```bash
.\mvnw.cmd spring-boot:run
```

По умолчанию приложение стартует на `http://localhost:8080`.

## Учётные записи

- **admin / admin** (роль ADMIN, доступ к `/terminals`)
- **accountant / accountant** (роль ACCOUNTANT)

## Настройки (env)

- **DB_URL**: JDBC URL (по умолчанию `jdbc:postgresql://localhost:5432/pos`)
- **DB_USERNAME**, **DB_PASSWORD**
- **MPC_BASE_URL**: базовый URL МПЦ (по умолчанию `http://localhost:9000`)
- **MPC_BANK_CODE**: код банка (по умолчанию `HALYK`)
- **MPC_BEARER_TOKEN**: Bearer Token (хранить в переменных окружения)
- **MPC_POLLING_ENABLED**: включить/выключить плановый опрос (`true/false`)
- **MPC_POLLING_CRON**: cron (по умолчанию каждые 15 минут)

## IntelliJ IDEA (важно)

- Проект должен запускаться на **Java 17** (Project SDK = 17).
- Импортируй как Maven‑проект по `pom.xml`.
- Если МПЦ недоступен локально, можно отключить плановый опрос:
  - `MPC_POLLING_ENABLED=false`

## Страницы UI

- `/dashboard` — KPI + статус МПЦ + ручной запуск опроса
- `/transactions` — фильтры + таблица транзакций (20/стр)
- `/postings` — фильтры + таблица проводок (20/стр)
- `/reports` — отчёты (сегодня/неделя/месяц/период)
- `/terminals` — список терминалов (ADMIN)

