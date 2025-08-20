package br.edu.infnet.victorapi.modules.users.repository;

import br.edu.infnet.victorapi.modules.users.entity.User;
import br.edu.infnet.victorapi.modules.users.dto.UserRole;
import br.edu.infnet.victorapi.modules.users.dto.UserInfoDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface IUserRepository extends JpaRepository<User, Integer> {

    Optional<User> findByEmail(String email);

    @Query("SELECT u FROM User u WHERE u.email = :email AND u.isActive = true")
    UserDetails findByEmailForAuth(@Param("email") String email);

    boolean existsByEmail(String email);

    List<User> findByIsActiveTrue();

    List<User> findByDepartmentId(Integer departmentId);

    List<User> findByRole(UserRole role);

    Page<User> findByIsActiveTrue(Pageable pageable);

    @Query("SELECT u FROM User u WHERE LOWER(u.name) LIKE LOWER(CONCAT('%', :name, '%')) AND u.isActive = true")
    List<User> findByNameContainingIgnoreCase(@Param("name") String name);

    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.passwordHash = :passwordHash, u.updatedAt = :updatedAt WHERE u.email = :email")
    int updatePassword(@Param("passwordHash") String passwordHash,
                       @Param("email") String email,
                       @Param("updatedAt") LocalDateTime updatedAt);

    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.lastLogin = :lastLogin WHERE u.email = :email")
    int updateLastLogin(@Param("email") String email, @Param("lastLogin") LocalDateTime lastLogin);

    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.isActive = false, u.updatedAt = :updatedAt WHERE u.id = :id")
    int deactivateUser(@Param("id") Integer id, @Param("updatedAt") LocalDateTime updatedAt);

    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.isActive = true, u.updatedAt = :updatedAt WHERE u.id = :id")
    int activateUser(@Param("id") Integer id, @Param("updatedAt") LocalDateTime updatedAt);

    @Query("SELECT new br.edu.infnet.victorapi.modules.users.dto.UserInfoDTO(" +
            "u.id, u.name, u.email, u.phone, u.position, u.isActive, " +
            "u.lastLogin, u.createdAt, u.role, CAST(null as string)) " +
            "FROM User u " +
            "WHERE u.email = :email")
    Optional<UserInfoDTO> findUserInfoByEmail(@Param("email") String email);

    @Query("SELECT COUNT(u) FROM User u WHERE u.departmentId = :departmentId AND u.isActive = true")
    Long countActiveUsersByDepartment(@Param("departmentId") Integer departmentId);

    @Query("SELECT u FROM User u WHERE u.createdAt BETWEEN :startDate AND :endDate ORDER BY u.createdAt DESC")
    List<User> findUsersCreatedBetween(@Param("startDate") LocalDateTime startDate,
                                       @Param("endDate") LocalDateTime endDate);

    @Query("SELECT u FROM User u WHERE u.lastLogin < :cutoffDate OR u.lastLogin IS NULL ORDER BY u.lastLogin ASC")
    List<User> findInactiveUsers(@Param("cutoffDate") LocalDateTime cutoffDate);
}