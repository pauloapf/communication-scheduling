Feature: Communication Scheduling Service

  @completeCrud
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


