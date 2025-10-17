// ========== FilterStrategy.java (Interface) ==========
package ipms.control;

import ipms.entity.InternshipOpportunity;
import java.util.List;

/**
 * Strategy interface for filtering internships
 * Implements STRATEGY pattern
 */
public interface FilterStrategy {
    List<InternshipOpportunity> filter(List<InternshipOpportunity> internships);
}