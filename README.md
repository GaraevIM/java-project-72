[![CI](https://github.com/GaraevIM/java-project-72/actions/workflows/gradle.yml/badge.svg?branch=main)](https://github.com/GaraevIM/java-project-72/actions/workflows/gradle.yml)
[![hexlet-check](https://github.com/GaraevIM/java-project-72/actions/workflows/hexlet-check.yml/badge.svg?branch=main)](https://github.com/GaraevIM/java-project-72/actions/workflows/hexlet-check.yml)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=GaraevIM_java-project-72&metric=alert_status)](https://sonarcloud.io/project/overview?id=GaraevIM_java-project-72)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=GaraevIM_java-project-72&metric=coverage)](https://sonarcloud.io/project/overview?id=GaraevIM_java-project-72)

# Page Analyzer

Page Analyzer is a web application for analyzing websites and collecting basic SEO-related information.  
It allows you to add pages, run checks, and store information about HTTP status code, `title`, `h1`, and `description`.

## Demo

[Live app](https://java-project-72-hfdb.onrender.com)

## Features

- add websites to the database
- prevent duplicates by normalized URL
- run page availability checks
- collect SEO-related data from HTML:
    - `title`
    - `h1`
    - `meta description`
- store check history for each website
- show the latest check status on the websites list page

## Tech stack

- Java 21
- Javalin
- JTE
- H2 / PostgreSQL
- HikariCP
- Unirest
- Jsoup
- JUnit 5
- MockWebServer
- JaCoCo
- SonarCloud
- GitHub Actions

## Run locally

### Clone repository

```bash
git clone https://github.com/GaraevIM/java-project-72.git
cd java-project-72/app
```

### Build project

```bash
./gradlew clean build
```

### Run application

```bash
./gradlew run
```

By default, the application starts on port `7070`.

## Run tests

```bash
./gradlew test
```

## Run full verification

```bash
./gradlew check
```

## Environment variables

For production you can configure the database connection through the environment variable:

```bash
JDBC_DATABASE_URL
```

If the variable is not set, the application uses the in-memory H2 database by default.

## Project status

This project was created as part of the Hexlet Java Developer course.
