package server.services;

import server.database.DatabaseConnection;
import shared.classes.*;
import shared.interfaces.AdminService;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

public class AdminServiceImpl extends UnicastRemoteObject implements AdminService, Serializable {
    private static final long serialVersionUID = 1L; // Add a serialVersionUID
    private static final Connection con = DatabaseConnection.setCon(); // creates the connection to the database
    private static String query; // holds the sql query
    private static Statement stmt; // used to execute queries without parameters
    private static CallableStatement callStmt;
    private static PreparedStatement preparedStatement; // prepares and executes parameterized sql queries
    private static ResultSet resultSet; // stores the result returned by executing a query

    public AdminServiceImpl() throws RemoteException {
        super();
    }

    public String generateNewUserID() {
        String latestUserID = "SELECT MAX(userID) FROM user;";
        try {
            stmt = con.createStatement();
            ResultSet resultSet = stmt.executeQuery(latestUserID);

            if (resultSet.next()) {
                latestUserID = resultSet.getString(1);
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

        query = "{CALL viewStudent()}";

        try {
            callStmt = con.prepareCall(query);
            resultSet = callStmt.executeQuery();

            while (resultSet.next()) {
                String studentID = resultSet.getString(1);
                String firstName = resultSet.getString(2);
                String lastName = resultSet.getString(3);
                long phoneNumber = resultSet.getLong(4);
                String email = resultSet.getString(5);
                String role = resultSet.getString(6);
                String password = resultSet.getString(7);
                double balance = resultSet.getDouble(8);
                String academicLevel = resultSet.getString(9);
                String visibility = resultSet.getString(10);

                Student student = new Student(studentID, firstName, lastName, phoneNumber, email, role, password, balance, academicLevel, visibility);
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
        query = "{CALL addStudent(?, ?, ?, ?, ?, ?, ?, ?, ?)}";
        try {
            con.setAutoCommit(false);
            String newUserID = generateNewUserID();
            String newUserPassword = generateNewUserPassword(newStudent);

            callStmt = con.prepareCall(query);

            callStmt.setString(1, newUserID);
            callStmt.setString(2, newStudent.getFirstName());
            callStmt.setString(3, newStudent.getLastName());
            callStmt.setLong(4, newStudent.getPhoneNumber());
            callStmt.setString(5, newStudent.getEmail());
            callStmt.setString(6, newStudent.getRole());
            callStmt.setString(7, newUserPassword);
            callStmt.setDouble(8, newStudent.getBalance());
            callStmt.setString(9, newStudent.getAcademicLevel());

            callStmt.executeUpdate();
            con.commit();
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
        query = "{CALL modifyStudent(?, ?)}";

        try {
            callStmt = con.prepareCall(query);
            callStmt.setString(1, studentID);
            callStmt.setString(2, newPassword);
            callStmt.executeUpdate();
        } catch (SQLException e1) {
            e1.printStackTrace();
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }

    @Override
    public void removeStudent(Student student) throws RemoteException {
        query = "{CALL removeStudent(?)}";

        try {
            callStmt = con.prepareCall(query);
            callStmt.setString(1, student.getUserID());
            callStmt.executeUpdate();
        } catch (SQLException e1) {
            e1.printStackTrace();
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }


    @Override
    public List<Tutor> viewTutor() throws RemoteException{
        List<Tutor> tutorList = new ArrayList<>();

        query = "{CALL viewTutor()}";

        try {
            callStmt = con.prepareCall(query);
            resultSet = callStmt.executeQuery();

            while (resultSet.next()) {
                String tutorID = resultSet.getString(1);
                String firstName = resultSet.getString(2);
                String lastName = resultSet.getString(3);
                long phoneNumber = resultSet.getLong(4);
                String email = resultSet.getString(5);
                String role = resultSet.getString(6);
                String pass = resultSet.getString(7);
                String expertise = resultSet.getString(8);
                String visibility = resultSet.getString(9);

                Tutor tutor = new Tutor(tutorID, firstName, lastName, phoneNumber, email, role, pass, expertise, visibility);
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
        query = "{CALL addTutor(?, ?, ?, ?, ?, ?, ?, ?)}";

        try {
            con.setAutoCommit(false); // creates a transaction for grouped query

            String newUserID = generateNewUserID();
            String newUserPassword = generateNewUserPassword(newTutor);

            callStmt = con.prepareCall(query);

            callStmt.setString(1, newUserID);
            callStmt.setString(2, newTutor.getFirstName());
            callStmt.setString(3, newTutor.getLastName());
            callStmt.setLong(4, newTutor.getPhoneNumber());
            callStmt.setString(5, newTutor.getEmail());
            callStmt.setString(6, newTutor.getRole());
            callStmt.setString(7, newUserPassword);
            callStmt.setString(8, newTutor.getExpertise());

            callStmt.executeUpdate();
            con.commit();
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
        query = "{CALL modifyTutor(?, ?)}";

        try {
            callStmt = con.prepareCall(query);
            callStmt.setString(1, tutorID);
            callStmt.setString(2, newPassword);
            callStmt.executeUpdate();
        } catch (SQLException e1) {
            e1.printStackTrace();
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }

    @Override
    public void removeTutor(Tutor tutor) throws RemoteException {
        query = "{CALL removeTutor(?)}";

        try {
            callStmt = con.prepareCall(query);
            callStmt.setString(1, tutor.getUserID());
            callStmt.executeUpdate();
        } catch (SQLException e1) {
            e1.printStackTrace();
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }


    @Override
    public List<TutorSession> viewSession() throws RemoteException{
        List<TutorSession> allSessions = new ArrayList<>();
        query = "{CALL viewSession()}";

        try{
            callStmt = con.prepareCall(query);
            resultSet = callStmt.executeQuery();

            while (resultSet.next()){
                allSessions.add(
                        new TutorSession(resultSet.getString("sessionID"),
                                resultSet.getString("sessionStatus"),
                                resultSet.getDate("sessionDate").toLocalDate(),
                                resultSet.getTime("sessionTime").toLocalTime(),
                                resultSet.getInt("sessionDuration"),
                                resultSet.getString("sessionType"),
                                resultSet.getString("sessionMode"),
                                resultSet.getInt("numberOfStudents"),
                                resultSet.getInt("maximumStudents"),
                                resultSet.getInt("sessionPrice"),
                                resultSet.getString("academicLevel"),
                                resultSet.getString("tutorID"),
                                resultSet.getString("subjectID"),
                                resultSet.getString("visibility")));
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return allSessions;
    }

    @Override
    public void addSession(TutorSession session) throws RemoteException, SQLException {
        query = "{CALL addSession(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}";

        try {
            con.setAutoCommit(false);
            callStmt = con.prepareCall(query);
            callStmt.setString(1, session.getSessionID());
            callStmt.setString(2, session.getTutorID());
            callStmt.setString(3, session.getSubjectID());
            callStmt.setString(4, session.getSessionStatus());
            callStmt.setDate(5, java.sql.Date.valueOf(session.getSessionDate()));
            callStmt.setTime(6, java.sql.Time.valueOf(session.getSessionTime()));
            callStmt.setInt(7, session.getSessionDuration());
            callStmt.setString(8, session.getSessionType());
            callStmt.setString(9, session.getSessionMode());
            callStmt.setInt(10, session.getNumberOfStudents());
            callStmt.setInt(11, session.getMaximumStudents());
            callStmt.setDouble(12, session.getSessionPrice());

            callStmt.executeUpdate();
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
    public void modifySession(TutorSession session) throws RemoteException, SQLException {
        query = "{CALL modifySession(?, ?, ?, ?, ?, ?, ?, ?)}";

        try {
            con.setAutoCommit(false);

            callStmt = con.prepareCall(query);
            callStmt.setString(1, session.getSessionID());
            callStmt.setString(2, session.getSessionMode());
            callStmt.setString(3, session.getSessionType());
            callStmt.setInt(4, session.getNumberOfStudents());
            callStmt.setInt(5, session.getMaximumStudents());
            callStmt.setInt(6, session.getSessionPrice());
            callStmt.setString(7, session.getSessionStatus());
            callStmt.setString(8, session.getVisibility());


            callStmt.executeUpdate();
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
    public int deleteSession(String sessionID) throws RemoteException {
        query = "{CALL deleteSession(?)}";

        try {
            callStmt = con.prepareCall(query);
            callStmt.setString(1, sessionID);
            callStmt.executeUpdate();
        } catch (SQLIntegrityConstraintViolationException e) {
            return -1;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public List<Subject> viewSubject() throws RemoteException {
        List<Subject> allSubject = new ArrayList<>();
        query = "{CALL viewSubject()}";
        try{
            callStmt = con.prepareCall(query);
            resultSet = callStmt.executeQuery();

            while (resultSet.next()){
                allSubject.add(new Subject(resultSet.getString("subjectID"), resultSet.getString("subjectName"),
                        resultSet.getString("subjectDescription"), resultSet.getString("academicLevel"), resultSet.getString("visibility")));
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return allSubject;
    }

    @Override
    public void addSubject(Subject subject) throws RemoteException, SQLException {
        query = "{CALL addSubject(?, ?, ?, ?)}";

        try {
            con.setAutoCommit(false);
            callStmt = con.prepareCall(query);
            callStmt.setString(1, subject.getSubjectID());
            callStmt.setString(2, subject.getSubjectName());
            callStmt.setString(3, subject.getSubjectDescription());
            callStmt.setString(4, subject.getAcademicLevel());

            callStmt.executeUpdate();
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
    public void modifySubject(String subjectID, String academicLevel, String subjectVisibility) throws RemoteException, SQLException {
        subjectID = subjectID.replaceAll(".*subjectID=(\\d+),.*", "$1");
        query = "{CALL modifySubject(?,?,?)}";

        try {
            con.setAutoCommit(false);

            callStmt = con.prepareCall(query);
            callStmt.setString(1, subjectID);
            callStmt.setString(2, academicLevel);
            System.out.println("visibility: " + subjectVisibility);
            callStmt.setString(3, subjectVisibility);

            callStmt.executeUpdate();
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
    public int deleteSubject(String subjectID) throws RemoteException{
        query = "{CALL deleteSubject(?)}";
        try {
            callStmt = con.prepareCall(query);
            callStmt.setString(1, subjectID);
            callStmt.executeUpdate();
        } catch (SQLIntegrityConstraintViolationException e){
            return -1;
        }catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;

    }

    @Override
    public List<LessonPlan> viewLessonPlan() throws RemoteException {
        List<LessonPlan> allLessonPlans = new ArrayList<>();

//        query = "SELECT lessonPlanID, subjectID, subjectName, academicLevel, objectives, topicsCovered FROM lessonplan " +
//                "INNER JOIN subject USING(subjectID);";
        query = "{CALL viewLessonPlan()}";

        try {
            callStmt = con.prepareCall(query);
            resultSet = callStmt.executeQuery();

            while (resultSet.next()) {
                LessonPlan lessonPlan = new LessonPlan(
                        resultSet.getString("lessonPlanID"),
                        resultSet.getString("subjectID"),
                        resultSet.getString("subjectName"),
                        resultSet.getString("academicLevel"),
                        resultSet.getString("objectives"),
                        resultSet.getString("topicsCovered"),
                        resultSet.getString("visibility")
                );

                allLessonPlans.add(lessonPlan);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return allLessonPlans;
    }

    @Override
    public void modifyLessonPlan(String lessonPlanID, String visibility) throws RemoteException, SQLException {
//        subjectID = subjectID.replaceAll(".*subjectID=(\\d+),.*", "$1");
        query = "{CALL modifyLessonPlan(?,?)}";

        try {
            con.setAutoCommit(false);

            callStmt = con.prepareCall(query);
            callStmt.setString(1, lessonPlanID);
            callStmt.setString(2, visibility);
//            System.out.println("visibility: " + subjectVisibility);
//            callStmt.setString(3, subjectVisibility);

            callStmt.executeUpdate();
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
    public List<Payment> viewPayment() throws RemoteException{
        List<Payment> studentPayments = new ArrayList<>();

        query = "{CALL viewPayment()}";

        try {
            callStmt = con.prepareCall(query);
            resultSet = callStmt.executeQuery();

            while (resultSet.next()) {
                String paymentID = resultSet.getString(1);
                String studentID = resultSet.getString(2);
                String studName = resultSet.getString(3);
                double amount = resultSet.getDouble(4);
                LocalDate date = resultSet.getDate(5).toLocalDate();
                LocalTime time = resultSet.getTime(6).toLocalTime();
                String paymentMethod = resultSet.getString(7);

                Payment payment = new Payment(paymentID, studentID, studName, date, time, paymentMethod, amount);
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
