# Algorithm based share taxi TANNAE
This service provides taxi service which passengers can ride taxi together with other passengers.

Server will match with passengers that moves on similar path.

The main purpose is to pay low price than general taxi service.


## Development Plan
* **Topic**   
TANNAE project is a updated taxi service which provides sharing service.  
Passengers can share same taxi which moves on similar path.   
When single service ends passengers who shared same taxi pays the total fee in proportion to distance.  
By this service passengers will pay 40~50% lower price than using normal taxi service.  

* **Requirements**    
*Main service* : Allows passenger to use the service and drivers can use navigation.  
*Sub service* : Provides account|lost & found|QnA(FAQ)|history|charge services.   

* **Tools**  
  - ***Client[Android]***   
  IDE : Android Studio    
  Language : Java   
  UI design : XML     
  - ***Server[AWS EC2 Ubuntu]***   
  IDE : Visual Studio Code    
  Framework : Express.js(Node.js)   
  Language : Javascript
  Database : InnoDB engine based MySQL
  - ***Network Communication(HTTP)***   
  Retrofit2 : Client(request) to Server(response) communication library    
  Socket.io : Bi-directional communication library    
  - ***Code Manage***   
  GitHub    
  
* **Team**  
***Choi Jae Won[Leader]*** &rarr; Back-end Server Developer  
***Kim Dong Hyeon*** &rarr; Front-end UI Developer  
***Lee Seung Chan*** &rarr; Front-end Java Develop
