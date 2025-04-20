package server.services;

import shared.classes.Student;
import shared.classes.TutorSession;
import shared.interfaces.TutorService;
import shared.classes.LessonPlan;
import server.database.DatabaseConnection;
import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TutorServiceImpl extends UnicastRemoteObject implements TutorService, Serializable {
    private static final long serialVersionUID = 1L; // Add a serialVersionUID
    private Connection connection;

    public TutorServiceImpl() throws RemoteException {
        super();
        this.connection = DatabaseConnection.setCon();
    }

    @Override
    public LessonPlan createLessonPlan(String lessonPlanID, String subjectID, String objectives, String topicsCovered) throws RemoteException {
        LessonPlan lessonPlan = new LessonPlan();
        try (Connection conn = DatabaseConnection.setCon();
             PreparedStatement stmt = conn.prepareStatement(
                     "INSERT INTO lessonplan (lessonPlanID, subjectID, objectives, topicsCovered) VALUES (?, ?, ?, ?)"
             )) {
            // Set parameters
            stmt.setString(1, lessonPlanID);
            stmt.setString(2, subjectID);
            stmt.setString(3, objectives);
            stmt.setString(4, topicsCovered);
            stmt.executeUpdate();
            // Populate the LessonPlan object
            lessonPlan.setLessonPlanID(lessonPlanID);
            lessonPlan.setSubjectID(subjectID);
            lessonPlan.setObjectives(objectives);
            lessonPlan.setTopicsCovered(topicsCovered);
        } catch (SQLException e) {
            throw new RemoteException("Database error: " + e.getMessage());
        }
        return lessonPlan;
    }

    @Override
    public List<TutorSession> viewSessionList() throws RemoteException {
        List<TutorSession> tutorsessions = new ArrayList<>();
        String query = "SELECT ts.sessionID, ts.tutorID, ts.subjectID, s.subjectName, ts.sessionStatus, " +
                "ts.sessionDate, ts.sessionTime, ts.sessionDuration, ts.numberOfStudents, " +
                "ts.maximumStudents, ts.sessionPrice, b.sessionMode " +
                "FROM tutorsession ts " +
                "JOIN subject s ON ts.subjectID = s.subjectID " +
                "LEFT JOIN booking b ON ts.sessionID = b.sessionID"; // Use LEFT JOIN to include all sessions

        try {
            Statement stmt = connection.createStatement();
            ResultSet resultSet = stmt.executeQuery(query);

            while (resultSet.next()) {
                String sessionID = resultSet.getString("sessionID");
                int tutorID = resultSet.getInt("tutorID");
                String subjectID = resultSet.getString("subjectID");
                String subjectName = resultSet.getString("subjectName");
                String sessionStatus = resultSet.getString("sessionStatus");
                String sessionDate = resultSet.getDate("sessionDate").toString();
                String sessionTime = resultSet.getString("sessionTime");
                int sessionDuration = resultSet.getInt("sessionDuration");
                int numberOfStudents = resultSet.getInt("numberOfStudents");
                int maximumStudents = resultSet.getInt("maximumStudents");
                double sessionPrice = resultSet.getDouble("sessionPrice");
                String sessionMode = resultSet.getString("sessionMode");

                TutorSession session = new TutorSession(sessionID, tutorID, subjectID, subjectName,
                        sessionDate, sessionTime, sessionDuration, sessionStatus, numberOfStudents, maximumStudents, sessionPrice, sessionMode);
                tutorsessions.add(session);
                System.out.println(session);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RemoteException("Error retrieving session list: " + e.getMessage(), e);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tutorsessions;
    }

    @Override
    public List<Student> getStudentsBySession(String sessionID) throws RemoteException {
        List<Student> students = new ArrayList<>();
        String query = "SELECT u.userID, u.firstName, u.lastName " +
                "FROM booking b " +
                "JOIN student s ON b.studentID = s.studentID " +
                "JOIN user u ON s.studentID = u.userID " +
                "WHERE b.sessionID = ?";

        try (Connection conn = DatabaseConnection.setCon();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, sessionID);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String userID = rs.getString("userID");
                String firstName = rs.getString("firstName");
                String lastName = rs.getString("lastName");

                // Provide default/dummy values for the fields not returned by the query
                long phoneNumber = 0L;
                String email = "";
                String role = "student";
                double balance = 0.0;
                String academicLevel = "";

                Student student = new Student(userID, firstName, lastName, phoneNumber, email, role, balance, academicLevel);
                students.add(student);
            }
        } catch (SQLException e) {
            throw new RemoteException("Database error while retrieving students: " + e.getMessage());
        }
        return students;
    }

}