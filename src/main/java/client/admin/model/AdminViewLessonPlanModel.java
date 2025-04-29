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
        this.adminService = AdminClient.getAdminService();
    }

//    public Map<LessonPlan, List<String>> fetchLessonPlan() throws RemoteException {
//        return adminService.viewLessonPlan();
//    }

    public List<LessonPlan> fetchLessonPlan() throws RemoteException {
        try {
            return adminService.viewLessonPlan();
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public List<LessonPlan> getLessonPlanCollege() {
        List<LessonPlan> collegeLessonPlans = new ArrayList<>();
        List<LessonPlan> allLessonPlans = new ArrayList<>();
        try {
            allLessonPlans = fetchLessonPlan();

        }catch (RemoteException e){
            e.printStackTrace();
        }
        for (LessonPlan lessonPlan : allLessonPlans) {

            if ("College".equalsIgnoreCase(lessonPlan.getAcademicLevel()) && "Available".equalsIgnoreCase(lessonPlan.getVisibility())) {
                collegeLessonPlans.add(lessonPlan);
            }
        }

        return collegeLessonPlans;
    }

    public List<LessonPlan> getLessonPlanHighSchool() {
        List<LessonPlan> highSchoolLessonPlans = new ArrayList<>();
        List<LessonPlan> allLessonPlans = new ArrayList<>();

        try {
            allLessonPlans = fetchLessonPlan();

        }catch (RemoteException e){
            e.printStackTrace();
        }
        for (LessonPlan lessonPlan : allLessonPlans) {

            if ("High School".equalsIgnoreCase(lessonPlan.getAcademicLevel()) && "Available".equalsIgnoreCase(lessonPlan.getVisibility())) {
                highSchoolLessonPlans.add(lessonPlan);
            }
        }

        return highSchoolLessonPlans;
    }

    public List<LessonPlan> getArchivedLessonPlanCollege() {
        List<LessonPlan> collegeArchivedLessonPlans = new ArrayList<>();
        List<LessonPlan> allLessonPlans = new ArrayList<>();
        try {
            allLessonPlans = fetchLessonPlan();

        }catch (RemoteException e){
            e.printStackTrace();
        }
        for (LessonPlan lessonPlan : allLessonPlans) {

            if ("College".equalsIgnoreCase(lessonPlan.getAcademicLevel()) && "Archived".equalsIgnoreCase(lessonPlan.getVisibility())) {
                collegeArchivedLessonPlans.add(lessonPlan);
            }
        }

        return collegeArchivedLessonPlans;
    }

    public List<LessonPlan> getArchivedLessonPlanHighSchool() {
        List<LessonPlan> highSchoolArchivedLessonPlans = new ArrayList<>();
        List<LessonPlan> allLessonPlans = new ArrayList<>();

        try {
            allLessonPlans = fetchLessonPlan();

        }catch (RemoteException e){
            e.printStackTrace();
        }
        for (LessonPlan lessonPlan : allLessonPlans) {

            if ("High School".equalsIgnoreCase(lessonPlan.getAcademicLevel()) && "Archived".equalsIgnoreCase(lessonPlan.getVisibility())) {
                highSchoolArchivedLessonPlans.add(lessonPlan);
            }
        }

        return highSchoolArchivedLessonPlans;
    }
}