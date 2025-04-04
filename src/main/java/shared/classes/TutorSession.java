package shared.classes;

import java.time.LocalDateTime;

public class TutorSession {
    private int sessionID;
    private int tutorID;
    private int subjectID;
    private String sessionStatus;
    private LocalDateTime sessionDate;
    private String sessionTime;
    private int sessionDuration;
    private int numberOfStudents;
    private int maximumStudents;
    private double sessionPrice;

    // Constructor
    public TutorSession(int sessionID, int tutorID, int subjectID, String sessionStatus, LocalDateTime sessionDate, String sessionTime, int sessionDuration, int numberOfStudents, int maximumStudents, double sessionPrice) {
        this.sessionID = sessionID;
        this.tutorID = tutorID;
        this.subjectID = subjectID;
        this.sessionStatus = sessionStatus;
        this.sessionDate = sessionDate;
        this.sessionTime = sessionTime;
        this.sessionDuration = sessionDuration;
        this.numberOfStudents = numberOfStudents;
        this.maximumStudents = maximumStudents;
        this.sessionPrice = sessionPrice;
    }

    // Getters and Setters
    public int getSessionID() {
        return sessionID;
    }

    public void setSessionID(int sessionID) {
        this.sessionID = sessionID;
    }

    public int getTutorID() {
        return tutorID;
    }

    public void setTutorID(int tutorID) {
        this.tutorID = tutorID;
    }

    public int getSubjectID() {
        return subjectID;
    }

    public void setSubjectID(int subjectID) {
        this.subjectID = subjectID;
    }

    public String getSessionStatus() {
        return sessionStatus;
    }

    public void setSessionStatus(String sessionStatus) {
        this.sessionStatus = sessionStatus;
    }

    public LocalDateTime getSessionDate() {
        return sessionDate;
    }

    public void setSessionDate(LocalDateTime sessionDate) {
        this.sessionDate = sessionDate;
    }

    public String getSessionTime() {
        return sessionTime;
    }

    public void setSessionTime(String sessionTime) {
        this.sessionTime = sessionTime;
    }

    public int getSessionDuration() {
        return sessionDuration;
    }

    public void setSessionDuration(int sessionDuration) {
        this.sessionDuration = sessionDuration;
    }

    public int getNumberOfStudents() {
        return numberOfStudents;
    }

    public void setNumberOfStudents(int numberOfStudents) {
        this.numberOfStudents = numberOfStudents;
    }

    public int getMaximumStudents() {
        return maximumStudents;
    }

    public void setMaximumStudents(int maximumStudents) {
        this.maximumStudents = maximumStudents;
    }

    public double getSessionPrice() {
        return sessionPrice;
    }

    public void setSessionPrice(double sessionPrice) {
        this.sessionPrice = sessionPrice;
    }

    // Override toString for easy object representation
    @Override
    public String toString() {
        return "TutorSession{" +
                "sessionID=" + sessionID +
                ", tutorID=" + tutorID +
                ", subjectID=" + subjectID +
                ", sessionStatus='" + sessionStatus + '\'' +
                ", sessionDate=" + sessionDate +
                ", sessionTime='" + sessionTime + '\'' +
                ", sessionDuration=" + sessionDuration +
                ", numberOfStudents=" + numberOfStudents +
                ", maximumStudents=" + maximumStudents +
                ", sessionPrice=" + sessionPrice +
                '}';
    }
}

