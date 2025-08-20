package br.edu.infnet.victorapi.modules.projectstatus.repository;

import br.edu.infnet.victorapi.modules.projectstatus.entity.ProjectStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface IProjectStatusRepository extends JpaRepository<ProjectStatus, Long> {

    @Query("SELECT ps FROM ProjectStatus ps WHERE ps.name = :name AND ps.isActive = true")
    Optional<ProjectStatus> findByNameAndActiveTrue(@Param("name") String name);

    @Query("SELECT ps FROM ProjectStatus ps WHERE ps.code = :code AND ps.isActive = true")
    Optional<ProjectStatus> findByCodeAndActiveTrue(@Param("code") String code);

    @Query("SELECT ps FROM ProjectStatus ps WHERE ps.isActive = true ORDER BY ps.sortOrder ASC, ps.name ASC")
    List<ProjectStatus> findAllByActiveTrueOrderBySortOrderAndName();

    @Query("SELECT ps FROM ProjectStatus ps WHERE ps.isActive = true ORDER BY ps.sortOrder ASC, ps.name ASC")
    Page<ProjectStatus> findAllByActiveTrueOrderBySortOrderAndName(Pageable pageable);

    @Query("SELECT COUNT(ps) FROM ProjectStatus ps WHERE ps.code = :code AND ps.isActive = true")
    Long countByCodeAndActiveTrue(@Param("code") String code);

    @Query("SELECT COUNT(ps) FROM ProjectStatus ps WHERE ps.code = :code AND ps.id != :id AND ps.isActive = true")
    Long countByCodeAndIdNotAndActiveTrue(@Param("code") String code, @Param("id") Long id);

    @Query("SELECT COUNT(ps) FROM ProjectStatus ps WHERE ps.name = :name AND ps.isActive = true")
    Long countByNameAndActiveTrue(@Param("name") String name);

    @Query("SELECT COUNT(ps) FROM ProjectStatus ps WHERE ps.name = :name AND ps.id != :id AND ps.isActive = true")
    Long countByNameAndIdNotAndActiveTrue(@Param("name") String name, @Param("id") Long id);

    @Query("SELECT ps FROM ProjectStatus ps WHERE ps.isInitial = true AND ps.isActive = true")
    Optional<ProjectStatus> findInitialStatus();

    @Query("SELECT ps FROM ProjectStatus ps WHERE ps.isFinal = true AND ps.isActive = true ORDER BY ps.sortOrder ASC")
    List<ProjectStatus> findFinalStatuses();
}
