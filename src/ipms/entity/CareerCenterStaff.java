// ========== CareerCenterStaff.java ==========
package ipms.entity;

/**
 * Career Center Staff class
 */
public class CareerCenterStaff extends User {
    private static final long serialVersionUID = 1L;

    private final String staffDepartment;
    private final String email;

    public CareerCenterStaff(String userID, String name, String password,
            String staffDepartment, String email) {
        super(userID, name, password);
        this.staffDepartment = staffDepartment;
        this.email = email;
    }

    // Getters
    public String getStaffDepartment() {
        return staffDepartment;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public void displayMenu() {
        System.out.println("\n=== CAREER CENTER STAFF MENU ===");
        System.out.println("1. Approve Company Representatives");
        System.out.println("2. Approve Internship Opportunities");
        System.out.println("3. Approve Withdrawal Requests");
        System.out.println("4. Generate Reports");
        System.out.println("5. View All Internships");
        System.out.println("6. Change Password");
        System.out.println("7. Logout");
    }

    @Override
    public String getRole() {
        return "STAFF";
    }

    @Override
    public String toString() {
        return String.format("Staff[ID=%s, Name=%s, Dept=%s]",
                userID, name, staffDepartment);
    }
}