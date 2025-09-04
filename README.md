# PomoStudy API

API para gerenciar usuários, tarefas, metas e categorias para o aplicativo PomoStudy.

## Tecnologias Utilizadas

*   Java 21
*   Spring Boot 3.4.0
*   Spring Web
*   Spring Data JPA
*   PostgreSQL
*   Maven
*   SpringDoc OpenAPI (Swagger)

## Endpoints da API

A URL base para todos os endpoints é `/api`.

### Autenticação

Adicionar futuramente.

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

## Como Executar o Projeto

1.  **Clone o repositório:**
    ```bash
    git clone https://github.com/seu-usuario/PomoStudy.git
    ```
2.  **Configure o banco de dados:**
    *   Crie um banco de dados PostgreSQL.
    *   Atualize as configurações do banco de dados no arquivo `src/main/resources/application.properties`.
3.  **Execute a aplicação:**
    ```bash
    mvn spring-boot:run
    ```
4.  **Acesse a documentação da API (Swagger):**
    *   Abra o seu navegador e acesse `http://localhost:8080/swagger-ui.html`.
