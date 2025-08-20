package br.edu.infnet.victorapi.modules.offices.repository;

import br.edu.infnet.victorapi.modules.offices.entity.Offices;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IOfficesRepository extends JpaRepository<Offices, Integer> {

    @Query("SELECT o FROM Offices o WHERE o.isActive = true")
    List<Offices> findAllActive();

    @Query("SELECT o FROM Offices o WHERE o.isActive = true")
    Page<Offices> findAllActive(Pageable pageable);

    @Query("SELECT o FROM Offices o WHERE o.id = :id AND o.isActive = true")
    Optional<Offices> findByIdAndActive(@Param("id") Integer id);

    @Query("SELECT o FROM Offices o WHERE o.code = :code AND o.isActive = true")
    Optional<Offices> findByCodeAndActive(@Param("code") String code);

    @Query("SELECT o FROM Offices o WHERE o.email = :email AND o.isActive = true")
    Optional<Offices> findByEmailAndActive(@Param("email") String email);

    @Query("SELECT o FROM Offices o WHERE o.isMainOffice = true AND o.isActive = true")
    Optional<Offices> findMainOffice();

    @Query("SELECT o FROM Offices o WHERE o.countryId = :countryId AND o.isActive = true")
    List<Offices> findByCountry(@Param("countryId") Integer countryId);

    @Query("SELECT o FROM Offices o WHERE o.city = :city AND o.isActive = true")
    List<Offices> findByCity(@Param("city") String city);

    @Query("SELECT o FROM Offices o WHERE LOWER(o.name) LIKE LOWER(CONCAT('%', :name, '%')) AND o.isActive = true")
    List<Offices> findByNameContainingIgnoreCaseAndActive(@Param("name") String name);

    boolean existsByCodeAndIsActiveTrue(String code);

    boolean existsByEmailAndIsActiveTrue(String email);

    boolean existsByCodeAndIdNotAndIsActiveTrue(String code, Integer id);

    boolean existsByEmailAndIdNotAndIsActiveTrue(String email, Integer id);

    boolean existsByIsMainOfficeAndIsActiveTrueAndIdNot(Boolean isMainOffice, Integer id);
}
