# Algorithm based share taxi TANNAE
```
This service provides taxi service which passengers can ride taxi together with other passengers.

Server will match with passengers that moves on similar path.

The main purpose is to pay low price than general taxi service.
```

## Development Plan
* **Topic**   
```
TANNAE project is a updated taxi service which provides sharing service.  
Passengers can share same taxi which moves on similar path.   
When single service ends passengers who shared same taxi pays the total fee in proportion to distance.  
By this service passengers will pay 40~50% lower price than using normal taxi service.  
```

* **Requirements**    
```
*Main service* : Allows passenger to use the service and drivers can use navigation.  
*Sub service* : Provides account|lost & found|QnA(FAQ)|history|charge services.   
```

* **Tools**  
  - ***Client[Android]***   
  ```
  IDE : Android Studio    
  Language : Java   
  UI design : XML     
  ```
  - ***Server[AWS EC2 Ubuntu]***  
  ``` 
  IDE : Visual Studio Code    
  Framework : Express.js(Node.js)   
  Language : Javascript
  Database : InnoDB engine based MySQL
  ```
  - ***Network Communication(HTTP)***   
  ```
  Retrofit2 : Client(request) to Server(response) communication library    
  Socket.io : Bi-directional communication library    
  ```
  - ***Code Manage***   
  ```
  GitHub 
  ```
  
* **Team**  

***Choi Jae Won[Leader]***  
```
Back-end Developer  
  Server & Algorithm Develop
  Build Database 
  Client-Server Network Develop
```
***Kim Dong Hyeon***  
```
Front-end UI Developer
  UI/UX Design * Develop
  Map Develop
```  
***Lee Seung Chan***  
```
Front-end Java Develop
  Client View & Event handler Develop
  SharedPreferences Develop
```

## Analysis 

* **Requirements Statement**
  - ***Functional Requirement***  
  ```
  Function for Account    : Sign In | Sign Out | Register | Withdrawal | Find Account | Edit Account
  Function for Driver     : Check service request | Change operation status
  Function for Passenger  : Request Service | Rate driver
  Function for User       : Lost & Found | QnA(FAQ) | Check History | Charge point
  ```
  - ***Non-Functional Requirement***
  ```
  Client/Server application   : Project is developed based on Client/Server.
  User Familiarity            : Develop Ui/UX intuitively for easy using.
  System Scalability          : Easy maintenance
  System Connectivity         : Server must track users position and update database in real time
  System Response Speed       : Response time must be faster than 1s for single request
  Security                    : Users private info must be secured
  ```
* **Use Case**  
  - ***Use Case Diagram***    
  ![image](https://user-images.githubusercontent.com/87649850/172292202-1ee5b828-4e94-408d-af57-15467c80557c.png)   
  - ***Use Case Description***    
  ![ezgif-4-b67b3c8901](https://user-images.githubusercontent.com/87649850/172345551-b6b8a4e2-9673-4b3f-a238-0f6f96018275.gif)  
* **Diagram**  
  - ***Sequence Diagram***   
  ![ezgif-4-b616ccf8b3](https://user-images.githubusercontent.com/87649850/172347060-dc84d195-fd34-4250-9a32-1995beba0bcc.gif)   
  - ***Screen FLow Diagram***   
  ![image](https://user-images.githubusercontent.com/87649850/172292634-80fb4887-c3df-44ab-9e75-caf781c32c18.png)   
  - ***Class Diagram***   
  ![image](https://user-images.githubusercontent.com/87649850/172292660-6c32670f-a6f7-4f39-aa6b-cd23fac7f2ab.png)   
  
## Design 
  
* **Diagram**     
  - ***Class Diagram***   
  ![image](https://user-images.githubusercontent.com/87649850/172328033-63d1e21d-e920-4712-98c0-5cd3132071da.png)   
  - ***Sequence Diagram[Example(Payment & Assessment)]***   
  ![ezgif-4-21f048d82e](https://user-images.githubusercontent.com/87649850/172348272-9fc28398-fbb6-4270-88b7-f5babfd05498.gif)   
* **User Interface**    
![ezgif-4-9b27879f3d](https://user-images.githubusercontent.com/87649850/172342692-03dc58b5-c009-41bc-bcf3-4390f3b44449.gif)

* **Database Table**    
![ezgif-4-975930555d](https://user-images.githubusercontent.com/87649850/172344271-9d9ce7c1-8b66-44ad-9c33-97e04a48cf1d.gif)
