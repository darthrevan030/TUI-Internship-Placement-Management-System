// ========== ApplicationStatus.java ==========
package ipms.entity;

/**
 * Enum for application status
 */
public enum ApplicationStatus {
    PENDING("Pending"),
    SUCCESSFUL("Successful"),
    UNSUCCESSFUL("Unsuccessful"),
    WITHDRAWN("Withdrawn");

    private final String displayName;

    ApplicationStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }
}