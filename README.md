# desafio-communication-scheduling

Sistema de exemplo responsável por:

> Realizar o agendamento da comunicação em banco de dados

> Consultar comunicações agendadas no banco

> Remover agendamentos que ainda não foram processados

# Linguagens/frameworks/especificações envolvidos:

> Java

> Spring Boot

> Jpa

> Junit 5, Mockito, Cucumber, Jacoco

> MySQL e H2

> Swagger 2.0

> Maven

> Docker

> REST/JSON

# Contrato

Especificação do serviços está no [`swagger - api.yml`](src/main/resources/swagger/api.yml)

# Estrutura da aplicação

- Controller e Models do serviço são geradas a partir do swagger usando plugin springfox-swagger2
- Classe [`CommunicationApi`](/src/main/java/com/desafio/scheduling/communication/api/CommunicationApi.java) e [`CommunicationDelegateService.java`](/src/main/java/com/desafio/scheduling/communication/api/CommunicationDelegateService.java) delegam o processamento para a classe de serviço [`CommunicationSchedulingServiceImpl.java`](/src/main/java/com/desafio/scheduling/communication/service/impl/CommunicationSchedulingServiceImpl.java) responsável por fazer a integração com o banco de dados via [`CommunicationSchedulingDaoImpl.java`](/src/main/java/com/desafio/scheduling/communication/dao/impl/CommunicationSchedulingDaoImpl.java)
- A base de dados é criada utilizando o flyway-core através do arquivo [`V1_1__Create_TABLES.sql`](/src/main/resources/db/migration/V1_1__Create_TABLES.sql)

# Build

Para buildar (via linha de comando ou IDE):

```
mvn clean package
```

Na IDE é necessário rodar o maven->generate-sources e depois maven->package
  - Maven generate-sources irá gerar as classes de model/controller em \target\generated-sources\swagger\src\main\java
    - Caso as classes não sejam reconhecidas como path pela IDE, é necessário incluí-las no build path
  - Maven package irá gerar a interface CommunicationApi em com.desafio.scheduling.communication.api
  - No pom.xml está configurado o plugin swagger-codegen-maven-plugin que faz este controle

Observação: pode ignorar os 2 alertas de erro no pom, eles não influenciam na compilação

# Teste

## Teste de Integração Automatizado

A classe [`CommunicationSchedulingIntegrationTest.java`](/src/test/java/com/desafio/scheduling/communication/integration/test/CommunicationSchedulingIntegrationTest.java) possui os testes de integração automatizados utilizando cucumber. A base de dados é criado em h2 em memória com o DDL sendo feito pelo flyway.

## Teste Manual

Subir a aplicação utilizando spring boot. Como main class deve utilizar Swagger2SpringBoot e o spring profile dev

Os comandados abaixo via curl podem ser utilizados

```json

--Cria agendamento
curl -i -d '{\"scheduleDate\": \"2030-06-13T17\",\"phoneNumber\": \"11999999999\",\"email\": \"xx@yy.com\",\"sendType\": \"1\",\"message\":\"Menagem de Agendamento de Comunicacao\"}' -H "Content-Type: application/json"  -X POST "http://localhost:8080/communication/"

--Consulta agendamento
curl -i -H 'Content-Type: application/json' -X GET 'http://localhost:8080/communication/1'

--Deleta agendamento 
curl -i -H 'Content-Type: application/json' -X DELETE 'http://localhost:8080/communication/1'

```

Há também um arquivo postman para realizar os testes: [`desafio-communication-scheduling.postman_collection.json`](/src/test/resources/postman/desafio-communication-scheduling.postman_collection.json)

## Teste via Docker usando MySQL

Pré Requisitos:
- [Instalar Docker](https://docs.docker.com/desktop/)
- Inicializar o docker

1. Compilar o projeto usando mvn package

2. No terminal, acessar diretorio raiz do repositório e rodar o comando abaixo que cria as imagens e sobe os containers 

```
docker-compose up
```

Obs: No arquivo [`docker-compose.yml`](/docker-compose.yml) é criada uma imagem docker do mysql e do desafio-communication-scheduling e iniciado os containers

3. Executar os testes usando os comandos de curl ou via postman conforme seção anterior 

4. Para se conectar com o banco de dados utilize user/password@localhost:3306/db

5. Outros comandos para docker compose

Comando abaixo sobe os containers uma vez que já tenha sido executado o up

```
docker-compose start
```

Comando abaixo finaliza a execução dos containers

```
docker-compose stop
```

Comando abaixo remove os containers e imagens

```
docker-compose rm
```

## Logs

Logs são criados no diretório [C:]/appl/logs/desafio-communication-scheduling.log

# Cobertura de Testes - Jacoco

Com o plugin do maven do jacoco é possível ver a cobertura de testes. 

Basta executar um mvn test ou package e abrir o arquivo \target\site\jacoco\index.html em um browser

# Evoluções possíveis

- Autenticação
- Operação de atualização
- Consulta por data
- Consulta por destinatário

