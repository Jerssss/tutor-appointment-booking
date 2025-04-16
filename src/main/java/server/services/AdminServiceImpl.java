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
import java.util.*;

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
        query = "SELECT sessionID, sessionDate, sessionTime, sessionDuration, subjectLevel, subjectID FROM tutorsession\n" +
                "INNER JOIN subject USING (subjectID); ";

        try{
            preparedStatement = con.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()){
                allSessions.add(Arrays.asList(resultSet.getString("sessionID"), String.valueOf(resultSet.getDate("sessionDate")), String.valueOf(resultSet.getTime("sessionTime")), resultSet.getString("sessionDuration"),
                        resultSet.getString("subjectLevel"), resultSet.getString("subjectID")));
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return allSessions;
    }

    @Override
    public void addSession(TutorSession session) throws RemoteException, SQLException {
        query = "INSERT INTO tutorsession (sessionID, tutorID, subjectID, sessionStatus, sessionDate, sessionTime, sessionDuration, numberOfStudents, maximumStudents, sessionPrice) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try {
            con.setAutoCommit(false);

            preparedStatement = con.prepareStatement(query);
            preparedStatement.setString(1, session.getSessionID());
            preparedStatement.setString(2, session.getTutorID());
            preparedStatement.setString(3, session.getSubjectID());
            preparedStatement.setString(4, session.getSessionStatus());
            preparedStatement.setDate(5, java.sql.Date.valueOf(session.getSessionDate()));
            preparedStatement.setTime(6, java.sql.Time.valueOf(session.getSessionTime()));
            preparedStatement.setInt(7, session.getSessionDuration());
            preparedStatement.setInt(8, session.getNumberOfStudents());
            preparedStatement.setInt(9, session.getMaximumStudents());
            preparedStatement.setDouble(10, session.getSessionPrice());

            preparedStatement.executeUpdate();
            con.commit();

        } catch (SQLException e1) {
            if (con != null) con.rollback();
            e1.printStackTrace();
        } catch (Exception e2) {
            e2.printStackTrace();
        } finally {
            if (con != null) con.setAutoCommit(true);
        }
    }

    @Override
    public void modifySession(String sessionID, String sessionMode, String sessionType) throws RemoteException, SQLException {
        query = "UPDATE tutorsession SET numberOfStudents = 1 WHERE sessionID = ?";

        try {
            con.setAutoCommit(false);

            preparedStatement = con.prepareStatement(query);
            preparedStatement.setString(1, sessionID);

            preparedStatement.executeUpdate();
            con.commit();

        } catch (SQLException e1) {
            if (con != null) con.rollback();
            e1.printStackTrace();
        } catch (Exception e2) {
            e2.printStackTrace();
        } finally {
            if (con != null) con.setAutoCommit(true);
        }
    }

    @Override
    public List<String> getAllTutorName() throws RemoteException{
        List<String> allTutorName = new ArrayList<>();
        query = "SELECT CONCAT(firstName,' ', lastName) AS names FROM user\n" +
                "WHERE role = 'tutor';";

        try{
            preparedStatement = con.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()){
                allTutorName.add(resultSet.getString("names"));
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return allTutorName;
    }

    @Override
    public List<String> getAllSubjects() throws RemoteException{
        List<String> allSubjects = new ArrayList<>();
        query = "SELECT subjectName FROM `subject`;";
        try{
            preparedStatement = con.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()){
                allSubjects.add(resultSet.getString("subjectName"));
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return allSubjects;
    }

    @Override
    public String getSubjectID(String subjectName) throws RemoteException{
        String subjectID = "";
        query = "SELECT subjectID FROM subject WHERE subjectName = ?";

        try{
            preparedStatement = con.prepareStatement(query);
            preparedStatement.setString(1, subjectName);
            resultSet = preparedStatement.executeQuery();

            while(resultSet.next()){
                subjectID = resultSet.getString("subjectID");
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return subjectID;
    }
    @Override
    public String getTutorID(String tutorName) throws RemoteException{
        String tutorID = "";
        query = "SELECT userID FROM user WHERE CONCAT(firstName, ' ', lastName) = ?;";
        try{
            preparedStatement = con.prepareStatement(query);
            preparedStatement.setString(1, tutorName);
            resultSet = preparedStatement.executeQuery();

            while(resultSet.next()){
                tutorID = resultSet.getString("userID");
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return tutorID;
    }

    @Override
    public List<String> getAllSessionID() throws RemoteException{
        List<String> allSessionID = new ArrayList<>();
        query = "SELECT sessionID FROM `tutorsession`;";
        try{
            preparedStatement = con.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()){
                allSessionID.add(resultSet.getString("sessionID"));
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return allSessionID;
    }

    @Override
    public Map<LocalTime, Integer> getTutorSchedule(String tutorID, String date) throws RemoteException {
        Map<LocalTime, Integer> scheduleMap = new LinkedHashMap<>();
        String query = "SELECT sessionTime, sessionDuration FROM tutorsession WHERE tutorID = ? AND sessionDate = ?";

        try {
            preparedStatement = con.prepareStatement(query);
            preparedStatement.setString(1, tutorID);
            preparedStatement.setString(2, date);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                LocalTime sessionTime = resultSet.getTime("sessionTime").toLocalTime();
                int sessionDuration = resultSet.getInt("sessionDuration");
                scheduleMap.put(sessionTime, sessionDuration);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return scheduleMap;
    }

    @Override
    public List<Subject> viewSubject() throws RemoteException {
        List<Subject> allSubject = new ArrayList<>();
        query = "SELECT * FROM subject;";

        try{
            preparedStatement = con.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()){
                allSubject.add(new Subject(resultSet.getString("subjectID"), resultSet.getString("subjectName"),
                        resultSet.getString("subjectDescription"), resultSet.getString("subjectLevel")));
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return allSubject;
    }

    @Override
    public void addSubject(Subject subject) throws RemoteException, SQLException {
        query = "INSERT INTO subject (subjectID, subjectName, subjectDescription, subjectLevel) VALUES (?, ?, ?, ?)";

        try {
            con.setAutoCommit(false); // Disable auto-commit before manual commit

            preparedStatement = con.prepareStatement(query);
            preparedStatement.setString(1, subject.getSubjectID());
            preparedStatement.setString(2, subject.getSubjectName());
            preparedStatement.setString(3, subject.getSubjectDescription());
            preparedStatement.setString(4, subject.getSubjectLevel());

            preparedStatement.executeUpdate();
            con.commit();

        } catch (SQLException e1) {
            if (con != null) con.rollback();
            e1.printStackTrace();
        } catch (Exception e2) {
            e2.printStackTrace();
        } finally {
            if (con != null) con.setAutoCommit(true);
        }
    }

    @Override
    public void modifySubject(String subjectID, String academicLevel) throws RemoteException, SQLException {
        subjectID = subjectID.replaceAll(".*subjectID=(\\d+),.*", "$1");
        query = "UPDATE subject SET subjectLevel = ? WHERE subjectID = ?";

        try {
            con.setAutoCommit(false);

            preparedStatement = con.prepareStatement(query);
            preparedStatement.setString(1, academicLevel);
            preparedStatement.setString(2, subjectID);

            preparedStatement.executeUpdate();
            con.commit();

        } catch (SQLException e1) {
            if (con != null) con.rollback();
            e1.printStackTrace();
        } catch (Exception e2) {
            e2.printStackTrace();
        } finally {
            if (con != null) con.setAutoCommit(true);
        }
    }

    @Override
    public Map<LessonPlan, String> viewLessonPlan() throws RemoteException {
        Map<LessonPlan, String> allLessonPlans = new HashMap<>();

//        query = "SELECT lessonPlanID, subjectID, subjectName, subjectLevel, objectives, topicsCovered FROM lessonplan " +
//                "INNER JOIN subject USING(subjectID);";
        query = "SELECT lessonPlanID, subjectID, subjectLevel, objectives, topicsCovered FROM lessonplan " +
                "INNER JOIN subject USING(subjectID);";

        try {
            preparedStatement = con.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                LessonPlan lessonPlan = new LessonPlan(
                        resultSet.getString("lessonPlanID"),
                        resultSet.getString("subjectID"),
                        resultSet.getString("objectives"),
                        resultSet.getString("topicsCovered")
                );

//                allLessonPlans.put(lessonPlan, Arrays.asList(resultSet.getString("subjectName"), resultSet.getString("subjectLevel")));
                allLessonPlans.put(lessonPlan, resultSet.getString("subjectLevel"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return allLessonPlans;
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
