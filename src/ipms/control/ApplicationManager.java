//==========ApplicationManager.java(SINGLETON)==========
package ipms.control;

import ipms.entity.*;
import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Manages all internship applications
 * Implements SINGLETON pattern
 */
public class ApplicationManager {
    private static ApplicationManager instance;
    private List<Application> applications;
    private List<WithdrawalRequest> withdrawalRequests;
    private int nextAppID;
    private int nextReqID;
    private static final String APPLICATIONS_FILE = "data/applications.dat";

    private ApplicationManager() {
        this.applications = new ArrayList<>();
        this.withdrawalRequests = new ArrayList<>();
        this.nextAppID = 1;
        this.nextReqID = 1;
        loadApplications();
    }

    public static ApplicationManager getInstance() {
        if (instance == null) {
            instance = new ApplicationManager();
        }
        return instance;
    }

    /**
     * Load applications from file
     */
    @SuppressWarnings("unchecked")
    private void loadApplications() {
        File file = new File(APPLICATIONS_FILE);
        if (file.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(
                    new FileInputStream(file))) {
                applications = (List<Application>) ois.readObject();
                withdrawalRequests = (List<WithdrawalRequest>) ois.readObject();
                nextAppID = ois.readInt();
                nextReqID = ois.readInt();
            } catch (IOException | ClassNotFoundException e) {
                System.err.println("Error loading applications: " + e.getMessage());
            }
        }
    }

    /**
     * Save applications to file
     */
    public void saveApplications() {
        try {
            File file = new File(APPLICATIONS_FILE);
            file.getParentFile().mkdirs();

            try (ObjectOutputStream oos = new ObjectOutputStream(
                    new FileOutputStream(file))) {
                oos.writeObject(applications);
                oos.writeObject(withdrawalRequests);
                oos.writeInt(nextAppID);
                oos.writeInt(nextReqID);
            }
        } catch (IOException e) {
            System.err.println("Error saving applications: " + e.getMessage());
        }
    }

    /**
     * Submit a new application
     */
    public Application submitApplication(Student student, InternshipOpportunity internship) {
        // Validation checks
        if (!student.canApply()) {
            System.out.println("Cannot apply: Maximum 3 applications or already accepted placement.");
            return null;
        }

        if (!internship.isEligibleStudent(student)) {
            System.out.println("Not eligible for this internship.");
            return null;
        }

        // Check for duplicate application
        for (Application app : applications) {
            if (app.getStudent().equals(student) &&
                    app.getOpportunity().equals(internship) &&
                    app.getStatus() != ApplicationStatus.WITHDRAWN) {
                System.out.println("Already applied for this internship.");
                return null;
            }
        }

        // Create new application
        String appID = "APP" + String.format("%05d", nextAppID++);
        Application app = new Application(appID, student, internship);

        applications.add(app);
        student.addApplication(app);
        internship.addApplication(app);

        saveApplications();
        return app;
    }

    /**
     * Review application (by company rep)
     */
    public void reviewApplication(Application app, boolean approve) {
        if (approve) {
            app.setStatus(ApplicationStatus.SUCCESSFUL);
        } else {
            app.setStatus(ApplicationStatus.UNSUCCESSFUL);
        }
        saveApplications();
    }

    /**
     * Student accepts placement
     */
    public boolean acceptPlacement(Student student, Application app) {
        if (student.acceptPlacement(app)) {
            app.getOpportunity().incrementFilledSlots();

            // Check if internship is now filled
            InternshipOpportunity opp = app.getOpportunity();
            if (opp.getFilledSlots() >= opp.getNumSlots()) {
                opp.setStatus(OpportunityStatus.FILLED);
            }

            saveApplications();
            InternshipManager.getInstance().saveInternships();
            return true;
        }
        return false;
    }

    /**
     * Request withdrawal
     */
    public WithdrawalRequest requestWithdrawal(Application app, String reason) {
        boolean isAfterPlacement = (app.getStudent().getAcceptedPlacement() == app);

        String reqID = "WR" + String.format("%05d", nextReqID++);
        WithdrawalRequest request = new WithdrawalRequest(reqID, app, reason, isAfterPlacement);

        withdrawalRequests.add(request);
        app.setWithdrawalRequest(request);

        saveApplications();
        return request;
    }

    /**
     * Approve withdrawal (by staff)
     */
    public void approveWithdrawal(WithdrawalRequest request) {
        request.setStatus(WithdrawalStatus.APPROVED);
        request.getApplication().setStatus(ApplicationStatus.WITHDRAWN);

        // If it was an accepted placement, decrement filled slots
        Application app = request.getApplication();
        if (request.isAfterPlacement()) {
            app.getOpportunity().decrementFilledSlots(); 
            app.getStudent().acceptPlacement(null); // Clear accepted placement
        }

        saveApplications();
    }

    /**
     * Reject withdrawal
     */
    public void rejectWithdrawal(WithdrawalRequest request) {
        request.setStatus(WithdrawalStatus.REJECTED);
        saveApplications();
    }

    /**
     * Get applications for an internship
     */
    public List<Application> getApplicationsForInternship(InternshipOpportunity internship) {
        return applications.stream()
                .filter(a -> a.getOpportunity().equals(internship))
                .collect(Collectors.toList());
    }

    /**
     * Get applications for a student
     */
    public List<Application> getApplicationsForStudent(Student student) {
        return applications.stream()
                .filter(a -> a.getStudent().equals(student))
                .collect(Collectors.toList());
    }

    /**
     * Get pending withdrawal requests
     */
    public List<WithdrawalRequest> getPendingWithdrawals() {
        return withdrawalRequests.stream()
                .filter(r -> r.getStatus() == WithdrawalStatus.PENDING)
                .collect(Collectors.toList());
    }

    public List<Application> getAllApplications() {
        return new ArrayList<>(applications);
    }
}