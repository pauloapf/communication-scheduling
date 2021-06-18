Feature: Communication Scheduling Service

  @CompleteCrud
  Scenario Outline: Complete CRUD
    When I create a communication scheduling with "<email>","<phoneNumber>","<sendType>","<message>" 
    Then I validate that create returns "<email>","<phoneNumber>","<sendType>","<message>",<id>,<httpStatus>,"<internalCode>"
    When I get a communication scheduling with "<id>" 
    Then I get that returns "<email>","<phoneNumber>","<sendType>","<message>",<id>,<httpStatus>,"<internalCode>"
    When I delete a communication scheduling with "<id>" 
    Then I delete that returns <httpStatus>
    When I get a communication scheduling after delete with "<id>" 
    Then I get after delete that returns
    
    Examples: 
      | email  	| phoneNumber	 | sendType   | message 		| id 	| httpStatus 	| internalCode	|
      | x@x.com	|   				   | 1   				| Mensagem A 	| 1		| 201					|								|
      |  				| 11900000000  | 2   				| Mensagem B 	| 2		| 201					|								|


#Outros cenarios a serem testados
## Criação agendamento com data schedule inferior ou igual a data atual
## Criação agendamento com com destinario incorreto de acordo com o sendType e capturar o internalcode/httpstatus
## Criação agendamento sem campos obrigatorios (classes geradas pelo swagger automaticamente validam campos obrigatorios)
## Tentativa de remoção de agendamento que não existe
## Tentativa de remoção de agendamento já processado
## Consulta de agendamento que não existe