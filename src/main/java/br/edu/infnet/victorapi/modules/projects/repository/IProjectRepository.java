package br.edu.infnet.victorapi.modules.projects.repository;

import br.edu.infnet.victorapi.modules.projects.entity.Project;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface IProjectRepository extends JpaRepository<Project, Long> {

    @Query("SELECT p FROM Project p WHERE p.name = :name")
    Optional<Project> findByName(@Param("name") String name);

    @Query("SELECT p FROM Project p ORDER BY p.name ASC")
    List<Project> findAllByActiveTrueOrderByName();

    @Query("SELECT p FROM Project p ORDER BY p.name ASC")
    Page<Project> findAllByActiveTrueOrderByName(Pageable pageable);

    @Query("SELECT COUNT(p) FROM Project p WHERE p.name = :name")
    Long countByNameAndActiveTrue(@Param("name") String name);

    @Query("SELECT COUNT(p) FROM Project p WHERE p.name = :name AND p.id != :id")
    Long countByNameAndIdNotAndActiveTrue(@Param("name") String name, @Param("id") Long id);

    @Query("SELECT p FROM Project p WHERE p.projectTypesId = :projectTypeId ORDER BY p.name ASC")
    List<Project> findByProjectTypeIdAndActiveTrue(@Param("projectTypeId") Integer projectTypeId);

    @Query("SELECT p FROM Project p WHERE p.projectStatusesId = :projectStatusId ORDER BY p.name ASC")
    List<Project> findByProjectStatusIdAndActiveTrue(@Param("projectStatusId") Integer projectStatusId);

    @Query("SELECT p FROM Project p WHERE p.originProjectsId = :parentId ORDER BY p.name ASC")
    List<Project> findByParentProjectIdAndActiveTrue(@Param("parentId") Integer parentId);
}
