package HospitalManagenentSystem;

import java.sql.*;
import java.util.Scanner;

public class Patient {

    private final Connection connection;
    private final Scanner scanner;

    public Patient(Connection connection, Scanner scanner) {
        this.connection = connection;
        this.scanner = scanner;
    }


    public void addPatient() {
        System.out.print("Enter Patient name : ");
        String name = scanner.next();
        System.out.print("Enter Contect info : ");
        String conntect = scanner.next();
        System.out.print("Enter Age of Patient : ");
        int age = scanner.nextInt();
        System.out.print("Enter Gender of Patient : ");
        String gender = scanner.next();
        System.out.print("Enter Diagonistic : ");
        String diagonistic = scanner.next();

        try {
            String Query = " INSERT INTO patient (patient_name ,contect_info, age,gender,diagonistic) VALUES (?,?,?,?,?)";
            PreparedStatement preparedStatement = connection.prepareStatement(Query);
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, conntect);
            preparedStatement.setInt(3, age);
            preparedStatement.setString(4, gender);
            preparedStatement.setString(5, diagonistic);
            int rs = preparedStatement.executeUpdate();
            if (rs > 0) {
                System.out.println("Appointment Added Successfully ");
            } else {
                System.out.println("Appointment Failed ");
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }

    public void viewPatient() {
        try {
            String Query = "";
            System.out.println("1. All Patients");
            System.out.println("2. Specific Patients");
            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    Query = "SELECT * FROM patient ";
                    PreparedStatement preparedStatement = connection.prepareStatement(Query);
                    ResultSet rs = preparedStatement.executeQuery();
                    System.out.println();
                    System.out.println("Patient Records : ");
                    System.out.println("+-------+---------------------+----------------+---------+----------------+----------------+");
                    System.out.println("| ID    |       Name          |    Contact     |   Age   |     Gender     |   Diagnostics  |");
                    System.out.println("+-------+---------------------+----------------+---------+----------------+----------------+");
                    while (rs.next()) {
                        int ID = rs.getInt("patient_id");
                        String Name = rs.getString("patient_name");
                        String Contact = rs.getString("contect_info");
                        int Age = rs.getInt("age");
                        String Gender = rs.getString("gender");
                        String Diagnostics = rs.getString("diagonistic");
                        System.out.printf("|%-7s|%-21s|%-16s|%-9s|%-16s|%-16s| \n ", ID, Name, Contact, Age, Gender, Diagnostics);
                        System.out.println("+-------+---------------------+----------------+---------+----------------+----------------+");
                        System.out.println();
                    }
                    break;

                case 2:
                    Query = "SELECT * FROM patient WHERE patient_id = ?";
                    System.out.print("Enter Pationt id : ");
                    int p_id = scanner.nextInt();
                    PreparedStatement preparedStmt = connection.prepareStatement(Query);
                    preparedStmt.setInt(1, p_id);
                    ResultSet RS = preparedStmt.executeQuery();
                    System.out.println();
                    System.out.println("Patient Records : ");
                    System.out.println("+-------+---------------------+----------------+---------+----------------+----------------+");
                    System.out.println("| ID    |       Name          |    Contact     |   Age   |     Gender     |   Diagnostics  |");
                    System.out.println("+-------+---------------------+----------------+---------+----------------+----------------+");
                    while (RS.next()) {
                        int ID = RS.getInt("patient_id");
                        String Name = RS.getString("patient_name");
                        String Contact = RS.getString("contect_info");
                        int Age = RS.getInt("age");
                        String Gender = RS.getString("gender");
                        String Diagnostics = RS.getString("diagonistic");
                        System.out.printf("|%-7s|%-21s|%-16s|%-9s|%-16s|%-16s| \n ", ID, Name, Contact, Age, Gender, Diagnostics);
                        System.out.println("+-------+---------------------+----------------+---------+----------------+----------------+");
                        System.out.println();
                    }
                    break;

                default:
                    System.out.println(" Invalid Choice");
            }


        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }


    public boolean getPatientById(int id) {
        try {
            String Query = "SELECT * FROM patient WHERE patient_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(Query);
            preparedStatement.setInt(1, id);
            ResultSet rs = preparedStatement.executeQuery();
            return rs.next();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return false;
    }
}
