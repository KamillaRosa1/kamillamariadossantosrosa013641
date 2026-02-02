# Projeto Desenvolvedor Back End - API Artistas & Álbuns

## Dados da Inscrição

* **Candidata:** Kamilla Maria dos Santos Rosa
* **Objetivo:** Seleção de formação de cadastro reserva para contratação temporaria.
* **Cargo:** Analista de Tecnologia da Informação - Perfil Engenheiro da Computação.
* **Projeto Executado:** Anexo II - A - Projeto de desenvolvimento BackEnd.

## Descrição do projeto

Este projeto consiste em uma API REST para gerenciamento de artistas e álbuns, com foco em segurança JWT, integração com armazenamento S3 (MinIO).

## Tecnologias e Arquitetura

* **Linguagem:** Java 21 (Spring Boot 4.0.2)

* **Banco de Dados:** PostgreSQL 15

* **Versionamento de DB:** Flyway (Migrações e Carga Inicial)

* **Armazenamento de Objetos:** MinIO (API S3 via AWS SDK v2 para Java)

* **Segurança:** Spring Security + JWT (AccessToken: 5min / Rate Limit: 10 req/min)
  
* **Mensageria:** WebSocket (STOMP) para notificações em tempo real.

* **Client de & Documentação:**

  * Thunder Client: Validação preliminar de endpoints e autenticação antes da implementação do Swagger.

  * Swagger / OpenAPI 3.0: Implementado via springdoc-openapi. A documentação interativa está disponível em /swagger-ui.html, permitindo a exploração dos schemas, autenticação via Bearer Token e execução de testes funcionais diretamente pelo navegador.

* **Testes Unitários:** Os testes foram implementados utilizando **JUnit 5**, **Mockito** e o suporte de **Spring Boot Test**.

* **Ambiente de Desenvolvimento:** Visual Studio Code (VS Code)

## Execução da Aplicação

### Pré-requisitos

* Docker e Docker Compose
* Java 21

### 1. Clonar o repositório

```bash
git clone https://github.com/KamillaRosa1/kamillamariadossantosrosa013641.git
```

### 2. Subir containers com Docker

O projeto utiliza **docker-compose** para orquestrar os serviços de banco de dados (PostgreSQL) e armazenamento de objetos (MinIO).  
Para iniciar os containers em segundo plano:

```bash
docker-compose up -d
```

### 3. Criar bucket manual no MinIO

Após subir o MinIO, acesse o console em [http://localhost:9000](http://localhost:9000) utilizando as credenciais definidas no `application.properties`.

Faça o login com:

* **Usuário:** `admin`
* **Senha:** `password123`

Em seguida, crie manualmente o bucket chamado:

* **Bucket:** `fotos-artistas`

Esse bucket será utilizado pela aplicação para armazenar as imagens de capa dos álbuns.

### 4. Executar a aplicação Spring Boot

Com os containers ativos, rode a aplicação:

```bash
./mvnw spring-boot:run
```

### 5. Testes com Swagger

Acesse a interface técnica para testar os endpoints:

URL: <http://localhost:8080/swagger-ui/index.html>

* **Autenticação:**
  
  * Localize o grupo de endpoints `/api/v1/auth/login`
  * Clique em `POST` e em seguida em `Try it out`
  * No corpo da requisição insira:

```JSON
{
  "username": "admin",
  "password": "password123"
}
```

  * Clique em `Execute`. Na resposta `Server response`, copie o valor do campo token (sem as aspas).
  * Clique em `Authorize` no topo da página, cole o token e confirme.

* **Upload da Capa**
  
  * Localize o endpoint `/api/v1/albuns/{id}/capa`, clique em `POST` e depois em `Try it out`.
  * No campo id, insira o ID de um álbum já cadastrado no banco.
  * No campo `file`, clique em Escolher arquivo e selecione a imagem (ex: harakiri.jpg)
  * Clique em `Execute`.
  * A resposta será um link, copie este link integralmente.

* **Persistência da Imagem e alteração cadastral - PUT:** Utilize o link obtido no passo anterior para preencher o campo `imagemCapaUrl` no PUT de Artistas ou Álbuns para salvar. Após salvar o registro com o link, ao realizar um GET, o campo passará a exibir apenas o nome definitivo do objeto armazenado no MinIO (ex: 12345_harakiri.jpg)
  
  * /api/v1/artistas/{id}: 
  ```JSON
  {
    "id": 0,
    "nome": "string",
    "tipo": "string",
    "albuns": [
      {
        "id": 0,
        "titulo": "string",
        "imagemCapaUrl": "LINK"
      }
    ]
  }
  ```

  * /api/v1/albuns/{id}:
  ```JSON
  {
    "id": 0,
    "titulo": "string",
    "imagemCapaUrl": "LINK",
    "artistas": [
            0
    ]
  }
  ```

* **Consultas e Paginação - GET:** Para os endpoints de listagem, os parâmetros de paginação e ordenação seguem a estrutura abaixo:
  * Para /api/v1/artistas:

    ```JSON
    {
      "page": 0,
      "size": 15,
      "sort": [
        "nome,desc"
      ]
    }
    ```

  * Para /api/v1/albuns:
    ```JSON
    {
      "page": 0,
      "size": 15,
      "sort": [
        "titulo,desc"
      ]
    }
    ```

  Observação: para pesquisa ascendente é só trocar o `desc` pelo `asc` no JSON.

* **Método sem parâmetro:**

  * /api/v1/regionais: Retorna a listagem de regiões disponíveis.

### 6. Testes Unitários

Execute os testes com:

```bash
./mvnw test "-Dspring.profiles.active=test"
```

A API ficará disponível em: `http://localhost:8080`

## Carga Inicial

A aplicação utiliza **Flyway** para versionamento do banco de dados.  
Na migration `V2__insert_initial_data.sql` foram inseridos os dados iniciais de artistas e álbuns conforme especificado no enunciado do projeto (ex.: Serj Tankian, Mike Shinoda, Michel Teló, Guns N’ Roses).

## Status de Implementação

|     Requisitos Gerais     |  Status   |
|:--------------------------|:---------:|
|a) Segurança (CORS)        |[Concluído]|
|b) JWT (5 min + Renovação) |[Concluído]|
|c) POST, PUT, GET          |[Concluído]|
|d) Paginação de álbuns     |[Concluído]|
|e) Consultas Parametrizadas|[Concluído]|
|f) Ordenação Alfabética    |[Concluído]|
|g) Upload de imagens       |[Concluído]|
|h) Armazenamento MinIO     |[Concluído]|
|i) Links pré-assinados     |[Concluído]|
|j) Versionar endpoints     |[Concluído]|
|k) Flyway Migrations       |[Concluído]|
|l) OpenAPI/Swagger         |[Concluído]|

|  Requisitos Sênior                               |   Status  |
|:-------------------------------------------------|:---------:|
|a) Health Checks                                  |[Concluído]|
|b) Testes Unitários                               |[Concluído]|
|c) WebSocket                                      |[Concluído]|
|d) Rate Limit                                     |[Concluído]|
|e) Endpoint Regionais                             |[Concluído]|

## Pendência

**Containerização:** A criação do Dockerfile para o empacotamento do ecossistema permanece como evolução planejada; atualmente, a aplicação deve ser executada via Maven, com o Docker provendo o suporte de dados e storage como já explicado acima em `Execução da aplicação`.

Justificativa: Foi priorizado a entrega dos requisitos e a cobertura de testes dentro do prazo estipulado.
