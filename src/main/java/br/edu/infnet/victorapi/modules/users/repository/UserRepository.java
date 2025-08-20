package br.edu.infnet.victorapi.modules.users.repository;

import br.edu.infnet.victorapi.modules.users.entity.User;
import br.edu.infnet.victorapi.modules.users.dto.UserRole;
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
import java.util.Optional;

@Repository
@Transactional
public class UserRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public User save(User user) {
        if (user.getId() == null) {
            entityManager.persist(user);
            return user;
        } else {
            return entityManager.merge(user);
        }
    }

    public Optional<User> findById(Integer id) {
        User user = entityManager.find(User.class, id);
        return Optional.ofNullable(user);
    }

    public List<User> findAll() {
        TypedQuery<User> query = entityManager.createQuery(
                "SELECT u FROM User u WHERE u.isActive = true ORDER BY u.name", User.class);
        return query.getResultList();
    }

    public void deleteById(Integer id) {
        User user = entityManager.find(User.class, id);
        if (user != null) {
            user.setIsActive(false);
            user.setUpdatedAt(LocalDateTime.now());
            entityManager.merge(user);
        }
    }

    public boolean existsById(Integer id) {
        return findById(id).isPresent();
    }

    public long count() {
        TypedQuery<Long> query = entityManager.createQuery(
                "SELECT COUNT(u) FROM User u WHERE u.isActive = true", Long.class);
        return query.getSingleResult();
    }

    public Optional<User> findByEmail(String email) {
        TypedQuery<User> query = entityManager.createQuery(
                "SELECT u FROM User u WHERE u.email = :email AND u.isActive = true", User.class);
        query.setParameter("email", email);
        List<User> results = query.getResultList();
        return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
    }

    public List<User> findByNameContaining(String name) {
        TypedQuery<User> query = entityManager.createQuery(
                "SELECT u FROM User u WHERE LOWER(u.name) LIKE LOWER(CONCAT('%', :name, '%')) " +
                        "AND u.isActive = true ORDER BY u.name", User.class);
        query.setParameter("name", name);
        return query.getResultList();
    }

    public List<User> findByDepartmentId(Integer departmentId) {
        TypedQuery<User> query = entityManager.createQuery(
                "SELECT u FROM User u WHERE u.departmentId = :departmentId AND u.isActive = true " +
                        "ORDER BY u.name", User.class);
        query.setParameter("departmentId", departmentId);
        return query.getResultList();
    }

    public List<User> findByRole(UserRole role) {
        TypedQuery<User> query = entityManager.createQuery(
                "SELECT u FROM User u WHERE u.role = :role AND u.isActive = true " +
                        "ORDER BY u.name", User.class);
        query.setParameter("role", role);
        return query.getResultList();
    }

    public Page<User> findUsersWithFilters(String name, Integer departmentId,
                                           UserRole role, Boolean isActive,
                                           Pageable pageable) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<User> query = cb.createQuery(User.class);
        Root<User> root = query.from(User.class);

        List<Predicate> predicates = new ArrayList<>();

        if (name != null && !name.trim().isEmpty()) {
            predicates.add(cb.like(cb.lower(root.get("name")),
                    "%" + name.toLowerCase() + "%"));
        }

        if (departmentId != null) {
            predicates.add(cb.equal(root.get("departmentId"), departmentId));
        }

        if (role != null) {
            predicates.add(cb.equal(root.get("role"), role));
        }

        if (isActive != null) {
            predicates.add(cb.equal(root.get("isActive"), isActive));
        } else {
            predicates.add(cb.equal(root.get("isActive"), true));
        }

        if (!predicates.isEmpty()) {
            query.where(cb.and(predicates.toArray(new Predicate[0])));
        }

        query.orderBy(cb.asc(root.get("name")));

        TypedQuery<User> typedQuery = entityManager.createQuery(query);
        typedQuery.setFirstResult((int) pageable.getOffset());
        typedQuery.setMaxResults(pageable.getPageSize());
        List<User> users = typedQuery.getResultList();

        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        Root<User> countRoot = countQuery.from(User.class);
        countQuery.select(cb.count(countRoot));

        if (!predicates.isEmpty()) {
            List<Predicate> countPredicates = new ArrayList<>();

            if (name != null && !name.trim().isEmpty()) {
                countPredicates.add(cb.like(cb.lower(countRoot.get("name")),
                        "%" + name.toLowerCase() + "%"));
            }
            if (departmentId != null) {
                countPredicates.add(cb.equal(countRoot.get("departmentId"), departmentId));
            }
            if (role != null) {
                countPredicates.add(cb.equal(countRoot.get("role"), role));
            }
            if (isActive != null) {
                countPredicates.add(cb.equal(countRoot.get("isActive"), isActive));
            } else {
                countPredicates.add(cb.equal(countRoot.get("isActive"), true));
            }

            countQuery.where(cb.and(countPredicates.toArray(new Predicate[0])));
        }

        Long total = entityManager.createQuery(countQuery).getSingleResult();

        return new PageImpl<>(users, pageable, total);
    }

    public boolean updatePassword(String email, String newPasswordHash) {
        TypedQuery<User> query = entityManager.createQuery(
                "SELECT u FROM User u WHERE u.email = :email AND u.isActive = true", User.class);
        query.setParameter("email", email);

        List<User> users = query.getResultList();
        if (!users.isEmpty()) {
            User user = users.get(0);
            user.setPasswordHash(newPasswordHash);
            user.setUpdatedAt(LocalDateTime.now());
            entityManager.merge(user);
            return true;
        }
        return false;
    }

    public boolean updateLastLogin(String email) {
        TypedQuery<User> query = entityManager.createQuery(
                "SELECT u FROM User u WHERE u.email = :email AND u.isActive = true", User.class);
        query.setParameter("email", email);

        List<User> users = query.getResultList();
        if (!users.isEmpty()) {
            User user = users.get(0);
            user.setLastLogin(LocalDateTime.now());
            entityManager.merge(user);
            return true;
        }
        return false;
    }

    public boolean existsByEmail(String email) {
        TypedQuery<Long> query = entityManager.createQuery(
                "SELECT COUNT(u) FROM User u WHERE u.email = :email", Long.class);
        query.setParameter("email", email);
        return query.getSingleResult() > 0;
    }

    public List<User> findInactiveUsers(LocalDateTime cutoffDate) {
        TypedQuery<User> query = entityManager.createQuery(
                "SELECT u FROM User u WHERE (u.lastLogin < :cutoffDate OR u.lastLogin IS NULL) " +
                        "AND u.isActive = true ORDER BY u.lastLogin ASC", User.class);
        query.setParameter("cutoffDate", cutoffDate);
        return query.getResultList();
    }

    public Long countByDepartment(Integer departmentId) {
        TypedQuery<Long> query = entityManager.createQuery(
                "SELECT COUNT(u) FROM User u WHERE u.departmentId = :departmentId AND u.isActive = true",
                Long.class);
        query.setParameter("departmentId", departmentId);
        return query.getSingleResult();
    }

    public Long countByRole(UserRole role) {
        TypedQuery<Long> query = entityManager.createQuery(
                "SELECT COUNT(u) FROM User u WHERE u.role = :role AND u.isActive = true", Long.class);
        query.setParameter("role", role);
        return query.getSingleResult();
    }

    public boolean activateUser(Integer userId) {
        User user = entityManager.find(User.class, userId);
        if (user != null) {
            user.setIsActive(true);
            user.setUpdatedAt(LocalDateTime.now());
            entityManager.merge(user);
            return true;
        }
        return false;
    }

    public boolean deactivateUser(Integer userId) {
        User user = entityManager.find(User.class, userId);
        if (user != null) {
            user.setIsActive(false);
            user.setUpdatedAt(LocalDateTime.now());
            entityManager.merge(user);
            return true;
        }
        return false;
    }
}