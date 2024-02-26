# Banking Application Backend

## Description

Welcome to the Banking Application Backend repository! 
This project is a Java Spring-based banking application designed to provide users with a comprehensive set of features 
for managing their bank accounts and transactions. It follows a multi-layered architecture, 
integrating with a PostgreSQL database and providing secure authentication and authorization mechanisms. 
The application also utilizes external APIs for currency exchange rates and offers a user-friendly interface through Swagger UI.

## Table of Contents

- [Installation](#installation)
- [Usage](#usage)
- [Features](#features)
- [Technologies](#technologies)
- [License](#license)

## Installation

To install and run the Banking Application, follow these steps:

1. Clone the repository to your local machine:
   ```bash
   git clone https://github.com/andyBzg/banking-app.git
   ```

2. Set up your database connection by adding these properties to `resources/application.properties`:
   ```
   spring.datasource.url=
   spring.datasource.username=
   spring.datasource.password=
   ```

## Usage

To use the Banking Application, follow these steps:

1. Run the App (by default on port 8080)
2. Open this link in your browser: http://localhost:8080/swagger-ui/index.html
3. Use Swagger UI for interacting with application backend

## Features

* Registration and authentication
* Management of bank accounts
* Financial transactions and scheduled transfers
* Viewing transaction history
* Integration with external API for currency exchange rates

## Technologies

The Banking Application is developed using the following technologies:

* Spring Boot
* Spring Security
* Spring Web
* Spring Data JPA
* Hibernate
* REST API
* PostgreSQL
* Swagger UI
* Lombok
* JUnit5 and Mockito
* Maven

## License

This project is licensed under the [MIT License](LICENSE).
