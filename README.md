# Projeto Desenvolvedor Back End - API Artistas & Álbuns

Este projeto consiste em uma API REST para gerenciamento de artistas e álbuns, com foco em segurança JWT, integração com armazenamento S3 (MinIO).

## Tecnologias e Arquitetura

* **Linguagem:** Java 21 (Spring Boot 4.0.2)

* **Banco de Dados:** PostgreSQL 15

* **Versionamento de DB:** Flyway (Migrações e Carga Inicial)

* **Armazenamento de Objetos:** MinIO (API S3 via AWS SDK v2 para Java)

* **Segurança:** Spring Security + JWT (JSON Web Token)

* **Client de Testes:** Thunder Client (uma extensão do Visual Studio Code, que oferece uma interface amigável para testes de APIs)

* **Ambiente de Desenvolvimento:** Visual Studio Code (VS Code)

## Execução da Aplicação

### Pré-requisitos

* Docker e Docker Compose
* Java 21+

### 1. Clonar o repositório

```bash
git clone https://github.com/KamillaRosa1/kamillamariadossantosrosa013641.git
```

### 1. Subir containers com Docker

O projeto utiliza **docker-compose** para orquestrar os serviços de banco de dados (PostgreSQL) e armazenamento de objetos (MinIO).  
Para iniciar os containers em segundo plano:

```bash
docker-compose up -d
```

### 2. Criar bucket manual no MinIO

Após subir o MinIO, acesse o console em [http://localhost:9000](http://localhost:9000) utilizando as credenciais definidas no `application.properties`.

Faça o login com:

* **Usuário:** `admin`
* **Senha:** `password123`

Em seguida, crie manualmente o bucket chamado:

* **Bucket:** `fotos-artistas`

Esse bucket será utilizado pela aplicação para armazenar as imagens de capa dos álbuns.

### 3. Executar a aplicação Spring Boot

Com os containers ativos, rode a aplicação:

```bash
mvn spring-boot:run
```

A API ficará disponível em: `http://localhost:8080`

## Carga Inicial

A aplicação utiliza **Flyway** para versionamento do banco de dados.  
Na migration `V2__insert_initial_data.sql` foram inseridos os dados iniciais de artistas e álbuns conforme especificado no enunciado do projeto (ex.: Serj Tankian, Mike Shinoda, Michel Teló, Guns N’ Roses).

## Status de Implementação

|     Requisitos Gerais     |  Status   |
|:--------------------------|:---------:|
|a) Segurança (CORS)        | [ ]       |
|b) JWT (5 min + Renovação) | [Parcial] |
|c) POST, PUT, GET          | [ ]       |
|d) Paginação de álbuns     | [Parcial] |
|e) Consultas Parametrizadas| [Parcial] |
|f) Ordenação Alfabética    | [ ]       |
|g) Upload de imagens       | [ ]       |
|h) Armazenamento MinIO     |[Concluído]|
|i) Links pré-assinados     | [ ]       |
|j) Versionar endpoints     | [Parcial] |
|k) Flyway Migrations       |[Concluído]|
|l) OpenAPI/Swagger         | [ ]       |

|  Requisitos Sênior  |   Status  |
|:--------------------|:---------:|
|a) Health Checks     |[Concluído]|
|b) Testes Unitários  | [ ]       |
|c) WebSocket         | [ ]       |
|d) Rate Limit        | [ ]       |
|e) Endpoint Regionais| [ ]       |
