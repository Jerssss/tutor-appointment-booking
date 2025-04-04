package shared.classes;

public class LessonPlan {
    private int lessonPlanID;
    private int subjectID;
    private String objectives;
    private String topicsCovered;

    // Constructor
    public LessonPlan(int lessonPlanID, int subjectID, String objectives, String topicsCovered) {
        this.lessonPlanID = lessonPlanID;
        this.subjectID = subjectID;
        this.objectives = objectives;
        this.topicsCovered = topicsCovered;
    }

    // Getters and Setters
    public int getLessonPlanID() {
        return lessonPlanID;
    }

    public void setLessonPlanID(int lessonPlanID) {
        this.lessonPlanID = lessonPlanID;
    }

    public int getSubjectID() {
        return subjectID;
    }

    public void setSubjectID(int subjectID) {
        this.subjectID = subjectID;
    }

    public String getObjectives() {
        return objectives;
    }

    public void setObjectives(String objectives) {
        this.objectives = objectives;
    }

    public String getTopicsCovered() {
        return topicsCovered;
    }

    public void setTopicsCovered(String topicsCovered) {
        this.topicsCovered = topicsCovered;
    }

    // Override toString for easy object representation
    @Override
    public String toString() {
        return "LessonPlan{" +
                "lessonPlanID=" + lessonPlanID +
                ", subjectID=" + subjectID +
                ", objectives='" + objectives + '\'' +
                ", topicsCovered='" + topicsCovered + '\'' +
                '}';
    }
}

