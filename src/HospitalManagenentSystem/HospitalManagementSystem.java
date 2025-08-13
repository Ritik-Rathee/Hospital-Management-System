package HospitalManagenentSystem;


import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

class Thread extends java.lang.Thread {
    @Override
    public void run() {
        ArrayList<Integer> arr = new ArrayList<>();
        arr.add(1);
        arr.add(2);
        arr.add(3);
        arr.add(4);
        arr.add(5);
        Collections.shuffle(arr);
        for (int i = 0; i < arr.get(1); i++) {
            System.out.print(".");
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                System.out.println(e.getMessage());
            }
        }
    }
}


public class HospitalManagementSystem {

    private static final String url = "jdbc:mysql://localhost:3306/hospital";
    private static final String username = "root";
    private static final String password = "Ritik@999652";


    public void bookAppointment(Doctor doctor, Patient patient, Connection connection, Scanner scanner) {

        try {
            System.out.print("Enter Pationt id :");
            int p_id = scanner.nextInt();
            System.out.print("Enter Doctor id :");
            int d_id = scanner.nextInt();
            System.out.print("Enter Appointment Date (YYYY-MM-DD) :");
            String date = scanner.next();

            if (doctor.getDoctorById(d_id) && patient.getPatientById(p_id)) {
                if (isDocAvailable(connection, d_id, date)) {
                    String Query = "INSERT INTO appointments (patient_id,doctor_id,appointment_date) VALUES (?,?,?)";
                    PreparedStatement preparedStatement = connection.prepareStatement(Query);
                    preparedStatement.setInt(1, p_id);
                    preparedStatement.setInt(2, d_id);
                    preparedStatement.setString(3, date);
                    int rs = preparedStatement.executeUpdate();
                    if (rs > 0) {
                        System.out.println("Appointment Booked");
                    } else {
                        System.out.println("Booking Failed");
                    }
                }

            } else {
                System.out.println("Patient or Doctor not available");
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void ckeckAppointment(Connection connection, Scanner scanner) {
        try {
            String Query = "";
            System.out.println("1. All Appointments");
            System.out.println("2. For Specific Patients");
            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    Query = "SELECT * FROM appointments ";
                    PreparedStatement preparedStatement = connection.prepareStatement(Query);
                    ResultSet rs = preparedStatement.executeQuery();
                    System.out.println();
                    System.out.println("Patient Records : ");
                    System.out.println("+----------------+--------------+--------------+---------------------+");
                    System.out.println("| Appointment ID | Patient ID   | Doctor ID    | Appointment Date    |");
                    System.out.println("+----------------+--------------+--------------+---------------------+");
                    while (rs.next()) {
                        int A_ID = rs.getInt("appointment_id");
                        int P_ID = rs.getInt("patient_id");
                        int D_ID = rs.getInt("doctor_id");
                        String A_DATE = rs.getString("appointment_date");
                        System.out.printf("| %-14s | %-12s | %-12s | %-19s | \n ", A_ID, P_ID, D_ID, A_DATE);
                        System.out.println("+----------------+--------------+--------------+---------------------+");
                        System.out.println();
                    }
                    break;

                case 2:
                    Query = "SELECT * FROM appointments WHERE appointment_id = ?";
                    System.out.print("Enter Appointment ID : ");
                    int p_id = scanner.nextInt();
                    PreparedStatement preparedStmt = connection.prepareStatement(Query);
                    preparedStmt.setInt(1, p_id);
                    ResultSet RS = preparedStmt.executeQuery();
                    System.out.println();
                    System.out.println("Patient Records : ");
                    System.out.println("+----------------+--------------+--------------+---------------------+");
                    System.out.println("| Appointment ID | Patient ID   | Doctor ID    | Appointment Date    |");
                    System.out.println("+----------------+--------------+--------------+---------------------+");
                    while (RS.next()) {
                        int A_ID = RS.getInt("appointment_id");
                        int P_ID = RS.getInt("patient_id");
                        int D_ID = RS.getInt("doctor_id");
                        String A_DATE = RS.getString("appointment_date");
                        System.out.printf("| %-14s | %-12s | %-12s | %-19s | \n ", A_ID, P_ID, D_ID, A_DATE);
                        System.out.println("+----------------+--------------+--------------+---------------------+");
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


    public boolean isDocAvailable(Connection connection, int d_id, String date) {
        try {
            String Query = "SELECT COUNT(*) FROM appointments WHERE doctor_id = ? AND appointment_date = ? ";
            PreparedStatement preparedStatement = connection.prepareStatement(Query);
            preparedStatement.setInt(1, d_id);
            preparedStatement.setString(2, date);
            ResultSet rs = preparedStatement.executeQuery();
            int a = 0;
            while (rs.next()) {
                a = rs.getInt(1);
            }
            return a < 10;

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return false;
    }


    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        Thread t1 = new Thread();

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }

        try {
            Connection connection = DriverManager.getConnection(url, username, password);
            HospitalManagementSystem hospitalManagementSystem = new HospitalManagementSystem();
            Patient patient = new Patient(connection, scanner);
            Doctor doctor = new Doctor(connection, scanner);

            System.out.println("HOSPITAL MANAGEMENT SYSTEM");
            while (true) {
                System.out.println("1. Add Patient ");
                System.out.println("2. Check Patient");
                System.out.println("3. Available Doctor");
                System.out.println("4. Book Appointment");
                System.out.println("5. Check Appointment");
                System.out.println("6. Exit");
                int choice = scanner.nextInt();
                switch (choice) {
                    case 1:
                        patient.addPatient();
                        break;
                    case 2:
                        patient.viewPatient();
                        break;
                    case 3:
                        doctor.viewDoctor();
                        break;
                    case 4:
                        hospitalManagementSystem.bookAppointment(doctor, patient, connection, scanner);
                        break;
                    case 5:
                        hospitalManagementSystem.ckeckAppointment(connection, scanner);
                        break;
                    case 6:
                        connection.close();
                        System.out.print("Exiting");
                        t1.start();
                        t1.join();
                        System.out.println();
                        System.out.println("GOOD BYE ");
                        return;
                    default:
                        System.out.println(" Invalid Choice ");
                }
            }

        } catch (SQLException | InterruptedException e) {
            System.out.println(e.getMessage());
        }
    }
}
