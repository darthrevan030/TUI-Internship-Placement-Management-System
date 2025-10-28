package ipms.entity;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.*;

/**
 * Represents an internship opportunity in the system.
 */
public class InternshipOpportunity implements Serializable {
    private static final long serialVersionUID = 1L;

    private String opportunityID;
    private String title;
    private String description;
    private InternshipLevel level;
    private String preferredMajor;
    private LocalDate openingDate;
    private LocalDate closingDate;
    private OpportunityStatus status;
    private String companyName;
    private CompanyRepresentative representative;
    private int numSlots;
    private int filledSlots;
    private boolean isVisible;
    private List<Application> applications;

    /**
     * Constructor for InternshipOpportunity.
     * 
     * @param opportunityID  Unique identifier
     * @param title          Job title
     * @param description    Job description
     * @param level          Internship level (BASIC, INTERMEDIATE, ADVANCED)
     * @param preferredMajor Preferred major for applicants
     * @param openingDate    Application opening date
     * @param closingDate    Application closing date
     * @param companyName    Name of the company
     * @param rep            Company representative in charge
     * @param numSlots       Number of available slots
     */
    public InternshipOpportunity(String opportunityID, String title,
            String description, InternshipLevel level,
            String preferredMajor, LocalDate openingDate,
            LocalDate closingDate, String companyName,
            CompanyRepresentative rep, int numSlots) {
        this.opportunityID = opportunityID;
        this.title = title;
        this.description = description;
        this.level = level;
        this.preferredMajor = preferredMajor;
        this.openingDate = openingDate;
        this.closingDate = closingDate;
        this.status = OpportunityStatus.PENDING;
        this.companyName = companyName;
        this.representative = rep;
        this.numSlots = numSlots;
        this.filledSlots = 0;
        this.isVisible = false;
        this.applications = new ArrayList<>();
    }

    // Getters
    public String getOpportunityID() {
        return opportunityID;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public InternshipLevel getLevel() {
        return level;
    }

    public String getPreferredMajor() {
        return preferredMajor;
    }

    public LocalDate getOpeningDate() {
        return openingDate;
    }

    public LocalDate getClosingDate() {
        return closingDate;
    }

    public OpportunityStatus getStatus() {
        return status;
    }

    public String getCompanyName() {
        return companyName;
    }

    public CompanyRepresentative getRepresentative() {
        return representative;
    }

    public int getNumSlots() {
        return numSlots;
    }

    public int getFilledSlots() {
        return filledSlots;
    }

    public boolean isVisible() {
        return isVisible;
    }

    public List<Application> getApplications() {
        return applications;
    }

    // Setters
    public void setStatus(OpportunityStatus status) {
        this.status = status;
    }

    public void setVisible(boolean visible) {
        this.isVisible = visible;
    }

    /**
     * Increments filled slots when a student accepts placement.
     */
    public void incrementFilledSlots() {
        this.filledSlots++;
        if (this.filledSlots >= this.numSlots) {
            this.status = OpportunityStatus.FILLED;
        }
    }

    /**
     * Decrements filled slots when withdrawal is approved.
     */
    public void decrementFilledSlots() {
        if (this.filledSlots > 0) {
            this.filledSlots--;
        }
        // Change status back from FILLED if slots become available
        if (this.filledSlots < this.numSlots && this.status == OpportunityStatus.FILLED) {
            this.status = OpportunityStatus.APPROVED;
        }
    }

    /**
     * Checks if internship is currently open for applications.
     * 
     * return true if open, false otherwise
     */
    public boolean isOpen() {
        LocalDate today = LocalDate.now();
        return status == OpportunityStatus.APPROVED &&
                !today.isBefore(openingDate) &&
                !today.isAfter(closingDate) &&
                filledSlots < numSlots;
    }

    /**
     * Checks if a student is eligible for this internship.
     * 
     * param student Student to check
     * return true if eligible, false otherwise
     */
    public boolean isEligibleStudent(Student student) {
        // Check major
        if (!student.getMajor().equalsIgnoreCase(preferredMajor)) {
            return false;
        }

        // Check level eligibility
        if (!student.isEligibleForLevel(this.level)) {
            return false;
        }

        // Check if internship is approved and visible, or if student already applied
        if (status != OpportunityStatus.APPROVED && status != OpportunityStatus.FILLED) {
            // If not approved, check if student already applied
            for (Application app : applications) {
                if (app.getStudent().getUserID().equals(student.getUserID())) {
                    return true; // Can still view if already applied
                }
            }
            return false;
        }

        // If not visible, check if student already applied
        if (!isVisible) {
            for (Application app : applications) {
                if (app.getStudent().getUserID().equals(student.getUserID())) {
                    return true; // Can still view if already applied
                }
            }
            return false;
        }

        // Check if slots are available (for open status)
        if (filledSlots >= numSlots) {
            return false;
        }

        // Check if still within application period
        LocalDate today = LocalDate.now();
        if (today.isBefore(openingDate) || today.isAfter(closingDate)) {
            return false;
        }

        return true;
    }


    /**
     * Adds an application to this internship.
     * 
     * param app Application to add
     */
    public void addApplication(Application app) {
        applications.add(app);
    }

    @Override
    public String toString() {
        return String.format("Internship[ID=%s, Title=%s, Company=%s, Level=%s, Status=%s, Slots=%d/%d]",
                opportunityID, title, companyName, level, status, filledSlots, numSlots);
    }
}