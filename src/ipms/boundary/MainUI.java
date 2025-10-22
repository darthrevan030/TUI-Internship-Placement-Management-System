// ========== MainUI.java ==========
package ipms.boundary;

import ipms.control.*;
import ipms.entity.*;
import java.util.List;
import java.util.Scanner;

/**
 * Main user interface - entry point for the application
 */
public class MainUI {
    private Scanner scanner;
    private AuthenticationManager authManager;
    private UserManager userManager;

    public MainUI() {
        this.scanner = new Scanner(System.in);
        this.authManager = AuthenticationManager.getInstance();
        this.userManager = UserManager.getInstance();
    }

    /**
     * Main application loop
     */
    public void start() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("  INTERNSHIP PLACEMENT MANAGEMENT SYSTEM");
        System.out.println("=".repeat(60));

        // Debug: Use getUsersByType instead of getAllUsers
        System.out.println("\n=== SYSTEM INITIALIZATION DEBUG ===");
        List<User> allUsers = userManager.getUsersByType(User.class);
        System.out.println("Number of users loaded: " + allUsers.size());
        System.out.println("\nUsers in system:");
        
        System.out.println("\nSTUDENTS:");
        for (User u : userManager.getUsersByType(Student.class)) {
            Student s = (Student) u;
            System.out.println("  - " + s.getUserID() + " | " + s.getName() + " | Year " + s.getYearOfStudy() + " | " + s.getMajor());
        }
        
        System.out.println("\nSTAFF:");
        for (User u : userManager.getUsersByType(CareerCenterStaff.class)) {
            System.out.println("  - " + u.getUserID() + " | " + u.getName());
        }
        
        System.out.println("\nCOMPANY REPS:");
        for (User u : userManager.getUsersByType(CompanyRepresentative.class)) {
            CompanyRepresentative cr = (CompanyRepresentative) u;
            System.out.println("  - " + cr.getUserID() + " | " + cr.getName() + " | Approved: " + cr.isApproved());
        }
        System.out.println("===================================\n");

        while (true) {
            if (!authManager.isLoggedIn()) {
                displayLoginMenu();
            } else {
                User currentUser = authManager.getCurrentUser();

                if (currentUser instanceof Student) {
                    new StudentUI((Student) currentUser, scanner).displayMenu();
                } else if (currentUser instanceof CompanyRepresentative) {
                    new CompanyRepUI((CompanyRepresentative) currentUser, scanner).displayMenu();
                } else if (currentUser instanceof CareerCenterStaff) {
                    new StaffUI((CareerCenterStaff) currentUser, scanner).displayMenu();
                }

                // Check if still logged in (user might have logged out)
                if (!authManager.isLoggedIn()) {
                    System.out.println("\nLogged out successfully.");
                }
            }
        }
    }

    /**
     * Display login menu
     */
    private void displayLoginMenu() {
        System.out.println("\n=== LOGIN MENU ===");
        System.out.println("1. Login");
        System.out.println("2. Register as Company Representative");
        System.out.println("3. Exit");

        int choice = InputValidator.getIntInput(scanner, "Enter choice: ", 1, 3);

        switch (choice) {
            case 1:
                handleLogin();
                break;
            case 2:
                handleCompanyRepRegistration();
                break;
            case 3:
                System.out.println("\nThank you for using IPMS. Goodbye!");
                System.exit(0);
        }
    }

    /**
     * Handle user login
     */
    private void handleLogin() {
        System.out.println("\n--- Login ---");
        System.out.print("User ID: ");
        String userID = scanner.nextLine().trim();

        System.out.print("Password: ");
        String password = scanner.nextLine().trim();

        // DEBUG: Check what's in UserManager
        System.out.println("\n=== DEBUG INFO ===");
        System.out.println("Entered UserID: '" + userID + "'");
        System.out.println("Entered Password: '" + password + "'");
        
        User foundUser = userManager.getUser(userID);
        if (foundUser == null) {
            System.out.println("ERROR: User '" + userID + "' NOT FOUND in UserManager");
            System.out.println("Available users:");
            for (User u : userManager.getUsersByType(User.class)) {
                System.out.println("  - " + u.getUserID());
            }
        } else {
            System.out.println("User found: " + foundUser.getName());
            System.out.println("User type: " + foundUser.getClass().getSimpleName());
            System.out.println("Calling verifyPassword...");
            boolean passwordMatch = foundUser.verifyPassword(password);
            System.out.println("Password verification result: " + passwordMatch);
            
            if (foundUser instanceof CompanyRepresentative) {
                CompanyRepresentative rep = (CompanyRepresentative) foundUser;
                System.out.println("CompanyRep approval status: " + rep.isApproved());
            }
        }
        System.out.println("==================\n");

        User user = authManager.authenticate(userID, password);

        if (user != null) {
            System.out.println("\nLogin successful! Welcome, " + user.getName());
        } else {
            System.out.println("\nLogin failed. Invalid credentials or account not approved.");
        }
    }

    /**
     * Handle company representative registration
     */
    private void handleCompanyRepRegistration() {
        System.out.println("\n--- Register as Company Representative ---");

        String email = InputValidator.getStringInput(scanner, "Email (this will be your User ID): ");
        String name = InputValidator.getStringInput(scanner, "Full Name: ");
        String password = InputValidator.getStringInput(scanner, "Password: ");
        String companyName = InputValidator.getStringInput(scanner, "Company Name: ");
        String department = InputValidator.getStringInput(scanner, "Department: ");
        String position = InputValidator.getStringInput(scanner, "Position: ");

        boolean success = userManager.registerCompanyRep(email, name, password,
                companyName, department, position);

        if (success) {
            System.out.println("\nRegistration successful!");
            System.out.println("Your account is pending approval from Career Center Staff.");
            System.out.println("You will be able to login once approved.");
        } else {
            System.out.println("\nRegistration failed. Email already exists.");
        }
    }

    /**
     * Main entry point
     */
    public static void main(String[] args) {
        MainUI mainUI = new MainUI();
        mainUI.start();
    }
}