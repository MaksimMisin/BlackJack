# Blackjack
A RESTful blackjack web service and a Java client.

## Setting up
The server is pakcaged as a .war file and is located in `blackjack_server/target/blackjack_server.war`. In principle it can be deployed on a standard Tomcat web server without any modifications. Afterwards you simply need to run a standalone client, found in `blackjack_client/target/blackjack_client-0.0.1-SNAPSHOT.jar` and at the login screen specify the web address of the server running the game.

To build the server or client yourself you need to point your terminal to the corresponding root directory and invoke
```bash
mvn clean
mvn package -DskipTests
```
This will re-build both java archives and place them in the previoulsy specified locations.

### Configuration
Both server and a client contain `logback.xml` file, located in `src/main/resources` directory. Using it one can configure both the level as well as the destination of log messages (by default they are printed in a console). Additionally, blackjack_server contains `config.properties` file that can be used to change security settings and adjust minimum bet.


## API calls

The base target for all api calls is `/blackjack_server/webapi`. Bellow is the summary of available requests

| URI                 | POST               | GET            | DELETE      |
| ------------------- | ------------------ | -------------- | ----------- | 
| /players            | [Create new player](#post-players)  | [List players](#get-players)   |             |
| /players/john       |                    | [Show John](#get-playersjohn)      | [Delete John](#delte-playersjohn) |
| /players/john/topUp | [Add money to balance](#post-playersjohn-topUp)  |
| /players/john/bet   | [Start new game with specified bet](#post-playersjohnbet) |
| /players/john/hit   | [Get extra card](#post-playersjohn-hit) |
| /players/john/stand | [Finish game](#post-playersjohnstand)    |

An example of error message can be found [here](#error-message).

#### GET `/players`
----
  List all players

* **Success Response:**

  * **Code:** 200  
    **Content:** `[{"name":"sam","balance":3.0},{"name":"john","balance":405.0}]`
    

#### GET `/players/john`
----
  Show John

* **Success Response:**

  **Code:** 200  
  **Content:** `{"name":"john","balance":405.0}`

#### POST `/players`
----
  Create a new player
  
* **Sample Call:**

  **Content:** `{"name":"john"}`
* **Success Response:**

   **Code:** 201  
   **Content:** `{"playerId":"cc993800-18ac-46bf-b064-996b9d7210c1","name":"john","balance":0.0}`

#### POST `/players/john/topUp`
----
  Add money to John's balance
  
* **Sample Call:**

  **Content:** `{"playerId":"cc993800-18ac-46bf-b064-996b9d7210c1","topUp":300}`
* **Success Response:**

   **Code:** 200  
   **Content:** `{"name":"john","balance":300.0}`
   
#### POST `/players/john/hit`
----
  Start new game and bet some money
  
* **Sample Call:**

  **Content:** `{"playerId":"cc993800-18ac-46bf-b064-996b9d7210c1","bet":10}`
* **Success Response:**

   **Code:** 200  
   **Content:** 
```javascript
{"name":"john",
 "balance":290.0,
 "table":{
	"bet":10.0,
    "playerHand":{
    	"cards":[{"rank":"QUEEN","suit":"SPADES"},{"rank":"FIVE","suit":"HEARTS"}]},
    "dealerHand":{
    	"cards":[{"rank":"JACK","suit":"SPADES"}]},
 	"finished":false,"playerWinnings":0.0}
 }
 ```

#### POST `/players/john/hit`
----
  Get another card
  
* **Sample Call:**

  **Content:** `{"playerId":"cc993800-18ac-46bf-b064-996b9d7210c1"}`
* **Success Response:**

   **Code:** 200  
   **Content:** 
```javascript
{"name":"john",
 "balance":290.0,
 "table":{
 	"bet":10.0,
    "playerHand":{
    	"cards":[{"rank":"QUEEN","suit":"SPADES"},{"rank":"FIVE","suit":"HEARTS"},{"rank":"SIX","suit":"HEARTS"}]},
    "dealerHand":{
    	"cards":[{"rank":"JACK","suit":"SPADES"}]},
	"finished":false,"playerWinnings":0.0} 
}
```

#### POST `/players/john/stand`
----
  Finish the game
  
* **Sample Call:**

  **Content:** `{"playerId":"cc993800-18ac-46bf-b064-996b9d7210c1"}`
* **Success Response:**

   **Code:** 200  
   **Content:** 
```javascript
{"name":"john",
 "balance":310.0,
 "table":{
 	"bet":10.0,
    "playerHand":{
    	"cards":[{"rank":"QUEEN","suit":"SPADES"},{"rank":"FIVE","suit":"HEARTS"},{"rank":"SIX","suit":"HEARTS"}]},
    "dealerHand":{
    	"cards":[{"rank":"JACK","suit":"SPADES"},{"rank":"EIGHT","suit":"HEARTS"}]},
	"finished":true,"playerWinnings":20.0} 
}
 ```

#### DELETE `/players/john`
----
  Log out the player
  
* **Sample Call:**

  **Header:** `playerId: cc993800-18ac-46bf-b064-996b9d7210c1`
* **Success Response:**

   **Code:** 200  
   **Content:**  `{"name":"john", "balance":310.0}`
   
### Error message

In case a player wants to perform an illegal action the server returns an error
message that typically looks like this:
```js
{"timestamp":"2016-11-23T13:06:50.628+02:00",
"responseStatus":"NOT_FOUND",
"statusCode":404,
"errorMessage":"Requested player was not found."}
```


