package br.edu.infnet.victorapi.modules.sector.repository;

import br.edu.infnet.victorapi.modules.sector.entity.Sector;
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
public interface ISectorRepository extends JpaRepository<Sector, Integer> {

    Optional<Sector> findByCode(String code);

    boolean existsByCode(String code);

    boolean existsByName(String name);

    List<Sector> findByIsActiveTrue();

    Page<Sector> findByIsActiveTrue(Pageable pageable);

    @Query("SELECT s FROM Sector s WHERE LOWER(s.name) LIKE LOWER(CONCAT('%', :name, '%')) AND s.isActive = true")
    List<Sector> findByNameContainingIgnoreCase(@Param("name") String name);

    @Modifying
    @Transactional
    @Query("UPDATE Sector s SET s.isActive = false, s.updatedAt = :updatedAt WHERE s.id = :id")
    int deactivateSector(@Param("id") Integer id, @Param("updatedAt") LocalDateTime updatedAt);

    @Modifying
    @Transactional
    @Query("UPDATE Sector s SET s.isActive = true, s.updatedAt = :updatedAt WHERE s.id = :id")
    int activateSector(@Param("id") Integer id, @Param("updatedAt") LocalDateTime updatedAt);

    @Query("SELECT COUNT(s) FROM Sector s WHERE s.isActive = true")
    Long countActiveSectors();

    @Query("SELECT s FROM Sector s WHERE s.createdAt BETWEEN :startDate AND :endDate ORDER BY s.createdAt DESC")
    List<Sector> findSectorsCreatedBetween(@Param("startDate") LocalDateTime startDate,
                                           @Param("endDate") LocalDateTime endDate);
}