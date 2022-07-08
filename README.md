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
Main service : Allows passenger to use the service and drivers can use navigation.  

Sub service : Provides account|lost & found|QnA(FAQ)|history|charge services.   
```   

* **Schedule**    
  <p align="center">
    <img src= "https://user-images.githubusercontent.com/87649850/174002744-2636aba7-25db-48fd-8648-a98ea1461184.png">    
    <img src= "https://user-images.githubusercontent.com/87649850/174002754-3184ad2d-1cac-46e8-89b6-cbf47be43c6e.png">  
  </p>


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

***Choi Jae Won[Leader - Back-end Developer]***  
```
Server & Algorithm Develop  
  
Build Database 
  
Client-Server Network Develop
```
***Kim Dong Hyun[Front-end UI Developer]***  
```
UI/UX Design Develop
  
Map Develop
```  
***Lee Seung Chan[Front-end Java Developer]***  
```
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
  <p align="center">
    <img src= "https://user-images.githubusercontent.com/87649850/172292202-1ee5b828-4e94-408d-af57-15467c80557c.png">
  </p>
  
  - ***Use Case Description***    
  <p align="center">
    <img src= "https://user-images.githubusercontent.com/87649850/172345551-b6b8a4e2-9673-4b3f-a238-0f6f96018275.gif">
  </p>

* **Diagram**  
  - ***Sequence Diagram***   
  <p align="center">
    <img src= "https://user-images.githubusercontent.com/87649850/172347060-dc84d195-fd34-4250-9a32-1995beba0bcc.gif">
  </p>
  
  - ***Screen FLow Diagram***   
  <p align="center">
    <img src= "https://user-images.githubusercontent.com/87649850/172292634-80fb4887-c3df-44ab-9e75-caf781c32c18.png">
  </p>

  - ***Class Diagram***   
  <p align="center">
    <img src= "https://user-images.githubusercontent.com/87649850/172292660-6c32670f-a6f7-4f39-aa6b-cd23fac7f2ab.png">
  </p>
  
## Design 
  
* **Diagram**     
  - ***Class Diagram***   
  <p align="center">
    <img src= "https://user-images.githubusercontent.com/87649850/172328033-63d1e21d-e920-4712-98c0-5cd3132071da.png">
  </p>
  
  - ***Sequence Diagram[Example(Payment & Assessment)]***   
  <p align="center">
    <img src= "https://user-images.githubusercontent.com/87649850/172348272-9fc28398-fbb6-4270-88b7-f5babfd05498.gif">
  </p>

* **User Interface**    
<p align="center">
  <img src= "https://user-images.githubusercontent.com/87649850/172342692-03dc58b5-c009-41bc-bcf3-4390f3b44449.gif">
</p>

* **Database Table**    
<p align="center">
  <img src= "https://user-images.githubusercontent.com/87649850/172344271-9d9ce7c1-8b66-44ad-9c33-97e04a48cf1d.gif">
</p> 

## Implementation   

* **Link**  

  Android Code Link   
  https://github.com/codesver/Capstone/tree/master/TANNAE

  Server Code Link  
  https://github.com/codesver/Capstone/tree/master/Server

* **Structure**   
<p align="center">
<img src="https://user-images.githubusercontent.com/87649850/172361065-ad2c5631-bbac-4379-9b80-c1868af0a9de.png">
</p>    

* **Implementation Size**     
<p align="center">
<img src="https://user-images.githubusercontent.com/87649850/172361142-fb9e5c29-227a-4617-bf72-dcf2d476c34d.png">
</p>    

* **Demo**  
https://user-images.githubusercontent.com/87649850/174003448-78cb473b-f622-4312-b6eb-e537aea6a30f.mp4   

* **Efficiency**
  - ***Test Method***
  ```
    Number of test case is 100 and the method of testing is as follows. There are three passengers who rides same taxi for service.
  Single test case ends when all three passengers get on and get off. The order in which service is requested remains the same,
  but all cases of boarding and getting off positions for each passenger are tested. In addition, since the final fee is settled in
  proportion to the distance, the test is conducted in consideration of the distance, and the service use area is limited to Gwangju.
  ```
  - ***Test Result***
  ```
    As a result of creating and testing of 100 test cases, the rate discount rate for the first service requester is 43%.
  The rate discount rate for the second service requester is 52%. The rate discount rate for the third service requester is 46%,
  and the average discount rate is 46%. This figure corresponds to 40-50% or more of the initially set rate discount rate,
  and it is judged that the algorithm has been implemented successfully.
  
    The reason why the discount rate for the second passenger is greater than that of the other two passengers is
  that the first passenger and the third passenger have an average number of solo rides at the beginning and end,
  while the second passenger does not have a relatively large number of solo rides in more tests. For this reason,
  Version 2.0 is expected to reduce the rate ratio of first and last passengers and to reduce the rate burden of first and last
  passengers by applying a method of calculating the rate that other users share.
  ```   
  ![image](https://user-images.githubusercontent.com/87649850/172363510-2a9480a6-b048-4b9b-8ae5-22a23794c7ab.png)   
