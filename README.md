# **Cookbook Api**

This project is a simple API to simulate a cookbook where you can read, insert, edit or delete your recipes.
The main goal of this project was to practice some things I've been learning over the last couple of months, such as:
* Using flyway to manage DB migrations
* Practice and inprove testing (unit and integration)
* The use of OpenAPI to create API documentation
* The use of github actions to create CI and CD workflows
* Building and pushing a docker image to dockerhub

## Running the application

There are two ways of building the application

1. ### Using maven:
  Before installing and building the application, please, run:
  
  ```
  
  docker compose up -d cookbookdb
  
  ```
  
  on the root of the project, in order to build the container for PostgresSQL database installation.
  
  Then you can install by running:
  
  ```
  
  cd backend
  
  ```
  
  Followed by:
  
  ```
  
  mvn clean install
  
  ```
  
  and, after you can run by entering:
  
  ```
  
  mvn spring-boot:run
  
  ```

2. ### Using the Docker image:

   Start by pulling the image from Docker Hub:

   ```

   docker pull rpissarra/cookbook-api

   ```

   Then you can run the application by running:

   ```

   docker compose up -d
   
   ```
    on the root of the project.

## Documentation and Rest Interface Interaction

  As to obtain OpenAPI documentation in JSON format, please visit http://localhost:8080/recipe-openapi.
  To interact with the endpoints (including actuator), please refer to http://localhost:8080/recipe-ui.html. 
  In both cases, make sure the application is running.

## Future possible integrations

  * Create a frontend for the API
  * Changes in the CD workflow to deploy to AWS
   
