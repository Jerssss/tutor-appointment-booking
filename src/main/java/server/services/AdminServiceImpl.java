package server.services;

import server.database.DatabaseConnection;
import shared.classes.*;
import shared.interfaces.AdminService;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AdminServiceImpl implements Remote, AdminService, Serializable {
    private static final long serialVersionUID = 1L; // Add a serialVersionUID
    private static Connection con = DatabaseConnection.setCon(); // creates the connection to the database
    private static String query; // holds the sql query
    private static Statement stmt; // used to execute queries without parameters
    private static PreparedStatement preparedStatement; // prepares and executes parameterized sql queries
    private static ResultSet resultSet; // stores the result returned by executing a query

    public String generateNewUserID() {
        String latestUserID = "SELECT userID FROM user ORDER BY userID DESC LIMIT 1";
        try {
            stmt = con.createStatement();
            ResultSet resultSet = stmt.executeQuery(latestUserID);

            if (resultSet.next()) {
                latestUserID = resultSet.getString("userID");
            } else {
                latestUserID = "2210001"; // Default if no records exist
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        int userID = Integer.parseInt(latestUserID) + 1;
        String newUserID = String.valueOf(userID);
        return newUserID;
    }

    public String generateNewUserPassword(User newUser) {
        String userID = generateNewUserID();
        String initials = ("" + newUser.getLastName().charAt(0) + newUser.getFirstName().charAt(0));
        String password = ("" + userID + initials);
        return password;
    }

    @Override
    public List<Student> viewStudent() throws RemoteException{
        List<Student> studentList = new ArrayList<>();

        query = "SELECT s.studentID, u.firstName, u.lastName, u.phoneNumber, u.email, u.role, s.balance, s.academicLevel " +
                "FROM student s " +
                "INNER JOIN user u ON s.studentID = u.userID;";

        try {
            stmt = con.createStatement();
            resultSet = stmt.executeQuery(query);

            while (resultSet.next()) {
                String tutorID = resultSet.getString(1);
                String firstName = resultSet.getString(2);
                String lastName = resultSet.getString(3);
                long phoneNumber = resultSet.getLong(4);
                String email = resultSet.getString(5);
                String role = resultSet.getString(6);
                double balance = resultSet.getDouble(7);
                String academicLevel = resultSet.getString(8);

                Student student = new Student(tutorID, firstName, lastName, phoneNumber, email, role, balance, academicLevel);
                studentList.add(student);
            }
        } catch (SQLException e1) {
            e1.printStackTrace();
        } catch (Exception e2) {
            e2.printStackTrace();
        }
        return studentList;
    }

    @Override
    public void addStudent(Student newStudent) throws RemoteException, SQLException {
        String latestUserID = "SELECT userID FROM user ORDER BY userID DESC LIMIT 1";
        String query1 = "INSERT INTO user (userID, firstName, lastName, phoneNumber, email, role, password)" +
                "VALUES (?, ?, ?, ?, ?, ?, ?);";
        String query2 = "INSERT INTO student (studentID, balance, academicLevel)" +
                "VALUES (?, ?, ?);";
        try {
            con.setAutoCommit(false); // creates a transaction for grouped query

            String newUserID = generateNewUserID();
            String newUserPassword = generateNewUserPassword(newStudent);

            PreparedStatement preparedStatement1 = con.prepareStatement(query1);
            preparedStatement1.setString(1, newUserID);
            preparedStatement1.setString(2, newStudent.getFirstName());
            preparedStatement1.setString(3, newStudent.getLastName());
            preparedStatement1.setLong(4, newStudent.getPhoneNumber());
            preparedStatement1.setString(5, newStudent.getEmail());
            preparedStatement1.setString(6, newStudent.getRole());
            preparedStatement1.setString(7, newUserPassword);
            preparedStatement1.executeUpdate();

            PreparedStatement preparedStatement2 = con.prepareStatement(query2);
            preparedStatement2.setString(1, newUserID);
            preparedStatement2.setDouble(2, newStudent.getBalance());
            preparedStatement2.setString(3, newStudent.getAcademicLevel());
            preparedStatement2.executeUpdate();

            con.commit(); // commits both queries
        } catch (SQLException e1) {
            if (con != null) con.rollback(); // Rollback on error
            e1.printStackTrace();
        } catch (Exception e2) {
            e2.printStackTrace();
        } finally {
            if (con != null) con.setAutoCommit(true); // Reset autocommit
        }
    }
    @Override
    public void modifyStudent(String studentID, String newPassword) throws RemoteException {
        String query = "UPDATE user SET password = ? WHERE userID = ?";
        try {
            preparedStatement = con.prepareStatement(query);
            preparedStatement.setString(1, newPassword);
            preparedStatement.setString(2, studentID);
            preparedStatement.executeUpdate();
        } catch (SQLException e1) {
            e1.printStackTrace();
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }

    @Override
    public List<Tutor> viewTutor() throws RemoteException{
        List<Tutor> tutorList = new ArrayList<>();

        query = "SELECT t.tutorID, u.firstName, u.lastName, u.phoneNumber, u.email, u.role, t.expertise " +
                "FROM tutor t " +
                "INNER JOIN user u ON t.tutorID = u.userID;";

        try {
            stmt = con.createStatement();
            resultSet = stmt.executeQuery(query);

            while (resultSet.next()) {
                String tutorID = resultSet.getString(1);
                String firstName = resultSet.getString(2);
                String lastName = resultSet.getString(3);
                long phoneNumber = resultSet.getLong(4);
                String email = resultSet.getString(5);
                String role = resultSet.getString(6);
                String expertise = resultSet.getString(7);

                Tutor tutor = new Tutor(tutorID, firstName, lastName, phoneNumber, email, role, expertise);
                tutorList.add(tutor);
            }
        } catch (SQLException e1) {
            e1.printStackTrace();
        } catch (Exception e2) {
            e2.printStackTrace();
        }
        return tutorList;
    }

    @Override
    public void addTutor(Tutor newTutor) throws RemoteException, SQLException {
        String query1 = "INSERT INTO user (userID, firstName, lastName, phoneNumber, email, role, password)" +
                "VALUES (?, ?, ?, ?, ?, ?);";
        String query2 = "INSERT INTO tutor (tutorID, expertise)" +
                "VALUES (?, ?);";
        try {
            con.setAutoCommit(false); // creates a transaction for grouped query

            String newUserID = generateNewUserID();
            String newUserPassword = generateNewUserPassword(newTutor);

            PreparedStatement preparedStatement1 = con.prepareStatement(query1);
            preparedStatement1.setString(1, newUserID);
            preparedStatement1.setString(2, newTutor.getFirstName());
            preparedStatement1.setString(3, newTutor.getLastName());
            preparedStatement1.setLong(4, newTutor.getPhoneNumber());
            preparedStatement1.setString(5, newTutor.getEmail());
            preparedStatement1.setString(6, newTutor.getRole());
            preparedStatement1.setString(7, newUserPassword);
            preparedStatement1.executeUpdate();

            PreparedStatement preparedStatement2 = con.prepareStatement(query2);
            preparedStatement2.setString(1, newUserID);
            preparedStatement2.setString(2, newTutor.getExpertise());
            preparedStatement2.executeUpdate();

            con.commit(); // commits both queries
        } catch (SQLException e1) {
            if (con != null) con.rollback(); // Rollback on error
            e1.printStackTrace();
        } catch (Exception e2) {
            e2.printStackTrace();
        } finally {
            if (con != null) con.setAutoCommit(true); // Reset autocommit
        }
    }

    @Override
    public void modifyTutor(String tutorID, String newPassword) throws RemoteException {
        String query = "UPDATE user SET password = ? WHERE userID = ?";

        try {
            preparedStatement = con.prepareStatement(query);
            preparedStatement.setString(1, newPassword);
            preparedStatement.setString(2, tutorID);
            preparedStatement.executeUpdate();
        } catch (SQLException e1) {
            e1.printStackTrace();
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }


    @Override
    public List<List<String>> viewSession() throws RemoteException{
        List<List<String>> allSessions = new ArrayList<>();
        query = "SELECT sessionDate, sessionTime, sessionDuration, subjectLevel, subjectID FROM tutorsession\n" +
                "INNER JOIN subject USING (subjectID); ";

        try{
            preparedStatement = con.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()){
                allSessions.add(Arrays.asList(String.valueOf(resultSet.getDate("sessionDate")), String.valueOf(resultSet.getTime("sessionTime")), resultSet.getString("sessionDuration"),
                        resultSet.getString("subjectLevel"), resultSet.getString("subjectID")));
            }


            for (List<String> sessions : allSessions){
                System.out.println(sessions);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return allSessions;
    }

    @Override
    public TutorSession addSession() throws RemoteException {
        return null;
    }

    @Override
    public TutorSession modifySession() throws RemoteException {
        return null;
    }

    @Override
    public List<Subject> viewSubject() throws RemoteException {
        return null;
    }

    @Override
    public Subject addSubject() throws RemoteException {
        return null;
    }

    @Override
    public Subject modifySubject() throws RemoteException {
        return null;
    }

    @Override
    public List<LessonPlan> viewLessonPlan() throws RemoteException {
        return null;
    }

    @Override
    public LessonPlan addLessonPlan() throws RemoteException {
        return null;
    }

    @Override
    public LessonPlan modifyLessonPlan() throws RemoteException {
        return null;
    }

    @Override
    public List<Payment> viewPayment() throws RemoteException{
        List<Payment> studentPayments = new ArrayList<>();

        query = "SELECT * FROM payment";

        try {
            stmt = con.createStatement();
            resultSet = stmt.executeQuery(query);
            while (resultSet.next()) {
                String paymentID = resultSet.getString(1);
                String studentID = resultSet.getString(2);
                double amount = resultSet.getDouble(3);
                LocalDate date = resultSet.getDate(4).toLocalDate();
                LocalTime time = resultSet.getTime(5).toLocalTime();
                LocalDateTime paymentDateTime = LocalDateTime.of(date, time);
                String paymentMethod = resultSet.getString(5);

                Payment payment = new Payment(paymentID, studentID, amount, paymentDateTime, paymentMethod);
                studentPayments.add(payment);
            }
        } catch (SQLException e1) {
            e1.printStackTrace();
        } catch (Exception e2) {
            e2.printStackTrace();
        }
        return studentPayments;
    }
}
