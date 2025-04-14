package server.services;

import shared.interfaces.TutorService;
import shared.classes.LessonPlan;
import server.database.DatabaseConnection;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.*;

public class TutorServiceImpl extends UnicastRemoteObject implements TutorService, Serializable {
    private static final long serialVersionUID = 1L; // Add a serialVersionUID

    public TutorServiceImpl() throws RemoteException {
        super();
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
}