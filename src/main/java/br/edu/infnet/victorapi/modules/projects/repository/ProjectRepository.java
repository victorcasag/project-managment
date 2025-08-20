package br.edu.infnet.victorapi.modules.projects.repository;

import br.edu.infnet.victorapi.modules.projects.entity.Project;
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

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Repository
public class ProjectRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public Page<Project> findWithFilters(String name, Integer departmentsId, Integer projectTypesId,
                                       Integer sectorsId, Integer areasId, Integer projectStatusesId,
                                       Integer originProjectsId, Integer countriesId, Integer clientsSuppliersId,
                                       Integer lastProjectStatusesId, Integer coinTypeId, Integer originProposalId,
                                       Boolean billableFl, Boolean internationalFl, String projectDir, String site,
                                       Boolean isDefault, BigDecimal exchangeRateFrom, BigDecimal exchangeRateTo,
                                       String openingEmail, String classification, Boolean investimentFl,
                                       Boolean productFl, Pageable pageable) {
        
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Project> query = cb.createQuery(Project.class);
        Root<Project> root = query.from(Project.class);

        List<Predicate> predicates = new ArrayList<>();

        if (name != null && !name.trim().isEmpty()) {
            predicates.add(cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%"));
        }

        if (departmentsId != null) {
            predicates.add(cb.equal(root.get("departmentsId"), departmentsId));
        }

        if (projectTypesId != null) {
            predicates.add(cb.equal(root.get("projectTypesId"), projectTypesId));
        }

        if (sectorsId != null) {
            predicates.add(cb.equal(root.get("sectorsId"), sectorsId));
        }

        if (areasId != null) {
            predicates.add(cb.equal(root.get("areasId"), areasId));
        }

        if (projectStatusesId != null) {
            predicates.add(cb.equal(root.get("projectStatusesId"), projectStatusesId));
        }

        if (originProjectsId != null) {
            predicates.add(cb.equal(root.get("originProjectsId"), originProjectsId));
        }

        if (countriesId != null) {
            predicates.add(cb.equal(root.get("countriesId"), countriesId));
        }

        if (clientsSuppliersId != null) {
            predicates.add(cb.equal(root.get("clientsSuppliersId"), clientsSuppliersId));
        }

        if (lastProjectStatusesId != null) {
            predicates.add(cb.equal(root.get("lastProjectStatusesId"), lastProjectStatusesId));
        }

        if (coinTypeId != null) {
            predicates.add(cb.equal(root.get("coinTypeId"), coinTypeId));
        }

        if (originProposalId != null) {
            predicates.add(cb.equal(root.get("originProposalId"), originProposalId));
        }

        if (billableFl != null) {
            predicates.add(cb.equal(root.get("billableFl"), billableFl));
        }

        if (internationalFl != null) {
            predicates.add(cb.equal(root.get("internationalFl"), internationalFl));
        }

        if (projectDir != null && !projectDir.trim().isEmpty()) {
            predicates.add(cb.like(cb.lower(root.get("projectDir")), "%" + projectDir.toLowerCase() + "%"));
        }

        if (site != null && !site.trim().isEmpty()) {
            predicates.add(cb.like(cb.lower(root.get("site")), "%" + site.toLowerCase() + "%"));
        }

        if (isDefault != null) {
            predicates.add(cb.equal(root.get("isDefault"), isDefault));
        }

        if (exchangeRateFrom != null) {
            predicates.add(cb.greaterThanOrEqualTo(root.get("exchangeRate"), exchangeRateFrom));
        }

        if (exchangeRateTo != null) {
            predicates.add(cb.lessThanOrEqualTo(root.get("exchangeRate"), exchangeRateTo));
        }

        if (openingEmail != null && !openingEmail.trim().isEmpty()) {
            predicates.add(cb.like(cb.lower(root.get("openingEmail")), "%" + openingEmail.toLowerCase() + "%"));
        }

        if (classification != null && !classification.trim().isEmpty()) {
            predicates.add(cb.like(cb.lower(root.get("classification")), "%" + classification.toLowerCase() + "%"));
        }

        if (investimentFl != null) {
            predicates.add(cb.equal(root.get("investimentFl"), investimentFl));
        }

        if (productFl != null) {
            predicates.add(cb.equal(root.get("productFl"), productFl));
        }

        query.where(predicates.toArray(new Predicate[0]));
        query.orderBy(cb.asc(root.get("name")));

        List<Project> result = entityManager.createQuery(query)
                .setFirstResult((int) pageable.getOffset())
                .setMaxResults(pageable.getPageSize())
                .getResultList();

        long total = countWithFilters(name, departmentsId, projectTypesId, sectorsId, areasId, projectStatusesId,
                originProjectsId, countriesId, clientsSuppliersId, lastProjectStatusesId, coinTypeId, originProposalId,
                billableFl, internationalFl, projectDir, site, isDefault, exchangeRateFrom, exchangeRateTo,
                openingEmail, classification, investimentFl, productFl);

        return new PageImpl<>(result, pageable, total);
    }

    private long countWithFilters(String name, Integer departmentsId, Integer projectTypesId,
                                Integer sectorsId, Integer areasId, Integer projectStatusesId,
                                Integer originProjectsId, Integer countriesId, Integer clientsSuppliersId,
                                Integer lastProjectStatusesId, Integer coinTypeId, Integer originProposalId,
                                Boolean billableFl, Boolean internationalFl, String projectDir, String site,
                                Boolean isDefault, BigDecimal exchangeRateFrom, BigDecimal exchangeRateTo,
                                String openingEmail, String classification, Boolean investimentFl,
                                Boolean productFl) {
        
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        Root<Project> root = countQuery.from(Project.class);

        List<Predicate> predicates = new ArrayList<>();

        if (name != null && !name.trim().isEmpty()) {
            predicates.add(cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%"));
        }

        if (departmentsId != null) {
            predicates.add(cb.equal(root.get("departmentsId"), departmentsId));
        }

        if (projectTypesId != null) {
            predicates.add(cb.equal(root.get("projectTypesId"), projectTypesId));
        }

        if (sectorsId != null) {
            predicates.add(cb.equal(root.get("sectorsId"), sectorsId));
        }

        if (areasId != null) {
            predicates.add(cb.equal(root.get("areasId"), areasId));
        }

        if (projectStatusesId != null) {
            predicates.add(cb.equal(root.get("projectStatusesId"), projectStatusesId));
        }

        if (originProjectsId != null) {
            predicates.add(cb.equal(root.get("originProjectsId"), originProjectsId));
        }

        if (countriesId != null) {
            predicates.add(cb.equal(root.get("countriesId"), countriesId));
        }

        if (clientsSuppliersId != null) {
            predicates.add(cb.equal(root.get("clientsSuppliersId"), clientsSuppliersId));
        }

        if (lastProjectStatusesId != null) {
            predicates.add(cb.equal(root.get("lastProjectStatusesId"), lastProjectStatusesId));
        }

        if (coinTypeId != null) {
            predicates.add(cb.equal(root.get("coinTypeId"), coinTypeId));
        }

        if (originProposalId != null) {
            predicates.add(cb.equal(root.get("originProposalId"), originProposalId));
        }

        if (billableFl != null) {
            predicates.add(cb.equal(root.get("billableFl"), billableFl));
        }

        if (internationalFl != null) {
            predicates.add(cb.equal(root.get("internationalFl"), internationalFl));
        }

        if (projectDir != null && !projectDir.trim().isEmpty()) {
            predicates.add(cb.like(cb.lower(root.get("projectDir")), "%" + projectDir.toLowerCase() + "%"));
        }

        if (site != null && !site.trim().isEmpty()) {
            predicates.add(cb.like(cb.lower(root.get("site")), "%" + site.toLowerCase() + "%"));
        }

        if (isDefault != null) {
            predicates.add(cb.equal(root.get("isDefault"), isDefault));
        }

        if (exchangeRateFrom != null) {
            predicates.add(cb.greaterThanOrEqualTo(root.get("exchangeRate"), exchangeRateFrom));
        }

        if (exchangeRateTo != null) {
            predicates.add(cb.lessThanOrEqualTo(root.get("exchangeRate"), exchangeRateTo));
        }

        if (openingEmail != null && !openingEmail.trim().isEmpty()) {
            predicates.add(cb.like(cb.lower(root.get("openingEmail")), "%" + openingEmail.toLowerCase() + "%"));
        }

        if (classification != null && !classification.trim().isEmpty()) {
            predicates.add(cb.like(cb.lower(root.get("classification")), "%" + classification.toLowerCase() + "%"));
        }

        if (investimentFl != null) {
            predicates.add(cb.equal(root.get("investimentFl"), investimentFl));
        }

        if (productFl != null) {
            predicates.add(cb.equal(root.get("productFl"), productFl));
        }

        countQuery.select(cb.count(root));
        countQuery.where(predicates.toArray(new Predicate[0]));

        return entityManager.createQuery(countQuery).getSingleResult();
    }
}
