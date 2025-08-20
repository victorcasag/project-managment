package br.edu.infnet.victorapi.modules.proposalstatus.repository;

import br.edu.infnet.victorapi.modules.proposalstatus.entity.ProposalStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface IProposalStatusRepository extends JpaRepository<ProposalStatus, Integer> {

    Optional<ProposalStatus> findByCode(String code);

    boolean existsByCode(String code);

    boolean existsByName(String name);

    List<ProposalStatus> findByIsActiveTrue();

    Page<ProposalStatus> findByIsActiveTrue(Pageable pageable);

    @Query("SELECT ps FROM ProposalStatus ps WHERE LOWER(ps.name) LIKE LOWER(CONCAT('%', :name, '%')) AND ps.isActive = true ORDER BY ps.sortOrder ASC, ps.name ASC")
    List<ProposalStatus> findByNameContainingIgnoreCase(@Param("name") String name);

    @Modifying
    @Transactional
    @Query("UPDATE ProposalStatus ps SET ps.isActive = false, ps.updatedAt = :updatedAt WHERE ps.id = :id")
    int deactivateProposalStatus(@Param("id") Integer id, @Param("updatedAt") LocalDateTime updatedAt);

    @Modifying
    @Transactional
    @Query("UPDATE ProposalStatus ps SET ps.isActive = true, ps.updatedAt = :updatedAt WHERE ps.id = :id")
    int activateProposalStatus(@Param("id") Integer id, @Param("updatedAt") LocalDateTime updatedAt);

    @Query("SELECT COUNT(ps) FROM ProposalStatus ps WHERE ps.isActive = true")
    Long countActiveProposalStatuses();

    @Query("SELECT ps FROM ProposalStatus ps WHERE ps.createdAt BETWEEN :startDate AND :endDate ORDER BY ps.createdAt DESC")
    List<ProposalStatus> findProposalStatusesCreatedBetween(@Param("startDate") LocalDateTime startDate,
                                                           @Param("endDate") LocalDateTime endDate);

    @Query("SELECT ps FROM ProposalStatus ps WHERE ps.isActive = true ORDER BY ps.sortOrder ASC, ps.name ASC")
    List<ProposalStatus> findAllOrderedBySort();

    @Query("SELECT ps FROM ProposalStatus ps WHERE ps.isInitial = true AND ps.isActive = true")
    List<ProposalStatus> findInitialStatuses();

    @Query("SELECT ps FROM ProposalStatus ps WHERE ps.isFinal = true AND ps.isActive = true")
    List<ProposalStatus> findFinalStatuses();
}
