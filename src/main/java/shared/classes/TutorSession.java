package shared.classes;

import java.io.Serializable;

public class TutorSession implements Serializable {
    // Session and tutor info
    private String sessionID;
    private int tutorID;

    // Subject details
    private String subjectID;
    private String subjectName;
    private transient String subjectLevel;

    // Session schedule
    private String sessionDate;
    private String sessionTime;
    private int sessionDuration;

    // Enrollment and status
    private String sessionStatus;
    private int numberOfStudents;
    private int maximumStudents;

    // Pricing and mode
    private double sessionPrice;
    private String sessionMode;

    // Constructor
    public TutorSession(String sessionID, int tutorID, String subjectID, String subjectName,
                        String sessionDate, String sessionTime, int sessionDuration,
                        String sessionStatus, int numberOfStudents, int maximumStudents,
                        double sessionPrice, String sessionMode) {
        this.sessionID = sessionID;
        this.tutorID = tutorID;
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

    public String getSessionDate() {
        return sessionDate;
    }

    public void setSessionDate(String sessionDate) {
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

    public String getSessionTime() {
        return sessionTime;
    }

    public void setSessionTime(String sessionTime) {
        this.sessionTime = sessionTime;
    }

    public String getSubjectID() {
        return subjectID;
    }

    public void setSubjectID(String subjectID) {
        this.subjectID = subjectID;
    }

    public String getSubjectLevel() {
        return subjectLevel;
    }

    public void setSubjectLevel(String subjectLevel) {
        this.subjectLevel = subjectLevel;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public String getTutorID() {
        return String.valueOf(tutorID);
    }

    public void setTutorID(int tutorID) {
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
        return null;
    }

}
