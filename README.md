## Технологии
Spring Boot, JDBC, Postgres, Flyway, JUnit, Mockito, Docker <br />
## Инструкция
1. Конфигурация БД изменяется в файлах `./flyway.conf` и `./src/main/resources/db.properties`
2. Подъём БД и запуск приложения в контейнере: `docker-compose up`
3. Миграции: `./mvnw flyway:migrate`
4. Очистка БД: `./mvnw flyway:clean`
## Описание
Приложение поддерживает два режима работы: пользователь и центральный банк:
#### Центральный банк
1. Регестрирует новые банки
2. Начисляет проценты
3. Призоводит отмену операций
#### Пользователь
1. Может войти или поэтапно зарегестрироваться
2. Открывает новые счёта одного из 3 типов
3. Производит действия со счётами
## Ключевые особенности 
- БД поднимается в контейнере и поддерживает миграции 
- SQL запросы к БД прописаны вручную
- Подключение к БД происходит через данные, считываемые из файла `db.properties`
- Поэтапная регистрация предполагает, что пока пользователь не заполнил все данные (адрес и номер паспорта) он помечен подозрительным. Это накладывает соответствующие ограничения