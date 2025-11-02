// ========== StudentUI.java ==========
package ipms.boundary;

import ipms.control.*;
import ipms.entity.*;
import java.util.*;

/**
 * User interface for Student operations
 */
public class StudentUI {
    private final Student student;
    private final Scanner scanner;
    private final InternshipManager internshipManager;
    private final ApplicationManager applicationManager;
    private final AuthenticationManager authManager;

    public StudentUI(Student student, Scanner scanner) {
        this.student = student;
        this.scanner = scanner;
        this.internshipManager = InternshipManager.getInstance();
        this.applicationManager = ApplicationManager.getInstance();
        this.authManager = AuthenticationManager.getInstance();
    }

    /**
     * Display student menu
     */
    public void displayMenu() {
        while (authManager.isLoggedIn()) {
            student.displayMenu();
            int choice = InputValidator.getIntInput(scanner, "\nEnter choice: ", 1, 7);

            switch (choice) {
                case 1 -> viewInternshipOpportunities();
                case 2 -> applyForInternship();
                case 3 -> viewMyApplications();
                case 4 -> acceptPlacement();
                case 5 -> requestWithdrawal();
                case 6 -> changePassword();
                case 7 -> {
                    authManager.logout();
                    return;
                }
            }
        }
    }

    /**
     * View available internship opportunities
     */
    private void viewInternshipOpportunities() {
        System.out.println("\n=== AVAILABLE INTERNSHIP OPPORTUNITIES ===");

        List<InternshipOpportunity> internships = internshipManager.getVisibleInternshipsForStudent(student);

        if (internships.isEmpty()) {
            System.out.println("No internship opportunities available at the moment.");
            return;
        }

        System.out.println("Found " + internships.size() + " opportunities:\n");

        for (int i = 0; i < internships.size(); i++) {
            InternshipOpportunity opp = internships.get(i);
            System.out.printf("%d. %s%n", i + 1, opp.getOpportunityID());
            System.out.printf("   Title: %s%n", opp.getTitle());
            System.out.printf("   Company: %s%n", opp.getCompanyName());
            System.out.printf("   Level: %s | Major: %s%n", opp.getLevel(), opp.getPreferredMajor());
            System.out.printf("   Slots Available: %d/%d%n",
                    opp.getNumSlots() - opp.getFilledSlots(), opp.getNumSlots());
            System.out.printf("   Closing Date: %s%n", opp.getClosingDate());
            System.out.println("   " + "-".repeat(50));
        }
    }

    /**
     * Apply for an internship
     */
    private void applyForInternship() {
        if (!student.canApply()) {
            System.out.println("\nYou cannot apply for more internships.");
            System.out.println("Reason: Maximum 3 applications or you have already accepted a placement.");
            return;
        }

        List<InternshipOpportunity> internships = internshipManager.getVisibleInternshipsForStudent(student);

        if (internships.isEmpty()) {
            System.out.println("\nNo internship opportunities available to apply for.");
            return;
        }

        System.out.println("\n=== APPLY FOR INTERNSHIP ===");
        viewInternshipOpportunities();

        System.out.print("\nEnter internship number to apply (0 to cancel): ");
        int choice = InputValidator.getIntInput(scanner, "", 0, internships.size());

        if (choice == 0)
            return;

        InternshipOpportunity selected = internships.get(choice - 1);

        System.out.println("\nYou are applying for:");
        System.out.println("Title: " + selected.getTitle());
        System.out.println("Company: " + selected.getCompanyName());

        if (InputValidator.getConfirmation(scanner, "\nConfirm application?")) {
            Application app = applicationManager.submitApplication(student, selected);
            if (app != null) {
                System.out.println("\n✓ Application submitted successfully!");
                System.out.println("Application ID: " + app.getApplicationID());
            } else {
                System.out.println("\n✗ Application failed.");
            }
        }
    }

    /**
     * View student's applications
     */
    private void viewMyApplications() {
        System.out.println("\n=== MY APPLICATIONS ===");

        List<Application> applications = student.getApplications();

        if (applications.isEmpty()) {
            System.out.println("You have not applied for any internships yet.");
            return;
        }

        System.out.println("Total Applications: " + applications.size());
        System.out.println();

        for (int i = 0; i < applications.size(); i++) {
            Application app = applications.get(i);
            System.out.printf("%d. Application ID: %s%n", i + 1, app.getApplicationID());
            System.out.printf("   Internship: %s%n", app.getOpportunity().getTitle());
            System.out.printf("   Company: %s%n", app.getOpportunity().getCompanyName());
            System.out.printf("   Status: %s%n", app.getStatus());
            System.out.printf("   Applied On: %s%n", app.getApplicationDate());

            if (app.getWithdrawalRequest() != null) {
                System.out.printf("   Withdrawal Status: %s%n",
                        app.getWithdrawalRequest().getStatus());
            }

            System.out.println("   " + "-".repeat(50));
        }
    }

    /**
     * Accept internship placement
     */
    private void acceptPlacement() {
        System.out.println("\n=== ACCEPT PLACEMENT ===");

        List<Application> successfulApps = new ArrayList<>();
        for (Application app : student.getApplications()) {
            if (app.getStatus() == ApplicationStatus.SUCCESSFUL) {
                successfulApps.add(app);
            }
        }

        if (successfulApps.isEmpty()) {
            System.out.println("You have no successful applications to accept.");
            return;
        }

        if (student.getAcceptedPlacement() != null) {
            System.out.println("You have already accepted a placement:");
            System.out.println(student.getAcceptedPlacement().getOpportunity().getTitle());
            return;
        }

        System.out.println("Successful Applications:");
        for (int i = 0; i < successfulApps.size(); i++) {
            Application app = successfulApps.get(i);
            System.out.printf("%d. %s - %s%n", i + 1,
                    app.getOpportunity().getTitle(),
                    app.getOpportunity().getCompanyName());
        }

        int choice = InputValidator.getIntInput(scanner,
                "\nSelect application to accept (0 to cancel): ",
                0, successfulApps.size());

        if (choice == 0)
            return;

        Application selected = successfulApps.get(choice - 1);

        System.out.println("\n⚠ WARNING: Accepting this placement will withdraw all other applications.");

        if (InputValidator.getConfirmation(scanner, "Confirm acceptance?")) {
            if (applicationManager.acceptPlacement(student, selected)) {
                System.out.println("\n✓ Placement accepted successfully!");
                System.out.println("Congratulations on your internship at " +
                        selected.getOpportunity().getCompanyName());
            }
        }
    }

    /**
     * Request withdrawal
     */
    private void requestWithdrawal() {
        System.out.println("\n=== REQUEST WITHDRAWAL ===");

        List<Application> activeApps = new ArrayList<>();
        for (Application app : student.getApplications()) {
            if (app.getStatus() != ApplicationStatus.WITHDRAWN &&
                    app.getStatus() != ApplicationStatus.UNSUCCESSFUL &&
                    app.getWithdrawalRequest() == null) {
                activeApps.add(app);
            }
        }

        if (activeApps.isEmpty()) {
            System.out.println("You have no applications to withdraw.");
            return;
        }

        System.out.println("Active Applications:");
        for (int i = 0; i < activeApps.size(); i++) {
            Application app = activeApps.get(i);
            System.out.printf("%d. %s - %s (Status: %s)%n", i + 1,
                    app.getOpportunity().getTitle(),
                    app.getOpportunity().getCompanyName(),
                    app.getStatus());
        }

        int choice = InputValidator.getIntInput(scanner,
                "\nSelect application to withdraw (0 to cancel): ",
                0, activeApps.size());

        if (choice == 0)
            return;

        Application selected = activeApps.get(choice - 1);
        String reason = InputValidator.getStringInput(scanner, "Reason for withdrawal: ");

        if (InputValidator.getConfirmation(scanner, "Confirm withdrawal request?")) {
            WithdrawalRequest request = applicationManager.requestWithdrawal(selected, reason);
            System.out.println("\n✓ Withdrawal request submitted.");
            System.out.println("Request ID: " + request.getRequestID());
            System.out.println("Pending approval from Career Center Staff.");
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
