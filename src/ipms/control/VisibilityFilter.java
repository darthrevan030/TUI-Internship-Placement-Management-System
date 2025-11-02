// ========== VisibilityFilter.java ==========
package ipms.control;

import ipms.entity.InternshipOpportunity;
import java.util.*;
import java.util.stream.Collectors;

public class VisibilityFilter implements FilterStrategy {
    private final boolean visible;

    public VisibilityFilter(boolean visible) {
        this.visible = visible;
    }

    @Override
    public List<InternshipOpportunity> filter(List<InternshipOpportunity> internships) {
        return internships.stream()
                .filter(i -> i.isVisible() == visible)
                .collect(Collectors.toList());
    }
}