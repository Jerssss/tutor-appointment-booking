package shared.classes;

import java.io.Serializable;

public class Subject implements Serializable {
    private static final long serialVersionUID = 1L;
    private String subjectID;
    private String subjectName;
    private String subjectDescription;
    private String academicLevel;
    private String visibility;

    // Constructor
    public Subject(String subjectID, String subjectName, String subjectDescription, String academicLevel, String visibility) {
        this.subjectID = subjectID;
        this.subjectName = subjectName;
        this.subjectDescription = subjectDescription;
        this.academicLevel = academicLevel;
        this.visibility = visibility;
    }
    public Subject(String subjectID, String subjectName, String subjectDescription, String academicLevel) {
        this.subjectID = subjectID;
        this.subjectName = subjectName;
        this.subjectDescription = subjectDescription;
        this.academicLevel = academicLevel;
    }

    // Getters and Setters
    public String getSubjectID() {
        return subjectID;
    }

    public void setSubjectID(String subjectID) {
        this.subjectID = subjectID;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public String getSubjectDescription() {
        return subjectDescription;
    }

    public void setSubjectDescription(String subjectDescription) {
        this.subjectDescription = subjectDescription;
    }

    public String getAcademicLevel() {
        return academicLevel;
    }

    public void setAcademicLevel(String academicLevel) {
        this.academicLevel = academicLevel;
    }

    public String getVisibility() {
        return visibility;
    }
    public void setVisibility(String visibility) {
        this.visibility = visibility;
    }

    // Override toString for easy object representation
    @Override
    public String toString() {
        return "Subject{" +
                "subjectID=" + subjectID +
                ", subjectName='" + subjectName + '\'' +
                ", subjectDescription='" + subjectDescription + '\'' +
                ", academicLevel='" + academicLevel + '\'' +
                '}';
    }
}
