import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Scanner;

public class StudentDatabaseServer {

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        int port = Integer.parseInt(args[0]); //Port in which communication will occur

        ServerSocket serverSock = new ServerSocket(port);
        System.out.println("Server Started, Waiting for Client...");

        Socket clientSock = serverSock.accept();
        System.out.println("Client Connected!");

        File file = new File("database.txt");
        Scanner reader = new Scanner(file);

        ObjectInputStream in = new ObjectInputStream(clientSock.getInputStream());
        ObjectOutputStream out = new ObjectOutputStream(clientSock.getOutputStream());

        ArrayList<Student> studentList = new ArrayList<>();

        while (reader.hasNextLine())
        {
            String data = reader.nextLine();
            String[] studentData = data.split(" ");
            studentList.add(new Student(Integer.parseInt(studentData[0]), studentData[1], studentData[2], Double.parseDouble(studentData[3])));
        }

        reader.close();

        String userInput = "-1"; //All given input is in the form of strings

        while (Integer.parseInt(userInput) != 6) //If the input is a '6' then end the program
        {

            out.writeObject("Welcome to the student database manager. Please input a number that corresponds to the following options:\n\t1.Add a Student to the database.\n\t2.Display a Student based on the given ID.\n\t3.Display which students have a score that is greater than the given score.\n\t4.Display all the students in the database.\n\t5.Delete a Student from the database based on the given ID.\n\t6.Terminate the Program");
            out.flush();
            out.writeObject(true);
            out.flush();

            userInput = (String)in.readObject();

            if (userInput.equals(""))
                userInput = "-1";

            switch (Integer.parseInt(userInput))
            {
                case 1:
                    out.writeObject("You want to add a student to the database.\n Please input the student ID number.");
                    out.flush();
                    out.writeObject(true);
                    out.flush();
                    String studentID = (String) in.readObject();

                    out.writeObject("Please input the student First Name.");
                    out.flush();
                    out.writeObject(true);
                    out.flush();
                    String studentFirstName = (String) in.readObject();

                    out.writeObject("Please input the student Last Name.");
                    out.flush();
                    out.writeObject(true);
                    out.flush();
                    String studentLastName = (String) in.readObject();

                    out.writeObject("Please input the student Score.");
                    out.flush();
                    out.writeObject(true);
                    out.flush();
                    String studentScore = (String) in.readObject();

                    Student newStudent = new Student(Integer.parseInt(studentID), studentFirstName, studentLastName, Double.parseDouble((studentScore)));
                    studentList.add(newStudent);

                    break;
                case 2:
                    out.writeObject("You want to look up a student via ID.\nPlease input the student ID.");
                    out.flush();
                    out.writeObject(true);
                    out.flush();

                    int lookupID = Integer.parseInt((String)in.readObject());
                    for (int i = 0; i < studentList.size(); i++) {
                        if (studentList.get(i).ID == lookupID)
                        {
                            out.writeObject(studentList.get(i).printInfo());
                            out.flush();
                            out.writeObject(false);
                            out.flush();
                        }
                    }
                    break;
                case 3:
                    out.writeObject("You want to display all students that have a score greater than a specific score.\nPlease input a score.");
                    out.flush();
                    out.writeObject(true);
                    out.flush();

                    double minimumScore = Double.parseDouble((String)in.readObject());

                    for (int i = 0; i < studentList.size(); i++) {

                        if (studentList.get(i).score > minimumScore) {
                            out.writeObject(studentList.get(i).printInfo());
                            out.flush();
                            out.writeObject(false);
                            out.flush();
                        }
                    }

                    break;
                case 4:
                    out.writeObject("Displaying all students in the database.");
                    out.flush();
                    out.writeObject(false);
                    out.flush();

                    for (int i = 0; i < studentList.size(); i++) {
                        out.writeObject(studentList.get(i).printInfo());
                        out.flush();
                        out.writeObject(false);
                        out.flush();
                    }

                    break;

                case 5:
                    out.writeObject("You want to remove a student from the database.\nPlease input the ID of the student you wish to remove.");
                    out.flush();
                    out.writeObject(true);
                    out.flush();

                    int removalID = Integer.parseInt((String)in.readObject());
                    for (int i = 0; i < studentList.size(); i++) {
                        if (studentList.get(i).ID == removalID)
                        {
                            studentList.remove(i);
                        }
                    }

                    break;

            }

            if (userInput.equals(""))
                userInput = "-1";

            //Write to file after each iteration
            FileWriter writer = new FileWriter("database.txt");
            for (int i = 0; i < studentList.size(); i++) {
                writer.write(studentList.get(i).ID + " " + studentList.get(i).firstName + " " + studentList.get(i).lastName + " " + studentList.get(i).score + "\n");
            }
            writer.close();
        }

        out.writeObject("");
        out.flush();
        out.writeObject(false);
        out.flush();

        clientSock.close();
        serverSock.close();
    }

    public static class Student
    {
        int ID;
        String firstName;
        String lastName;
        double score;

        Student() //Empty Constructor
        {
            ID = -1;
            firstName = "";
            lastName = "";
            score = 0.0;
        }

        Student(int id, String FirstName, String LastName, double Score)
        {
            ID = id;
            firstName = FirstName;
            lastName = LastName;
            score = Score;
        }

        String printInfo()
        {
            return "Student: " + firstName + "\t" + lastName + "\tID:" + ID + "\tScore: " + score;
        }
    }
}