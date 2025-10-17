// ========== ReportGenerator.java ==========
package ipms.control;

import ipms.entity.*;
import java.util.*;
import java.time.LocalDate;

/**
 * Generates various reports for Career Center Staff
 */
public class ReportGenerator {
    private InternshipManager internshipManager;
    private ApplicationManager applicationManager;

    public ReportGenerator() {
        this.internshipManager = InternshipManager.getInstance();
        this.applicationManager = ApplicationManager.getInstance();
    }

    /**
     * Generate comprehensive internship report
     */
    public void generateInternshipReport(FilterStrategy filter) {
        List<InternshipOpportunity> internships = internshipManager.getAllInternships();

        if (filter != null) {
            internships = filter.filter(internships);
        }

        System.out.println("\n" + "=".repeat(80));
        System.out.println("INTERNSHIP OPPORTUNITIES REPORT");
        System.out.println("Generated on: " + LocalDate.now());
        System.out.println("Total Opportunities: " + internships.size());
        System.out.println("=".repeat(80));

        // Group by status
        Map<OpportunityStatus, Integer> statusCounts = new HashMap<>();
        Map<InternshipLevel, Integer> levelCounts = new HashMap<>();
        Map<String, Integer> majorCounts = new HashMap<>();

        for (InternshipOpportunity opp : internships) {
            statusCounts.merge(opp.getStatus(), 1, Integer::sum);
            levelCounts.merge(opp.getLevel(), 1, Integer::sum);
            majorCounts.merge(opp.getPreferredMajor(), 1, Integer::sum);
        }

        // Print summary
        System.out.println("\nBY STATUS:");
        statusCounts.forEach((status, count) -> System.out.printf("  %-15s: %d%n", status, count));

        System.out.println("\nBY LEVEL:");
        levelCounts.forEach((level, count) -> System.out.printf("  %-15s: %d%n", level, count));

        System.out.println("\nBY PREFERRED MAJOR:");
        majorCounts.forEach((major, count) -> System.out.printf("  %-15s: %d%n", major, count));

        System.out.println("\nDETAILED LISTING:");
        System.out.println("-".repeat(80));

        for (InternshipOpportunity opp : internships) {
            System.out.printf("%nID: %s%n", opp.getOpportunityID());
            System.out.printf("Title: %s%n", opp.getTitle());
            System.out.printf("Company: %s%n", opp.getCompanyName());
            System.out.printf("Level: %s | Major: %s | Status: %s%n",
                    opp.getLevel(), opp.getPreferredMajor(), opp.getStatus());
            System.out.printf("Slots: %d/%d | Visible: %s%n",
                    opp.getFilledSlots(), opp.getNumSlots(), opp.isVisible());
            System.out.printf("Period: %s to %s%n",
                    opp.getOpeningDate(), opp.getClosingDate());
            System.out.println("-".repeat(80));
        }
    }

    /**
     * Generate application statistics report
     */
    public void generateApplicationReport() {
        List<Application> apps = applicationManager.getAllApplications();

        System.out.println("\n" + "=".repeat(80));
        System.out.println("APPLICATION STATISTICS REPORT");
        System.out.println("Generated on: " + LocalDate.now());
        System.out.println("Total Applications: " + apps.size());
        System.out.println("=".repeat(80));

        Map<ApplicationStatus, Integer> statusCounts = new HashMap<>();
        for (Application app : apps) {
            statusCounts.merge(app.getStatus(), 1, Integer::sum);
        }

        System.out.println("\nAPPLICATIONS BY STATUS:");
        statusCounts.forEach((status, count) -> System.out.printf("  %-15s: %d (%.1f%%)%n",
                status, count, (count * 100.0 / apps.size())));

        System.out.println("\n" + "=".repeat(80));
    }
}