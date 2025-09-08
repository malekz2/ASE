package CollegeApp;

/**
 * @author malekzuhdi on 03/09/2025
 */
public class CollegePlan {
    private String programEnrolled;
    private String targetLocation;

    public CollegePlan(String programEnrolled, String targetLocation) {
        this.programEnrolled = programEnrolled;
        this.targetLocation = targetLocation;
    }

    public String getProgramEnrolled() {
        return programEnrolled;
    }

    public void setProgramEnrolled(String programEnrolled) {
        this.programEnrolled = programEnrolled;
    }

    public String getTargetLocation() {
        return targetLocation;
    }

    public void setTargetLocation(String targetLocation) {
        this.targetLocation = targetLocation;
    }

    @Override
    public String toString() {
        return "CollegePlan{" +
                "programEnrolled='" + programEnrolled + '\'' +
                ", targetLocation='" + targetLocation + '\'' +
                '}';
    }
}
