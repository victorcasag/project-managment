package br.edu.infnet.victorapi.modules.sector.repository;

import br.edu.infnet.victorapi.modules.sector.entity.Sector;
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
public class SectorRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public Sector save(Sector sector) {
        if (sector.getId() == null) {
            entityManager.persist(sector);
            return sector;
        } else {
            return entityManager.merge(sector);
        }
    }

    public Optional<Sector> findById(Integer id) {
        Sector sector = entityManager.find(Sector.class, id);
        return Optional.ofNullable(sector);
    }

    public List<Sector> findAll() {
        TypedQuery<Sector> query = entityManager.createQuery(
                "SELECT s FROM Sector s WHERE s.isActive = true ORDER BY s.name", Sector.class);
        return query.getResultList();
    }

    public void deleteById(Integer id) {
        Sector sector = entityManager.find(Sector.class, id);
        if (sector != null) {
            sector.setIsActive(false);
            sector.setUpdatedAt(LocalDateTime.now());
            entityManager.merge(sector);
        }
    }

    public boolean existsById(Integer id) {
        return findById(id).isPresent();
    }

    public long count() {
        TypedQuery<Long> query = entityManager.createQuery(
                "SELECT COUNT(s) FROM Sector s WHERE s.isActive = true", Long.class);
        return query.getSingleResult();
    }

    public Optional<Sector> findByCode(String code) {
        TypedQuery<Sector> query = entityManager.createQuery(
                "SELECT s FROM Sector s WHERE s.code = :code AND s.isActive = true", Sector.class);
        query.setParameter("code", code);
        List<Sector> results = query.getResultList();
        return results.isEmpty() ? Optional.empty() : Optional.of(results.getFirst());
    }

    public List<Sector> findByNameContaining(String name) {
        TypedQuery<Sector> query = entityManager.createQuery(
                "SELECT s FROM Sector s WHERE LOWER(s.name) LIKE LOWER(CONCAT('%', :name, '%')) " +
                        "AND s.isActive = true ORDER BY s.name", Sector.class);
        query.setParameter("name", name);
        return query.getResultList();
    }

    public Page<Sector> findSectorsWithFilters(String name, String code, Boolean isActive, Pageable pageable) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Sector> query = cb.createQuery(Sector.class);
        Root<Sector> root = query.from(Sector.class);

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

        query.where(cb.and(predicates.toArray(new Predicate[0])));

        query.orderBy(cb.asc(root.get("name")));

        TypedQuery<Sector> typedQuery = entityManager.createQuery(query);
        typedQuery.setFirstResult((int) pageable.getOffset());
        typedQuery.setMaxResults(pageable.getPageSize());
        List<Sector> sectors = typedQuery.getResultList();

        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        Root<Sector> countRoot = countQuery.from(Sector.class);
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

            countQuery.where(cb.and(countPredicates.toArray(new Predicate[0])));
        }

        Long total = entityManager.createQuery(countQuery).getSingleResult();

        return new PageImpl<>(sectors, pageable, total);
    }

    public boolean existsByCode(String code) {
        TypedQuery<Long> query = entityManager.createQuery(
                "SELECT COUNT(s) FROM Sector s WHERE s.code = :code", Long.class);
        query.setParameter("code", code);
        return query.getSingleResult() > 0;
    }

    public boolean existsByName(String name) {
        TypedQuery<Long> query = entityManager.createQuery(
                "SELECT COUNT(s) FROM Sector s WHERE s.name = :name", Long.class);
        query.setParameter("name", name);
        return query.getSingleResult() > 0;
    }

    public boolean activateSector(Integer sectorId) {
        Sector sector = entityManager.find(Sector.class, sectorId);
        if (sector != null) {
            sector.setIsActive(true);
            sector.setUpdatedAt(LocalDateTime.now());
            entityManager.merge(sector);
            return true;
        }
        return false;
    }

    public boolean deactivateSector(Integer sectorId) {
        Sector sector = entityManager.find(Sector.class, sectorId);
        if (sector != null) {
            sector.setIsActive(false);
            sector.setUpdatedAt(LocalDateTime.now());
            entityManager.merge(sector);
            return true;
        }
        return false;
    }
}