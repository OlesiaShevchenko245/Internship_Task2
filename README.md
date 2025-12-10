# Observations Service  
_Spring Boot REST API сервіс для управління астрономічними спостереженнями та авторами._

## Опис проєкту
Сервіс надає повний CRUD функціонал, пагінацію, фільтрацію, генерацію Excel звітів та імпорт даних з JSON.

___

## Технологічний стек
- Java 17
- Spring Boot 3.2.0
- PostgreSQL
- Liquibase
- Apache POI
- Maven
- Testcontainers
- JUnit 5

___

## Запуск
### Клонування репозиторію (після завантаження даного рипозиторію (https://github.com/OlesiaShevchenko245/Internship_Task2))
```
git clone <repository-url>
cd cosmorum-service
```
### Налаштування PostgreSQL (локально)
А) Через Docker:
```
docker run --name cosmorum-postgres \
  -e POSTGRES_DB=cosmorum_db \
  -e POSTGRES_USER=postgres \
  -e POSTGRES_PASSWORD=pass \
  -p 5432:5432 \
  -d postgres:17

```
Б) Локально: 
```
CREATE DATABASE cosmorum_db;
```
### Запуск
```
# з використанням Maven Wrapper 
./mvnw spring-boot:run

# якщо Maven встановлений глобально
mvn spring-boot:run
```
Додаток має запуститися на http://localhost:8080  

### Перевірка роботи
```
# отримати список авторів
curl http://localhost:8080/api/author
```
_Очікуваний результат - JSON з 5 авторами: Galileo Galilei, Edwin Hubble, Johannes Kepler, Caroline Herschel, Tycho Brahe._  

___

### Структура проєкту:
```
cosmorum-service/
├── src/
│   ├── main/
│   │   ├── java/com/cosmorum/
│   │   │   ├── controller/          # REST контролери
│   │   │   │   ├── AuthorController.java
│   │   │   │   └── ObservationController.java
│   │   │   ├── dto/                 # Data Transfer Objects
│   │   │   │   ├── AuthorDTO.java
│   │   │   │   ├── ObservationDTO.java
│   │   │   │   ├── ObservationListDTO.java
│   │   │   │   ├── ObservationFilterRequest.java
│   │   │   │   ├── ObservationListResponse.java
│   │   │   │   └── UploadResponse.java
│   │   │   ├── entity/              # JPA сутності
│   │   │   │   ├── Author.java
│   │   │   │   └── AstronomicalObservation.java
│   │   │   ├── exception/           # Обробка помилок
│   │   │   │   ├── GlobalExceptionHandler.java
│   │   │   │   ├── ResourceNotFoundException.java
│   │   │   │   └── DuplicateResourceException.java
│   │   │   ├── repository/          # JPA репозиторії
│   │   │   │   ├── AuthorRepository.java
│   │   │   │   └── AstronomicalObservationRepository.java
│   │   │   ├── service/             # Бізнес логіка
│   │   │   │   ├── AuthorService.java
│   │   │   │   └── ObservationService.java
│   │   │   └── CosmorumServiceApplication.java
│   │   └── resources/
│   │       ├── application.properties
│   │       └── db/changelog/
│   │           └── db.changelog-master.xml
│   └── test/
│       └── java/com/cosmorum/
│           ├── controller/
│           │   ├── AuthorControllerIntegrationTest.java
│           │   └── ObservationControllerIntegrationTest.java
│           └── CosmorumServiceApplicationTests.java
├── astronomical_observations_import.json  # Приклад даних для імпорту
├── pom.xml
└── README.md
```

