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

public class AdminServiceImpl extends UnicastRemoteObject implements AdminService, Serializable {    private static final long serialVersionUID = 1L; // Add a serialVersionUID
    protected Connection con;  // creates the connection to the database
    private static String query; // holds the sql query
    private static Statement stmt; // used to execute queries without parameters
    private static CallableStatement callStmt;
    private static PreparedStatement preparedStatement; // prepares and executes parameterized sql queries
    private static ResultSet resultSet; // stores the result returned by executing a query

    public AdminServiceImpl() throws RemoteException {
        this.con = DatabaseConnection.setCon();
        if (this.con == null) {
            throw new RemoteException("Database connection failed: Connection is null");
        }
        System.out.println("[SERVER] Successfully connected to the database.");
    }

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

                Student student = new Student(studentID, firstName, lastName, phoneNumber, email, role, password, balance, academicLevel);
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

                Tutor tutor = new Tutor(tutorID, firstName, lastName, phoneNumber, email, role, pass, expertise);
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
    public List<String> viewOtherSessionDetails(String sessionID) throws RemoteException{
        List<String> otherDetails = new ArrayList<>();
        query = "SELECT firstName, lastName, subjectName, academicLevel, sessionType, sessionMode, numberOfStudents, maximumStudents, sessionPrice FROM tutorsession\n" +
                "INNER JOIN subject USING (subjectID)\n" +
                "INNER JOIN user ON tutorID = userID\n" +
                "WHERE sessionID = ?; ";

        try{
            preparedStatement = con.prepareStatement(query);
            preparedStatement.setString(1, sessionID);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()){
                otherDetails.add(resultSet.getString("firstName")+" " + resultSet.getString("lastName"));
                otherDetails.add(resultSet.getString("subjectName"));
                otherDetails.add(resultSet.getString("academicLevel"));
                otherDetails.add(resultSet.getString("sessionType"));
                otherDetails.add(resultSet.getString("sessionMode"));
                otherDetails.add(resultSet.getString("numberOfStudents"));
                otherDetails.add(resultSet.getString("maximumStudents"));
                otherDetails.add(resultSet.getString("sessionPrice"));
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return otherDetails;
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
    public void modifySession(String sessionID, String sessionMode, String sessionType, String numStudents, String maxStudents, String sessionPrice, String sessionStatus, String sessionVisibility) throws RemoteException, SQLException {
        query = "{CALL modifySession(?, ?, ?, ?, ?, ?, ?, ?)}";

        try {
            con.setAutoCommit(false);

            callStmt = con.prepareCall(query);
            callStmt.setString(1, sessionID);
            callStmt.setString(2, sessionMode);
            callStmt.setString(3, sessionType);
            callStmt.setString(4, numStudents);
            callStmt.setString(5, maxStudents);
            callStmt.setString(6, sessionPrice);
            callStmt.setString(7, sessionStatus);
            callStmt.setString(8, sessionVisibility);


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
    public TutorSession getEditableDetails(String sessionID) throws RemoteException {
        TutorSession details = new TutorSession();
        query = "SELECT sessionType, sessionMode, numberOfStudents, maximumStudents, sessionPrice, sessionStatus, visibility FROM tutorsession\n" +
                "WHERE sessionID = ? ;";

        try{
            preparedStatement = con.prepareStatement(query);
            preparedStatement.setString(1, sessionID);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()){
                details.setSessionStatus(resultSet.getString("sessionStatus"));
                details.setSessionType(resultSet.getString("sessionType"));
                details.setSessionMode(resultSet.getString("sessionMode"));
                details.setNumberOfStudents(resultSet.getInt("numberOfStudents"));
                details.setMaximumStudents(resultSet.getInt("maximumStudents"));
                details.setSessionPrice(resultSet.getInt("sessionPrice"));
                details.setVisibility(resultSet.getString("visibility"));
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return details;
    }

    @Override
    public List<String> getAllTutorNames() throws RemoteException{
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
    public List<String> getAllSubjectNames() throws RemoteException{
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
    public List<String> getAllTutorIDs() throws RemoteException{
        List<String> allTutorID = new ArrayList<>();
        query = "SELECT userID FROM user WHERE role = 'Tutor'";

        try{
            preparedStatement = con.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();

            while(resultSet.next()){
                allTutorID.add(resultSet.getString("userID"));
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return allTutorID;
    }
    @Override
    public List<String> getAllSubjectIDs() throws RemoteException{
        List<String> allSubjectID = new ArrayList<>();
        query = "SELECT subjectID FROM subject";

        try{
            preparedStatement = con.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();

            while(resultSet.next()){
                allSubjectID.add(resultSet.getString("subjectID"));
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return allSubjectID;
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
    public String getSubjectName(String subjectID) throws RemoteException{
        String subjectName = "";
        query = "SELECT subjectName FROM subject WHERE subjectID = ?";

        try{
            preparedStatement = con.prepareStatement(query);
            preparedStatement.setString(1, subjectID);
            resultSet = preparedStatement.executeQuery();

            while(resultSet.next()){
                subjectName = resultSet.getString("subjectName");
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return subjectName;
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
    public String getTutorName(String tutorID) throws RemoteException{
        String tutorName = "";
        query = "SELECT CONCAT(firstName,' ', lastName) AS name FROM user WHERE userID = ?;";
        try{
            preparedStatement = con.prepareStatement(query);
            preparedStatement.setString(1, tutorID);
            resultSet = preparedStatement.executeQuery();

            while(resultSet.next()){
                tutorName = resultSet.getString("name");
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return tutorName;
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
        query = "{CALL viewSubject()}";

        try{
            callStmt = con.prepareCall(query);
            resultSet = callStmt.executeQuery();

            while (resultSet.next()){
                allSubject.add(new Subject(resultSet.getString("subjectID"), resultSet.getString("subjectName"),
                        resultSet.getString("subjectDescription"), resultSet.getString("academicLevel")));
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return allSubject;
    }
    @Override
    public List<String> viewOtherSubjectDetails(String subjectID) throws RemoteException{
        List<String> otherDetails = new ArrayList<>();
        query = "SELECT subjectName, subjectDescription FROM subject WHERE subjectID = ?;";

        try{
            preparedStatement = con.prepareStatement(query);
            preparedStatement.setString(1, subjectID);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()){
                otherDetails.add(resultSet.getString("subjectName"));
                otherDetails.add(resultSet.getString("subjectDescription"));
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return otherDetails;
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
    public void modifySubject(String subjectID, String academicLevel) throws RemoteException, SQLException {
        subjectID = subjectID.replaceAll(".*subjectID=(\\d+),.*", "$1");
        query = "{CALL modifySubject(?,?)}";

        try {
            con.setAutoCommit(false);

            callStmt = con.prepareCall(query);
            callStmt.setString(1, subjectID);
            callStmt.setString(2, academicLevel);

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
    public Map<LessonPlan, String> viewLessonPlan() throws RemoteException {
        Map<LessonPlan, String> allLessonPlans = new LinkedHashMap<>();

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
                        resultSet.getString("objectives"),
                        resultSet.getString("topicsCovered")
                );

//                allLessonPlans.put(lessonPlan, Arrays.asList(resultSet.getString("subjectName"), resultSet.getString("academicLevel")));
                allLessonPlans.put(lessonPlan, resultSet.getString("academicLevel"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return allLessonPlans;
    }

    @Override
    public List<String> viewOtherLessonPlanDetails(String lessonPlanID) throws RemoteException{
        List<String> otherDetails = new ArrayList<>();
        query = "SELECT subjectName, objectives, topicsCovered FROM lessonplan" +
                " INNER JOIN subject USING(subjectID) WHERE lessonPlanID = ?;";

        try{
            preparedStatement = con.prepareStatement(query);
            preparedStatement.setString(1, lessonPlanID);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()){
                otherDetails.add(resultSet.getString("subjectName"));
                otherDetails.add(resultSet.getString("objectives"));
                otherDetails.add(resultSet.getString("topicsCovered"));
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return otherDetails;
    }

    @Override
    public List<Payment> viewPayment() throws RemoteException{
        List<Payment> studentPayments = new ArrayList<>();

        query = "SELECT * FROM payment";

        try {
            callStmt = con.prepareCall(query);
            resultSet = callStmt.executeQuery();

            while (resultSet.next()) {
                String paymentID = resultSet.getString(1);
                String studentID = resultSet.getString(2);
                double amount = resultSet.getDouble(3);
                LocalDate date = resultSet.getDate(4).toLocalDate();
                LocalTime time = resultSet.getTime(5).toLocalTime();
                String paymentMethod = resultSet.getString(6);

                Payment payment = new Payment(paymentID, studentID, date, time, paymentMethod, amount);
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
