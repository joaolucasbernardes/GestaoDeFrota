# API de Gestão de Frotas com Microserviços

![Java](https://img.shields.io/badge/Java-17-blue?style=for-the-badge&logo=openjdk)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.3.1-brightgreen?style=for-the-badge&logo=spring)
![Maven](https://img.shields.io/badge/Maven-3.9-red?style=for-the-badge&logo=apachemaven)
![Docker](https://img.shields.io/badge/Docker-blue?style=for-the-badge&logo=docker)
![Kafka](https://img.shields.io/badge/Apache_Kafka-black?style=for-the-badge&logo=apachekafka)

Este projeto é um sistema de gestão de frotas desenvolvido com uma arquitetura de microserviços, como solução para um desafio técnico. A aplicação permite o cadastro de veículos e motoristas, o registro de abastecimentos e o cálculo de métricas de desempenho dos veículos, utilizando Apache Kafka para a comunicação assíncrona entre os serviços.

## 📜 Sumário

* [Arquitetura](#-arquitetura)
* [Tecnologias Utilizadas](#-tecnologias-utilizadas)
* [Funcionalidades Implementadas](#-funcionalidades-implementadas)
* [Como Executar o Projeto](#-como-executar-o-projeto)
    * [Pré-requisitos](#-pré-requisitos)
    * [Via Docker Compose (Método Recomendado)](#-via-docker-compose-método-recomendado)
* [Endpoints da API (Documentação Interativa)](#-endpoints-da-api-documentação-interativa)
* [Autor](#-autor)

## 🏗️ Arquitetura

O sistema é composto por três microserviços principais, cada um containerizado com Docker, que se comunicam de forma síncrona (REST) e assíncrona (Kafka).

* **Veículos Service (porta 8080)**: Responsável pelo cadastro, consulta e cálculo de desempenho dos veículos. Atua como consumidor de eventos Kafka para manter o hodômetro e o histórico de abastecimentos dos veículos atualizados.
* **Motoristas Service (porta 8081)**: Responsável pelo cadastro e consulta dos motoristas da frota.
* **Abastecimentos Service (porta 8082)**: Orquestra o registro de um novo abastecimento. Ele valida a existência do veículo e do motorista (via chamadas REST síncronas) e, em seguida, publica o evento de abastecimento em um tópico do Kafka para processamento assíncrono.

O **Veículos Service** consome os eventos do tópico `abastecimentos-registrados` para atualizar o hodômetro e o histórico de cada veículo, garantindo que os dados permaneçam consistentes e atualizados de forma reativa, sem acoplar diretamente a lógica de abastecimento à de veículos.

## ✨ Tecnologias Utilizadas

Este projeto foi construído utilizando as seguintes tecnologias:

* **Java 17**: Versão da linguagem Java.
* **Spring Boot 3.3.1**: Framework principal para a construção da API.
* **Apache Kafka**: Plataforma de streaming de eventos para comunicação assíncrona.
* **Maven**: Gerenciador de dependências e build do projeto.
* **Docker & Docker Compose**: Para containerização e orquestração de toda a aplicação e infraestrutura.
* **SpringDoc OpenAPI (Swagger)**: Para documentação interativa e testes da API.
* **Spring Boot Actuator**: Para endpoints de monitoramento de saúde da aplicação (`/actuator/health`).
* **Lombok**: Para redução de código boilerplate.
* **JUnit 5, Mockito & MockRestServiceServer**: Para os testes de integração.

## 🚀 Funcionalidades Implementadas

* **Gerenciamento de Veículos**:
    * Cadastro de novos veículos com validação de placa única.
    * Consulta de veículos por ID ou listagem completa.
    * Cálculo de desempenho do veículo (consumo médio Km/L e custo médio por Km).
    * Consulta do histórico de abastecimentos de um veículo.
* **Gerenciamento de Motoristas**:
    * Cadastro de novos motoristas com validação de CNH única.
    * Consulta de motoristas por ID.
* **Registro de Abastecimentos**:
    * Endpoint para registrar um novo abastecimento.
    * Validação síncrona da existência do veículo e do motorista antes do registro.
    * Validação de regras de negócio (hodômetro do abastecimento deve ser maior que o atual).
* **Comunicação Assíncrona**:
    * Publicação de um evento no tópico `abastecimentos-registrados` após um novo abastecimento.
    * Consumo do evento pelo `veiculos-service` para atualizar o hodômetro e o histórico do veículo.
* **Qualidade e Boas Práticas**:
    * **Documentação Interativa** com Swagger UI para cada microserviço.
    * **Testes de Integração** para os endpoints críticos, validando cenários de sucesso e de falha.
    * **Tratamento de Exceções Global** (`@RestControllerAdvice`) para padronizar as respostas de erro da API.
    * **Health Checks** (`/actuator/health`) em todos os serviços para monitoramento.
    * **Containerização completa** da aplicação e infraestrutura.

## ⚙️ Como Executar o Projeto

### Pré-requisitos

Para executar o projeto, você precisa ter o **Docker** e o **Docker Compose** instalados e em execução no seu sistema.

### Via Docker Compose (Método Recomendado)

Este é o método mais simples e recomendado, pois orquestra todos os contêineres necessários (Kafka, Zookeeper e os três microserviços) com um único comando.

1.  **Clone o repositório:**
    ```bash
    git clone https://github.com/joaolucasbernardes/GestaoDeFrota.git
    ```

2.  **Suba os contêineres com Docker Compose:**
    Navegue até a raiz do projeto (onde o arquivo `docker-compose.yml` está localizado) e execute o seguinte comando:
    ```bash
    docker-compose up --build
    ```
    O comando irá construir as imagens de cada serviço e iniciar todos os contêineres. Aguarde até que os logs se estabilizem.

A aplicação estará disponível nas seguintes portas:
* **Veículos Service**: `http://localhost:8080`
* **Motoristas Service**: `http://localhost:8081`
* **Abastecimentos Service**: `http://localhost:8082`

## 📋 Endpoints da API (Documentação Interativa)

Após iniciar a aplicação, a documentação completa e interativa de cada microserviço pode ser acessada via Swagger UI, onde você pode visualizar e **testar todos os endpoints diretamente do navegador**:

➡️ **Veículos Service**: **[http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)**

➡️ **Motoristas Service**: **[http://localhost:8081/swagger-ui.html](http://localhost:8081/swagger-ui.html)**

➡️ **Abastecimentos Service**: **[http://localhost:8082/swagger-ui.html](http://localhost:8082/swagger-ui.html)**

Abaixo estão os principais endpoints de cada serviço:

---

### 🚐 Veículos Service

| Verbo | Rota | Descrição |
| :--- | :--- | :--- |
| `POST` | `/veiculos` | Cadastra um novo veículo. |
| `GET` | `/veiculos` | Lista todos os veículos cadastrados. |
| `GET` | `/veiculos/{id}` | Busca um veículo pelo seu ID. |
| `GET` | `/veiculos/{id}/desempenho` | Calcula o desempenho do veículo. |
| `GET` | `/veiculos/{id}/abastecimentos`| Lista os abastecimentos de um veículo. |

---

### 👨‍✈️ Motoristas Service

| Verbo | Rota | Descrição |
| :--- | :--- | :--- |
| `POST` | `/motoristas` | Cadastra um novo motorista. |
| `GET` | `/motoristas/{id}`| Busca um motorista pelo seu ID. |

---

### ⛽ Abastecimentos Service

| Verbo | Rota | Descrição |
| :--- | :--- | :--- |
| `POST` | `/abastecimentos` | Registra um novo abastecimento. |

## 👤 Autor

**JOÃO LUCAS BERNARDES**

* **LinkedIn**: https://www.linkedin.com/in/joaolucasbernardes/