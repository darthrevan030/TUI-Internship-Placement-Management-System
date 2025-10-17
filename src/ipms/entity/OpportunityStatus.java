// ========== OpportunityStatus.java ==========
package ipms.entity;

/**
 * Enum for internship opportunity status
 */
public enum OpportunityStatus {
    PENDING("Pending"),
    APPROVED("Approved"),
    REJECTED("Rejected"),
    FILLED("Filled");

    private final String displayName;

    OpportunityStatus(String displayName) {
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