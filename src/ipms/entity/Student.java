// ========== Student.java ==========
package ipms.entity;

import java.util.*;

/**
 * Student class extending User
 * Represents a student user in the system
 */
public class Student extends User {
    private static final long serialVersionUID = 1L;
    
    private int yearOfStudy;
    private String major;
    private String email;
    private List<Application> applications;
    private Application acceptedPlacement;
    
    /**
     * Constructor for Student
     */
    public Student(String userID, String name, String password, 
                   int yearOfStudy, String major, String email) {
        super(userID, name, password);
        this.yearOfStudy = yearOfStudy;
        this.major = major;
        this.email = email;
        this.applications = new ArrayList<>();
        this.acceptedPlacement = null;
    }
    
    // Getters
    public int getYearOfStudy() { return yearOfStudy; }
    public String getMajor() { return major; }
    public String getEmail() { return email; }
    public List<Application> getApplications() { return applications; }
    public Application getAcceptedPlacement() { return acceptedPlacement; }
    
    // Setters
    public void setYearOfStudy(int year) { this.yearOfStudy = year; }
    public void setMajor(String major) { this.major = major; }
    
    /**
     * Check if student can apply for more internships
     * Business rule: Maximum 3 applications
     */
    public boolean canApply() {
        return applications.size() < 3 && acceptedPlacement == null;
    }
    
    /**
     * Add application to student's list
     */
    public void addApplication(Application app) {
        if (canApply()) {
            applications.add(app);
        }
    }
    
    /**
     * Accept an internship placement
     * Withdraws all other applications
     */
    public boolean acceptPlacement(Application app) {
        if (app == null) {
            this.acceptedPlacement = null;
            return true;
        }
        if (app.getStatus() == ApplicationStatus.SUCCESSFUL) {
            this.acceptedPlacement = app;
            // Withdraw all other applications
            for (Application a : applications) {
                if (a != app && a.getStatus() != ApplicationStatus.WITHDRAWN) {
                    a.setStatus(ApplicationStatus.WITHDRAWN);
                }
            }
            return true;
        }
        return false;
    }
    
    /**
     * Check eligibility for internship level
     */
    public boolean isEligibleForLevel(InternshipLevel level) {
        if (yearOfStudy <= 2) {
            return level == InternshipLevel.BASIC;
        }
        return true; // Year 3+ can apply for any level
    }
    
    @Override
    public void displayMenu() {
        System.out.println("\n=== STUDENT MENU ===");
        System.out.println("1. View Internship Opportunities");
        System.out.println("2. Apply for Internship");
        System.out.println("3. View My Applications");
        System.out.println("4. Accept Placement");
        System.out.println("5. Request Withdrawal");
        System.out.println("6. Change Password");
        System.out.println("7. Logout");
    }
    
    @Override
    public String getRole() {
        return "STUDENT";
    }
    
    @Override
    public String toString() {
        return String.format("Student[ID=%s, Name=%s, Year=%d, Major=%s]",
                           userID, name, yearOfStudy, major);
    }
}