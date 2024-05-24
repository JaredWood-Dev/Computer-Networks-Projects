How to run the files:
1. Extract StudentDatabaseServer.java, StudentDatabaseClient.java, and database.txt into the files accesible by the Texas State Linux servers.
2. Open a command line and ssh into the EROS server first using the command: 'ssh [your username]@eros.cs.txstate.edu'
3. Navigate to the location of the files using the 'cd' command. Example: 'cd NetworksProjects/Project1'
4. Run the java file StudentDatabseServer.java using the following command: 'java StudentDatabaseServerUDP.java'.
5. The server will then open and wait for a client to connect.
6. Open another command line and ssh into the ZEUS server next using the following command: 'ssh [your username]@zeus.cs.txstate.edu'
7. Navigate to the location of the files using the 'cd' command. Example: 'cd NetworksProjects/Project1'
8. Run the java file StudentDatabaseClient.java using the following command: 'java StudentDatabaseServer.java'.
9. Once connected the client will be displayed with the menu with varying options.

NOTES: 
-The Server and Port are hardcoded. The Client goes on ZEUS and the Server goes on EROS. Ensure to launch the server first.
-When the client reqeuests input ensure to provide the correct input for the data requested. If menu choice/id/score it expects a number. If first/last name it expects a string.
-Ensure database.txt is in the same directory as the java files and is named 'database.txt'