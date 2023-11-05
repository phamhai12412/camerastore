package ra.security.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ra.security.model.domain.Promotion;

import java.util.Optional;

public interface PromotionRepository extends JpaRepository<Promotion,Long> {
    Optional<Promotion> findByPromotionCode(String promotionCode);
}
