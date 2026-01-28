# GameLibrary-Prog3-Berta

### 1 Systemrequirements

Java Version: openjdk version "21.0.9" <br>
Maven Version: Apache Maven 3.9.9 <br>

### 2 Installation

1. Clone the Repository
    - In order to clone the repository copy this command into your terminal: <br>
    > git clone git@github.com:ctrl-404/GameLibrary.git

2. Build the Project
    - Use Maven to download dependencies and compile the project. Run this command in your terminal: <br>
    > mvn clean install

3. Run the Application
    - To run the application use this command in your terminal: <br>
    > mvn exec:java -Dexec.mainClass="de.htwsaar.Berta.Main"

<br>

Note: <br>
   If your are seeing errors regarding missing database classes you need to run the jooq first before compiling again. <br>
   1. Run: <br>
>   mvn generate-sources <br>
   2. Re-compile: <br>
>   mvn clean compile <br>
   3. Re-run the application: <br>
>   mvn exec:java -Dexec.mainClass="de.htwsaar.Berta.Main"
