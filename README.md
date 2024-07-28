# Sistema de Gestión de Candidatos

Este proyecto es una aplicación backend para gestionar la información de candidatos, utilizando Java 17 y SpringBoot 3. La aplicación proporciona una API REST para realizar operaciones CRUD en la base de datos.

## Descripción

El sistema permite gestionar candidatos, incluyendo la creación, lectura, actualización y eliminación de registros de candidatos. Utiliza Spring Boot para el desarrollo del backend, Spring Data JPA para la capa de acceso a datos, Spring Security para la gestión de la seguridad con JWT, y Flyway para la gestión de la base de datos.

## Tabla de Contenidos

- [Instalación](#instalación)
- [Uso](#uso)
- [Documentación de la API](#documentación-de-la-api)
- [Pruebas](#pruebas)


## Instalación

### Requisitos Previos

- [Java 11](https://www.oracle.com/java/technologies/javase-jdk11-downloads.html)
- [Maven](https://maven.apache.org/)

### Pasos de Instalación

1. Clonar el repositorio:
    ```sh
    git clone https://github.com/jesuslinares307/seek.git
    ```
2. Navegar al directorio del proyecto:
    ```sh
    cd seek
    ```
3. Construir y ejecutar la aplicación (actualmente en esta version todo se migra y carga a una base de datos en memoria de H2 con flyway integrado, no son necesarios pasos adicionales):
    ```sh
    mvn spring-boot:run
    ```

### Uso
La aplicación estará disponible en http://localhost:8080.

Endpoints Principales
- Generar Token con el siguiente usuario pre cargado como se solicito en el enunciado:
  ``` 
  {
  "username": "Jesus",
  "password": "12345678"
  }    
  ```
  En el endpoint de Autenticacion: 
- /api/v1/authenticate

Una vez obtenido el token se puede acceder 3 endpoints securizados:
- Crear candidato: POST /api/v1/candidates
- Obtener todos los candidatos: GET /api/v1/candidates
- Obtener candidato por ID: GET /api/v1/candidates/{id}

Y 2 endpoints los cuales quedaron disponibles para usar sin token de autenticacion como se indicaba en el enunciado:
- Actualizar candidato: PUT /api/v1/candidates/{id}
- Eliminar candidato: DELETE /api/v1/candidates/{id}

### Documentación de la API
La documentación completa de la API está disponible en Swagger. Accede a http://localhost:8080/swagger-ui.html para ver y probar los endpoints.


### Pruebas
Para ejecutar las pruebas unitarias e integración, utiliza el siguiente comando:

  ``` 
mvn test
  ``` 

### Postman Collection

En la carpeta de resources se encontrara una collection de postman con el nombre de Seek Challenge.postman_collection
La misma ya tiene configurada con variables el token para solo ir ejecutando los diferentes endpoints iniciando por la autenticación,
para seguir con el resto que se desee acceder.
