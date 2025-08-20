package br.edu.infnet.victorapi.modules.projectstatus.repository;

import br.edu.infnet.victorapi.modules.projectstatus.entity.ProjectStatus;
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
public class ProjectStatusRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public Page<ProjectStatus> findWithFilters(String name, String code, String color, Boolean isActive, Boolean isInitial, Boolean isFinal, Pageable pageable) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<ProjectStatus> query = cb.createQuery(ProjectStatus.class);
        Root<ProjectStatus> root = query.from(ProjectStatus.class);

        List<Predicate> predicates = new ArrayList<>();

        if (isActive != null) {
            predicates.add(cb.equal(root.get("isActive"), isActive));
        } else {
            predicates.add(cb.equal(root.get("isActive"), true));
        }

        if (name != null && !name.trim().isEmpty()) {
            predicates.add(cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%"));
        }

        if (code != null && !code.trim().isEmpty()) {
            predicates.add(cb.like(cb.lower(root.get("code")), "%" + code.toLowerCase() + "%"));
        }

        if (color != null && !color.trim().isEmpty()) {
            predicates.add(cb.like(cb.lower(root.get("color")), "%" + color.toLowerCase() + "%"));
        }

        if (isInitial != null) {
            predicates.add(cb.equal(root.get("isInitial"), isInitial));
        }

        if (isFinal != null) {
            predicates.add(cb.equal(root.get("isFinal"), isFinal));
        }

        query.where(predicates.toArray(new Predicate[0]));
        query.orderBy(cb.asc(root.get("sortOrder")), cb.asc(root.get("name")));

        List<ProjectStatus> result = entityManager.createQuery(query)
                .setFirstResult((int) pageable.getOffset())
                .setMaxResults(pageable.getPageSize())
                .getResultList();

        long total = countWithFilters(name, code, color, isActive, isInitial, isFinal);

        return new PageImpl<>(result, pageable, total);
    }

    private long countWithFilters(String name, String code, String color, Boolean isActive, Boolean isInitial, Boolean isFinal) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        Root<ProjectStatus> root = countQuery.from(ProjectStatus.class);

        List<Predicate> predicates = new ArrayList<>();

        if (isActive != null) {
            predicates.add(cb.equal(root.get("isActive"), isActive));
        } else {
            predicates.add(cb.equal(root.get("isActive"), true));
        }

        if (name != null && !name.trim().isEmpty()) {
            predicates.add(cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%"));
        }

        if (code != null && !code.trim().isEmpty()) {
            predicates.add(cb.like(cb.lower(root.get("code")), "%" + code.toLowerCase() + "%"));
        }

        if (color != null && !color.trim().isEmpty()) {
            predicates.add(cb.like(cb.lower(root.get("color")), "%" + color.toLowerCase() + "%"));
        }

        if (isInitial != null) {
            predicates.add(cb.equal(root.get("isInitial"), isInitial));
        }

        if (isFinal != null) {
            predicates.add(cb.equal(root.get("isFinal"), isFinal));
        }

        countQuery.select(cb.count(root));
        countQuery.where(predicates.toArray(new Predicate[0]));

        return entityManager.createQuery(countQuery).getSingleResult();
    }
}
