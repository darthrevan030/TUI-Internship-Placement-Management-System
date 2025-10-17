// ========== InternshipManager.java (SINGLETON) ==========
package ipms.control;

import ipms.entity.*;
import java.io.*;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Manages all internship opportunities
 * Implements SINGLETON pattern
 */
public class InternshipManager {
    private static InternshipManager instance;
    private List<InternshipOpportunity> internships;
    private int nextID;
    private static final String INTERNSHIPS_FILE = "data/internships.dat";
    
    private InternshipManager() {
        this.internships = new ArrayList<>();
        this.nextID = 1;
        loadInternships();
    }
    
    public static InternshipManager getInstance() {
        if (instance == null) {
            instance = new InternshipManager();
        }
        return instance;
    }
    
    /**
     * Load internships from file
     */
    @SuppressWarnings("unchecked")
    private void loadInternships() {
        File file = new File(INTERNSHIPS_FILE);
        if (file.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(
                    new FileInputStream(file))) {
                internships = (List<InternshipOpportunity>) ois.readObject();
                nextID = ois.readInt();
                System.out.println("Loaded " + internships.size() + " internships.");
            } catch (IOException | ClassNotFoundException e) {
                System.err.println("Error loading internships: " + e.getMessage());
            }
        }
    }
    
    /**
     * Save internships to file
     */
    public void saveInternships() {
        try {
            File file = new File(INTERNSHIPS_FILE);
            file.getParentFile().mkdirs();
            
            try (ObjectOutputStream oos = new ObjectOutputStream(
                    new FileOutputStream(file))) {
                oos.writeObject(internships);
                oos.writeInt(nextID);
            }
        } catch (IOException e) {
            System.err.println("Error saving internships: " + e.getMessage());
        }
    }
    
    /**
     * Create a new internship opportunity
     */
    public InternshipOpportunity createInternship(String title, String description,
                                                  InternshipLevel level, String preferredMajor,
                                                  LocalDate openingDate, LocalDate closingDate,
                                                  String companyName, CompanyRepresentative rep,
                                                  int numSlots) {
        String opportunityID = "INT" + String.format("%04d", nextID++);
        
        InternshipOpportunity internship = new InternshipOpportunity(
            opportunityID, title, description, level, preferredMajor,
            openingDate, closingDate, companyName, rep, numSlots);
        
        internships.add(internship);
        rep.addInternship(internship);
        saveInternships();
        
        return internship;
    }
    
    /**
     * Get internship by ID
     */
    public InternshipOpportunity getInternship(String opportunityID) {
        for (InternshipOpportunity internship : internships) {
            if (internship.getOpportunityID().equals(opportunityID)) {
                return internship;
            }
        }
        return null;
    }
    
    /**
     * Get all internships
     */
    public List<InternshipOpportunity> getAllInternships() {
        return new ArrayList<>(internships);
    }
    
    /**
     * Get visible internships for a student
     */
    public List<InternshipOpportunity> getVisibleInternshipsForStudent(Student student) {
        return internships.stream()
            .filter(i -> i.isVisible() && i.isEligibleStudent(student))
            .collect(Collectors.toList());
    }
    
    /**
     * Get internships created by a representative
     */
    public List<InternshipOpportunity> getInternshipsByRep(CompanyRepresentative rep) {
        return internships.stream()
            .filter(i -> i.getRepresentative().equals(rep))
            .collect(Collectors.toList());
    }
    
    /**
     * Get pending internships for approval
     */
    public List<InternshipOpportunity> getPendingInternships() {
        return internships.stream()
            .filter(i -> i.getStatus() == OpportunityStatus.PENDING)
            .collect(Collectors.toList());
    }
    
    /**
     * Approve an internship
     */
    public void approveInternship(InternshipOpportunity internship) {
        internship.setStatus(OpportunityStatus.APPROVED);
        internship.setVisible(true); // Make visible by default
        saveInternships();
    }
    
    /**
     * Reject an internship
     */
    public void rejectInternship(InternshipOpportunity internship) {
        internship.setStatus(OpportunityStatus.REJECTED);
        saveInternships();
    }
    
    /**
     * Toggle visibility of an internship
     */
    public void toggleVisibility(InternshipOpportunity internship) {
        internship.setVisible(!internship.isVisible());
        saveInternships();
    }
    
    /**
     * Apply filters to internship list
     * Demonstrates STRATEGY pattern
     */
    public List<InternshipOpportunity> filterInternships(
            List<InternshipOpportunity> internships, FilterStrategy filter) {
        return filter.filter(internships);
    }
}