// ========== CompanyRepresentative.java ==========
package ipms.entity;

import java.util.*;

/**
 * Company Representative class
 */
public class CompanyRepresentative extends User {
    private static final long serialVersionUID = 1L;
    
    private final String companyName;
    private final String department;
    private final String position;
    private final String email;
    private boolean isApproved;
    private final List<InternshipOpportunity> createdInternships;
    
    public CompanyRepresentative(String userID, String name, String password,
                                String companyName, String department, 
                                String position, String email) {
        super(userID, name, password);
        this.companyName = companyName;
        this.department = department;
        this.position = position;
        this.email = email;
        this.isApproved = false;
        this.createdInternships = new ArrayList<>();
    }
    
    // Getters
    public String getCompanyName() { return companyName; }
    public String getDepartment() { return department; }
    public String getPosition() { return position; }
    public String getEmail() { return email; }
    public boolean isApproved() { return isApproved; }
    public List<InternshipOpportunity> getCreatedInternships() { 
        return createdInternships; 
    }
    
    // Setters
    public void setApproved(boolean approved) { this.isApproved = approved; }
    
    /**
     * Check if rep can create more internships
     * Business rule: Maximum 5 internships
     */
    public boolean canCreateInternship() {
        return createdInternships.size() < 5;
    }
    
    /**
     * Add internship to rep's created list
     */
    public void addInternship(InternshipOpportunity internship) {
        if (canCreateInternship()) {
            createdInternships.add(internship);
        }
    }
    
    @Override
    public void displayMenu() {
        System.out.println("\n=== COMPANY REPRESENTATIVE MENU ===");
        System.out.println("1. Create Internship Opportunity");
        System.out.println("2. View My Internship Opportunities");
        System.out.println("3. View Applications");
        System.out.println("4. Review Applications");
        System.out.println("5. Toggle Visibility");
        System.out.println("6. Change Password");
        System.out.println("7. Logout");
    }
    
    @Override
    public String getRole() {
        return "COMPANY_REP";
    }
    
    @Override
    public String toString() {
        return String.format("CompanyRep[ID=%s, Name=%s, Company=%s, Approved=%b]",
                           userID, name, companyName, isApproved);
    }
}
