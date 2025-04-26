package client.admin.model;

import client.AdminClient;
import shared.classes.TutorSession;
import shared.interfaces.AdminService;

import java.rmi.RemoteException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AdminAddSessionPopUpModel {
    private final AdminService adminService;
    private static List<LocalTime> availableStartTimes = new ArrayList<>();

    public AdminAddSessionPopUpModel(){
        this.adminService = AdminClient.getAdminProcessors();
    }

    public List<String> getAllTutorNames() throws RemoteException {
        return adminService.getAllTutorNames();
    }

    public List<String> getAllTutorIDs() throws RemoteException{
        return adminService.getAllTutorIDs();
    }

    public List<String> getAllSubjectNames() throws RemoteException {
        return adminService.getAllSubjectNames();
    }

    public List<String> getAllSubjectIDs() throws RemoteException{
        return adminService.getAllSubjectIDs();
    }

    public String getTutorName(String tutorID) throws RemoteException {
        return adminService.getTutorName(tutorID);
    }

    public String getTutorID(String tutorName) throws RemoteException {
        return adminService.getTutorID(tutorName);
    }

    public String getSubjectName(String subjectID) throws RemoteException {
        return adminService.getSubjectName(subjectID);
    }

    public String getSubjectID(String subjectName) throws RemoteException {
        return adminService.getSubjectID(subjectName);
    }


    public List<String> getAvailableTimeTutor(String tutorName, String date) throws RemoteException {
        Map<LocalTime, Integer> currentSched = adminService.getTutorSchedule(adminService.getTutorID(tutorName), date);
        availableStartTimes = generateTimeslots();

        for (Map.Entry<LocalTime, Integer> entry : currentSched.entrySet()) {
            LocalTime sessionTime = entry.getKey();
            int sessionDuration = entry.getValue();
            availableStartTimes.remove(sessionTime);

            for (int i = 30; i< sessionDuration; i+=30){
                sessionTime = sessionTime.plusMinutes(30);
                availableStartTimes.remove(sessionTime);
            }
        }

        List<String> times = new ArrayList<>();
        for (LocalTime time : availableStartTimes){
            times.add(time.toString());
        }

        return times;
    }

    public List<String> getAvailableDurations(LocalTime time){
        List<String> durations = new ArrayList<>();
        int counter = 60;
        while (counter <= 150){
            if (availableStartTimes.contains(time.plusMinutes(counter))){
                durations.add(String.valueOf(counter));
            } else {
                break;
            }
            counter += 30;
        }

        if (durations.isEmpty()){
            durations.add("Please choose another start time. Expected end times are not available.");
        }

        return durations;
    }

    public void addNewSession(String chosenTutor, LocalDate chosenDate, String chosenStartTime, String chosenDuration, String chosenSubject,
                              String chosenMode, String chosenType, String maxStudents, String price) throws RemoteException, SQLException {
        TutorSession newSession = new TutorSession(generateSessionID(), chosenTutor, chosenSubject, "Scheduled",
                chosenDate, LocalTime.parse(chosenStartTime), chosenMode, chosenType, Integer.parseInt(chosenDuration), 0, Integer.parseInt(maxStudents),
                Double.parseDouble(price));
        adminService.addSession(newSession);
    }

    private List<LocalTime> generateTimeslots(){
        LocalTime startTime = LocalTime.of(8, 0);
        LocalTime endTime = LocalTime.of(17, 0);
        LocalTime currentTime = startTime;
        List<LocalTime> allTimes = new ArrayList<>();

        while (currentTime.isBefore(endTime.plusMinutes(1))) {
            allTimes.add(currentTime);
            currentTime = currentTime.plusMinutes(30);
        }
        return allTimes;
    }

    private String generateSessionID() throws RemoteException {
        List<String> sessionIDs = adminService.getAllSessionID();

        int maxNumber = 0;

        for (String sessionID : sessionIDs) {
            String numberPart = sessionID.substring(1);
            int number = Integer.parseInt(numberPart);
            if (number > maxNumber) {
                maxNumber = number;
            }
        }
        return String.format("S%03d", (maxNumber+1));
    }
}
