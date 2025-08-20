package br.edu.infnet.victorapi.modules.projecttype.repository;

import br.edu.infnet.victorapi.modules.projecttype.entity.ProjectType;
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
public class ProjectTypeRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public Page<ProjectType> findWithFilters(String name, String code, Boolean active, Pageable pageable) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<ProjectType> query = cb.createQuery(ProjectType.class);
        Root<ProjectType> root = query.from(ProjectType.class);

        List<Predicate> predicates = new ArrayList<>();

        if (active != null) {
            predicates.add(cb.equal(root.get("isActive"), active));
        } else {
            predicates.add(cb.equal(root.get("isActive"), true));
        }

        if (name != null && !name.trim().isEmpty()) {
            predicates.add(cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%"));
        }

        if (code != null && !code.trim().isEmpty()) {
            predicates.add(cb.like(cb.lower(root.get("code")), "%" + code.toLowerCase() + "%"));
        }

        query.where(predicates.toArray(new Predicate[0]));
        query.orderBy(cb.asc(root.get("name")));

        List<ProjectType> result = entityManager.createQuery(query)
                .setFirstResult((int) pageable.getOffset())
                .setMaxResults(pageable.getPageSize())
                .getResultList();

        long total = countWithFilters(name, code, active);

        return new PageImpl<>(result, pageable, total);
    }

    private long countWithFilters(String name, String code, Boolean active) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        Root<ProjectType> root = countQuery.from(ProjectType.class);

        List<Predicate> predicates = new ArrayList<>();

        if (active != null) {
            predicates.add(cb.equal(root.get("isActive"), active));
        } else {
            predicates.add(cb.equal(root.get("isActive"), true));
        }

        if (name != null && !name.trim().isEmpty()) {
            predicates.add(cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%"));
        }

        if (code != null && !code.trim().isEmpty()) {
            predicates.add(cb.like(cb.lower(root.get("code")), "%" + code.toLowerCase() + "%"));
        }

        countQuery.select(cb.count(root));
        countQuery.where(predicates.toArray(new Predicate[0]));

        return entityManager.createQuery(countQuery).getSingleResult();
    }
}
