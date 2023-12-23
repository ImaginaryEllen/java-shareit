# Share It 📲

___

### ✏️ Описание: 
Сервис для аренды, позволяющий пользователям делиться друг с другом на время
необходимыми вещами, писать отзывы, осуществлять бронь или оставлять запрос на
необходимую им вещь.

___

### 🔨 Стек: 
REST-сервисы с использованием Spring Boot, Maven, Lombok, JPA, PostgreSQL.

___

### 📑 Схема базы данных:
<img width="751" alt="image" src="https://github.com/ImaginaryEllen/java-shareit/assets/124062632/3db032b7-9a24-49d2-9165-f6335178f9cc">

---

### 📝 Примеры реализованных эндпоинтов:

<details>
<summary>Получение вещи по id=2</summary>

```
http://localhost:8080//items/2
```

</details>

<details>
<summary>Поиск вещей по ключевым словам (названию/описанию)</summary>

```
http://localhost:8080/items/search?text=отвертка
```

</details>

<details>
<summary>Изменение статуса бронирования с id=2</summary>

```
http://localhost:8080/bookings/2?approved=true
```

</details>

---

### 🎯 Статус:
Завершен ✅