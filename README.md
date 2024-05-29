# Weather Application
This Spring Boot application retrieves weather data from a third-party API using RestTemplate. To enhance performance, the application uses Redis for caching, checks the database, and finally queries the third-party API if necessary. The application features a custom annotation for input validation, logging via AOP, and a rate limiter to manage users' daily requests.

### Features

* Data Retrieval: Weather data is fetched from a third-party API.
* Caching: Redis is used to cache weather data for faster response times.
* Database Lookup: If data is not found in the cache, it checks the database.
* Third-party API: If data is not available in the cache or database, it retrieves the data from a third-party API and stores it in both the database and cache for future requests.
* Custom Annotation: Validates user input (city name) using a custom annotation.
* Logging: AOP (Aspect-Oriented Programming) is used for logging.
* Rate Limiting: Controls the number of requests a user can make per day.

### Project Structure
``` plaintext
src/main/java/com/example/weather
├── WeatherApplication.java
├── config
│   ├── SpringCachingConfig.java
│   ├── RestTemplateConfig.java
├── controller
│   ├── WeatherController.java
├── service
│   ├── WeatherService.java
├── repository
│   ├── WeatherRepository.java
├── entity
│   ├── Weather.java
├── aspects
│   ├── LoggingAspect.java
├── validation
│   ├── CityNameConstraint.java
│   ├── CityNameValidator.java
├── redis
│   ├── RedisConfig.java
│   ├── RedisWeather.java
│   ├── RedisWeatherRepository.java
├── exception
│   ├── GlobalExceptionHandler.java
└── 

```

### Key Components
* WeatherApplication: Main entry point of the Spring Boot application.
* RedisConfig: Configuration class for Redis.
* WeatherController: REST controller for handling weather data requests.
* WeatherService: Service class for weather data logic.
* WeatherRepository: Repository interface for database operations.
* Weather: Entity class representing weather data.
* LoggingAspect: AOP aspect for logging.
* CityNameConstraint: Custom annotation for validating city names.
* CityNameValidator: Validator class for ValidCityName annotation.
* CustomExceptionHandler: Handles custom exceptions.
* RateLimiter: Utility class for managing rate limiting.

#### Custom Annotation
The application uses a custom annotation @CityNameConstraint to validate city names. The annotation is implemented using CityNameValidator.

#### Logging
All requests and responses are logged using AOP (Aspect-Oriented Programming) with the LoggingAspect class.

#### Contact
For any inquiries, please contact sultanli.rehman@gmail.com
