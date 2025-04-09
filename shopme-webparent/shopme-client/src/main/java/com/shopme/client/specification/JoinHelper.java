package com.shopme.client.specification;

import jakarta.persistence.criteria.*;
import jakarta.persistence.metamodel.ListAttribute;
import jakarta.persistence.metamodel.SetAttribute;
import jakarta.persistence.metamodel.SingularAttribute;

public class JoinHelper {
    public static <K, Z> ListJoin<K, Z> join(From<?, K> from, ListAttribute<K, Z> attribute, JoinType joinType) {
        for (Join<K, ?> join : from.getJoins()) {

            boolean sameName = join.getAttribute().getName().equals(attribute.getName());

            if (sameName && join.getJoinType().equals(joinType)) {

                return (ListJoin<K, Z>) join;
            }
        }
        return from.join(attribute, joinType);
    }

    public static <K, Z> SetJoin<K, Z> join(From<?, K> from, SetAttribute<K, Z> attribute, JoinType joinType) {
        for (Join<K, ?> join : from.getJoins()) {

            boolean sameName = join.getAttribute().getName().equals(attribute.getName());

            if (sameName && join.getJoinType().equals(joinType)) {
                return (SetJoin<K, Z>) join;
            }
        }
        return from.join(attribute, joinType);
    }

    public static <K, Z> Join<K, Z> join(From<?, K> from, SingularAttribute<K, Z> attribute, JoinType joinType) {
        for (Join<K, ?> join : from.getJoins()) {

            boolean sameName = join.getAttribute().getName().equals(attribute.getName());

            if (sameName && join.getJoinType().equals(joinType)) {
                return (Join<K, Z>) join;
            }
        }
        return from.join(attribute, joinType);
    }

    public static <K, Z> Join<K, Z> join(From<?, K> from, String attributeName, JoinType joinType) {
        for (Join<K, ?> join : from.getJoins()) {

            boolean sameName = join.getAttribute().getName().equals(attributeName);

            if (sameName && join.getJoinType().equals(joinType)) {
                return (Join<K, Z>) join;
            }
        }
        return from.join(attributeName, joinType);
    }
}