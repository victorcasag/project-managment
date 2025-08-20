package br.edu.infnet.victorapi.modules.projecttype.repository;

import br.edu.infnet.victorapi.modules.projecttype.entity.ProjectType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface IProjectTypeRepository extends JpaRepository<ProjectType, Long> {

    @Query("SELECT pt FROM ProjectType pt WHERE pt.name = :name AND pt.isActive = true")
    Optional<ProjectType> findByNameAndActiveTrue(@Param("name") String name);

    @Query("SELECT pt FROM ProjectType pt WHERE pt.code = :code AND pt.isActive = true")
    Optional<ProjectType> findByCodeAndActiveTrue(@Param("code") String code);

    @Query("SELECT pt FROM ProjectType pt WHERE pt.isActive = true ORDER BY pt.name ASC")
    List<ProjectType> findAllByActiveTrueOrderByName();

    @Query("SELECT pt FROM ProjectType pt WHERE pt.isActive = true ORDER BY pt.name ASC")
    Page<ProjectType> findAllByActiveTrueOrderByName(Pageable pageable);

    @Query("SELECT COUNT(pt) FROM ProjectType pt WHERE pt.code = :code AND pt.isActive = true")
    Long countByCodeAndActiveTrue(@Param("code") String code);

    @Query("SELECT COUNT(pt) FROM ProjectType pt WHERE pt.code = :code AND pt.id != :id AND pt.isActive = true")
    Long countByCodeAndIdNotAndActiveTrue(@Param("code") String code, @Param("id") Long id);

    @Query("SELECT COUNT(pt) FROM ProjectType pt WHERE pt.name = :name AND pt.isActive = true")
    Long countByNameAndActiveTrue(@Param("name") String name);

    @Query("SELECT COUNT(pt) FROM ProjectType pt WHERE pt.name = :name AND pt.id != :id AND pt.isActive = true")
    Long countByNameAndIdNotAndActiveTrue(@Param("name") String name, @Param("id") Long id);
}
