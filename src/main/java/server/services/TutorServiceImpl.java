package server.services;

import shared.classes.*;
import shared.interfaces.TutorService;
import server.database.DatabaseConnection;
import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TutorServiceImpl extends UnicastRemoteObject implements TutorService, Serializable {
    private static final long serialVersionUID = 1L;
    private static final Connection con = DatabaseConnection.setCon();
    private static Statement stmt;

    public TutorServiceImpl() throws RemoteException {
        super();
    }

    public String generateNewLessonPlanID() throws SQLException {
        String query = "SELECT lessonPlanID FROM lessonplan ORDER BY lessonPlanID DESC LIMIT 1";
        String latestLessonPlanID = "LP0";

        try (Statement stmt = con.createStatement();
             ResultSet resultSet = stmt.executeQuery(query)) {
            if (resultSet.next()) {
                latestLessonPlanID = resultSet.getString("lessonPlanID");
            }
        }
        String numericPart = latestLessonPlanID.replaceAll("[^0-9]", "");
        int nextID = Integer.parseInt(numericPart) + 1;
        return "LP" + nextID;
    }

    @Override
    public void addLessonPlan(LessonPlan newLessonPlan) throws RemoteException, SQLException {
        String subjectID = newLessonPlan.getSubjectID();
        String subjectName = null;

        String subjectQuery = "SELECT subjectName FROM subject WHERE subjectID = ?";
        try (PreparedStatement subjectStmt = con.prepareStatement(subjectQuery)) {
            subjectStmt.setString(1, subjectID);
            ResultSet rs = subjectStmt.executeQuery();
            if (rs.next()) {
                subjectName = rs.getString("subjectName");
            } else {
                throw new SQLException("Subject ID not found: " + subjectID);
            }
        }

        String insertQuery = "INSERT INTO lessonplan (lessonPlanID, subjectID, objectives, topicsCovered, visibility) VALUES (?, ?, ?, ?, ?)";
        try {
            con.setAutoCommit(false);
            String newLessonPlanID = generateNewLessonPlanID();

            try (PreparedStatement preparedStmt = con.prepareStatement(insertQuery)) {
                preparedStmt.setString(1, newLessonPlanID);
                preparedStmt.setString(2, subjectID);
                preparedStmt.setString(3, newLessonPlan.getObjectives());
                preparedStmt.setString(4, newLessonPlan.getTopicsCovered());
                preparedStmt.setString(5, "Available");
                preparedStmt.executeUpdate();
            }
            con.commit();
            System.out.println("[SERVER] Successfully added lesson plan with ID: " + newLessonPlanID);
        } catch (SQLException e) {
            con.rollback();
            System.err.println("[SERVER ERROR] Failed to add lesson plan: " + e.getMessage());
            throw new SQLException("Failed to add lesson plan: " + e.getMessage(), e);
        } finally {
            con.setAutoCommit(true);
        }
    }

    @Override
    public List<TutorSession> viewSessionList(String tutorID) throws RemoteException {
        List<TutorSession> tutorSessions = new ArrayList<>();
        String query = "SELECT ts.sessionID, ts.tutorID, ts.subjectID, s.subjectName, ts.sessionStatus, " +
                "ts.sessionDate, ts.sessionTime, ts.sessionDuration, ts.numberOfStudents, " +
                "ts.maximumStudents, ts.sessionPrice, b.sessionMode " +
                "FROM tutorsession ts " +
                "JOIN subject s ON ts.subjectID = s.subjectID " +
                "LEFT JOIN booking b ON ts.sessionID = b.sessionID " +
                "WHERE ts.tutorID = ?";

        try (Connection conn = DatabaseConnection.setCon();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, tutorID);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                TutorSession session = new TutorSession(
                        rs.getString("sessionID"),
                        rs.getString("tutorID"),
                        rs.getString("subjectID"),
                        rs.getString("subjectName"),
                        rs.getDate("sessionDate").toLocalDate(),
                        rs.getTime("sessionTime").toLocalTime(),
                        rs.getInt("sessionDuration"),
                        rs.getString("sessionStatus"),
                        rs.getInt("numberOfStudents"),
                        rs.getInt("maximumStudents"),
                        rs.getDouble("sessionPrice"),
                        rs.getString("sessionMode")
                );
                tutorSessions.add(session);
                System.out.println("[SERVER] Retrieved session: " + session);
            }
        } catch (SQLException e) {
            throw new RemoteException("Error retrieving session list: " + e.getMessage(), e);
        }
        return tutorSessions;
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
                Student student = new Student(
                        rs.getString("userID"),
                        rs.getString("firstName"),
                        rs.getString("lastName")
                );
                students.add(student);
            }
        } catch (SQLException e) {
            throw new RemoteException("Database error while retrieving students: " + e.getMessage());
        }
        return students;
    }

    @Override
    public List<LessonPlan> viewLessonPlanByTutor(String tutorID) throws RemoteException {
        List<LessonPlan> lessonPlanList = new ArrayList<>();
        String query = "SELECT lp.lessonPlanID, lp.subjectID, s.subjectName, lp.objectives, lp.topicsCovered, lp.visibility " +
                "FROM lessonplan lp " +
                "JOIN subject s ON lp.subjectID = s.subjectID " +
                "JOIN tutor t ON FIND_IN_SET(s.subjectName, t.expertise) > 0 " +
                "WHERE t.tutorID = ? AND lp.visibility = 'Available'";

        try (Connection conn = DatabaseConnection.setCon();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, tutorID);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                LessonPlan lessonPlan = new LessonPlan(
                        rs.getString("lessonPlanID"),
                        rs.getString("subjectID"),
                        rs.getString("subjectName"),
                        rs.getString("objectives"),
                        rs.getString("topicsCovered")
                );
                lessonPlanList.add(lessonPlan);
                System.out.println("[SERVER] Retrieved lesson plan: " + lessonPlan);
            }
        } catch (SQLException e) {
            throw new RemoteException("Error retrieving lesson plans: " + e.getMessage(), e);
        }
        return lessonPlanList;
    }

    @Override
    public void modifyLessonPlan(String lessonPlanID, String newObjectives, String newTopicsCovered) throws RemoteException {
        String query = "UPDATE lessonplan SET objectives = ?, topicsCovered = ? WHERE lessonPlanID = ?";
        try (PreparedStatement stmt = con.prepareStatement(query)) {
            stmt.setString(1, newObjectives);
            stmt.setString(2, newTopicsCovered);
            stmt.setString(3, lessonPlanID);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("[SERVER] Updated lesson plan with ID: " + lessonPlanID);
            } else {
                System.out.println("[SERVER] No lesson plan found with ID: " + lessonPlanID);
            }
        } catch (SQLException e) {
            throw new RemoteException("Error updating lesson plan: " + e.getMessage(), e);
        }
    }

    @Override
    public void deleteLessonPlan(String lessonPlanID) throws RemoteException {
        String query = "UPDATE lessonplan SET visibility = 'Archived' WHERE lessonPlanID = ?";
        try (PreparedStatement stmt = con.prepareStatement(query)) {
            stmt.setString(1, lessonPlanID);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("[SERVER] Archived lesson plan with ID: " + lessonPlanID);
            } else {
                System.out.println("[SERVER] No lesson plan found with ID: " + lessonPlanID);
            }
        } catch (SQLException e) {
            throw new RemoteException("Error archiving lesson plan: " + e.getMessage(), e);
        }
    }

    @Override
    public TutorSession getSessionDetails(String sessionID) throws RemoteException {
        String query = "SELECT ts.sessionID, ts.tutorID, ts.subjectID, s.subjectName, ts.sessionStatus, " +
                "ts.sessionDate, ts.sessionTime, ts.sessionDuration, ts.numberOfStudents, " +
                "ts.maximumStudents, ts.sessionPrice, b.sessionMode, ts.sessionType " +
                "FROM tutorsession ts " +
                "JOIN subject s ON ts.subjectID = s.subjectID " +
                "LEFT JOIN booking b ON ts.sessionID = b.sessionID " +
                "WHERE ts.sessionID = ?";

        try (Connection conn = DatabaseConnection.setCon();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, sessionID);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new TutorSession(
                        rs.getString("sessionID"),
                        rs.getString("tutorID"),
                        rs.getString("subjectID"),
                        rs.getString("subjectName"),
                        rs.getDate("sessionDate").toLocalDate(),
                        rs.getTime("sessionTime").toLocalTime(),
                        rs.getInt("sessionDuration"),
                        rs.getString("sessionStatus"),
                        rs.getInt("numberOfStudents"),
                        rs.getInt("maximumStudents"),
                        rs.getDouble("sessionPrice"),
                        rs.getString("sessionMode"),
                        rs.getString("sessionType")
                );
            }
        } catch (SQLException e) {
            throw new RemoteException("Database error while retrieving session details: " + e.getMessage());
        }
        return null;
    }

    @Override
    public LessonPlan getLessonPlanDetails(String lessonPlanID) throws RemoteException {
        String query = "SELECT lp.lessonPlanID, lp.subjectID, s.subjectName, lp.objectives, lp.topicsCovered " +
                "FROM lessonplan lp " +
                "LEFT JOIN subject s ON lp.subjectID = s.subjectID " +
                "WHERE lp.lessonPlanID = ?";

        try (Connection conn = DatabaseConnection.setCon();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, lessonPlanID);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new LessonPlan(
                        rs.getString("lessonPlanID"),
                        rs.getString("subjectID"),
                        rs.getString("subjectName"),
                        rs.getString("objectives"),
                        rs.getString("topicsCovered")
                );
            }
        } catch (SQLException e) {
            throw new RemoteException("Database error while retrieving lesson plan details: " + e.getMessage());
        }
        return null;
    }

    @Override
    public List<String> getSubjectsByTutorExpertise(String tutorID) throws RemoteException {
        List<String> subjects = new ArrayList<>();
        String query = "SELECT expertise FROM tutor WHERE tutorID = ?";

        try (Connection conn = DatabaseConnection.setCon();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, tutorID);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String expertise = rs.getString("expertise");
                System.out.println("[SERVER] Expertise for tutor " + tutorID + ": " + expertise);
                if (expertise != null && !expertise.trim().isEmpty()) {
                    subjects = Arrays.asList(expertise.split("\\s*,\\s*"));
                } else {
                    System.out.println("[SERVER] No expertise found for tutor " + tutorID);
                }
            } else {
                System.out.println("[SERVER] No tutor found with ID " + tutorID);
            }
        } catch (SQLException e) {
            System.err.println("[SERVER ERROR] Error retrieving tutor expertise: " + e.getMessage());
            throw new RemoteException("Error retrieving tutor expertise: " + e.getMessage(), e);
        }

        List<String> subjectNames = new ArrayList<>();
        String subjectQuery = "SELECT subjectName FROM subject WHERE subjectName = ? AND visibility = 'Available'";
        try (Connection conn = DatabaseConnection.setCon();
             PreparedStatement stmt = conn.prepareStatement(subjectQuery)) {
            for (String expertise : subjects) {
                stmt.setString(1, expertise.trim());
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    subjectNames.add(rs.getString("subjectName"));
                } else {
                    System.out.println("[SERVER] No subject found for expertise: " + expertise);
                }
            }
        } catch (SQLException e) {
            System.err.println("[SERVER ERROR] Error mapping expertise to subjects: " + e.getMessage());
            throw new RemoteException("Error mapping expertise to subjects: " + e.getMessage(), e);
        }

        System.out.println("[SERVER] Subjects for tutor " + tutorID + ": " + subjectNames);
        return subjectNames;
    }

    @Override
    public LessonPlan getLessonPlanBySubjectID(String subjectID) throws RemoteException {
        String query = "SELECT lp.lessonPlanID, lp.subjectID, s.subjectName, lp.objectives, lp.topicsCovered " +
                "FROM lessonplan lp " +
                "JOIN subject s ON lp.subjectID = s.subjectID " +
                "WHERE lp.subjectID = ?";

        try (Connection conn = DatabaseConnection.setCon();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, subjectID);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new LessonPlan(
                        rs.getString("lessonPlanID"),
                        rs.getString("subjectID"),
                        rs.getString("subjectName"),
                        rs.getString("objectives"),
                        rs.getString("topicsCovered")
                );
            }
        } catch (SQLException e) {
            throw new RemoteException("Database error while retrieving lesson plan details: " + e.getMessage());
        }
        return null;
    }

    @Override
    public String getSubjectIDByName(String subjectName) throws RemoteException {
        String query = "SELECT subjectID FROM subject WHERE subjectName = ? AND visibility = 'Available'";
        try (Connection conn = DatabaseConnection.setCon();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, subjectName);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getString("subjectID");
            }
        } catch (SQLException e) {
            throw new RemoteException("Error retrieving subject ID for name: " + subjectName, e);
        }
        return null;
    }

    @Override
    public List<LessonPlan> viewArchivedLessonPlansByTutor(String tutorID) throws RemoteException {
        List<LessonPlan> archivedLessonPlanList = new ArrayList<>();
        String query = "SELECT lp.lessonPlanID, lp.subjectID, s.subjectName, lp.objectives, lp.topicsCovered " +
                "FROM lessonplan lp " +
                "JOIN subject s ON lp.subjectID = s.subjectID " +
                "JOIN tutor t ON FIND_IN_SET(s.subjectName, t.expertise) > 0 " +
                "WHERE t.tutorID = ? AND lp.visibility = 'Archived'";

        try (Connection conn = DatabaseConnection.setCon();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, tutorID);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                LessonPlan lessonPlan = new LessonPlan(
                        rs.getString("lessonPlanID"),
                        rs.getString("subjectID"),
                        rs.getString("subjectName"),
                        rs.getString("objectives"),
                        rs.getString("topicsCovered")
                );
                archivedLessonPlanList.add(lessonPlan);
            }
        } catch (SQLException e) {
            throw new RemoteException("Error retrieving archived lesson plans: " + e.getMessage(), e);
        }
        return archivedLessonPlanList;
    }

    @Override
    public String getAcademicLevelBySubjectName(String subjectName) throws RemoteException {
        String query = "SELECT academicLevel FROM subject WHERE subjectName = ? AND visibility = 'Available'";
        try (Connection conn = DatabaseConnection.setCon();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, subjectName);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String academicLevel = rs.getString("academicLevel");
                System.out.println("[SERVER] Academic level for subject " + subjectName + ": " + academicLevel);
                return academicLevel;
            } else {
                System.out.println("[SERVER] No subject found for name: " + subjectName);
            }
        } catch (SQLException e) {
            System.err.println("[SERVER ERROR] Error retrieving academic level for subject: " + subjectName + ": " + e.getMessage());
            throw new RemoteException("Error retrieving academic level for subject: " + subjectName, e);
        }
        return null;
    }
}