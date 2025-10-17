// ========== WithdrawalRequest.java ==========
package ipms.entity;

import java.time.LocalDate;

/**
 * Represents a withdrawal request from student
 */
public class WithdrawalRequest implements java.io.Serializable {
    private static final long serialVersionUID = 1L;

    private String requestID;
    private Application application;
    private String reason;
    private LocalDate requestDate;
    private WithdrawalStatus status;
    private boolean isAfterPlacement;

    public WithdrawalRequest(String requestID, Application application,
            String reason, boolean isAfterPlacement) {
        this.requestID = requestID;
        this.application = application;
        this.reason = reason;
        this.requestDate = LocalDate.now();
        this.status = WithdrawalStatus.PENDING;
        this.isAfterPlacement = isAfterPlacement;
    }

    // Getters
    public String getRequestID() {
        return requestID;
    }

    public Application getApplication() {
        return application;
    }

    public String getReason() {
        return reason;
    }

    public LocalDate getRequestDate() {
        return requestDate;
    }

    public WithdrawalStatus getStatus() {
        return status;
    }

    public boolean isAfterPlacement() {
        return isAfterPlacement;
    }

    // Setters
    public void setStatus(WithdrawalStatus status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return String.format("WithdrawalRequest[ID=%s, Application=%s, Status=%s, AfterPlacement=%b]",
                requestID, application.getApplicationID(), status, isAfterPlacement);
    }
}