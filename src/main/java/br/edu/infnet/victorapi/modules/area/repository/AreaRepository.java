package br.edu.infnet.victorapi.modules.area.repository;

import br.edu.infnet.victorapi.modules.area.entity.Area;
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
public class AreaRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public Page<Area> findWithFilters(String name, String code, String description, Boolean isActive, Pageable pageable) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Area> query = cb.createQuery(Area.class);
        Root<Area> root = query.from(Area.class);

        List<Predicate> predicates = new ArrayList<>();

        if (name != null && !name.trim().isEmpty()) {
            predicates.add(cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%"));
        }

        if (code != null && !code.trim().isEmpty()) {
            predicates.add(cb.like(cb.lower(root.get("code")), "%" + code.toLowerCase() + "%"));
        }

        if (description != null && !description.trim().isEmpty()) {
            predicates.add(cb.like(cb.lower(root.get("description")), "%" + description.toLowerCase() + "%"));
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
        List<Area> results = entityManager.createQuery(query)
                .setFirstResult((int) pageable.getOffset())
                .setMaxResults(pageable.getPageSize())
                .getResultList();

        // Count total results
        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        Root<Area> countRoot = countQuery.from(Area.class);
        countQuery.select(cb.count(countRoot));
        countQuery.where(predicates.toArray(new Predicate[0]));
        Long total = entityManager.createQuery(countQuery).getSingleResult();

        return new PageImpl<>(results, pageable, total);
    }

    public List<Area> findActiveByNameContaining(String name) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Area> query = cb.createQuery(Area.class);
        Root<Area> root = query.from(Area.class);

        List<Predicate> predicates = new ArrayList<>();
        predicates.add(cb.equal(root.get("isActive"), true));
        
        if (name != null && !name.trim().isEmpty()) {
            predicates.add(cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%"));
        }

        query.where(predicates.toArray(new Predicate[0]));
        query.orderBy(cb.asc(root.get("name")));

        return entityManager.createQuery(query).getResultList();
    }

    public List<Area> findActiveByCodeContaining(String code) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Area> query = cb.createQuery(Area.class);
        Root<Area> root = query.from(Area.class);

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
