package br.edu.infnet.victorapi.modules.proposals.repository;

import br.edu.infnet.victorapi.modules.proposals.entity.Proposals;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface IProposalRepository extends JpaRepository<Proposals, Integer> {

    Optional<Proposals> findByProposalNumber(String proposalNumber);

    boolean existsByProposalNumber(String proposalNumber);

    @Query("SELECT p FROM Proposals p WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%')) ORDER BY p.createdAt DESC")
    List<Proposals> findByNameContainingIgnoreCase(@Param("name") String name);

    @Query("SELECT p FROM Proposals p WHERE p.createdAt BETWEEN :startDate AND :endDate ORDER BY p.createdAt DESC")
    List<Proposals> findProposalsCreatedBetween(@Param("startDate") LocalDateTime startDate,
                                               @Param("endDate") LocalDateTime endDate);

    @Query("SELECT p FROM Proposals p WHERE p.responsibleId = :responsibleId ORDER BY p.createdAt DESC")
    List<Proposals> findByResponsibleId(@Param("responsibleId") Integer responsibleId);

    @Query("SELECT p FROM Proposals p WHERE p.statusId = :statusId ORDER BY p.createdAt DESC")
    List<Proposals> findByStatusId(@Param("statusId") Integer statusId);

    @Query("SELECT p FROM Proposals p WHERE p.departmentId = :departmentId ORDER BY p.createdAt DESC")
    List<Proposals> findByDepartmentId(@Param("departmentId") Integer departmentId);

    @Query("SELECT p FROM Proposals p WHERE p.clientSupplierId = :clientSupplierId ORDER BY p.createdAt DESC")
    List<Proposals> findByClientSupplierId(@Param("clientSupplierId") Integer clientSupplierId);

    @Query("SELECT p FROM Proposals p WHERE p.value BETWEEN :minValue AND :maxValue ORDER BY p.value DESC")
    List<Proposals> findByValueBetween(@Param("minValue") BigDecimal minValue, @Param("maxValue") BigDecimal maxValue);

    @Query("SELECT p FROM Proposals p WHERE p.estimatedStart BETWEEN :startDate AND :endDate ORDER BY p.estimatedStart ASC")
    List<Proposals> findByEstimatedStartBetween(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    @Query("SELECT p FROM Proposals p WHERE p.priority >= :minPriority ORDER BY p.priority DESC, p.createdAt DESC")
    List<Proposals> findByPriorityGreaterThanEqual(@Param("minPriority") Integer minPriority);

    @Query("SELECT COUNT(p) FROM Proposals p")
    Long countAllProposals();

    @Query("SELECT SUM(p.value) FROM Proposals p WHERE p.value IS NOT NULL")
    BigDecimal sumAllProposalValues();

    @Query("SELECT p FROM Proposals p WHERE p.originProposalId = :originProposalId ORDER BY p.createdAt DESC")
    List<Proposals> findByOriginProposalId(@Param("originProposalId") Integer originProposalId);

    @Query("SELECT p FROM Proposals p ORDER BY p.createdAt DESC")
    Page<Proposals> findAllOrderByCreatedAtDesc(Pageable pageable);
}
