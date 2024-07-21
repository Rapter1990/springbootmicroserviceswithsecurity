# Spring Boot Microservices with JWT Implementation

<p align="center">
    <img src="screenshots/spring_boot_microservices_jwt_implementation_main.png" alt="Main Information" width="700" height="500">
</p>

### 📖 Information

<ul style="list-style-type:disc">
  <li>This project demonstrates a <b>Spring Boot microservices</b> architecture with <b>JWT-based authentication</b> and <b>role-based</b> access control. The setup includes an <b>API Gateway</b> to manage <b>routing</b> and <b>authentication</b>.</li> 
</ul>

<ul style="list-style-type:disc">
  <li>Roles and Permissions:
    <ul>
      <li><b>Admin</b> and <b>User</b> roles each have their own authentication and authorization mechanisms defined by their roles.</li>
      <li><b>Admin</b> can:
        <ul>
          <li>Create products</li>
          <li>Retrieve all products</li>
          <li>Retrieve products by ID</li>
          <li>Update products by ID</li>
          <li>Delete products by ID</li>
        </ul>
      </li>
      <li><b>User</b> can:
        <ul>
          <li>Retrieve all products</li>
          <li>Retrieve products by ID</li>
        </ul>
      </li>
    </ul>
  </li>
</ul>

### Explore Rest APIs

<table style="width:100%">
  <tr>
      <th>Method</th>
      <th>Url</th>
      <th>Description</th>
      <th>Request Body</th>
      <th>Header</th>
      <th>Valid Path Variable</th>
      <th>No Path Variable</th>
  </tr>
  <tr>
      <td>POST</td>
      <td>/api/v1/authentication/admin/register</td>
      <td>Admin Register</td>
      <td>AdminRegisterRequest</td>
      <td></td>
      <td></td>
      <td></td>
  <tr>
  <tr>
      <td>POST</td>
      <td>/api/v1/authentication/admin/login</td>
      <td>Admin Login</td>
      <td>LoginRequest</td>
      <td></td>
      <td></td>
      <td></td>
  <tr>
  <tr>
      <td>POST</td>
      <td>/api/v1/authentication/admin/refreshtoken</td>
      <td>Admin Refresh Token</td>
      <td>TokenRefreshRequest</td>
      <td></td>
      <td></td>
      <td></td>
  <tr>
  <tr>
      <td>POST</td>
      <td>/api/v1/authentication/admin/logout</td>
      <td>Admin Logout</td>
      <td>TokenInvalidateRequest</td>
      <td></td>
      <td></td>
      <td></td>
  <tr>
  <tr>
      <td>POST</td>
      <td>/api/v1/authentication/user/register</td>
      <td>User Register</td>
      <td>UserRegisterRequest</td>
      <td></td>
      <td></td>
      <td></td>
  <tr>
  <tr>
      <td>POST</td>
      <td>/api/v1/authentication/user/login</td>
      <td>User Login</td>
      <td>LoginRequest</td>
      <td></td>
      <td></td>
      <td></td>
  <tr>
  <tr>
      <td>POST</td>
      <td>/api/v1/authentication/user/refreshtoken</td>
      <td>User Refresh Token</td>
      <td>TokenRefreshRequest</td>
      <td></td>
      <td></td>
      <td></td>
  <tr>
  <tr>
      <td>POST</td>
      <td>/api/v1/authentication/user/logout</td>
      <td>User Logout</td>
      <td>TokenInvalidateRequest</td>
      <td></td>
      <td></td>
      <td></td>
  <tr>
  <tr>
      <td>POST</td>
      <td>/api/v1/products</td>
      <td>Create Product</td>
      <td>ProductCreateRequest</td>
      <td></td>
      <td></td>
      <td></td>
  <tr>
  <tr>
      <td>GET</td>
      <td>/api/v1/products/{productId}</td>
      <td>Get Product By Id</td>
      <td></td>
      <td></td>
      <td>ProductId</td>
      <td></td>
  <tr>
  <tr>
      <td>GET</td>
      <td>/api/v1/products</td>
      <td>Get Products</td>
      <td>ProductPagingRequest</td>
      <td></td>
      <td></td>
      <td></td>
  <tr>
  <tr>
      <td>PUT</td>
      <td>/api/v1/products/{productId}</td>
      <td>Update Product By Id</td>
      <td>ProductUpdateRequest</td>
      <td></td>
      <td>ProductId</td>
      <td></td>
  <tr>
  <tr>
      <td>DELETE</td>
      <td>/api/v1/products/{productId}</td>
      <td>Delete Product By Id</td>
      <td></td>
      <td></td>
      <td>ProductId</td>
      <td></td>
  <tr>
</table>


### Technologies

---
- Java 21
- Spring Boot 3.0
- Restful API
- Lombok
- Maven
- Junit5
- Mockito
- Integration Tests
- Docker
- Docker Compose
- CI/CD (Github Actions)
- Spring Cloud
- Postman
- Spring Security
- JWT

### Postman

```
Import postman collection under postman_collection folder
```


### Prerequisites

#### Define Variable in .env file for product service and user service

```
DATABASE_USERNAME={DATABASE_USERNAME}
DATABASE_PASSWORD={DATABASE_PASSWORD}
```

---
- Maven or Docker
---


### Docker Run
The application can be built and run by the `Docker` engine. The `Dockerfile` has multistage build, so you do not need to build and run separately.

Please follow directions shown below in order to build and run the application with Docker Compose file;

```sh
$ cd springbootmicroserviceswithsecurity
$ docker-compose up -d
```

If you change anything in the project and run it on Docker, you can also use this command shown below

```sh
$ cd springbootmicroserviceswithsecurity
$ docker-compose up --build
```

---
### Maven Run
To build and run the application with `Maven`, please follow the directions shown below;

```sh
$ cd springbootmicroserviceswithsecurity
$ cd eurekaserver
$ mvn clean install
$ mvn spring-boot:run
$ cd ..
$ cd apigateway
$ mvn clean install
$ mvn spring-boot:run
$ cd ..
$ cd authservice
$ mvn clean install
$ mvn spring-boot:run
$ cd ..
$ cd userservice
$ mvn clean install
$ mvn spring-boot:run
$ cd ..
$ cd productservice
$ mvn clean install
$ mvn spring-boot:run
```

---
### Docker Image Location

```
https://hub.docker.com/repository/docker/noyandocker/springbootmicroserviceswithsecurityeurekaserver/general
https://hub.docker.com/repository/docker/noyandocker/springbootmicroserviceswithsecurityapigateway/general
https://hub.docker.com/repository/docker/noyandocker/springbootmicroserviceswithsecurityauthservice/general
https://hub.docker.com/repository/docker/noyandocker/springbootmicroserviceswithsecurityuserservice/general
https://hub.docker.com/repository/docker/noyandocker/springbootmicroserviceswithsecurityproductservice/general
```

### Screenshots

<details>
<summary>Click here to show the screenshots of project</summary>
    <p> Figure 1 </p>
    <img src ="screenshots/eureka_server_image.PNG">
    <p> Figure 2 </p>
    <img src ="screenshots/docker_image.PNG">
    <p> Figure 3 </p>
    <img src ="screenshots/0_register_admin.PNG">
    <p> Figure 4 </p>
    <img src ="screenshots/0_login_admin.PNG">
    <p> Figure 5 </p>
    <img src ="screenshots/0_refresh_token_admin.PNG">
    <p> Figure 6 </p>
    <img src ="screenshots/0_logout_admin.PNG">
    <p> Figure 7 </p>
    <img src ="screenshots/2_register_user.PNG">
    <p> Figure 8 </p>
    <img src ="screenshots/2_login_user.PNG">
    <p> Figure 9 </p>
    <img src ="screenshots/2_refresh_token_user.PNG">
    <p> Figure 10 </p>
    <img src ="screenshots/2_logout_user.PNG">
    <p> Figure 11 </p>
    <img src ="screenshots/3_create_product_by_user.PNG">
    <p> Figure 12 </p>
    <img src ="screenshots/1_get_product_by_admin.PNG">
    <p> Figure 13 </p>
    <img src ="screenshots/3_get_product_by_user.PNG">
    <p> Figure 14 </p>
    <img src ="screenshots/3_get_products_by_user.PNG">
    <p> Figure 15 </p>
    <img src ="screenshots/3_update_product_by_admin.PNG">
    <p> Figure 16 </p>
    <img src ="screenshots/3_delete_product_by_admin.PNG">
</details>


### Contributors

- [Sercan Noyan Germiyanoğlu](https://github.com/Rapter1990)