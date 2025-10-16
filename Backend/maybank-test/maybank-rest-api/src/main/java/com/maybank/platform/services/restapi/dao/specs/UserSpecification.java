package com.maybank.platform.services.restapi.dao.specs;

import com.maybank.platform.services.restapi.model.User;
import com.maybank.platform.services.restapi.payload.request.UserListingRequest;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class UserSpecification {
    public static Specification<User> filter(UserListingRequest req) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (req.getDisplayName() != null && !req.getDisplayName().isBlank()) {
                predicates.add(cb.like(cb.lower(root.get("displayName")), "%" + req.getDisplayName().toLowerCase() + "%"));
            }

            // Add more conditions as needed
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
