// ========== InternshipLevel.java ==========
package ipms.entity;

/**
 * Enum for internship difficulty levels
 */
public enum InternshipLevel {
    BASIC("Basic"),
    INTERMEDIATE("Intermediate"),
    ADVANCED("Advanced");
    
    private final String displayName;
    
    InternshipLevel(String displayName) {
        this.displayName = displayName;
    }
    
    public String getDisplayName() {
        return displayName;
    }
    
    @Override
    public String toString() {
        return displayName;
    }
}