// ========== User.java (Abstract Base Class) ==========
package ipms.entity;

/**
 * Abstract base class representing a user in the system
 */
public abstract class User implements java.io.Serializable {
    private static final long serialVersionUID = 1L;
    
    protected String userID;
    protected String name;
    protected String password;
    
    /**
     * Constructor for User
     * @param userID Unique identifier for user
     * @param name Full name of user
     * @param password User's password
     */
    public User(String userID, String name, String password) {
        this.userID = userID;
        this.name = name;
        this.password = password;
    }
    
    // Getters and Setters (Encapsulation)
    public String getUserID() { return userID; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    /**
     * Change user password
     * @param oldPassword Current password
     * @param newPassword New password
     * @return true if successful
     */
    public boolean changePassword(String oldPassword, String newPassword) {
        if (this.password.equals(oldPassword)) {
            this.password = newPassword;
            return true;
        }
        return false;
    }
    
    /**
     * Verify password
     * @param password Password to verify
     * @return true if correct
     */
    public boolean verifyPassword(String password) {
        return this.password.equals(password);
    }
    
    /**
     * Abstract method - each user type displays differently
     * Demonstrates POLYMORPHISM
     */
    public abstract void displayMenu();
    
    /**
     * Get user role type
     * @return String representation of role
     */
    public abstract String getRole();
}