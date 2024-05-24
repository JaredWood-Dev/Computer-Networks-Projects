import java.net.*;
import java.io.*;
import java.util.Scanner;

public class StudentDatabaseClient {

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        int port = Integer.parseInt(args[0]); //Port in which communication will occur
        String serverAddress = args[1]; //Remember this is assigned through DHCP, may need to change

        Scanner scan = new Scanner(System.in); //Import the scanner for user input

        Socket sock = new Socket(serverAddress, port);


        ObjectOutputStream out = new ObjectOutputStream(sock.getOutputStream());
        ObjectInputStream in = new ObjectInputStream(sock.getInputStream());

        String serverData = "-1";
        boolean responseExpected = false;

        while (!serverData.equals("")) //While the received data from the server is not whitespace
        {

            serverData = (String)in.readObject();

            System.out.println(serverData);

            responseExpected = (boolean)in.readObject();

            if (responseExpected)
            {
                System.out.print("> ");
                out.writeObject(scan.nextLine());
                out.flush();
            }

        }

        System.out.println("Thank you for using the Student database manager! Closing Program...");

        sock.close();
    }
}