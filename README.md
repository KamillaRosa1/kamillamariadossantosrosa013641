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

## Status de Implementação

### Requisitos Gerais

* **a) Segurança (CORS)**
* **b) JWT (5 min + Renovação)**
* **c) POST, PUT, GET**
* **d) Paginação de álbuns**
* **e) Consultas Parametrizadas**
* **f) Ordenação Alfabética**
* **g) Upload de imagens**
* **h) Armazenamento MinIO**
* **i) Links pré-assinados**
* **j) Versionar endpoints**
* **k) Flyway Migrations**
* **l) OpenAPI/Swagger**

### Requisitos Sênior

* **a) Health Checks**
* **b) Testes Unitários**
* **c) WebSocket**
* **d) Rate Limit**
* **e) Endpoint Regionais**