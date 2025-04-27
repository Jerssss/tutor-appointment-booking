package client.admin.model;

import client.AdminClient;
import shared.classes.Subject;
import shared.classes.Tutor;
import shared.classes.TutorSession;
import shared.interfaces.AdminService;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

public class AdminViewMoreSessionsPopUpModel {
    private final AdminService adminService;

    public AdminViewMoreSessionsPopUpModel( ){
        this.adminService = AdminClient.getAdminService();
    }

    public List<String> getDetails(String sessionID) throws RemoteException {
        List<TutorSession> sessions = adminService.viewSession();
        List<Tutor> tutors = adminService.viewTutor();
        List<Subject> subjects = adminService.viewSubject();
        List<String> sessionDetails = new ArrayList<>();

        for (TutorSession session : sessions){
            if(session.getSessionID().equals(sessionID)){
                for (Tutor tutor : tutors){
                    if (session.getTutorID().equals(tutor.getUserID())){
                        sessionDetails.add(tutor.getFirstName()+" "+tutor.getLastName());
                    }
                }

                for(Subject subject : subjects){
                    if (session.getSubjectID().equals(subject.getSubjectID())){
                        sessionDetails.add(subject.getSubjectName());
                        sessionDetails.add(subject.getAcademicLevel());
                    }
                }

                System.out.println("TYPE: " + session.getSessionType() );
                sessionDetails.add(session.getSessionType());
                sessionDetails.add(session.getSessionMode());
                sessionDetails.add(String.valueOf(session.getNumberOfStudents()));
                sessionDetails.add(String.valueOf(session.getMaximumStudents()));
                sessionDetails.add(String.valueOf(session.getSessionPrice()));
            }
        }

        return sessionDetails;
    }

}
