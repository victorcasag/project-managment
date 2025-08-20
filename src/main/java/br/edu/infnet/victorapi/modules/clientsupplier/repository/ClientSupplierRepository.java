package br.edu.infnet.victorapi.modules.clientsupplier.repository;

import br.edu.infnet.victorapi.modules.clientsupplier.entity.ClientSupplier;
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
public class ClientSupplierRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public Page<ClientSupplier> findWithFilters(String name, String document, String documentType, String email,
                                               String phone, String city, String state, String type,
                                               Integer countryId, Boolean isActive, Pageable pageable) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<ClientSupplier> query = cb.createQuery(ClientSupplier.class);
        Root<ClientSupplier> root = query.from(ClientSupplier.class);

        List<Predicate> predicates = new ArrayList<>();

        if (name != null && !name.trim().isEmpty()) {
            predicates.add(cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%"));
        }

        if (document != null && !document.trim().isEmpty()) {
            predicates.add(cb.like(cb.lower(root.get("document")), "%" + document.toLowerCase() + "%"));
        }

        if (documentType != null && !documentType.trim().isEmpty()) {
            predicates.add(cb.like(cb.lower(root.get("documentType")), "%" + documentType.toLowerCase() + "%"));
        }

        if (email != null && !email.trim().isEmpty()) {
            predicates.add(cb.like(cb.lower(root.get("email")), "%" + email.toLowerCase() + "%"));
        }

        if (phone != null && !phone.trim().isEmpty()) {
            predicates.add(cb.like(cb.lower(root.get("phone")), "%" + phone.toLowerCase() + "%"));
        }

        if (city != null && !city.trim().isEmpty()) {
            predicates.add(cb.like(cb.lower(root.get("city")), "%" + city.toLowerCase() + "%"));
        }

        if (state != null && !state.trim().isEmpty()) {
            predicates.add(cb.like(cb.lower(root.get("state")), "%" + state.toLowerCase() + "%"));
        }

        if (type != null && !type.trim().isEmpty()) {
            predicates.add(cb.equal(root.get("type"), type));
        }

        if (countryId != null) {
            predicates.add(cb.equal(root.get("countryId"), countryId));
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
        List<ClientSupplier> results = entityManager.createQuery(query)
                .setFirstResult((int) pageable.getOffset())
                .setMaxResults(pageable.getPageSize())
                .getResultList();

        // Count total results
        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        Root<ClientSupplier> countRoot = countQuery.from(ClientSupplier.class);
        countQuery.select(cb.count(countRoot));
        countQuery.where(predicates.toArray(new Predicate[0]));
        Long total = entityManager.createQuery(countQuery).getSingleResult();

        return new PageImpl<>(results, pageable, total);
    }

    public List<ClientSupplier> findActiveByNameContaining(String name) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<ClientSupplier> query = cb.createQuery(ClientSupplier.class);
        Root<ClientSupplier> root = query.from(ClientSupplier.class);

        List<Predicate> predicates = new ArrayList<>();
        predicates.add(cb.equal(root.get("isActive"), true));
        
        if (name != null && !name.trim().isEmpty()) {
            predicates.add(cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%"));
        }

        query.where(predicates.toArray(new Predicate[0]));
        query.orderBy(cb.asc(root.get("name")));

        return entityManager.createQuery(query).getResultList();
    }

    public List<ClientSupplier> findByTypeAndActive(String type) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<ClientSupplier> query = cb.createQuery(ClientSupplier.class);
        Root<ClientSupplier> root = query.from(ClientSupplier.class);

        List<Predicate> predicates = new ArrayList<>();
        predicates.add(cb.equal(root.get("isActive"), true));
        predicates.add(cb.equal(root.get("type"), type));

        query.where(predicates.toArray(new Predicate[0]));
        query.orderBy(cb.asc(root.get("name")));

        return entityManager.createQuery(query).getResultList();
    }
}
