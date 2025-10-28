// ========== StaffUI.java ==========
package ipms.boundary;

import ipms.control.*;
import ipms.entity.*;
import java.util.*;

/**
 * User interface for Career Center Staff operations
 */
public class StaffUI {
    private CareerCenterStaff staff;
    private Scanner scanner;
    private UserManager userManager;
    private InternshipManager internshipManager;
    private ApplicationManager applicationManager;
    private AuthenticationManager authManager;
    private ReportGenerator reportGenerator;

    public StaffUI(CareerCenterStaff staff, Scanner scanner) {
        this.staff = staff;
        this.scanner = scanner;
        this.userManager = UserManager.getInstance();
        this.internshipManager = InternshipManager.getInstance();
        this.applicationManager = ApplicationManager.getInstance();
        this.authManager = AuthenticationManager.getInstance();
        this.reportGenerator = new ReportGenerator();
    }

    /**
     * Display staff menu
     */
    public void displayMenu() {
        while (authManager.isLoggedIn()) {
            staff.displayMenu();
            int choice = InputValidator.getIntInput(scanner, "\nEnter choice: ", 1, 7);

            switch (choice) {
                case 1:
                    approveCompanyRepresentatives();
                    break;
                case 2:
                    approveInternshipOpportunities();
                    break;
                case 3:
                    approveWithdrawalRequests();
                    break;
                case 4:
                    generateReports();
                    break;
                case 5:
                    viewAllInternships();
                    break;
                case 6:
                    changePassword();
                    break;
                case 7:
                    authManager.logout();
                    return;
            }
        }
    }

    /**
     * Approve company representatives
     */
    private void approveCompanyRepresentatives() {
        System.out.println("\n=== APPROVE COMPANY REPRESENTATIVES ===");

        List<CompanyRepresentative> pendingReps = userManager.getPendingRepresentatives();

        if (pendingReps.isEmpty()) {
            System.out.println("No pending company representative registrations.");
            return;
        }

        System.out.println("Pending Registrations: " + pendingReps.size());
        System.out.println();

        for (int i = 0; i < pendingReps.size(); i++) {
            CompanyRepresentative rep = pendingReps.get(i);
            System.out.printf("%d. %s%n", i + 1, rep.getName());
            System.out.printf("   Email: %s%n", rep.getEmail());
            System.out.printf("   Company: %s%n", rep.getCompanyName());
            System.out.printf("   Department: %s%n", rep.getDepartment());
            System.out.printf("   Position: %s%n", rep.getPosition());
            System.out.println("   " + "-".repeat(60));
        }

        int choice = InputValidator.getIntInput(scanner,
                "\nSelect representative to review (0 to cancel): ", 0, pendingReps.size());

        if (choice == 0)
            return;

        CompanyRepresentative selected = pendingReps.get(choice - 1);

        System.out.println("\n1. Approve");
        System.out.println("2. Reject");
        System.out.println("3. Cancel");

        int decision = InputValidator.getIntInput(scanner, "\nEnter choice: ", 1, 3);

        if (decision == 3)
            return;

        if (decision == 1) {
            selected.setApproved(true);
            userManager.saveUsers();
            System.out.println("\n✓ Representative approved!");
            System.out.println(selected.getName() + " can now login to the system.");
        } else {
            System.out.println("\n✓ Representative rejected.");
            System.out.println("Note: Account remains in system but cannot login.");
        }
    }

    /**
     * Approve internship opportunities
     */
    private void approveInternshipOpportunities() {
        System.out.println("\n=== APPROVE INTERNSHIP OPPORTUNITIES ===");

        List<InternshipOpportunity> pendingInternships = internshipManager.getPendingInternships();

        if (pendingInternships.isEmpty()) {
            System.out.println("No pending internship opportunities.");
            return;
        }

        System.out.println("Pending Internships: " + pendingInternships.size());
        System.out.println();

        for (int i = 0; i < pendingInternships.size(); i++) {
            InternshipOpportunity opp = pendingInternships.get(i);
            System.out.printf("%d. %s%n", i + 1, opp.getOpportunityID());
            System.out.printf("   Title: %s%n", opp.getTitle());
            System.out.printf("   Company: %s%n", opp.getCompanyName());
            System.out.printf("   Representative: %s%n", opp.getRepresentative().getName());
            System.out.printf("   Level: %s | Major: %s%n", opp.getLevel(), opp.getPreferredMajor());
            System.out.printf("   Slots: %d%n", opp.getNumSlots());
            System.out.printf("   Period: %s to %s%n", opp.getOpeningDate(), opp.getClosingDate());
            System.out.printf("   Description: %s%n", opp.getDescription());
            System.out.println("   " + "-".repeat(60));
        }

        int choice = InputValidator.getIntInput(scanner,
                "\nSelect internship to review (0 to cancel): ", 0, pendingInternships.size());

        if (choice == 0)
            return;

        InternshipOpportunity selected = pendingInternships.get(choice - 1);

        System.out.println("\n1. Approve");
        System.out.println("2. Reject");
        System.out.println("3. Cancel");

        int decision = InputValidator.getIntInput(scanner, "\nEnter choice: ", 1, 3);

        if (decision == 3)
            return;

        if (decision == 1) {
            internshipManager.approveInternship(selected);
            System.out.println("\n✓ Internship approved!");
            System.out.println("Status changed to APPROVED and made visible to students.");
        } else {
            internshipManager.rejectInternship(selected);
            System.out.println("\n✓ Internship rejected.");
        }
    }

    /**
     * Approve withdrawal requests
     */
    private void approveWithdrawalRequests() {
        System.out.println("\n=== APPROVE WITHDRAWAL REQUESTS ===");

        List<WithdrawalRequest> pendingWithdrawals = applicationManager.getPendingWithdrawals();

        if (pendingWithdrawals.isEmpty()) {
            System.out.println("No pending withdrawal requests.");
            return;
        }

        System.out.println("Pending Withdrawal Requests: " + pendingWithdrawals.size());
        System.out.println();

        for (int i = 0; i < pendingWithdrawals.size(); i++) {
            WithdrawalRequest req = pendingWithdrawals.get(i);
            Application app = req.getApplication();

            System.out.printf("%d. Request ID: %s%n", i + 1, req.getRequestID());
            System.out.printf("   Student: %s%n", app.getStudent().getName());
            System.out.printf("   Internship: %s%n", app.getOpportunity().getTitle());
            System.out.printf("   Company: %s%n", app.getOpportunity().getCompanyName());
            System.out.printf("   Application Status: %s%n", app.getStatus());
            System.out.printf("   After Placement: %s%n", req.isAfterPlacement() ? "YES" : "NO");
            System.out.printf("   Reason: %s%n", req.getReason());
            System.out.printf("   Requested On: %s%n", req.getRequestDate());
            System.out.println("   " + "-".repeat(60));
        }

        int choice = InputValidator.getIntInput(scanner,
                "\nSelect request to review (0 to cancel): ", 0, pendingWithdrawals.size());

        if (choice == 0)
            return;

        WithdrawalRequest selected = pendingWithdrawals.get(choice - 1);

        System.out.println("\n1. Approve");
        System.out.println("2. Reject");
        System.out.println("3. Cancel");

        int decision = InputValidator.getIntInput(scanner, "\nEnter choice: ", 1, 3);

        if (decision == 3)
            return;

        if (decision == 1) {
            applicationManager.approveWithdrawal(selected);
            System.out.println("\n✓ Withdrawal request approved!");
            System.out.println("Application has been withdrawn.");
        } else {
            applicationManager.rejectWithdrawal(selected);
            System.out.println("\n✓ Withdrawal request rejected.");
            System.out.println("Application remains active.");
        }
    }

    /**
     * Generate reports with filters
     */
    private void generateReports() {
        System.out.println("\n=== GENERATE REPORTS ===");
        System.out.println("1. Internship Opportunities Report (with filters)");
        System.out.println("2. Application Statistics Report");
        System.out.println("3. Back");

        int choice = InputValidator.getIntInput(scanner, "\nEnter choice: ", 1, 3);

        if (choice == 3)
            return;

        if (choice == 1) {
            generateInternshipReport();
        } else {
            reportGenerator.generateApplicationReport();
            System.out.println("\nPress Enter to continue...");
            scanner.nextLine();
        }
    }

    /**
     * Generate internship report with filters
     */
    private void generateInternshipReport() {
        System.out.println("\n=== INTERNSHIP REPORT FILTERS ===");
        System.out.println("1. No Filter (All Internships)");
        System.out.println("2. Filter by Status");
        System.out.println("3. Filter by Level");
        System.out.println("4. Filter by Major");
        System.out.println("5. Filter by Visibility");
        System.out.println("6. Multiple Filters");

        int filterChoice = InputValidator.getIntInput(scanner, "\nEnter choice: ", 1, 6);

        FilterStrategy filter = null;

        switch (filterChoice) {
            case 1:
                filter = null; // No filter
                break;
            case 2:
                filter = createStatusFilter();
                break;
            case 3:
                filter = createLevelFilter();
                break;
            case 4:
                filter = createMajorFilter();
                break;
            case 5:
                filter = createVisibilityFilter();
                break;
            case 6:
                filter = createCompositeFilter();
                break;
        }

        reportGenerator.generateInternshipReport(filter);
        System.out.println("\nPress Enter to continue...");
        scanner.nextLine();
    }

    private FilterStrategy createStatusFilter() {
        System.out.println("\nSelect Status:");
        System.out.println("1. PENDING");
        System.out.println("2. APPROVED");
        System.out.println("3. REJECTED");
        System.out.println("4. FILLED");

        int choice = InputValidator.getIntInput(scanner, "Enter choice: ", 1, 4);
        OpportunityStatus status = OpportunityStatus.values()[choice - 1];
        return new StatusFilter(status);
    }

    private FilterStrategy createLevelFilter() {
        System.out.println("\nSelect Level:");
        System.out.println("1. BASIC");
        System.out.println("2. INTERMEDIATE");
        System.out.println("3. ADVANCED");

        int choice = InputValidator.getIntInput(scanner, "Enter choice: ", 1, 3);
        InternshipLevel level = InternshipLevel.values()[choice - 1];
        return new LevelFilter(level);
    }

    private FilterStrategy createMajorFilter() {
        String major = InputValidator.getStringInput(scanner, "\nEnter major (Full Name e.g. Computer Science, Data Science & AI): ");
        return new MajorFilter(major);
    }

    private FilterStrategy createVisibilityFilter() {
        boolean visible = InputValidator.getConfirmation(scanner, "\nFilter for visible internships?");
        return new VisibilityFilter(visible);
    }

    private FilterStrategy createCompositeFilter() {
        CompositeFilter composite = new CompositeFilter();

        System.out.println("\nBuilding Composite Filter...");

        if (InputValidator.getConfirmation(scanner, "Add status filter?")) {
            composite.addFilter(createStatusFilter());
        }

        if (InputValidator.getConfirmation(scanner, "Add level filter?")) {
            composite.addFilter(createLevelFilter());
        }

        if (InputValidator.getConfirmation(scanner, "Add major filter?")) {
            composite.addFilter(createMajorFilter());
        }

        if (InputValidator.getConfirmation(scanner, "Add visibility filter?")) {
            composite.addFilter(createVisibilityFilter());
        }

        return composite;
    }

    /**
     * View all internships
     */
    private void viewAllInternships() {
        System.out.println("\n=== ALL INTERNSHIP OPPORTUNITIES ===");

        List<InternshipOpportunity> internships = internshipManager.getAllInternships();

        if (internships.isEmpty()) {
            System.out.println("No internship opportunities in the system.");
            return;
        }

        System.out.println("Total Internships: " + internships.size());
        System.out.println();

        for (int i = 0; i < internships.size(); i++) {
            InternshipOpportunity opp = internships.get(i);
            System.out.printf("%d. %s%n", i + 1, opp.getOpportunityID());
            System.out.printf("   Title: %s%n", opp.getTitle());
            System.out.printf("   Company: %s%n", opp.getCompanyName());
            System.out.printf("   Level: %s | Major: %s%n", opp.getLevel(), opp.getPreferredMajor());
            System.out.printf("   Status: %s | Visible: %s%n", opp.getStatus(), opp.isVisible());
            System.out.printf("   Slots: %d/%d%n", opp.getFilledSlots(), opp.getNumSlots());
            System.out.printf("   Applications: %d%n", opp.getApplications().size());
            System.out.println("   " + "-".repeat(60));
        }

        System.out.println("\nPress Enter to continue...");
        scanner.nextLine();
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