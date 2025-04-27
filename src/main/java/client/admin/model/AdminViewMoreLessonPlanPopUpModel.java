package client.admin.model;

import shared.classes.LessonPlan;
import shared.classes.Subject;
import shared.interfaces.AdminService;
import client.AdminClient;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AdminViewMoreLessonPlanPopUpModel {
    private final AdminService adminService;

    public AdminViewMoreLessonPlanPopUpModel( ){
        this.adminService = AdminClient.getAdminService();
    }


    public List<String> getDetails(String lessonPlanID) throws RemoteException {
        List<String> lessonPlanDetails = new ArrayList<>();
        Map<LessonPlan, String> lessonPlans = adminService.viewLessonPlan();
        List<Subject> subjects = adminService.viewSubject();

        for(Map.Entry<LessonPlan, String> entry : lessonPlans.entrySet()){
            if (entry.getKey().getLessonPlanID().equals(lessonPlanID)){
                lessonPlanDetails.add(entry.getKey().getObjectives());
                lessonPlanDetails.add(entry.getKey().getTopicsCovered());
                for (Subject subject : subjects){
                    if (subject.getSubjectID().equals(entry.getKey().getSubjectID())){
                        lessonPlanDetails.add(subject.getSubjectName());
                    }
                }
            }
        }

        return lessonPlanDetails;
    }

}
