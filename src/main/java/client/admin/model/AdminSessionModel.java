package client.admin.model;

import shared.classes.TutorSession;
import shared.interfaces.AdminService;

import java.util.List;

public class AdminSessionModel {
    AdminService adminService;

    public AdminSessionModel(){
//        this.adminService = AdminClient.getConnection();
    }

    public List<TutorSession> fetchSessions(){
        List<TutorSession> sessions = (List<TutorSession>) adminService.viewSession();

        return sessions;
    }

    public boolean addSession(TutorSession newSession){

        return false;
    }

    public boolean modifySession(TutorSession session){

        return false;
    }

}
