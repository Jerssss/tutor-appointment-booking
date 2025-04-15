package shared.classes;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class TutorSession {
    private String sessionID;
    private String tutorID;
    private String subjectID;
    private String sessionStatus;
    private LocalDate sessionDate;
    private LocalTime sessionTime;
    private int sessionDuration;
    private int numberOfStudents;
    private int maximumStudents;
    private double sessionPrice;

    // Constructor
    public TutorSession(String sessionID, String tutorID, String subjectID, String sessionStatus, LocalDate sessionDate, LocalTime sessionTime, int sessionDuration, int numberOfStudents, int maximumStudents, double sessionPrice) {
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
    public String getSessionID() {
        return sessionID;
    }

    public void setSessionID(String sessionID) {
        this.sessionID = sessionID;
    }

    public String getTutorID() {
        return tutorID;
    }

    public void setTutorID(String tutorID) {
        this.tutorID = tutorID;
    }

    public String getSubjectID() {
        return subjectID;
    }

    public void setSubjectID(String subjectID) {
        this.subjectID = subjectID;
    }

    public String getSessionStatus() {
        return sessionStatus;
    }

    public void setSessionStatus(String sessionStatus) {
        this.sessionStatus = sessionStatus;
    }

    public LocalDate getSessionDate() {
        return sessionDate;
    }

    public void setSessionDate(LocalDate sessionDate) {
        this.sessionDate = sessionDate;
    }

    public LocalTime getSessionTime() {
        return sessionTime;
    }

    public void setSessionTime(LocalTime sessionTime) {
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

