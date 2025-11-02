package ipms.entity;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * Represents a student's application to an internship opportunity.
 */
public class Application implements Serializable {
    private static final long serialVersionUID = 1L;

    private final String applicationID;
    private final Student student;
    private final InternshipOpportunity opportunity;
    private ApplicationStatus status;
    private final LocalDate applicationDate;
    private WithdrawalRequest withdrawalRequest;

    /**
     * Constructor for Application.
     * 
     * @param applicationID Unique identifier for the application
     * @param student       Student who is applying
     * @param opportunity   Internship opportunity being applied to
     */
    public Application(String applicationID, Student student,
            InternshipOpportunity opportunity) {
        this.applicationID = applicationID;
        this.student = student;
        this.opportunity = opportunity;
        this.status = ApplicationStatus.PENDING;
        this.applicationDate = LocalDate.now();
        this.withdrawalRequest = null;
    }

    /**
     * Gets the application ID.
     * 
     * @return Application ID
     */
    public String getApplicationID() {
        return applicationID;
    }

    /**
     * Gets the student who applied.
     * 
     * @return Student object
     */
    public Student getStudent() {
        return student;
    }

    /**
     * Gets the internship opportunity.
     * 
     * @return InternshipOpportunity object
     */
    public InternshipOpportunity getOpportunity() {
        return opportunity;
    }

    /**
     * Gets the application status.
     * 
     * @return ApplicationStatus enum
     */
    public ApplicationStatus getStatus() {
        return status;
    }

    /**
     * Gets the application date.
     * 
     * @return LocalDate when application was submitted
     */
    public LocalDate getApplicationDate() {
        return applicationDate;
    }

    /**
     * Gets the withdrawal request if any.
     * 
     * @return WithdrawalRequest or null
     */
    public WithdrawalRequest getWithdrawalRequest() {
        return withdrawalRequest;
    }

    /**
     * Sets the application status.
     * 
     * @param status New status
     */
    public void setStatus(ApplicationStatus status) {
        this.status = status;
    }

    /**
     * Sets the withdrawal request.
     * 
     * @param request WithdrawalRequest object
     */
    public void setWithdrawalRequest(WithdrawalRequest request) {
        this.withdrawalRequest = request;
    }

    @Override
    public String toString() {
        return String.format("Application[ID=%s, Student=%s, Internship=%s, Status=%s]",
                applicationID, student.getName(),
                opportunity.getTitle(), status);
    }
}