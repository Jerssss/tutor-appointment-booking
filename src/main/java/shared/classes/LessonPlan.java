package shared.classes;

import java.io.Serializable;

public class LessonPlan implements Serializable {
    private static final long serialVersionUID = 1L;
    private String lessonPlanID;
    private String subjectID; // Store subject ID
    private String subjectName; // Store subject name
    private String academicLevel;
    private String topicsCovered;
    private String objectives;
    private String visibility;

    public LessonPlan(String lessonPlanID, String subjectID, String subjectName, String objectives, String topicsCovered) {
        this.lessonPlanID = lessonPlanID;
        this.subjectID = subjectID; // Initialize subjectID
        this.subjectName = subjectName; // Initialize subjectName
        this.objectives = objectives;
        this.topicsCovered = topicsCovered;
    }

    public LessonPlan(String lessonPlanID, String subjectID, String objectives, String topicsCovered) {
        this.lessonPlanID = lessonPlanID;
        this.subjectID = subjectID;
        this.objectives = objectives;
        this.topicsCovered = topicsCovered;
    }

    public LessonPlan(String lessonPlanID, String subjectID, String subjectName, String academicLevel, String objectives, String topicsCovered, String visibility) {
        this.lessonPlanID = lessonPlanID;
        this.subjectID = subjectID;
        this.subjectName = subjectName;
        this.academicLevel = academicLevel;
        this.objectives = objectives;
        this.topicsCovered = topicsCovered;
        this.visibility = visibility;
    }

    // Getters and Setters
    public String getLessonPlanID() { return lessonPlanID; }
    public void setLessonPlanID(String lessonPlanID) { this.lessonPlanID = lessonPlanID; }

    public String getSubjectID() { return subjectID; }
    public void setSubjectID(String subjectID) { this.subjectID = subjectID; }

    public String getObjectives() { return objectives; }
    public void setObjectives(String objectives) { this.objectives = objectives; }

    public String getTopicsCovered() { return topicsCovered; }
    public void setTopicsCovered(String topicsCovered) { this.topicsCovered = topicsCovered; }

    public String getSubjectName() { return subjectName; }

    public String getAcademicLevel() {
        return academicLevel;
    }

    public String getVisibility() {
        return visibility;
    }

    // Override toString for easy object representation
    @Override
    public String toString() {
        return "LessonPlan{" +
                "lessonPlanID=" + lessonPlanID +
                ", subjectID=" + subjectID +
                ", subjectName='" + subjectName + '\'' +
                ", objectives='" + objectives + '\'' +
                ", topicsCovered='" + topicsCovered + '\'' +
                '}';
    }
}

