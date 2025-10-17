// ========== WithdrawalStatus.java ==========
package ipms.entity;

/**
 * Enum for withdrawal request status
 */
public enum WithdrawalStatus {
    PENDING("Pending"),
    APPROVED("Approved"),
    REJECTED("Rejected");
    
    private final String displayName;
    
    WithdrawalStatus(String displayName) {
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
