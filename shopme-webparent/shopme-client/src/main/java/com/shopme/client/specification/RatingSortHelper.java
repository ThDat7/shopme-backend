package com.shopme.client.specification;

import com.shopme.common.entity.Review;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;

public class RatingSortHelper {
    public static void applyBayesianRatingSort(CriteriaQuery<?> cq, CriteriaBuilder cb, Join<?, Review> join) {
        cq.orderBy(cb.desc(
//                   "( (10 * 3.5) + COUNT(r.id) * COALESCE(AVG(r.rating), 0) ) / (10 + COUNT(r.id)) AS bayesian_score " +
                cb.quot(
                        cb.sum(
                                cb.prod(cb.literal(3.5), cb.literal(10)),
                                cb.prod(cb.count(join), cb.coalesce(cb.avg(join.get("rating")), cb.literal(0)))
                        ),
                        cb.sum(cb.literal(10), cb.count(join))
                ))
        );
    }
}
