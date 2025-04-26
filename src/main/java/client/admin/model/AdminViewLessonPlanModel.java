package client.admin.model;

import client.AdminClient;
import shared.classes.LessonPlan;
import shared.interfaces.AdminService;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdminViewLessonPlanModel {
    private final AdminService adminService;

    public AdminViewLessonPlanModel() {
        this.adminService = AdminClient.getAdminProcessors();
    }

//    public Map<LessonPlan, List<String>> fetchLessonPlan() throws RemoteException {
//        return adminService.viewLessonPlan();
//    }

    public Map<LessonPlan, String> fetchLessonPlan() throws RemoteException {
        return adminService.viewLessonPlan();
    }

    public List<LessonPlan> getLessonPlanCollege() {
        List<LessonPlan> collegeLessonPlans = new ArrayList<>();
        Map<LessonPlan, String> allLessonPlans = new HashMap<>();
        try {
            allLessonPlans = fetchLessonPlan();

        }catch (RemoteException e){
            e.printStackTrace();
        }
        for (Map.Entry<LessonPlan, String> entry : allLessonPlans.entrySet()) {

            if ("College".equalsIgnoreCase(entry.getValue())) {
                collegeLessonPlans.add(entry.getKey());
            }
        }

        return collegeLessonPlans;
    }

    public List<LessonPlan> getLessonPlanHighSchool() {
        List<LessonPlan> highSchoolLessonPlans = new ArrayList<>();
        Map<LessonPlan, String> allLessonPlans = new HashMap<>();
        try {
            allLessonPlans = fetchLessonPlan();

        }catch (RemoteException e){
            e.printStackTrace();
        }
        for (Map.Entry<LessonPlan, String> entry : allLessonPlans.entrySet()) {

            if ("High School".equalsIgnoreCase(entry.getValue())) {
                highSchoolLessonPlans.add(entry.getKey());
            }
        }

        return highSchoolLessonPlans;
    }
}