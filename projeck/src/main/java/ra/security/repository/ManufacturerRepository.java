package ra.security.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ra.security.model.domain.Category;
import ra.security.model.domain.Manufacturer;

public interface ManufacturerRepository extends JpaRepository<Manufacturer,Long> {
    Page<Manufacturer> findAllByNameContainingIgnoreCase(String name, Pageable pageable);
    Manufacturer findByName(String name);

}
