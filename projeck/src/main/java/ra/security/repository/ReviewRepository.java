package ra.security.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ra.security.exception.ProductException;
import ra.security.model.domain.FavoriteProduct;
import ra.security.model.domain.Product;
import ra.security.model.domain.Review;
import ra.security.model.domain.Users;
import ra.security.model.dto.request.ReviewRequestDto;
import ra.security.model.dto.response.ReviewResponseDto;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review,Long> {

    List<Review> findByUser_Id(Long user_id);

}