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
