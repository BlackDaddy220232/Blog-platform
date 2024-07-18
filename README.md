
# Blog platform

Blog platform is a simple web application that allows you to manage authors and articles. The application is built using the Spring Boot framework and provides a set of RESTful API endpoints to perform basic operations related to authors and their articles.

## Sonar Cloud
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=BlackDaddy220232_blog-platform&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=BlackDaddy220232_blog-platform) 
[![Code Smells](https://sonarcloud.io/api/project_badges/measure?project=BlackDaddy220232_blog-platform&metric=code_smells)](https://sonarcloud.io/summary/new_code?id=BlackDaddy220232_blog-platform)
[![Bugs](https://sonarcloud.io/api/project_badges/measure?project=BlackDaddy220232_blog-platform&metric=bugs)](https://sonarcloud.io/summary/new_code?id=BlackDaddy220232_blog-platform)

This project is integrated with the Sonor Cloud platform, which provides advanced code analysis and quality assurance tools to help us deliver high-quality, maintainable code.

[Link](https://sonarcloud.io/project/overview?id=BlackDaddy220232_blog-platform) to Sonar;

## API Reference
To interact with the "BlogPlatform" application's RESTful API endpoints, you can use the Postman request by this [link](https://www.postman.com/material-saganist-75818563/workspace/blog-platform/collection/33191456-0c709e2f-bc90-44a4-94b9-b5cb680995df?action=share&creator=33191456). Tap ```create a fork``` and use Postman for doing request.
#### Get all author's articles

```http
GET /authors/getAuthorArticles?nickname=${nickname}
```

| Parameter | Type     | Description                |
| :-------- | :------- | :------------------------- |
| `nickname` | `string` | **Required**. Nickname of author |

#### Get all articles

```http
GET /authors/getAllArticles
```
#### Create author

```http
POST /authors/createAuthor?nickname=${nickname}
```

| Parameter | Type     | Description                |
| :-------- | :------- | :------------------------- |
| `nickname` | `string` | **Required**. Nickname of author |

#### Create article

```http
POST /authors/createArticle?nickname=${nickname}&title=${title}
```

| Parameter | Type     | Description                |
| :-------- | :------- | :------------------------- |
| `nickname` | `string` | **Required**. Nickname of author |
| `title`    |  `string`| **Required** Title of article|

#### Delete author
```http
DELETE /authors/deleteAuthor?nickname=${nickname}
```
| Parameter | Type     | Description                |
| :-------- | :------- | :------------------------- |
| `nickname` | `string` | **Required**. Nickname of author |

#### Delete article
```http
DELETE /authors/deleteArticle?nickname=${nickname}&title=${title}
```

| Parameter | Type     | Description                |
| :-------- | :------- | :------------------------- |
| `nickname` | `string` | **Required**. Nickname of author |
| `title`    |  `string`| **Required** Title of article|

#### Change author's nickname

```http
PATCH /authors/changeNickname?oldNickname=${oldNickname}&newNickname=${newNickname}
```

| Parameter | Type     | Description                |
| :-------- | :------- | :------------------------- |
| `oldNickname` | `string` | **Required**. Old author's nickname |
| `newNickname`    |  `string`| **Required** New author's nickname|

#### Change article title
```http
PATCH /authors/changeArticle?nickname=${nickname}&oldTitle=${oldTitle}&newTitle=${newTitle}
```


## Prerequisites

- Java Development Kit (JDK) 17 or later
- Apache Maven 3.9.6
## Installation

### 1. Clone the repository:
    
```bash
  git clone https://github.com/BlackDaddy220232/blog-platform.git
```

### 2. Automical Installation

#### 1. Launch ```Setup.bat``` with Command Line
```bash
...\blog-platform\blogPlatform\Setup.bat
```
#### 2. Input your PostgreSQL ``username`` and ``password`` and enjoy!

### 2. Manual Installation

#### 1. Open ```application.properties``` by this path ``` \blog-platform\blogPlatform\src\main\resources```
#### 2. Please provide your username and password in the designated fields.

#### 3. Choose ```create``` or ```create-drop``` in the field ```spring.jpa.hibernate.ddl-auto```.

#### 4. Navigate to the project directory
```bash
\blog-platform\blogPlatform
```
#### 5. Execute the Maven command to clean the project and then build it
```bash
mvn clean install
```
#### 6. run the application using the following Java command:
```bash
java -jar \target\blogPlatform-0.0.1-SNAPSHOT.jar
```

## Tests

The service layer of the "BlogPlatform" application has **100% unit test coverage**. This means that every method and code path in the service layer is thoroughly tested, ensuring the correctness of the application's core functionality

## License
This project is licensed under the MIT License. See the ```LICENSE``` file for more information.

## Author

This application was developed by Alexander Mynzul.
