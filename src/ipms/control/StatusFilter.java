// ========== StatusFilter.java ==========
package ipms.control;

import ipms.entity.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Filter internships by status.
 * Implements Strategy pattern.
 */
public class StatusFilter implements FilterStrategy {
    private final OpportunityStatus status;
    
    /**
     * Constructor for StatusFilter.
     * X
     * @param status The status to filter by
     */
    public StatusFilter(OpportunityStatus status) {
        this.status = status;
    }
    
    /**
     * Filters internship opportunities by their status.
     * 
     * @param internships List of internship opportunities to filter
     * @return Filtered list containing only internships with matching status
     */
    @Override
    public List<InternshipOpportunity> filter(List<InternshipOpportunity> internships) {
        return internships.stream()
            .filter(i -> i.getStatus() == status)
            .collect(Collectors.toList());
    }
}