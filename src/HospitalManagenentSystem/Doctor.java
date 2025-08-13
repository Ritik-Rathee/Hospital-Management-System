package HospitalManagenentSystem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;

public class Doctor {
    private final Connection connection;
    private final Scanner scanner;

    public Doctor(Connection connection, Scanner scanner) {
        this.connection = connection;
        this.scanner = scanner;
    }


    public void viewDoctor() {
        try {
            String Query = "";
            System.out.println("1. All Doctors");
            System.out.println("2. Specific Doctors");
            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    Query = "SELECT * FROM doctors";
                    PreparedStatement preparedStatement = connection.prepareStatement(Query);
                    ResultSet rs = preparedStatement.executeQuery();
                    System.out.println("Doctors : ");
                    System.out.println("+-------+---------------------+----------------+");
                    System.out.println("| ID    |       Name          |   Speciality   |");
                    System.out.println("+-------+---------------------+----------------+");
                    while (rs.next()) {
                        int ID = rs.getInt("doctor_id");
                        String Name = rs.getString("doctor_name");
                        String Speciality = rs.getString("speciality");
                        System.out.printf("|%-7s|%-21s|%-16s|\n", ID, Name, Speciality);
                        System.out.println("+-------+---------------------+----------------+");
                    }
                    break;
                case 2:
                    Query = "SELECT * FROM doctors WHERE doctor_id = ?";
                    System.out.print("Enter Doctor id : ");
                    int d_id = scanner.nextInt();
                    PreparedStatement preparedStmt = connection.prepareStatement(Query);
                    preparedStmt.setInt(1, d_id);
                    ResultSet RS = preparedStmt.executeQuery();
                    System.out.println("Doctors : ");
                    System.out.println("+-------+---------------------+----------------+");
                    System.out.println("| ID    |       Name          |   Speciality   |");
                    System.out.println("+-------+---------------------+----------------+");
                    while (RS.next()) {
                        int ID = RS.getInt("doctor_id");
                        String Name = RS.getString("doctor_name");
                        String Speciality = RS.getString("speciality");
                        System.out.printf("|%-7s|%-21s|%-16s|\n", ID, Name, Speciality);
                        System.out.println("+-------+---------------------+----------------+");
                    }
                    break;
                default:
                    System.out.println(" Invalid Choice");
            }


        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public boolean getDoctorById(int id) {
        try {
            String Query = "SELECT * FROM doctors WHERE doctor_id = ?";
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
