# PomoStudy API

API para gerenciar usuários, tarefas, metas e categorias para o aplicativo PomoStudy.

## Status do Projeto

O projeto está em desenvolvimento e aberto a contribuições.

## Tecnologias Utilizadas

*   Java 21
*   Spring Boot 3.4.0
*   Spring Web
*   Spring Data JPA
*   PostgreSQL
*   Maven
*   SpringDoc OpenAPI (Swagger)

## O que foi aprendido neste projeto

Este projeto foi uma oportunidade de aprofundar meus conhecimentos em desenvolvimento de APIs REST com Spring Boot, aplicando as melhores práticas do mercado. Abaixo, destaco os principais conceitos e tecnologias que utilizei:

### **Arquitetura e Design de API**

*   **Arquitetura em Camadas:** O projeto foi estruturado em camadas (Controller, Service, Repository) para garantir a separação de responsabilidades e a manutenibilidade do código.
*   **DTOs (Data Transfer Objects):** Utilizei DTOs para desacoplar a representação dos dados da API das entidades do banco de dados, garantindo uma API mais flexível e segura.
*   **Mapeamento de Objetos:** Implementei mappers para converter DTOs em entidades e vice-versa, automatizando o processo e evitando código repetitivo.
*   **Validação de Dados:** Utilizei o Spring Boot Starter Validation e criei validadores customizados para garantir a integridade dos dados de entrada da API.
*   **Tratamento de Exceções:** Implementei um `GlobalExceptionHandler` para centralizar o tratamento de exceções e retornar mensagens de erro consistentes para o cliente.
*   **Documentação de API:** Utilizei o SpringDoc OpenAPI (Swagger) para gerar a documentação da API de forma automática, facilitando o consumo da API por outros desenvolvedores.

### **Spring Boot e Ecossistema**

*   **Spring Web:** Utilizei o Spring Web para criar os endpoints da API REST.
*   **Spring Data JPA:** Utilizei o Spring Data JPA para facilitar a persistência de dados com o PostgreSQL.
*   **Injeção de Dependências:** Utilizei a injeção de dependências do Spring para gerenciar os componentes da aplicação.
*   **Spring Boot DevTools:** Utilizei o Spring Boot DevTools para agilizar o desenvolvimento com recursos como o live reload.

### **Banco de Dados**

*   **PostgreSQL:** Utilizei o PostgreSQL como banco de dados relacional para persistir os dados da aplicação.
*   **H2 Database:** Utilizei o H2 como banco de dados em memória para os testes automatizados.

### **Boas Práticas**

*   **Enums:** Utilizei enums para representar conjuntos de valores fixos, como prioridade de tarefas e tipos de metas.
*   **Tagging Interfaces:** Utilizei tagging interfaces para agrupar validações em diferentes cenários (criação e atualização).
*   **Testes:** Criei testes de unidade para a camada de repositório, garantindo a qualidade e a estabilidade do código.

## Como Executar o Projeto

### **Pré-requisitos**

*   Java 21
*   Maven
*   PostgreSQL

### **Configuração**

1.  **Clone o repositório:**
    ```bash
    git clone https://github.com/JRomualdoDev/PomoStudy.git
    ```
2.  **Configure o banco de dados:**
    *   Crie um banco de dados PostgreSQL.
    *   Atualize as configurações do banco de dados no arquivo `src/main/resources/application.properties`.
    *   Caso queira usar o docker - Na pasta onde se encontra o arquivo docker-composer.yaml, executar no terminal docker-composer up -d

### **Executando a Aplicação**

```bash
mvn spring-boot:run
```

### **Executando os Testes**

```bash
mvn test
```

### **Acessando a Documentação da API (Swagger)**

Abra o seu navegador e acesse `http://localhost:8080/swagger-ui.html`.

## Endpoints da API

A URL base para todos os endpoints é `/api`.

### Usuários

*   **Criar Usuário**
    *   `POST /user`
    *   **Corpo da Requisição:**
        ```json
        {
          "name": "string",
          "email": "string",
          "password": "string"
        }
        ```
    *   **Resposta de Sucesso (201):**
        ```json
        {
          "id": "integer",
          "name": "string",
          "email": "string"
        }
        ```

*   **Editar Usuário**
    *   `PUT /user/{id}`
    *   **Corpo da Requisição:**
        ```json
        {
          "name": "string",
          "email": "string",
          "password": "string"
        }
        ```
    *   **Resposta de Sucesso (200):**
        ```json
        {
          "id": "integer",
          "name": "string",
          "email": "string"
        }
        ```

*   **Listar Todos os Usuários**
    *   `GET /user`
    *   **Resposta de Sucesso (200):**
        ```json
        [
          {
            "id": "integer",
            "name": "string",
            "email": "string"
          }
        ]
        ```

*   **Buscar Usuário por ID**
    *   `GET /user/{id}`
    *   **Resposta de Sucesso (200):**
        ```json
        {
          "id": "integer",
          "name": "string",
          "email": "string"
        }
        ```

*   **Excluir Usuário**
    *   `DELETE /user/{id}`
    *   **Resposta de Sucesso (204):** Sem conteúdo.

### Tarefas

*   **Criar Tarefa**
    *   `POST /task`
    *   **Corpo da Requisição:**
        ```json
        {
          "title": "string",
          "description": "string",
          "priority": "string",
          "status": "string",
          "userId": "integer",
          "categoryId": "integer"
        }
        ```
    *   **Resposta de Sucesso (201):**
        ```json
        {
          "id": "integer",
          "title": "string",
          "description": "string",
          "priority": "string",
          "status": "string",
          "userId": "integer",
          "categoryId": "integer"
        }
        ```

*   **Editar Tarefa**
    *   `PUT /task/{id}`
    *   **Corpo da Requisição:**
        ```json
        {
          "title": "string",
          "description": "string",
          "priority": "string",
          "status": "string",
          "userId": "integer",
          "categoryId": "integer"
        }
        ```
    *   **Resposta de Sucesso (200):**
        ```json
        {
          "id": "integer",
          "title": "string",
          "description": "string",
          "priority": "string",
          "status": "string",
          "userId": "integer",
          "categoryId": "integer"
        }
        ```

*   **Listar Todas as Tarefas**
    *   `GET /task`
    *   **Resposta de Sucesso (200):**
        ```json
        [
          {
            "id": "integer",
            "title": "string",
            "description": "string",
            "priority": "string",
            "status": "string",
            "userId": "integer",
            "categoryId": "integer"
          }
        ]
        ```

*   **Buscar Tarefa por ID**
    *   `GET /task/{id}`
    *   **Resposta de Sucesso (200):**
        ```json
        {
          "id": "integer",
          "title": "string",
          "description": "string",
          "priority": "string",
          "status": "string",
          "userId": "integer",
          "categoryId": "integer"
        }
        ```

*   **Excluir Tarefa**
    *   `DELETE /task/{id}`
    *   **Resposta de Sucesso (204):** Sem conteúdo.

### Metas

*   **Criar Meta**
    *   `POST /goal`
    *   **Corpo da Requisição:**
        ```json
        {
          "title": "string",
          "description": "string",
          "type": "string",
          "userId": "integer"
        }
        ```
    *   **Resposta de Sucesso (201):**
        ```json
        {
          "id": "integer",
          "title": "string",
          "description": "string",
          "type": "string",
          "userId": "integer"
        }
        ```

*   **Editar Meta**
    *   `PUT /goal/{id}`
    *   **Corpo da Requisição:**
        ```json
        {
          "title": "string",
          "description": "string",
          "type": "string",
          "userId": "integer"
        }
        ```
    *   **Resposta de Sucesso (200):**
        ```json
        {
          "id": "integer",
          "title": "string",
          "description": "string",
          "type": "string",
          "userId": "integer"
        }
        ```

*   **Listar Todas as Metas**
    *   `GET /goal`
    *   **Resposta de Sucesso (200):**
        ```json
        [
          {
            "id": "integer",
            "title": "string",
            "description": "string",
            "type": "string",
            "userId": "integer"
          }
        ]
        ```

*   **Buscar Meta por ID**
    *   `GET /goal/{id}`
    *   **Resposta de Sucesso (200):**
        ```json
        {
          "id": "integer",
          "title": "string",
          "description": "string",
          "type": "string",
          "userId": "integer"
        }
        ```

*   **Excluir Meta**
    *   `DELETE /goal/{id}`
    *   **Resposta de Sucesso (204):** Sem conteúdo.

### Categorias

*   **Criar Categoria**
    *   `POST /category`
    *   **Corpo da Requisição:**
        ```json
        {
          "name": "string",
          "userId": "integer"
        }
        ```
    *   **Resposta de Sucesso (201):**
        ```json
        {
          "id": "integer",
          "name": "string",
          "userId": "integer"
        }
        ```

*   **Editar Categoria**
    *   `PUT /category/{id}`
    *   **Corpo da Requisição:**
        ```json
        {
          "name": "string",
          "userId": "integer"
        }
        ```
    *   **Resposta de Sucesso (200):**
        ```json
        {
          "id": "integer",
          "name": "string",
          "userId": "integer"
        }
        ```

*   **Listar Todas as Categorias**
    *   `GET /category`
    *   **Resposta de Sucesso (200):**
        ```json
        [
          {
            "id": "integer",
            "name": "string",
            "userId": "integer"
          }
        ]
        ```

*   **Buscar Categoria por ID**
    *   `GET /category/{id}`
    *   **Resposta de Sucesso (200):**
        ```json
        {
          "id": "integer",
          "name": "string",
          "userId": "integer"
        }
        ```

*   **Excluir Categoria**
    *   `DELETE /category/{id}`
    *   **Resposta de Sucesso (204):** Sem conteúdo.

## Autor

*   **José Romualdo**
*   **LinkedIn:** [www.linkedin.com/in/j-romualdo](www.linkedin.com/in/j-romualdo)
*   **GitHub:** [https://github.com/JRomualdoDev](https://github.com/JRomualdoDev)

## Licença

Este projeto está licenciado sob a licença MIT.
