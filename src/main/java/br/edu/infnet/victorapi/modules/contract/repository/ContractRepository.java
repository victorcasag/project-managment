package br.edu.infnet.victorapi.modules.contract.repository;

import br.edu.infnet.victorapi.modules.contract.entity.Contract;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public class ContractRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public Contract save(Contract contract) {
        if (contract.getId() == null) {
            entityManager.persist(contract);
            return contract;
        } else {
            return entityManager.merge(contract);
        }
    }

    public Optional<Contract> findById(Integer id) {
        Contract contract = entityManager.find(Contract.class, id);
        return Optional.ofNullable(contract);
    }

    public List<Contract> findAll() {
        TypedQuery<Contract> query = entityManager.createQuery(
                "SELECT c FROM Contract c ORDER BY c.name", Contract.class);
        return query.getResultList();
    }

    public void deleteById(Integer id) {
        Contract contract = entityManager.find(Contract.class, id);
        if (contract != null) {
            entityManager.remove(contract);
        }
    }

    public boolean existsById(Integer id) {
        return findById(id).isPresent();
    }

    public long count() {
        TypedQuery<Long> query = entityManager.createQuery(
                "SELECT COUNT(c) FROM Contract c", Long.class);
        return query.getSingleResult();
    }

    public Page<Contract> findContractsWithFilters(String name, String contractNumber, String description,
                                                  Integer clientSupplierId, Integer coinTypeId, LocalDate startDateFrom,
                                                  LocalDate startDateTo, LocalDate endDateFrom, LocalDate endDateTo,
                                                  BigDecimal valueFrom, BigDecimal valueTo, Boolean isActive, Pageable pageable) {
        
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Contract> query = cb.createQuery(Contract.class);
        Root<Contract> root = query.from(Contract.class);

        List<Predicate> predicates = new ArrayList<>();

        if (name != null && !name.trim().isEmpty()) {
            predicates.add(cb.like(cb.lower(root.get("name")),
                    "%" + name.toLowerCase() + "%"));
        }

        if (contractNumber != null && !contractNumber.trim().isEmpty()) {
            predicates.add(cb.like(cb.lower(root.get("contractNumber")),
                    "%" + contractNumber.toLowerCase() + "%"));
        }

        if (description != null && !description.trim().isEmpty()) {
            predicates.add(cb.like(cb.lower(root.get("description")),
                    "%" + description.toLowerCase() + "%"));
        }

        if (clientSupplierId != null) {
            predicates.add(cb.equal(root.get("clientSupplierId"), clientSupplierId));
        }

        if (coinTypeId != null) {
            predicates.add(cb.equal(root.get("coinTypeId"), coinTypeId));
        }

        if (startDateFrom != null) {
            predicates.add(cb.greaterThanOrEqualTo(root.get("startDate"), startDateFrom));
        }

        if (startDateTo != null) {
            predicates.add(cb.lessThanOrEqualTo(root.get("startDate"), startDateTo));
        }

        if (endDateFrom != null) {
            predicates.add(cb.greaterThanOrEqualTo(root.get("endDate"), endDateFrom));
        }

        if (endDateTo != null) {
            predicates.add(cb.lessThanOrEqualTo(root.get("endDate"), endDateTo));
        }

        if (valueFrom != null) {
            predicates.add(cb.greaterThanOrEqualTo(root.get("value"), valueFrom));
        }

        if (valueTo != null) {
            predicates.add(cb.lessThanOrEqualTo(root.get("value"), valueTo));
        }

        if (isActive != null) {
            predicates.add(cb.equal(root.get("isActive"), isActive));
        }

        query.where(cb.and(predicates.toArray(new Predicate[0])));
        query.orderBy(cb.asc(root.get("name")));

        TypedQuery<Contract> typedQuery = entityManager.createQuery(query);
        typedQuery.setFirstResult((int) pageable.getOffset());
        typedQuery.setMaxResults(pageable.getPageSize());
        List<Contract> contracts = typedQuery.getResultList();

        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        Root<Contract> countRoot = countQuery.from(Contract.class);
        countQuery.select(cb.count(countRoot));

        if (!predicates.isEmpty()) {
            List<Predicate> countPredicates = new ArrayList<>();

            if (name != null && !name.trim().isEmpty()) {
                countPredicates.add(cb.like(cb.lower(countRoot.get("name")),
                        "%" + name.toLowerCase() + "%"));
            }

            if (contractNumber != null && !contractNumber.trim().isEmpty()) {
                countPredicates.add(cb.like(cb.lower(countRoot.get("contractNumber")),
                        "%" + contractNumber.toLowerCase() + "%"));
            }

            if (description != null && !description.trim().isEmpty()) {
                countPredicates.add(cb.like(cb.lower(countRoot.get("description")),
                        "%" + description.toLowerCase() + "%"));
            }

            if (clientSupplierId != null) {
                countPredicates.add(cb.equal(countRoot.get("clientSupplierId"), clientSupplierId));
            }

            if (coinTypeId != null) {
                countPredicates.add(cb.equal(countRoot.get("coinTypeId"), coinTypeId));
            }

            if (startDateFrom != null) {
                countPredicates.add(cb.greaterThanOrEqualTo(countRoot.get("startDate"), startDateFrom));
            }

            if (startDateTo != null) {
                countPredicates.add(cb.lessThanOrEqualTo(countRoot.get("startDate"), startDateTo));
            }

            if (endDateFrom != null) {
                countPredicates.add(cb.greaterThanOrEqualTo(countRoot.get("endDate"), endDateFrom));
            }

            if (endDateTo != null) {
                countPredicates.add(cb.lessThanOrEqualTo(countRoot.get("endDate"), endDateTo));
            }

            if (valueFrom != null) {
                countPredicates.add(cb.greaterThanOrEqualTo(countRoot.get("value"), valueFrom));
            }

            if (valueTo != null) {
                countPredicates.add(cb.lessThanOrEqualTo(countRoot.get("value"), valueTo));
            }

            if (isActive != null) {
                countPredicates.add(cb.equal(countRoot.get("isActive"), isActive));
            }

            countQuery.where(cb.and(countPredicates.toArray(new Predicate[0])));
        }

        Long total = entityManager.createQuery(countQuery).getSingleResult();

        return new PageImpl<>(contracts, pageable, total);
    }

    public List<Contract> findByNameContaining(String name) {
        TypedQuery<Contract> query = entityManager.createQuery(
                "SELECT c FROM Contract c WHERE LOWER(c.name) LIKE LOWER(CONCAT('%', :name, '%')) " +
                        "ORDER BY c.name", Contract.class);
        query.setParameter("name", name);
        return query.getResultList();
    }
}
