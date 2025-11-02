package ipms.control;

import ipms.entity.*;

/**
 * Manages authentication and login sessions.
 * Implements SINGLETON pattern.
*/
public class AuthenticationManager {
    private static AuthenticationManager instance;
    private User currentUser;
    private final UserManager userManager;
    
    /**
     * Private constructor for Singleton pattern.
     */
    private AuthenticationManager() {
        this.currentUser = null;
        this.userManager = UserManager.getInstance();
    }
    
    /**
     * Gets the singleton instance of AuthenticationManager.
     * 
     * @return AuthenticationManager instance
     */
    public static AuthenticationManager getInstance() {
        if (instance == null) {
            instance = new AuthenticationManager();
        }
        return instance;
    }
    
    /**
     * Authenticates a user with their credentials.
     * 
     * @param userID User identifier
     * @param password User password
     * @return User object if successful, null otherwise
     */
    public User authenticate(String userID, String password) {
        User user = userManager.getUser(userID);
        
        if (user == null) {
            return null;
        }
        
        // Check if company rep is approved
        if (user instanceof CompanyRepresentative rep) {
            if (!rep.isApproved()) {
                System.out.println("Account pending approval from Career Center Staff.");
                return null;
            }
        }
        
        // Verify password
        if (user.verifyPassword(password)) {
            this.currentUser = user;
            return user;
        }
        
        return null;
    }
    
    /**
     * Logs out the current user.
     */
    public void logout() {
        this.currentUser = null;
    }
    
    /**
     * Gets the currently logged in user.
     * 
     * @return Current user or null if no one is logged in
     */
    public User getCurrentUser() {
        return currentUser;
    }
    
    /**
     * Checks if a user is currently logged in.
     * 
     * @return true if user is logged in, false otherwise
     */
    public boolean isLoggedIn() {
        return currentUser != null;
    }
    
    /**
     * Changes password for the current user.
     * 
     * @param oldPassword Current password
     * @param newPassword New password
     * @return true if successful, false otherwise
     */
    public boolean changePassword(String oldPassword, String newPassword) {
        if (currentUser != null) {
            boolean success = currentUser.changePassword(oldPassword, newPassword);
            if (success) {
                userManager.saveUsers();
            }
            return success;
        }
        return false;
    }
}