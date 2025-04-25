package shared.classes;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;

public class TutorSession implements Serializable {
    // Session and tutor info
    private String sessionID;
    private String tutorID;

    // Subject details
    private String subjectID;
    private String subjectName;
    private transient String academicLevel;

    // Session schedule
    private LocalDate sessionDate;
    private LocalTime sessionTime;
    private int sessionDuration;

    // Enrollment and status
    private String sessionStatus;
    private int numberOfStudents;
    private int maximumStudents;

    // Pricing and mode
    private double sessionPrice;
    private String sessionMode;
    private String sessionType;

    // Constructor
    public TutorSession(String sessionID, int tutorID, String subjectID, String subjectName,
                        String sessionDate, String sessionTime, int sessionDuration,
                        String sessionStatus, int numberOfStudents, int maximumStudents,
                        double sessionPrice, String sessionMode) {
        this.sessionID = sessionID;
        this.tutorID = String.valueOf(tutorID);
        this.subjectID = subjectID;
        this.subjectName = subjectName;
        this.sessionDate = LocalDate.parse(sessionDate);
        this.sessionTime = LocalTime.parse(sessionTime);
        this.sessionDuration = sessionDuration;
        this.sessionStatus = sessionStatus;
        this.numberOfStudents = numberOfStudents;
        this.maximumStudents = maximumStudents;
        this.sessionPrice = sessionPrice;
        this.sessionMode = sessionMode;
    }

    public TutorSession(String sessionID, String tutorID, String subjectID, String sessionStatus,
                        LocalDate sessionDate, LocalTime sessionTime,String sessionMode, String sessionType,
                        int sessionDuration, int numberOfStudents,
                        int maximumStudents, double sessionPrice) {
        this.sessionID = sessionID;
        this.tutorID = tutorID;
        this.subjectID = subjectID;
        this.sessionStatus = sessionStatus;
        this.sessionDate = sessionDate;
        this.sessionTime = sessionTime;
        this.sessionMode = sessionMode;
        this.sessionType = sessionType;
        this.sessionDuration = sessionDuration;
        this.numberOfStudents = numberOfStudents;
        this.maximumStudents = maximumStudents;
        this.sessionPrice = sessionPrice;
    }

    public TutorSession(String sessionID, String sessionStatus,
                        LocalDate sessionDate, LocalTime sessionTime,
                        int sessionDuration, String academicLevel,
                        String tutorID, String subjectID) {
        this.sessionID = sessionID;
        this.sessionStatus = sessionStatus;
        this.sessionDate = sessionDate;
        this.sessionTime = sessionTime;
        this.sessionDuration = sessionDuration;
        this.academicLevel = academicLevel;
        this.tutorID = tutorID;
        this.subjectID = subjectID;

    }

    public TutorSession(String sessionStatus, String sessionMode,
                        String sessionType, int numberOfStudents,
                        int maximumStudents, double sessionPrice) {
        this.sessionStatus = sessionStatus;
        this.sessionMode = sessionMode;
        this.sessionType = sessionType;
        this.numberOfStudents = numberOfStudents;
        this.maximumStudents = maximumStudents;
        this.sessionPrice = sessionPrice;
    }

    public TutorSession(String sessionID, String s, String subjectID, String subjectName,
                        LocalDate sessionDate, LocalTime sessionTime,
                        int sessionDuration, String sessionStatus, int numberOfStudents,
                        int maximumStudents, double sessionPrice, String sessionMode) {
        this.sessionID = sessionID;
        this.subjectID = subjectID;
        this.subjectName = subjectName;
        this.sessionDate = sessionDate;
        this.sessionTime = sessionTime;
        this.sessionDuration = sessionDuration;
        this.sessionStatus = sessionStatus;
        this.numberOfStudents = numberOfStudents;
        this.maximumStudents = maximumStudents;
        this.sessionPrice = sessionPrice;
        this.sessionMode = sessionMode;
    }



    // Getters and Setters

    public int getMaximumStudents() {
        return maximumStudents;
    }

    public void setMaximumStudents(int maximumStudents) {
        this.maximumStudents = maximumStudents;
    }

    public int getNumberOfStudents() {
        return numberOfStudents;
    }

    public void setNumberOfStudents(int numberOfStudents) {
        this.numberOfStudents = numberOfStudents;
    }

    public int getSessionDuration() {
        return sessionDuration;
    }

    public void setSessionDuration(int sessionDuration) {
        this.sessionDuration = sessionDuration;
    }

    public LocalDate getSessionDate() {
        return sessionDate;
    }

    public void setSessionDate(LocalDate sessionDate) {
        this.sessionDate = sessionDate;
    }

    public String getSessionID() {
        return sessionID;
    }

    public void setSessionID(String sessionID) {
        this.sessionID = sessionID;
    }

    public String getSessionMode() {
        return sessionMode;
    }

    public void setSessionMode(String sessionMode) {
        this.sessionMode = sessionMode;
    }

    public int getSessionPrice() {
        return (int) sessionPrice;
    }

    public void setSessionPrice(double sessionPrice) {
        this.sessionPrice = sessionPrice;
    }

    public String getSessionStatus() {
        return sessionStatus;
    }

    public void setSessionStatus(String sessionStatus) {
        this.sessionStatus = sessionStatus;
    }

    public LocalTime getSessionTime() {
        return sessionTime;
    }

    public void setSessionTime(LocalTime sessionTime) {
        this.sessionTime = sessionTime;
    }

    public String getSubjectID() {
        return subjectID;
    }

    public void setSubjectID(String subjectID) {
        this.subjectID = subjectID;
    }

    public String getAcademicLevel() {
        return academicLevel;
    }

    public void setAcademicLevel(String academicLevel) {
        this.academicLevel = academicLevel;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public String getTutorID() {
        return tutorID;
    }

    public void setTutorID(String tutorID) {
        this.tutorID = tutorID;
    }

    // String representation
    @Override
    public String toString() {
        return "TutorSession{" +
                "sessionID='" + sessionID + '\'' +
                ", tutorID=" + tutorID +
                ", subjectID='" + subjectID + '\'' +
                ", subjectName='" + subjectName + '\'' +
                ", sessionStatus='" + sessionStatus + '\'' +
                ", sessionDate='" + sessionDate + '\'' +
                ", sessionTime='" + sessionTime + '\'' +
                ", sessionDuration=" + sessionDuration +
                ", numberOfStudents=" + numberOfStudents +
                ", maximumStudents=" + maximumStudents +
                ", sessionPrice=" + sessionPrice +
                ", sessionMode='" + sessionMode + '\'' +
                '}';
    }

    public String getSessionType() {
        return sessionType;
    }

    public void setSessionType(String sessionType) {
        this.sessionType = sessionType;
    }
}
