## Технологии
Java, Spring Boot, JDBC, Postgres, JUnit, Mockito <br />
Docker and Flyway are coming soon...
## Описание
Приложение поддерживает два режима работы: пользователь и центральный банк. 
### Центральный банк
1. Регестрирует новые банки
2. Начисляет проценты
3. Призоводит отмену операций
### Пользователь
1. Может войти или поэтапно зарегестрироваться
2. Открывает новые счёта одного из 3 типов
3. Производит действия со счётами
## Ключевые особенности 
- SQL запросы к БД прописаны вручную
- Подключение к БД происходит через данные, считываемые приложением из файла `db.properties`
- Поэтапная регистрация предполагает, что пока пользователь не заполнил все данные (адрес и номер паспорта) он помечен подозрительным. Это накладывает соответствующие ограничения
