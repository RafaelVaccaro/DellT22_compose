# Programa IT Academy – Processo Seletivo – Edição #22

Este projeto é um sistema full stack construído com **Spring Boot (Java)** no backend e **Angular** no frontend. Ele foi configurado para rodar facilmente usando **Docker Compose**, permitindo a qualquer pessoa iniciar a aplicação com um único comando.

---

## Estrutura do Projeto

DellT22/  
├── backend/ # Projeto Spring Boot  
├── frontend/ # Projeto Angular  
├── docker-compose.yml  
└── README.md  

---

## Como rodar o projeto

> **Pré-requisitos:**
> - [Docker](https://www.docker.com/)
> - [Docker Compose](https://docs.docker.com/compose/)

### Passos:

1. Descompacte o `.zip` do projeto.
2. Abra o terminal na **raiz do projeto** (onde está o `docker-compose.yml`).
3. Execute o seguinte comando:

```bash
docker-compose up --build
```

4. Acesse [http://localhost:4200](http://localhost:4200)
5. Quando quiser desligar o sistema, execute:

```bash
docker-compose down
```
