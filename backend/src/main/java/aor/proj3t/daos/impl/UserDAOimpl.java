package aor.proj3t.daos.impl;


import aor.proj3t.daos.interfaces.UserDAO;
import aor.proj3t.entities.Evaluation;
import aor.proj3t.entities.User;
import aor.proj3t.filters.UserFilter;
import jakarta.ejb.Stateless;
import jakarta.persistence.*;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Stateless
public class UserDAOimpl extends AbstractDAO<User, Long> implements UserDAO {


    @Override
    public Optional<User> findByEmail(String email) {
        User user = em.createQuery(
                        "SELECT u FROM User u WHERE u.email = :email", User.class)
                .setParameter("email", email)
                .getSingleResult();
        return Optional.of(user);
    }

    @Override
    public List<User> findByManager(Long managerId) {
        String query = "SELECT u FROM User u WHERE u.manager.id = :managerId";
        return em.createQuery(query, User.class)
                .setParameter("managerId", managerId)
                .getResultList();
    }

    @Override
    public boolean allUsersHaveManager() {
        Long count = em.createQuery(
                        "SELECT COUNT(u) FROM User u WHERE u.manager IS NULL", Long.class)
                .getSingleResult();
        return count == 0;
    }

    @Override
    public Long countAllUsers() {
        return em.createQuery(
                        "SELECT COUNT(u) FROM User u", Long.class)
                .getSingleResult();
    }

    @Override
    public Long countUsersWithoutManager() {
        return em.createQuery(
                "SELECT COUNT(u) FROM User u WHERE u.manager IS NULL", Long.class)
                .getSingleResult();
    }

    @Override
    public Long countUsersWithManager() {
        return em.createQuery(
                        "SELECT COUNT(u) FROM User u WHERE u.manager IS NOT NULL", Long.class)
                .getSingleResult();
    }

    @Override
    public List<User> getUsersByFilters(UserFilter filters) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<User> query = cb.createQuery(User.class);
        Root<User> root = query.from(User.class);
        List<Predicate> predicates = new ArrayList<>();

        if (filters.getId() != null) {
            predicates.add(cb.equal(root.get("id"), filters.getId()));
        }

        if (filters.getManager() != null) {
            predicates.add(cb.equal(root.get("manager"), filters.getManager()));
        }

        if (filters.isNoManager()) {
            predicates.add(cb.isNull(root.get("manager")));
        }

        if (filters.getWorkplace() != null) {
            predicates.add(cb.equal(root.get("workplace"), filters.getWorkplace()));
        }

        if (filters.getSearchText() != null && !filters.getSearchText().isEmpty()) {
            Predicate namePredicate = cb.or(
                    cb.like(root.get("firstName"), "%" + filters.getSearchText() + "%"),
                    cb.like(root.get("lastName"), "%" + filters.getSearchText() + "%"),
                    cb.like(root.get("email"), "%" + filters.getSearchText() + "%")
            );
            predicates.add(namePredicate);
        }

        query.where(predicates.toArray(new Predicate[0]));

        if (filters.getSortField() != null) {
            query.orderBy(filters.isAscending()
                    ? cb.asc(root.get(filters.getSortField().getFieldName()))
                    : cb.desc(root.get(filters.getSortField().getFieldName())));
        }

        return em.createQuery(query).getResultList();
    }

}


