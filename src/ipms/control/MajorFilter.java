// ========== MajorFilter.java ==========
package ipms.control;

import ipms.entity.InternshipOpportunity;
import java.util.*;
import java.util.stream.Collectors;

public class MajorFilter implements FilterStrategy {
    private final String major;

    public MajorFilter(String major) {
        this.major = major;
    }

    @Override
    public List<InternshipOpportunity> filter(List<InternshipOpportunity> internships) {
        return internships.stream()
                .filter(i -> i.getPreferredMajor().equalsIgnoreCase(major))
                .collect(Collectors.toList());
    }
}