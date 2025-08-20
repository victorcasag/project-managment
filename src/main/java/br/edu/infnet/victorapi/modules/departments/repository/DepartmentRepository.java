package br.edu.infnet.victorapi.modules.departments.repository;

import br.edu.infnet.victorapi.modules.departments.entity.Departments;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public class DepartmentRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public Departments save(Departments department) {
        if (department.getId() == null) {
            entityManager.persist(department);
            return department;
        } else {
            return entityManager.merge(department);
        }
    }

    public Optional<Departments> findById(Integer id) {
        Departments department = entityManager.find(Departments.class, id);
        return Optional.ofNullable(department);
    }

    public List<Departments> findAll() {
        TypedQuery<Departments> query = entityManager.createQuery(
                "SELECT d FROM Departments d ORDER BY d.name", Departments.class);
        return query.getResultList();
    }

    public void deleteById(Integer id) {
        Departments department = entityManager.find(Departments.class, id);
        if (department != null) {
            entityManager.remove(department);
        }
    }

    public boolean existsById(Integer id) {
        return findById(id).isPresent();
    }

    public long count() {
        TypedQuery<Long> query = entityManager.createQuery(
                "SELECT COUNT(d) FROM Departments d", Long.class);
        return query.getSingleResult();
    }

    public Page<Departments> findDepartmentsWithFilters(String name, String code, String description,
                                                       Boolean isActive, Pageable pageable) {
        
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Departments> query = cb.createQuery(Departments.class);
        Root<Departments> root = query.from(Departments.class);

        List<Predicate> predicates = new ArrayList<>();

        if (name != null && !name.trim().isEmpty()) {
            predicates.add(cb.like(cb.lower(root.get("name")),
                    "%" + name.toLowerCase() + "%"));
        }

        if (code != null && !code.trim().isEmpty()) {
            predicates.add(cb.like(cb.lower(root.get("code")),
                    "%" + code.toLowerCase() + "%"));
        }

        if (description != null && !description.trim().isEmpty()) {
            predicates.add(cb.like(cb.lower(root.get("description")),
                    "%" + description.toLowerCase() + "%"));
        }

        if (isActive != null) {
            predicates.add(cb.equal(root.get("isActive"), isActive));
        }

        query.where(cb.and(predicates.toArray(new Predicate[0])));
        query.orderBy(cb.asc(root.get("name")));

        TypedQuery<Departments> typedQuery = entityManager.createQuery(query);
        typedQuery.setFirstResult((int) pageable.getOffset());
        typedQuery.setMaxResults(pageable.getPageSize());
        List<Departments> departments = typedQuery.getResultList();

        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        Root<Departments> countRoot = countQuery.from(Departments.class);
        countQuery.select(cb.count(countRoot));

        if (!predicates.isEmpty()) {
            List<Predicate> countPredicates = new ArrayList<>();

            if (name != null && !name.trim().isEmpty()) {
                countPredicates.add(cb.like(cb.lower(countRoot.get("name")),
                        "%" + name.toLowerCase() + "%"));
            }

            if (code != null && !code.trim().isEmpty()) {
                countPredicates.add(cb.like(cb.lower(countRoot.get("code")),
                        "%" + code.toLowerCase() + "%"));
            }

            if (description != null && !description.trim().isEmpty()) {
                countPredicates.add(cb.like(cb.lower(countRoot.get("description")),
                        "%" + description.toLowerCase() + "%"));
            }

            if (isActive != null) {
                countPredicates.add(cb.equal(countRoot.get("isActive"), isActive));
            }

            countQuery.where(cb.and(countPredicates.toArray(new Predicate[0])));
        }

        Long total = entityManager.createQuery(countQuery).getSingleResult();

        return new PageImpl<>(departments, pageable, total);
    }

    public List<Departments> findByNameContaining(String name) {
        TypedQuery<Departments> query = entityManager.createQuery(
                "SELECT d FROM Departments d WHERE LOWER(d.name) LIKE LOWER(CONCAT('%', :name, '%')) " +
                        "ORDER BY d.name", Departments.class);
        query.setParameter("name", name);
        return query.getResultList();
    }
}
