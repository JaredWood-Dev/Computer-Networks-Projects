import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.Scanner;

public class StudentDatabaseServerUDP
{
    public static InetAddress clientAddr;
    public static int clientPort;
    public static void main(String[] args) throws IOException {

        //Open a socket for the server to use
        DatagramSocket serverSocket = new DatagramSocket(12345);

        //UDP uses byte[] to store and send data
        byte[] incomingData = new byte[1024];

        //Data management
        File file = new File("database.txt");
        Scanner reader = new Scanner(file);
        ArrayList<Student> studentList = new ArrayList<>();

        //Load the data from the database
        while (reader.hasNextLine())
        {
            String inputLine = reader.nextLine();
            String[] inputs = inputLine.split(" ");
            Student s = new Student(Integer.parseInt(inputs[0]), inputs[1], inputs[2], Double.parseDouble(inputs[3]));
            studentList.add(s);
        }
        String msg = receiveMessage(incomingData, serverSocket);
        //The server loop. The server should continue running until the client tells it to close.
        while (true)
        {

            //Reset the buffer
            incomingData = new byte[1024];
            //Welcome message.
            sendMessage("Welcome to the Student Database Management System. Please select one of the following:\n1.\tAdd a new student to the database.\n2.\tDisplay the information of a specific student.\n3.\tDisplay all of the students that current have a score greater than or equal to a specified amount.\n4.\tDisplay information about all the students in the database.\n5.\tDelete a student from the database.\n6.\tExit the program.", serverSocket, true);

            msg = receiveMessage(incomingData, serverSocket);

            if (msg.equals(""))
                continue;

            switch (msg)
            {
                //Adding the student to the database
                case "1":
                    sendMessage("You wish to add a student to the database.\nInput the student's ID number.", serverSocket, true);

                    int id = Integer.parseInt(receiveMessage(incomingData, serverSocket));

                    sendMessage("Input the student's first name.", serverSocket, true);

                    String fname = receiveMessage(incomingData, serverSocket);

                    sendMessage("Input the student's last name.", serverSocket, true);

                    String lname = receiveMessage(incomingData, serverSocket);

                    sendMessage("Input the student's score.", serverSocket, true);

                    double s = Double.parseDouble(receiveMessage(incomingData, serverSocket));

                    studentList.add(new Student(id, fname, lname, s));

                    sendMessage("Student added to database.", serverSocket, false);

                    break;
                //Display based on ID
                case "2":
                    sendMessage("Input the student's ID.", serverSocket, true);

                    int ID = Integer.parseInt(receiveMessage(incomingData, serverSocket));

                    for (int i = 0; i < studentList.size(); i++) {
                        if (studentList.get(i).GetID() == ID)
                        {
                            sendMessage(studentList.get(i).DisplayStudentData(), serverSocket, false);
                        }
                    }

                    sendMessage("StudentDisplayed.", serverSocket, false);

                    break;
                //Display all the students above to equal to target score
                case "3":
                    sendMessage("Input the score threshold.", serverSocket, true);

                    double score = Double.parseDouble(receiveMessage(incomingData,serverSocket));

                    for (int i = 0; i < studentList.size(); i++)
                    {
                        if (studentList.get(i).GetScore() >= score)
                        {
                            sendMessage(studentList.get(i).DisplayStudentData(), serverSocket, false);
                        }
                    }

                    sendMessage("Student List Displayed.", serverSocket, false);
                    break;
                case "4":
                    sendMessage("The entire list of students in the database:", serverSocket, false);

                    for (int i = 0; i < studentList.size(); i++) {
                        sendMessage(studentList.get(i).DisplayStudentData(), serverSocket, false);
                    }

                    sendMessage("Student List Displayed.", serverSocket, false);
                    break;
                //Delete a student from the database
                case "5":
                    sendMessage("Input Student ID. This Student will be Deleted.", serverSocket, true);

                    int _id = Integer.parseInt(receiveMessage(incomingData, serverSocket));

                    for (int i = 0; i < studentList.size(); i++) {
                        if (studentList.get(i).GetID() == _id)
                        {
                            studentList.remove(i);
                        }
                    }

                    break;
            }
            if (msg.equals("6"))
                break;

            //Write to file after each iteration
            FileWriter writer = new FileWriter("database.txt");
            for (int i = 0; i < studentList.size(); i++) {
                writer.write(studentList.get(i).GetID() + " " + studentList.get(i).GetFirstName() + " " + studentList.get(i).GetLastName() + " " + studentList.get(i).GetScore() + "\n");
            }
            writer.close();
        }
    }

    //This function handles getting messages from the client
    public static String receiveMessage(byte[] data, DatagramSocket socket) throws IOException {
        //Make a new packet to recive the incoming data from the serve
        DatagramPacket incomingPacket = new DatagramPacket(data, data.length);

        //Get the incoming data from the server
        socket.receive(incomingPacket);

        //Turn it into a string
        String msg = new String(incomingPacket.getData(), 0, incomingPacket.getLength());

        clientAddr = incomingPacket.getAddress();
        clientPort = incomingPacket.getPort();

        return msg;
    }

    //Handles sending the data to client
    public static void sendMessage(String message, DatagramSocket socket, boolean expectResponse) throws IOException {

        if (expectResponse)
            message += "EXPECT_RESPONSE";
        else
            message += "NO_REPLY";

        //Return a message to the client
        byte[] outgoingData = message.getBytes();

        //Create a packet with the destination address and port
        DatagramPacket outgoingPacket = new DatagramPacket(outgoingData, outgoingData.length, clientAddr, clientPort);

        //Send the information
        socket.send(outgoingPacket);
    }

    public static class Student
    {
        private int id;
        private String fname;
        private String lname;
        private double score;

        Student(int ID, String firstName, String lastName, double grade)
        {
            id = ID;
            fname = firstName;
            lname = lastName;
            score = grade;
        }

        String DisplayStudentData()
        {
            return(id + "\t" + fname + "\t" + lname + "\t" + score);
        }

        int GetID()
        {
            return id;
        }

        String GetFirstName()
        {
            return fname;
        }

        String GetLastName()
        {
            return lname;
        }

        double GetScore()
        {
            return score;
        }
    }
}
