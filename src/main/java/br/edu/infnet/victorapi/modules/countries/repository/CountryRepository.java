package br.edu.infnet.victorapi.modules.countries.repository;

import br.edu.infnet.victorapi.modules.countries.entity.Country;
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
public class CountryRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public Country save(Country country) {
        if (country.getId() == null) {
            entityManager.persist(country);
            return country;
        } else {
            return entityManager.merge(country);
        }
    }

    public Optional<Country> findById(Integer id) {
        Country country = entityManager.find(Country.class, id);
        return Optional.ofNullable(country);
    }

    public List<Country> findAll() {
        TypedQuery<Country> query = entityManager.createQuery(
                "SELECT c FROM Country c ORDER BY c.name", Country.class);
        return query.getResultList();
    }

    public void deleteById(Integer id) {
        Country country = entityManager.find(Country.class, id);
        if (country != null) {
            entityManager.remove(country);
        }
    }

    public boolean existsById(Integer id) {
        return findById(id).isPresent();
    }

    public long count() {
        TypedQuery<Long> query = entityManager.createQuery(
                "SELECT COUNT(c) FROM Country c", Long.class);
        return query.getSingleResult();
    }

    public Page<Country> findCountriesWithFilters(String name, String code2, String code3,
                                                 String currencyCode, Boolean isActive, Pageable pageable) {
        
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Country> query = cb.createQuery(Country.class);
        Root<Country> root = query.from(Country.class);

        List<Predicate> predicates = new ArrayList<>();

        if (name != null && !name.trim().isEmpty()) {
            predicates.add(cb.like(cb.lower(root.get("name")),
                    "%" + name.toLowerCase() + "%"));
        }

        if (code2 != null && !code2.trim().isEmpty()) {
            predicates.add(cb.like(cb.lower(root.get("code2")),
                    "%" + code2.toLowerCase() + "%"));
        }

        if (code3 != null && !code3.trim().isEmpty()) {
            predicates.add(cb.like(cb.lower(root.get("code3")),
                    "%" + code3.toLowerCase() + "%"));
        }

        if (currencyCode != null && !currencyCode.trim().isEmpty()) {
            predicates.add(cb.equal(root.get("currencyCode"), currencyCode));
        }

        if (isActive != null) {
            predicates.add(cb.equal(root.get("isActive"), isActive));
        }

        query.where(cb.and(predicates.toArray(new Predicate[0])));
        query.orderBy(cb.asc(root.get("name")));

        TypedQuery<Country> typedQuery = entityManager.createQuery(query);
        typedQuery.setFirstResult((int) pageable.getOffset());
        typedQuery.setMaxResults(pageable.getPageSize());
        List<Country> countries = typedQuery.getResultList();

        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        Root<Country> countRoot = countQuery.from(Country.class);
        countQuery.select(cb.count(countRoot));

        if (!predicates.isEmpty()) {
            List<Predicate> countPredicates = new ArrayList<>();

            if (name != null && !name.trim().isEmpty()) {
                countPredicates.add(cb.like(cb.lower(countRoot.get("name")),
                        "%" + name.toLowerCase() + "%"));
            }

            if (code2 != null && !code2.trim().isEmpty()) {
                countPredicates.add(cb.like(cb.lower(countRoot.get("code2")),
                        "%" + code2.toLowerCase() + "%"));
            }

            if (code3 != null && !code3.trim().isEmpty()) {
                countPredicates.add(cb.like(cb.lower(countRoot.get("code3")),
                        "%" + code3.toLowerCase() + "%"));
            }

            if (currencyCode != null && !currencyCode.trim().isEmpty()) {
                countPredicates.add(cb.equal(countRoot.get("currencyCode"), currencyCode));
            }

            if (isActive != null) {
                countPredicates.add(cb.equal(countRoot.get("isActive"), isActive));
            }

            countQuery.where(cb.and(countPredicates.toArray(new Predicate[0])));
        }

        Long total = entityManager.createQuery(countQuery).getSingleResult();

        return new PageImpl<>(countries, pageable, total);
    }

    public List<Country> findByNameContaining(String name) {
        TypedQuery<Country> query = entityManager.createQuery(
                "SELECT c FROM Country c WHERE LOWER(c.name) LIKE LOWER(CONCAT('%', :name, '%')) " +
                        "ORDER BY c.name", Country.class);
        query.setParameter("name", name);
        return query.getResultList();
    }
}
