package shared.classes;




import java.io.Serializable;




public class TutorSession implements Serializable {
    private String sessionID;
    private int tutorID;
    private String subjectID;
    private String subjectName;
    private String sessionDate;
    private String sessionTime;
    private int sessionDuration;
    private String sessionStatus;
    private int numberOfStudents;
    private int maximumStudents;
    private double sessionPrice;
    private String sessionMode; // New field for sessionMode


    // Updated constructor to include sessionMode
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
        this.sessionMode = sessionMode; // Initialize sessionMode
    }


    // Getters for all fields, including sessionMode
    public String getSessionMode() {
        return sessionMode;
    }
    // Getters and Setters
    public String getSessionID() {
        return sessionID;
    }




    public void setSessionID(String sessionID) {
        this.sessionID = sessionID;
    }




    public int getTutorID() {
        return tutorID;
    }




    public void setTutorID(int tutorID) {
        this.tutorID = tutorID;
    }




    public String getSubjectID() {
        return subjectID;
    }




    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }




    public String getSubjectName() {
        return subjectName;
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




    public String getSessionDate() {
        return sessionDate;
    }




    public void setSessionDate(String sessionDate) {
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
                '}';
    }
    public void setSessionMode(String sessionMode) {
        this.sessionMode = sessionMode;
    }

    public String getSessionType() {
        return null;
    }
}
