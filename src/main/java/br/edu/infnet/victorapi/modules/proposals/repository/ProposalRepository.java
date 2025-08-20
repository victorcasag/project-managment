package br.edu.infnet.victorapi.modules.proposals.repository;

import br.edu.infnet.victorapi.modules.proposals.entity.Proposals;
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
public class ProposalRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public Proposals save(Proposals proposal) {
        if (proposal.getId() == null) {
            entityManager.persist(proposal);
            return proposal;
        } else {
            return entityManager.merge(proposal);
        }
    }

    public Optional<Proposals> findById(Integer id) {
        Proposals proposal = entityManager.find(Proposals.class, id);
        return Optional.ofNullable(proposal);
    }

    public List<Proposals> findAll() {
        TypedQuery<Proposals> query = entityManager.createQuery(
                "SELECT p FROM Proposals p ORDER BY p.createdAt DESC", Proposals.class);
        return query.getResultList();
    }

    public void deleteById(Integer id) {
        Proposals proposal = entityManager.find(Proposals.class, id);
        if (proposal != null) {
            entityManager.remove(proposal);
        }
    }

    public boolean existsById(Integer id) {
        return findById(id).isPresent();
    }

    public long count() {
        TypedQuery<Long> query = entityManager.createQuery(
                "SELECT COUNT(p) FROM Proposals p", Long.class);
        return query.getSingleResult();
    }

    public Optional<Proposals> findByProposalNumber(String proposalNumber) {
        TypedQuery<Proposals> query = entityManager.createQuery(
                "SELECT p FROM Proposals p WHERE p.proposalNumber = :proposalNumber", Proposals.class);
        query.setParameter("proposalNumber", proposalNumber);
        List<Proposals> results = query.getResultList();
        return results.isEmpty() ? Optional.empty() : Optional.of(results.getFirst());
    }

    public boolean existsByProposalNumber(String proposalNumber) {
        TypedQuery<Long> query = entityManager.createQuery(
                "SELECT COUNT(p) FROM Proposals p WHERE p.proposalNumber = :proposalNumber", Long.class);
        query.setParameter("proposalNumber", proposalNumber);
        return query.getSingleResult() > 0;
    }

    public Page<Proposals> findProposalsWithFilters(String name, String proposalNumber, Integer departmentId,
                                                   Integer sectorId, Integer statusId, Integer responsibleId,
                                                   BigDecimal minValue, BigDecimal maxValue,
                                                   LocalDate startDate, LocalDate endDate,
                                                   Integer priority, Pageable pageable) {
        
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Proposals> query = cb.createQuery(Proposals.class);
        Root<Proposals> root = query.from(Proposals.class);

        List<Predicate> predicates = new ArrayList<>();

        if (name != null && !name.trim().isEmpty()) {
            predicates.add(cb.like(cb.lower(root.get("name")),
                    "%" + name.toLowerCase() + "%"));
        }

        if (proposalNumber != null && !proposalNumber.trim().isEmpty()) {
            predicates.add(cb.like(cb.lower(root.get("proposalNumber")),
                    "%" + proposalNumber.toLowerCase() + "%"));
        }

        if (departmentId != null) {
            predicates.add(cb.equal(root.get("departmentId"), departmentId));
        }

        if (sectorId != null) {
            predicates.add(cb.equal(root.get("sectorId"), sectorId));
        }

        if (statusId != null) {
            predicates.add(cb.equal(root.get("statusId"), statusId));
        }

        if (responsibleId != null) {
            predicates.add(cb.equal(root.get("responsibleId"), responsibleId));
        }

        if (minValue != null) {
            predicates.add(cb.greaterThanOrEqualTo(root.get("value"), minValue));
        }

        if (maxValue != null) {
            predicates.add(cb.lessThanOrEqualTo(root.get("value"), maxValue));
        }

        if (startDate != null) {
            predicates.add(cb.greaterThanOrEqualTo(root.get("estimatedStart"), startDate));
        }

        if (endDate != null) {
            predicates.add(cb.lessThanOrEqualTo(root.get("estimatedStart"), endDate));
        }

        if (priority != null) {
            predicates.add(cb.equal(root.get("priority"), priority));
        }

        query.where(cb.and(predicates.toArray(new Predicate[0])));
        query.orderBy(cb.desc(root.get("createdAt")));

        TypedQuery<Proposals> typedQuery = entityManager.createQuery(query);
        typedQuery.setFirstResult((int) pageable.getOffset());
        typedQuery.setMaxResults(pageable.getPageSize());
        List<Proposals> proposals = typedQuery.getResultList();

        // Count query
        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        Root<Proposals> countRoot = countQuery.from(Proposals.class);
        countQuery.select(cb.count(countRoot));

        if (!predicates.isEmpty()) {
            List<Predicate> countPredicates = new ArrayList<>();

            if (name != null && !name.trim().isEmpty()) {
                countPredicates.add(cb.like(cb.lower(countRoot.get("name")),
                        "%" + name.toLowerCase() + "%"));
            }

            if (proposalNumber != null && !proposalNumber.trim().isEmpty()) {
                countPredicates.add(cb.like(cb.lower(countRoot.get("proposalNumber")),
                        "%" + proposalNumber.toLowerCase() + "%"));
            }

            if (departmentId != null) {
                countPredicates.add(cb.equal(countRoot.get("departmentId"), departmentId));
            }

            if (sectorId != null) {
                countPredicates.add(cb.equal(countRoot.get("sectorId"), sectorId));
            }

            if (statusId != null) {
                countPredicates.add(cb.equal(countRoot.get("statusId"), statusId));
            }

            if (responsibleId != null) {
                countPredicates.add(cb.equal(countRoot.get("responsibleId"), responsibleId));
            }

            if (minValue != null) {
                countPredicates.add(cb.greaterThanOrEqualTo(countRoot.get("value"), minValue));
            }

            if (maxValue != null) {
                countPredicates.add(cb.lessThanOrEqualTo(countRoot.get("value"), maxValue));
            }

            if (startDate != null) {
                countPredicates.add(cb.greaterThanOrEqualTo(countRoot.get("estimatedStart"), startDate));
            }

            if (endDate != null) {
                countPredicates.add(cb.lessThanOrEqualTo(countRoot.get("estimatedStart"), endDate));
            }

            if (priority != null) {
                countPredicates.add(cb.equal(countRoot.get("priority"), priority));
            }

            countQuery.where(cb.and(countPredicates.toArray(new Predicate[0])));
        }

        Long total = entityManager.createQuery(countQuery).getSingleResult();

        return new PageImpl<>(proposals, pageable, total);
    }

    public List<Proposals> findByNameContaining(String name) {
        TypedQuery<Proposals> query = entityManager.createQuery(
                "SELECT p FROM Proposals p WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%')) " +
                        "ORDER BY p.createdAt DESC", Proposals.class);
        query.setParameter("name", name);
        return query.getResultList();
    }

    public BigDecimal getTotalValueByFilters(Integer statusId, Integer departmentId, LocalDate startDate, LocalDate endDate) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<BigDecimal> query = cb.createQuery(BigDecimal.class);
        Root<Proposals> root = query.from(Proposals.class);

        query.select(cb.sum(root.get("value")));

        List<Predicate> predicates = new ArrayList<>();

        if (statusId != null) {
            predicates.add(cb.equal(root.get("statusId"), statusId));
        }

        if (departmentId != null) {
            predicates.add(cb.equal(root.get("departmentId"), departmentId));
        }

        if (startDate != null) {
            predicates.add(cb.greaterThanOrEqualTo(root.get("createdAt"), startDate.atStartOfDay()));
        }

        if (endDate != null) {
            predicates.add(cb.lessThanOrEqualTo(root.get("createdAt"), endDate.plusDays(1).atStartOfDay()));
        }

        if (!predicates.isEmpty()) {
            query.where(cb.and(predicates.toArray(new Predicate[0])));
        }

        BigDecimal result = entityManager.createQuery(query).getSingleResult();
        return result != null ? result : BigDecimal.ZERO;
    }
}
