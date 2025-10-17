// ========== LevelFilter.java ==========
package ipms.control;

import ipms.entity.*;
import java.util.*;
import java.util.stream.Collectors;

public class LevelFilter implements FilterStrategy {
    private InternshipLevel level;

    public LevelFilter(InternshipLevel level) {
        this.level = level;
    }

    @Override
    public List<InternshipOpportunity> filter(List<InternshipOpportunity> internships) {
        return internships.stream()
                .filter(i -> i.getLevel() == level)
                .collect(Collectors.toList());
    }
}