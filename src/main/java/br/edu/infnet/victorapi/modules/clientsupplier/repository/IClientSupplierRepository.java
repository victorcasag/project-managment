package br.edu.infnet.victorapi.modules.clientsupplier.repository;

import br.edu.infnet.victorapi.modules.clientsupplier.entity.ClientSupplier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IClientSupplierRepository extends JpaRepository<ClientSupplier, Integer> {

    @Query("SELECT cs FROM ClientSupplier cs WHERE cs.isActive = true")
    List<ClientSupplier> findAllActive();

    @Query("SELECT cs FROM ClientSupplier cs WHERE cs.isActive = true")
    Page<ClientSupplier> findAllActive(Pageable pageable);

    @Query("SELECT cs FROM ClientSupplier cs WHERE cs.id = :id AND cs.isActive = true")
    Optional<ClientSupplier> findByIdAndActive(@Param("id") Integer id);

    @Query("SELECT cs FROM ClientSupplier cs WHERE cs.document = :document AND cs.isActive = true")
    Optional<ClientSupplier> findByDocumentAndActive(@Param("document") String document);

    @Query("SELECT cs FROM ClientSupplier cs WHERE cs.email = :email AND cs.isActive = true")
    Optional<ClientSupplier> findByEmailAndActive(@Param("email") String email);

    @Query("SELECT cs FROM ClientSupplier cs WHERE cs.type = :type AND cs.isActive = true")
    List<ClientSupplier> findByType(@Param("type") String type);

    @Query("SELECT cs FROM ClientSupplier cs WHERE cs.countryId = :countryId AND cs.isActive = true")
    List<ClientSupplier> findByCountry(@Param("countryId") Integer countryId);

    @Query("SELECT cs FROM ClientSupplier cs WHERE cs.city = :city AND cs.isActive = true")
    List<ClientSupplier> findByCity(@Param("city") String city);

    boolean existsByDocumentAndIsActiveTrue(String document);

    boolean existsByEmailAndIsActiveTrue(String email);

    boolean existsByDocumentAndIdNotAndIsActiveTrue(String document, Integer id);

    boolean existsByEmailAndIdNotAndIsActiveTrue(String email, Integer id);
}
