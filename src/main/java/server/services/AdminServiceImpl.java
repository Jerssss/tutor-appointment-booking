package server.services;

import server.database.DatabaseConnection;
import shared.classes.*;
import shared.interfaces.AdminService;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AdminServiceImpl implements Remote, AdminService, Serializable {
    private static final long serialVersionUID = 1L; // Add a serialVersionUID
    private static String query;
    private static PreparedStatement preparedStatement;
    private static ResultSet resultSet;

    private static Connection con = DatabaseConnection.setCon();
    @Override
    public List<Student> viewStudent()  throws RemoteException {
        return null;
    }

    @Override
    public Student addStudent() throws RemoteException {
        return null;
    }

    @Override
    public Student modifyStudent() throws RemoteException {
        return null;
    }

    @Override
    public Tutor viewTutor() throws RemoteException {
        return null;
    }

    @Override
    public Tutor addTutor() throws RemoteException {
        return null;
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
    public List<Payment> viewPayment() throws RemoteException {
        return null;
    }
}
