package br.edu.infnet.victorapi.modules.offices.repository;

import br.edu.infnet.victorapi.modules.offices.entity.Offices;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class OfficesRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public Page<Offices> findWithFilters(String name, String code, String city, String state,
                                        Integer countryId, String email, String phone,
                                        Boolean isMainOffice, Boolean isActive, Pageable pageable) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Offices> query = cb.createQuery(Offices.class);
        Root<Offices> root = query.from(Offices.class);

        List<Predicate> predicates = new ArrayList<>();

        if (name != null && !name.trim().isEmpty()) {
            predicates.add(cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%"));
        }

        if (code != null && !code.trim().isEmpty()) {
            predicates.add(cb.like(cb.lower(root.get("code")), "%" + code.toLowerCase() + "%"));
        }

        if (city != null && !city.trim().isEmpty()) {
            predicates.add(cb.like(cb.lower(root.get("city")), "%" + city.toLowerCase() + "%"));
        }

        if (state != null && !state.trim().isEmpty()) {
            predicates.add(cb.like(cb.lower(root.get("state")), "%" + state.toLowerCase() + "%"));
        }

        if (countryId != null) {
            predicates.add(cb.equal(root.get("countryId"), countryId));
        }

        if (email != null && !email.trim().isEmpty()) {
            predicates.add(cb.like(cb.lower(root.get("email")), "%" + email.toLowerCase() + "%"));
        }

        if (phone != null && !phone.trim().isEmpty()) {
            predicates.add(cb.like(cb.lower(root.get("phone")), "%" + phone.toLowerCase() + "%"));
        }

        if (isMainOffice != null) {
            predicates.add(cb.equal(root.get("isMainOffice"), isMainOffice));
        }

        if (isActive != null) {
            predicates.add(cb.equal(root.get("isActive"), isActive));
        } else {
            predicates.add(cb.equal(root.get("isActive"), true));
        }

        query.where(predicates.toArray(new Predicate[0]));

        // Apply sorting
        if (pageable.getSort().isSorted()) {
            pageable.getSort().forEach(order -> {
                if (order.isAscending()) {
                    query.orderBy(cb.asc(root.get(order.getProperty())));
                } else {
                    query.orderBy(cb.desc(root.get(order.getProperty())));
                }
            });
        } else {
            query.orderBy(cb.asc(root.get("name")));
        }

        // Execute query for results
        List<Offices> results = entityManager.createQuery(query)
                .setFirstResult((int) pageable.getOffset())
                .setMaxResults(pageable.getPageSize())
                .getResultList();

        // Count total results
        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        Root<Offices> countRoot = countQuery.from(Offices.class);
        countQuery.select(cb.count(countRoot));
        countQuery.where(predicates.toArray(new Predicate[0]));
        Long total = entityManager.createQuery(countQuery).getSingleResult();

        return new PageImpl<>(results, pageable, total);
    }

    public List<Offices> findActiveByNameContaining(String name) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Offices> query = cb.createQuery(Offices.class);
        Root<Offices> root = query.from(Offices.class);

        List<Predicate> predicates = new ArrayList<>();
        predicates.add(cb.equal(root.get("isActive"), true));
        
        if (name != null && !name.trim().isEmpty()) {
            predicates.add(cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%"));
        }

        query.where(predicates.toArray(new Predicate[0]));
        query.orderBy(cb.asc(root.get("name")));

        return entityManager.createQuery(query).getResultList();
    }

    public List<Offices> findActiveByCodeContaining(String code) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Offices> query = cb.createQuery(Offices.class);
        Root<Offices> root = query.from(Offices.class);

        List<Predicate> predicates = new ArrayList<>();
        predicates.add(cb.equal(root.get("isActive"), true));
        
        if (code != null && !code.trim().isEmpty()) {
            predicates.add(cb.like(cb.lower(root.get("code")), "%" + code.toLowerCase() + "%"));
        }

        query.where(predicates.toArray(new Predicate[0]));
        query.orderBy(cb.asc(root.get("code")));

        return entityManager.createQuery(query).getResultList();
    }
}
