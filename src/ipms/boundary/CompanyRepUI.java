// ========== CompanyRepUI.java ==========
package ipms.boundary;

import ipms.control.*;
import ipms.entity.*;
import java.time.LocalDate;
import java.util.*;

/**
 * User interface for Company Representative operations
 */
public class CompanyRepUI {
    private final CompanyRepresentative representative;
    private final Scanner scanner;
    private final InternshipManager internshipManager;
    private final ApplicationManager applicationManager;
    private final AuthenticationManager authManager;

    public CompanyRepUI(CompanyRepresentative representative, Scanner scanner) {
        this.representative = representative;
        this.scanner = scanner;
        this.internshipManager = InternshipManager.getInstance();
        this.applicationManager = ApplicationManager.getInstance();
        this.authManager = AuthenticationManager.getInstance();
    }

    /**
     * Display company rep menu
     */
    public void displayMenu() {
        while (authManager.isLoggedIn()) {
            representative.displayMenu();
            int choice = InputValidator.getIntInput(scanner, "\nEnter choice: ", 1, 7);

            switch (choice) {
                case 1 -> createInternshipOpportunity();
                case 2 -> viewMyInternships();
                case 3 -> viewApplications();
                case 4 -> reviewApplications();
                case 5 -> toggleVisibility();
                case 6 -> changePassword();
                case 7 -> {
                    authManager.logout();
                    return;
                }
            }
        }
    }

    /**
     * Create a new internship opportunity
     */
    private void createInternshipOpportunity() {
        System.out.println("\n=== CREATE INTERNSHIP OPPORTUNITY ===");

        if (!representative.canCreateInternship()) {
            System.out.println("You have reached the maximum limit of 5 internships.");
            return;
        }

        String title = InputValidator.getStringInput(scanner, "Internship Title: ");
        String description = InputValidator.getStringInput(scanner, "Description: ");

        System.out.println("\nSelect Internship Level:");
        System.out.println("1. BASIC");
        System.out.println("2. INTERMEDIATE");
        System.out.println("3. ADVANCED");
        int levelChoice = InputValidator.getIntInput(scanner, "Enter choice: ", 1, 3);
        InternshipLevel level = InternshipLevel.values()[levelChoice - 1];

        String preferredMajor = InputValidator.getStringInput(scanner, "Preferred Major (Full Name e.g. Computer Science, Data Science & AI): ");

        LocalDate openingDate = InputValidator.getDateInput(scanner, "Opening Date");
        LocalDate closingDate = InputValidator.getDateInput(scanner, "Closing Date");

        if (closingDate.isBefore(openingDate)) {
            System.out.println("\n✗ Closing date cannot be before opening date.");
            return;
        }

        int numSlots = InputValidator.getIntInput(scanner, "Number of Slots (1-10): ", 1, 10);

        System.out.println("\n--- Summary ---");
        System.out.println("Title: " + title);
        System.out.println("Company: " + representative.getCompanyName());
        System.out.println("Level: " + level);
        System.out.println("Major: " + preferredMajor);
        System.out.println("Period: " + openingDate + " to " + closingDate);
        System.out.println("Slots: " + numSlots);

        if (InputValidator.getConfirmation(scanner, "\nConfirm creation?")) {
            InternshipOpportunity internship = internshipManager.createInternship(
                    title, description, level, preferredMajor, openingDate, closingDate,
                    representative.getCompanyName(), representative, numSlots);

            System.out.println("\n✓ Internship opportunity created!");
            System.out.println("Opportunity ID: " + internship.getOpportunityID());
            System.out.println("Status: PENDING (awaiting approval from Career Center Staff)");
        }
    }

    /**
     * View internships created by this representative
     */
    private void viewMyInternships() {
        System.out.println("\n=== MY INTERNSHIP OPPORTUNITIES ===");

        List<InternshipOpportunity> internships = representative.getCreatedInternships();

        if (internships.isEmpty()) {
            System.out.println("You have not created any internship opportunities yet.");
            return;
        }

        System.out.println("Total Internships Created: " + internships.size());
        System.out.println();

        for (int i = 0; i < internships.size(); i++) {
            InternshipOpportunity opp = internships.get(i);
            System.out.printf("%d. %s%n", i + 1, opp.getOpportunityID());
            System.out.printf("   Title: %s%n", opp.getTitle());
            System.out.printf("   Level: %s | Major: %s%n", opp.getLevel(), opp.getPreferredMajor());
            System.out.printf("   Status: %s | Visible: %s%n", opp.getStatus(), opp.isVisible());
            System.out.printf("   Slots: %d/%d filled%n", opp.getFilledSlots(), opp.getNumSlots());
            System.out.printf("   Period: %s to %s%n", opp.getOpeningDate(), opp.getClosingDate());
            System.out.printf("   Applications: %d%n", opp.getApplications().size());
            System.out.println("   " + "-".repeat(60));
        }
    }

    /**
     * View applications for internships
     */
    private void viewApplications() {
        System.out.println("\n=== VIEW APPLICATIONS ===");

        List<InternshipOpportunity> internships = representative.getCreatedInternships();

        if (internships.isEmpty()) {
            System.out.println("You have no internship opportunities.");
            return;
        }

        System.out.println("Select Internship:");
        for (int i = 0; i < internships.size(); i++) {
            InternshipOpportunity opp = internships.get(i);
            System.out.printf("%d. %s (%d applications)%n",
                    i + 1, opp.getTitle(), opp.getApplications().size());
        }

        int choice = InputValidator.getIntInput(scanner,
                "\nEnter choice (0 to cancel): ", 0, internships.size());

        if (choice == 0)
            return;

        InternshipOpportunity selected = internships.get(choice - 1);
        List<Application> applications = selected.getApplications();

        if (applications.isEmpty()) {
            System.out.println("\nNo applications for this internship yet.");
            return;
        }

        System.out.println("\n--- Applications for: " + selected.getTitle() + " ---");

        for (int i = 0; i < applications.size(); i++) {
            Application app = applications.get(i);
            Student student = app.getStudent();

            System.out.printf("%n%d. Application ID: %s%n", i + 1, app.getApplicationID());
            System.out.printf("   Student: %s (ID: %s)%n", student.getName(), student.getUserID());
            System.out.printf("   Year: %d | Major: %s%n", student.getYearOfStudy(), student.getMajor());
            System.out.printf("   Email: %s%n", student.getEmail());
            System.out.printf("   Status: %s%n", app.getStatus());
            System.out.printf("   Applied On: %s%n", app.getApplicationDate());
            System.out.println("   " + "-".repeat(60));
        }
    }

    /**
     * Review and approve/reject applications
     */
    private void reviewApplications() {
        System.out.println("\n=== REVIEW APPLICATIONS ===");

        List<InternshipOpportunity> internships = representative.getCreatedInternships();

        if (internships.isEmpty()) {
            System.out.println("You have no internship opportunities.");
            return;
        }

        // Find internships with pending applications
        List<InternshipOpportunity> internshipsWithPending = new ArrayList<>();
        for (InternshipOpportunity opp : internships) {
            boolean hasPending = false;
            for (Application app : opp.getApplications()) {
                if (app.getStatus() == ApplicationStatus.PENDING) {
                    hasPending = true;
                    break;
                }
            }
            if (hasPending) {
                internshipsWithPending.add(opp);
            }
        }

        if (internshipsWithPending.isEmpty()) {
            System.out.println("No pending applications to review.");
            return;
        }

        System.out.println("Internships with Pending Applications:");
        for (int i = 0; i < internshipsWithPending.size(); i++) {
            InternshipOpportunity opp = internshipsWithPending.get(i);
            long pendingCount = opp.getApplications().stream()
                    .filter(a -> a.getStatus() == ApplicationStatus.PENDING)
                    .count();
            System.out.printf("%d. %s (%d pending)%n", i + 1, opp.getTitle(), pendingCount);
        }

        int oppChoice = InputValidator.getIntInput(scanner,
                "\nSelect internship (0 to cancel): ", 0, internshipsWithPending.size());

        if (oppChoice == 0)
            return;

        InternshipOpportunity selected = internshipsWithPending.get(oppChoice - 1);
        List<Application> pendingApps = new ArrayList<>();

        for (Application app : selected.getApplications()) {
            if (app.getStatus() == ApplicationStatus.PENDING) {
                pendingApps.add(app);
            }
        }

        System.out.println("\n--- Pending Applications ---");
        for (int i = 0; i < pendingApps.size(); i++) {
            Application app = pendingApps.get(i);
            Student student = app.getStudent();
            System.out.printf("%d. %s - Year %d, %s (Applied: %s)%n",
                    i + 1, student.getName(), student.getYearOfStudy(),
                    student.getMajor(), app.getApplicationDate());
        }

        int appChoice = InputValidator.getIntInput(scanner,
                "\nSelect application to review (0 to cancel): ", 0, pendingApps.size());

        if (appChoice == 0)
            return;

        Application selectedApp = pendingApps.get(appChoice - 1);
        Student student = selectedApp.getStudent();

        System.out.println("\n--- Application Details ---");
        System.out.println("Student: " + student.getName());
        System.out.println("Student ID: " + student.getUserID());
        System.out.println("Year: " + student.getYearOfStudy());
        System.out.println("Major: " + student.getMajor());
        System.out.println("Email: " + student.getEmail());
        System.out.println("Application Date: " + selectedApp.getApplicationDate());

        System.out.println("\n1. Approve");
        System.out.println("2. Reject");
        System.out.println("3. Cancel");

        int decision = InputValidator.getIntInput(scanner, "\nEnter choice: ", 1, 3);

        if (decision == 3)
            return;

        boolean approve = (decision == 1);
        String action = approve ? "approve" : "reject";

        if (InputValidator.getConfirmation(scanner, "Confirm " + action + " this application?")) {
            applicationManager.reviewApplication(selectedApp, approve);
            System.out.println("\n✓ Application " + (approve ? "approved" : "rejected") + "!");

            if (approve) {
                System.out.println("The student can now accept this placement.");
            }
        }
    }

    /**
     * Toggle visibility of an internship
     */
    private void toggleVisibility() {
        System.out.println("\n=== TOGGLE VISIBILITY ===");

        List<InternshipOpportunity> internships = representative.getCreatedInternships();

        if (internships.isEmpty()) {
            System.out.println("You have no internship opportunities.");
            return;
        }

        // Only show approved internships
        List<InternshipOpportunity> approvedInternships = new ArrayList<>();
        for (InternshipOpportunity opp : internships) {
            if (opp.getStatus() == OpportunityStatus.APPROVED) {
                approvedInternships.add(opp);
            }
        }

        if (approvedInternships.isEmpty()) {
            System.out.println("You have no approved internships to toggle visibility.");
            return;
        }

        System.out.println("Approved Internships:");
        for (int i = 0; i < approvedInternships.size(); i++) {
            InternshipOpportunity opp = approvedInternships.get(i);
            System.out.printf("%d. %s (Currently: %s)%n",
                    i + 1, opp.getTitle(), opp.isVisible() ? "VISIBLE" : "HIDDEN");
        }

        int choice = InputValidator.getIntInput(scanner,
                "\nSelect internship (0 to cancel): ", 0, approvedInternships.size());

        if (choice == 0)
            return;

        InternshipOpportunity selected = approvedInternships.get(choice - 1);
        String newState = selected.isVisible() ? "HIDDEN" : "VISIBLE";

        if (InputValidator.getConfirmation(scanner, "Set visibility to " + newState + "?")) {
            internshipManager.toggleVisibility(selected);
            System.out.println("\n✓ Visibility updated to: " + newState);
        }
    }

    /**
     * Change password
     */
    private void changePassword() {
        System.out.println("\n=== CHANGE PASSWORD ===");

        String oldPassword = InputValidator.getStringInput(scanner, "Current Password: ");
        String newPassword = InputValidator.getStringInput(scanner, "New Password: ");
        String confirmPassword = InputValidator.getStringInput(scanner, "Confirm New Password: ");

        if (!newPassword.equals(confirmPassword)) {
            System.out.println("\n✗ Passwords do not match.");
            return;
        }

        if (authManager.changePassword(oldPassword, newPassword)) {
            System.out.println("\n✓ Password changed successfully!");
        } else {
            System.out.println("\n✗ Current password is incorrect.");
        }
    }
}