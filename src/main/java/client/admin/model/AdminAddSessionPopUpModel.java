package client.admin.model;

import client.AdminClient;
import shared.classes.Tutor;
import shared.classes.TutorSession;
import shared.classes.Subject;
import shared.interfaces.AdminService;

import java.rmi.RemoteException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdminAddSessionPopUpModel {
    private final AdminService adminService;
    private static List<LocalTime> availableStartTimes = new ArrayList<>();

    public AdminAddSessionPopUpModel(){
        this.adminService = AdminClient.getAdminService();
    }

    public List<String> getAllTutorNames() throws RemoteException {
        List<String> tutorNames = new ArrayList<>();
        List<Tutor> tutors = adminService.viewTutor();

        for (Tutor tutor: tutors){
            tutorNames.add(tutor.getFirstName()+" "+ tutor.getLastName());
        }

        return tutorNames;
    }

    public List<String> getAllTutorIDs() throws RemoteException{
        List<String> tutorIDs = new ArrayList<>();
        List<Tutor> tutors = adminService.viewTutor();

        for (Tutor tutor: tutors){
            tutorIDs.add(tutor.getUserID());
        }

        return tutorIDs;
    }

    public List<String> getAllSubjectNames() throws RemoteException {
        List<String> subjectNames = new ArrayList<>();
        List<Subject> subjects = adminService.viewSubject();

        for (Subject subject : subjects){
            subjectNames.add(subject.getSubjectName());
        }
        return subjectNames;
    }

    public List<String> getAllSubjectIDs() throws RemoteException{
        List<String> subjectIDs = new ArrayList<>();
        List<Subject> subjects = adminService.viewSubject();

        for (Subject subject : subjects){
            subjectIDs.add(subject.getSubjectID());
        }
        return subjectIDs;
    }

    public String getTutorName(String tutorID) throws RemoteException {
        String tutorName = "";
        List<Tutor> tutors = adminService.viewTutor();

        for (Tutor tutor: tutors){
            if (tutorID.equals(tutor.getUserID())){
                tutorName = tutor.getFirstName()+" "+tutor.getLastName();
            }
        }

        return tutorName;
    }

    public String getTutorID(String tutorName) throws RemoteException {
        String tutorID = "";
        List<Tutor> tutors = adminService.viewTutor();

        for (Tutor tutor: tutors){
            if (tutorName.equals(tutor.getFirstName()+" "+tutor.getLastName())){
                tutorID = tutor.getUserID();
            }
        }

        return tutorID;
    }

    public String getSubjectName(String subjectID) throws RemoteException {
        List<Subject> subjects = adminService.viewSubject();
        String subjectName = "";

        System.out.println("SUBJECT ID: " + subjectID);
        for(Subject subject : subjects){
            System.out.println("DVSA SUBJECT ID: " + subject.getSubjectID());

            if (subject.getSubjectID().equals(subjectID)){
                subjectName = subject.getSubjectName();
            }
        }
        return subjectName;
    }

    public String getSubjectID(String subjectName) throws RemoteException {
        List<Subject> subjects = adminService.viewSubject();
        String subjectID = "";

        for(Subject subject : subjects){
            if (subject.getSubjectName().equals(subjectName)){
                subjectID = subject.getSubjectID();
            }
        }
        return subjectID;
    }


    public List<String> getAvailableTimeTutor(String tutorID, String date) throws RemoteException {
        Map<LocalTime, Integer> currentSched = getTutorSchedule(tutorID, date);
//                adminService.getTutorSchedule(getTutorID(tutorName), date);


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

    public Map<LocalTime, Integer> getTutorSchedule(String tutorID, String date) throws RemoteException {
        Map<LocalTime, Integer> currentSched = new HashMap<>();
        List<TutorSession> sessions = adminService.viewSession();
        for (TutorSession session : sessions){
            if (session.getTutorID().equals(tutorID) && String.valueOf(session.getSessionDate()).equals(date)){
                currentSched.put(session.getSessionTime(), session.getSessionDuration());
            }
        }
        return currentSched;
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
        List<TutorSession> sessions = adminService.viewSession();
        List<String> sessionIDs = new ArrayList<>();
        for (TutorSession session : sessions){
            sessionIDs.add(session.getSessionID());
        }

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
