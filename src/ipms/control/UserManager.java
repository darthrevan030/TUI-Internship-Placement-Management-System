//==========UserManager.java(SINGLETON)==========
package ipms.control;

import ipms.entity.*;
import java.io.*;
import java.util.*;

/**
 * Manages all users in the system
 * Implements SINGLETON pattern
 */
public class UserManager {
    private static UserManager instance;
    private Map<String, User> users;
    private static final String USERS_FILE = "data/users.dat";

    private UserManager() {
        this.users = new HashMap<>();
        loadUsers();
    }

    public static UserManager getInstance() {
        if (instance == null) {
            instance = new UserManager();
        }
        return instance;
    }

    /**
     * Load users from file or initialize from CSV
     */
    public void loadUsers() {
        File file = new File(USERS_FILE);

        if (file.exists()) {
            loadFromSerialized();
        } else {
            initializeFromCSV();
        }
    }

    /**
     * Load users from serialized file
     */
    @SuppressWarnings("unchecked")
    private void loadFromSerialized() {
        try (ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream(USERS_FILE))) {
            users = (Map<String, User>) ois.readObject();
            System.out.println("Loaded " + users.size() + " users from file.");
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error loading users: " + e.getMessage());
            initializeFromCSV();
        }
    }

    /**
     * Initialize users from CSV files
     */
    private void initializeFromCSV() {
        // Load students
        loadStudentsFromCSV("data/sample_student_list.csv");
        // Load staff
        loadStaffFromCSV("data/sample_staff_list.csv");

        System.out.println("Initialized " + users.size() + " users from CSV files.");
        saveUsers();
    }

    /**
     * Load students from CSV
     */
    private void loadStudentsFromCSV(String filename) {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            boolean firstLine = true;

            while ((line = br.readLine()) != null) {
                if (firstLine) {
                    firstLine = false;
                    continue; // Skip header
                }

                String[] parts = line.split(",");
                if (parts.length >= 5) {
                    String studentID = parts[0].trim();
                    String name = parts[1].trim();
                    String major = parts[2].trim();
                    int year = Integer.parseInt(parts[3].trim());
                    String email = parts[4].trim();

                    Student student = new Student(studentID, name, "password",
                            year, major, email);
                    users.put(studentID, student);
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading students: " + e.getMessage());
        }
    }

    /**
     * Load staff from CSV
     */
    private void loadStaffFromCSV(String filename) {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            boolean firstLine = true;

            while ((line = br.readLine()) != null) {
                if (firstLine) {
                    firstLine = false;
                    continue; // Skip header
                }

                String[] parts = line.split(",");
                if (parts.length >= 5) {
                    String staffID = parts[0].trim();
                    String name = parts[1].trim();
                    String role = parts[2].trim();
                    String department = parts[3].trim();
                    String email = parts[4].trim();

                    CareerCenterStaff staff = new CareerCenterStaff(staffID, name,
                            "password", department, email);
                    users.put(staffID, staff);
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading staff: " + e.getMessage());
        }
    }

    /**
     * Save users to file
     */
    public void saveUsers() {
        try {
            File file = new File(USERS_FILE);
            file.getParentFile().mkdirs(); // Create directory if not exists

            try (ObjectOutputStream oos = new ObjectOutputStream(
                    new FileOutputStream(file))) {
                oos.writeObject(users);
            }
        } catch (IOException e) {
            System.err.println("Error saving users: " + e.getMessage());
        }
    }

    /**
     * Register a new company representative
     */
    public boolean registerCompanyRep(String email, String name, String password,
            String companyName, String department,
            String position) {
        if (users.containsKey(email)) {
            return false; // User already exists
        }

        CompanyRepresentative rep = new CompanyRepresentative(
                email, name, password, companyName, department, position, email);
        users.put(email, rep);
        saveUsers();
        return true;
    }

    /**
     * Get user by ID
     */
    public User getUser(String userID) {
        return users.get(userID);
    }

    /**
     * Get all users of a specific type
     */
    public List<User> getUsersByType(Class<? extends User> userType) {
        List<User> result = new ArrayList<>();
        for (User user : users.values()) {
            if (userType.isInstance(user)) {
                result.add(user);
            }
        }
        return result;
    }

    /**
     * Get all pending company representatives
     */
    public List<CompanyRepresentative> getPendingRepresentatives() {
        List<CompanyRepresentative> pending = new ArrayList<>();
        for (User user : users.values()) {
            if (user instanceof CompanyRepresentative) {
                CompanyRepresentative rep = (CompanyRepresentative) user;
                if (!rep.isApproved()) {
                    pending.add(rep);
                }
            }
        }
        return pending;
    }
}