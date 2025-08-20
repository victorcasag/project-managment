package br.edu.infnet.victorapi.modules.proposalstatus.repository;

import br.edu.infnet.victorapi.modules.proposalstatus.entity.ProposalStatus;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
@Transactional
public class ProposalStatusRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public ProposalStatus save(ProposalStatus proposalStatus) {
        if (proposalStatus.getId() == null) {
            entityManager.persist(proposalStatus);
            return proposalStatus;
        } else {
            return entityManager.merge(proposalStatus);
        }
    }

    public Optional<ProposalStatus> findById(Integer id) {
        ProposalStatus proposalStatus = entityManager.find(ProposalStatus.class, id);
        return Optional.ofNullable(proposalStatus);
    }

    public List<ProposalStatus> findAll() {
        TypedQuery<ProposalStatus> query = entityManager.createQuery(
                "SELECT ps FROM ProposalStatus ps WHERE ps.isActive = true ORDER BY ps.sortOrder ASC, ps.name ASC", 
                ProposalStatus.class);
        return query.getResultList();
    }

    public void deleteById(Integer id) {
        ProposalStatus proposalStatus = entityManager.find(ProposalStatus.class, id);
        if (proposalStatus != null) {
            proposalStatus.setIsActive(false);
            proposalStatus.setUpdatedAt(LocalDateTime.now());
            entityManager.merge(proposalStatus);
        }
    }

    public boolean existsById(Integer id) {
        return findById(id).isPresent();
    }

    public long count() {
        TypedQuery<Long> query = entityManager.createQuery(
                "SELECT COUNT(ps) FROM ProposalStatus ps WHERE ps.isActive = true", Long.class);
        return query.getSingleResult();
    }

    public Optional<ProposalStatus> findByCode(String code) {
        TypedQuery<ProposalStatus> query = entityManager.createQuery(
                "SELECT ps FROM ProposalStatus ps WHERE ps.code = :code AND ps.isActive = true", 
                ProposalStatus.class);
        query.setParameter("code", code);
        List<ProposalStatus> results = query.getResultList();
        return results.isEmpty() ? Optional.empty() : Optional.of(results.getFirst());
    }

    public List<ProposalStatus> findByNameContaining(String name) {
        TypedQuery<ProposalStatus> query = entityManager.createQuery(
                "SELECT ps FROM ProposalStatus ps WHERE LOWER(ps.name) LIKE LOWER(CONCAT('%', :name, '%')) " +
                        "AND ps.isActive = true ORDER BY ps.sortOrder ASC, ps.name ASC", ProposalStatus.class);
        query.setParameter("name", name);
        return query.getResultList();
    }

    public Page<ProposalStatus> findProposalStatusesWithFilters(String name, String code, Boolean isActive, 
                                                               Boolean isInitial, Boolean isFinal, 
                                                               Pageable pageable) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<ProposalStatus> query = cb.createQuery(ProposalStatus.class);
        Root<ProposalStatus> root = query.from(ProposalStatus.class);

        List<Predicate> predicates = new ArrayList<>();

        if (name != null && !name.trim().isEmpty()) {
            predicates.add(cb.like(cb.lower(root.get("name")),
                    "%" + name.toLowerCase() + "%"));
        }

        if (code != null && !code.trim().isEmpty()) {
            predicates.add(cb.like(cb.lower(root.get("code")),
                    "%" + code.toLowerCase() + "%"));
        }

        predicates.add(cb.equal(root.get("isActive"), Objects.requireNonNullElse(isActive, true)));

        if (isInitial != null) {
            predicates.add(cb.equal(root.get("isInitial"), isInitial));
        }

        if (isFinal != null) {
            predicates.add(cb.equal(root.get("isFinal"), isFinal));
        }

        query.where(cb.and(predicates.toArray(new Predicate[0])));

        query.orderBy(cb.asc(root.get("sortOrder")), cb.asc(root.get("name")));

        TypedQuery<ProposalStatus> typedQuery = entityManager.createQuery(query);
        typedQuery.setFirstResult((int) pageable.getOffset());
        typedQuery.setMaxResults(pageable.getPageSize());
        List<ProposalStatus> proposalStatuses = typedQuery.getResultList();

        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        Root<ProposalStatus> countRoot = countQuery.from(ProposalStatus.class);
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
            countPredicates.add(cb.equal(countRoot.get("isActive"), Objects.requireNonNullElse(isActive, true)));

            if (isInitial != null) {
                countPredicates.add(cb.equal(countRoot.get("isInitial"), isInitial));
            }

            if (isFinal != null) {
                countPredicates.add(cb.equal(countRoot.get("isFinal"), isFinal));
            }

            countQuery.where(cb.and(countPredicates.toArray(new Predicate[0])));
        }

        Long total = entityManager.createQuery(countQuery).getSingleResult();

        return new PageImpl<>(proposalStatuses, pageable, total);
    }

    public boolean existsByCode(String code) {
        TypedQuery<Long> query = entityManager.createQuery(
                "SELECT COUNT(ps) FROM ProposalStatus ps WHERE ps.code = :code", Long.class);
        query.setParameter("code", code);
        return query.getSingleResult() > 0;
    }

    public boolean existsByName(String name) {
        TypedQuery<Long> query = entityManager.createQuery(
                "SELECT COUNT(ps) FROM ProposalStatus ps WHERE ps.name = :name", Long.class);
        query.setParameter("name", name);
        return query.getSingleResult() > 0;
    }

    public boolean activateProposalStatus(Integer proposalStatusId) {
        ProposalStatus proposalStatus = entityManager.find(ProposalStatus.class, proposalStatusId);
        if (proposalStatus != null) {
            proposalStatus.setIsActive(true);
            proposalStatus.setUpdatedAt(LocalDateTime.now());
            entityManager.merge(proposalStatus);
            return true;
        }
        return false;
    }

    public boolean deactivateProposalStatus(Integer proposalStatusId) {
        ProposalStatus proposalStatus = entityManager.find(ProposalStatus.class, proposalStatusId);
        if (proposalStatus != null) {
            proposalStatus.setIsActive(false);
            proposalStatus.setUpdatedAt(LocalDateTime.now());
            entityManager.merge(proposalStatus);
            return true;
        }
        return false;
    }
}
