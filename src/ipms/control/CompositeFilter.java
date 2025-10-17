// ========== CompositeFilter.java ==========
package ipms.control;

import ipms.entity.InternshipOpportunity;
import java.util.*;

/**
 * Composite filter that applies multiple filters in sequence
 */
public class CompositeFilter implements FilterStrategy {
    private List<FilterStrategy> filters;

    public CompositeFilter() {
        this.filters = new ArrayList<>();
    }

    public void addFilter(FilterStrategy filter) {
        filters.add(filter);
    }

    @Override
    public List<InternshipOpportunity> filter(List<InternshipOpportunity> internships) {
        List<InternshipOpportunity> result = new ArrayList<>(internships);
        for (FilterStrategy filter : filters) {
            result = filter.filter(result);
        }
        return result;
    }
}