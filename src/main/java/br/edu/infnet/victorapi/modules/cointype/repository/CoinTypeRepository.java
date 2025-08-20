package br.edu.infnet.victorapi.modules.cointype.repository;

import br.edu.infnet.victorapi.modules.cointype.entity.CoinType;
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
public class CoinTypeRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public CoinType save(CoinType coinType) {
        if (coinType.getId() == null) {
            entityManager.persist(coinType);
            return coinType;
        } else {
            return entityManager.merge(coinType);
        }
    }

    public Optional<CoinType> findById(Integer id) {
        CoinType coinType = entityManager.find(CoinType.class, id);
        return Optional.ofNullable(coinType);
    }

    public List<CoinType> findAll() {
        TypedQuery<CoinType> query = entityManager.createQuery(
                "SELECT c FROM CoinType c ORDER BY c.name", CoinType.class);
        return query.getResultList();
    }

    public void deleteById(Integer id) {
        CoinType coinType = entityManager.find(CoinType.class, id);
        if (coinType != null) {
            entityManager.remove(coinType);
        }
    }

    public boolean existsById(Integer id) {
        return findById(id).isPresent();
    }

    public long count() {
        TypedQuery<Long> query = entityManager.createQuery(
                "SELECT COUNT(c) FROM CoinType c", Long.class);
        return query.getSingleResult();
    }

    public Page<CoinType> findCoinTypesWithFilters(String name, String code, String symbol,
                                                  Boolean isActive, Pageable pageable) {
        
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<CoinType> query = cb.createQuery(CoinType.class);
        Root<CoinType> root = query.from(CoinType.class);

        List<Predicate> predicates = new ArrayList<>();

        if (name != null && !name.trim().isEmpty()) {
            predicates.add(cb.like(cb.lower(root.get("name")),
                    "%" + name.toLowerCase() + "%"));
        }

        if (code != null && !code.trim().isEmpty()) {
            predicates.add(cb.like(cb.lower(root.get("code")),
                    "%" + code.toLowerCase() + "%"));
        }

        if (symbol != null && !symbol.trim().isEmpty()) {
            predicates.add(cb.like(cb.lower(root.get("symbol")),
                    "%" + symbol.toLowerCase() + "%"));
        }

        if (isActive != null) {
            predicates.add(cb.equal(root.get("isActive"), isActive));
        }

        query.where(cb.and(predicates.toArray(new Predicate[0])));
        query.orderBy(cb.asc(root.get("name")));

        TypedQuery<CoinType> typedQuery = entityManager.createQuery(query);
        typedQuery.setFirstResult((int) pageable.getOffset());
        typedQuery.setMaxResults(pageable.getPageSize());
        List<CoinType> coinTypes = typedQuery.getResultList();

        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        Root<CoinType> countRoot = countQuery.from(CoinType.class);
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

            if (symbol != null && !symbol.trim().isEmpty()) {
                countPredicates.add(cb.like(cb.lower(countRoot.get("symbol")),
                        "%" + symbol.toLowerCase() + "%"));
            }

            if (isActive != null) {
                countPredicates.add(cb.equal(countRoot.get("isActive"), isActive));
            }

            countQuery.where(cb.and(countPredicates.toArray(new Predicate[0])));
        }

        Long total = entityManager.createQuery(countQuery).getSingleResult();

        return new PageImpl<>(coinTypes, pageable, total);
    }

    public List<CoinType> findByNameContaining(String name) {
        TypedQuery<CoinType> query = entityManager.createQuery(
                "SELECT c FROM CoinType c WHERE LOWER(c.name) LIKE LOWER(CONCAT('%', :name, '%')) " +
                        "ORDER BY c.name", CoinType.class);
        query.setParameter("name", name);
        return query.getResultList();
    }
}
