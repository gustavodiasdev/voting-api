
# Sistema de Votação em Cooperativas

Este é um projeto de exemplo de um sistema de votação para cooperativas, onde cada associado possui um voto e as decisões são tomadas em assembleias. O sistema é construído em Java usando Spring Boot e oferece uma API REST para gerenciar e participar de sessões de votação.

## Funcionalidades

- Cadastrar uma nova pauta
- Abrir uma sessão de votação em uma pauta
- Receber votos dos associados em pautas
- Contabilizar os votos e fornecer o resultado da votação

## Tecnologias Utilizadas

- Java 11
- Spring Boot 2.5
- Spring Data JPA
- H2 Database
- Lombok
- JUnit 5
- Mockito

## Estrutura do Projeto

```plaintext
src/
├── main/
│   ├── java/
│   │   └── com/
│   │       └── example/
│   │           └── voting/
│   │               ├── controller/
│   │               ├── service/
│   │               ├── repository/
│   │               ├── model/
│   │               └── dto/
│   └── resources/
│       └── application.properties
└── test/
    ├── java/
    │   └── com/
    │       └── example/
    │           └── voting/
    │               ├── controller/
    │               └── service/
    └── resources/
```

## Instalação

1. Clone o repositório:

```bash
git clone https://github.com/gustavodiasdev/voting-api.git
cd voting-api
```

2. Compile e execute o projeto usando Maven:

```bash
./mvnw spring-boot:run
```

3. A aplicação estará disponível em `http://localhost:8080`.

## Endpoints da API

### Cadastrar uma nova pauta

- **URL:** `/api/v1/votacao/pautas`
- **Método:** `POST`
- **Body:** `{"descricao": "Descrição da Pauta"}`
- **Resposta:** `200 OK`

### Abrir uma sessão de votação

- **URL:** `/api/v1/votacao/pautas/{pautaId}/sessao`
- **Método:** `POST`
- **Parâmetros:** `dataFim` (opcional)
- **Resposta:** `200 OK`

### Receber votos dos associados

- **URL:** `/api/v1/votacao/pautas/{pautaId}/votos`
- **Método:** `POST`
- **Parâmetros:** `associadoId`, `voto` (boolean)
- **Resposta:** `200 OK`

### Contabilizar os votos e fornecer o resultado da votação

- **URL:** `/api/v1/votacao/pautas/{pautaId}/resultado`
- **Método:** `GET`
- **Resposta:** `200 OK`

## Integração com Sistema Externo

O sistema integra-se com um serviço externo que verifica se um associado pode votar usando o CPF.

- **URL de Verificação:** `GET https://user-info.herokuapp.com/users/{cpf}`

### Exemplo de Verificação

- **Request:** `GET https://user-info.herokuapp.com/users/12345678901`
- **Response:** 
  - `200 OK` com body `{"status": "ABLE_TO_VOTE"}` ou `{"status": "UNABLE_TO_VOTE"}`
  - `404 Not Found` se o CPF for inválido

## Testes

Para executar os testes unitários, use o seguinte comando:

```bash
./mvnw test
```
