# desafio-communication-scheduling

Serviço responsável por:
> Realizar o agendamento da comunicação
> Consultar comunicações agendadas
> Remover agendamentos que ainda não foram processados

# Contrato

Especificação do serviços está no [`swagger`](\src\main\resources\swagger\api.yml)

# Estrutura

- Controller e Models do serviço são geradas a partir do swagger usando plugin springfox-swagger2
- Classe [`CommunicationApi`](g\src\main\java\com\desafio\scheduling\communication\api\CommunicationApi.java) e [`CommunicationDelegateService.java`](\src\main\java\com\desafio\scheduling\communication\api\CommunicationDelegateService.java) delegam o processamento para a classe de serviço [`CommunicationSchedulingServiceImpl.java`](\src\main\java\com\desafio\scheduling\communication\service\impl\CommunicationSchedulingServiceImpl.java) responsável por fazer a integração com o banco de dados via [`CommunicationSchedulingDaoImpl.java`](\src\main\java\com\desafio\scheduling\communication\dao\impl\CommunicationSchedulingDaoImpl.java)
- A base de dados é criada utilizando o flyway-core 

# Build

Para buildar basta rodar 

```
mvn clean package
```

Na IDE (eclipse ou sts) é necessário rodar o maven->generate-sources e após o maven package
  - Maven generate-sources irá gerar as classes de model/controller em \target\generated-sources\swagger\src\main\java
    - Caso as classes não apareçam, é necessário incluir no build path
  - Maven package irá gerar a interface CommunicationApi em com.desafio.scheduling.communication.api
  - No pom.xml está configurado o plugin swagger-codegen-maven-plugin que faz este controle

Observação: pode ignorar os alertas de erro no pom, eles não influenciam na compilação

# Testando

## Teste Automatizado de Integração
A classe [`CommunicationSchedulingIntegrationTest.java`](\src\test\java\com\desafio\scheduling\communication\integration\test\CommunicationSchedulingIntegrationTest.java) possui os testes de integração automatizados utilizando cucumber. A base de dados é criado no h2 em memoria com o DDL sendo feito pelo flyway.

## Teste Manual

Subir a aplicação usando spring boot, como main class deve utilizar Swagger2SpringBoot e o profile de dev

Rodar os comandos abaixo
```json
--Cria agendamento
curl -i -d '{\"scheduleDate\": \"2030-06-13T17\",\"phoneNumber\": \"11999999999\",\"email\": \"xx@yy.com\",\"sendType\": \"1\",\"message\":\"Menagem de Agendamento de Comunicacao\"}' -H "Content-Type: application/json"  -X POST "http://localhost:8080/communication/"

--Consulta agendamento
curl -i -H 'Content-Type: application/json' -X GET 'http://localhost:8080/communication/1'

--Deleta agendamento 
curl -i -H 'Content-Type: application/json' -X DELETE 'http://localhost:8080/communication/1'

``

## Teste via Docker usando MySQL

Pendente

# Cobertura de Testes - Jacoco

Com o plugin do maven do jacoco é possível ver a cobertura de testes. Basta executar um mvn test ou package e abrir o arquivo \target\site\jacoco\index.html em um browser