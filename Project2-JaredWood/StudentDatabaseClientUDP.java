import java.io.IOException;
import java.net.*;
import java.util.Scanner;

public class StudentDatabaseClientUDP
{
    public static void main(String[] args) throws IOException {
        Scanner scan = new Scanner(System.in);

        //Open the client socket for network connectivity
        DatagramSocket clientSocket = new DatagramSocket();

        //Set the destination address to that of the client
        //TODO: Replace local host with eros.txstate.edu
        InetAddress serverAddr = InetAddress.getByName("eros.cs.txstate.edu");
        int serverPort = 12345;

        byte[] initialData = "Client Connected.".getBytes();
        DatagramPacket initialPacket = new DatagramPacket(initialData, initialData.length, serverAddr, serverPort);

        //Send the data
        clientSocket.send(initialPacket);

        //Time to get data from the server
        byte[] firstData = new byte[1024];

        DatagramPacket firstPacket = new DatagramPacket(firstData, firstData.length);

        clientSocket.receive(firstPacket);

        //Convert the packet back ito a string
        String firstResponse = new String(firstPacket.getData(), 0, firstPacket.getLength());

        boolean expectResponse = true;

        if (firstResponse.contains("EXPECT_RESPONSE"))
        {
            expectResponse = true;
            firstResponse = firstResponse.replaceAll("EXPECT_RESPONSE", "").trim();
        }
        if (firstResponse.contains("NO_REPLY"))
        {
            expectResponse = false;
            firstResponse = firstResponse.replaceAll("NO_REPLY", "").trim();
        }

        System.out.println(firstResponse);

        //The client loop
        while (true)
        {
            byte[] incomingData = new byte[1024];

            if (expectResponse)
            {
                //Get some input to sent to the server
                System.out.print("Please provide your input. >\t");
                String outgoingMessage = scan.nextLine();

                //Put it into byte and package it into a Datagram
                byte[] outgoingData = outgoingMessage.getBytes();
                DatagramPacket outgoingPacket = new DatagramPacket(outgoingData, outgoingData.length, serverAddr, serverPort);

                //Send the data
                clientSocket.send(outgoingPacket);
                System.out.println(new String(outgoingPacket.getData(), 0, outgoingPacket.getLength()));

                //Edge case to exit the loop
                if (outgoingMessage.equals("6"))
                {
                    System.out.println("Stopping Programs.");
                    break;
                }
            }
            else
            {
                String outgoingMessage = "";

                //Put it into byte and package it into a Datagram
                byte[] outgoingData = outgoingMessage.getBytes();
                DatagramPacket outgoingPacket = new DatagramPacket(outgoingData, outgoingData.length, serverAddr, serverPort);

                //Send the data
                //clientSocket.send(outgoingPacket);
            }

            DatagramPacket incomingPacket = new DatagramPacket(incomingData, incomingData.length);

            clientSocket.receive(incomingPacket);

            //Convert the packet back ito a string
            String response = new String(incomingPacket.getData(), 0, incomingPacket.getLength());

            if (response.contains("EXPECT_RESPONSE"))
            {
                expectResponse = true;
                response = response.replaceAll("EXPECT_RESPONSE", "").trim();
            }
            if (response.contains("NO_REPLY"))
            {
                expectResponse = false;
                response = response.replaceAll("NO_REPLY", "").trim();
            }

            System.out.println(response);
        }
    }
}
