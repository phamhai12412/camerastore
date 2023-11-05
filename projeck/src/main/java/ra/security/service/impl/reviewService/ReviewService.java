package ra.security.service.impl.reviewService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ra.security.exception.CartItemException;
import ra.security.exception.ProductException;
import ra.security.model.domain.*;
import ra.security.model.dto.request.ReviewRequestDto;
import ra.security.model.dto.response.PromotionResponseDTO;
import ra.security.model.dto.response.ReviewResponseDto;
import ra.security.repository.IUserRepository;
import ra.security.repository.OrderRepository;
import ra.security.repository.ProductRepository;
import ra.security.repository.ReviewRepository; // Assuming you have a ReviewRepository
import ra.security.service.IReviewService;

import javax.persistence.EntityNotFoundException;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReviewService implements IReviewService {
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private IUserRepository userRepository;
    @Autowired
    private ReviewRepository reviewRepository; //
@Autowired
private OrderRepository orderRepository;
    @Override

    public List<ReviewResponseDto> getAllReviews(String username) throws ProductException {
        Users users = null;
        if (username != null) {
            users = userRepository.findByUsername(username).orElseThrow(() -> new ProductException("không tìm thấy use"));

        } else {
            throw new ProductException("token xác thực không hợp lệ");
        }
        List<Review> reviews = reviewRepository.findByUser_Id(users.getId());
        return reviews.stream()
                .map(this::reviewResponseDto)
                .collect(Collectors.toList());
    }
@Override
    public ReviewResponseDto addReview(ReviewRequestDto reviewRequestDto, String username) throws ProductException {
    Users users = null;
    if (username != null) {
        users = userRepository.findByUsername(username).orElseThrow(() -> new ProductException("không tìm thấy use"));

    } else {
        throw new ProductException("token xác thực không hợp lệ");
    }
        Product product = productRepository.findById(reviewRequestDto.getProductId())
                .orElseThrow(() -> new ProductException("Không tìm thấy sản phẩm"));

List<Product> products= orderRepository.findProductsFromDeliveredOrdersByUserId(OrderStatus.DELIVERED,users.getId());
if (products.indexOf(product)==-1){
    throw new ProductException("bạn chưa mua sản phẩm này");
}
List<Review> reviewList=reviewRepository.findByUser_Id(users.getId());
    for (Review r:reviewList
         ) {
        if(r.getProduct().getId()==product.getId()){
            throw new ProductException("bạn đã đánh giá sản phẩm này rồi");
        }
    }
        Review review = new Review();
        review.setComment(reviewRequestDto.getComment());
        review.setRating(reviewRequestDto.getRating());
        review.setDatetime(new Date());
        review.setProduct(product);
        review.setUser(users);
    users.getReviewList().add(review);
        review = reviewRepository.save(review);
userRepository.save(users);
        return reviewResponseDto(review);
    }
    @Override
    public ReviewResponseDto updateReview(Long reviewId, ReviewRequestDto reviewRequestDto, String username) throws ProductException{
        Users users = null;
        if (username != null) {
            users = userRepository.findByUsername(username).orElseThrow(() -> new ProductException("không tìm thấy use"));

        } else {
            throw new ProductException("token xác thực không hợp lệ");
        }
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ProductException("Không tìm thấy đánh giá với ID: " + reviewId));
        Product product = productRepository.findById(reviewRequestDto.getProductId())
                .orElseThrow(() -> new ProductException("Không tìm thấy sản phẩm"));
        // Cập nhật thông tin đánh giá
        review.setComment(reviewRequestDto.getComment());
        review.setRating(reviewRequestDto.getRating());
        review.setDatetime(new Date());
        review.setUser(users);
review.setProduct(product);
        // Lưu cập nhật
        review = reviewRepository.save(review);

        return reviewResponseDto(review);
    }
    @Override

    public void deleteReview(Long id,String username) throws ProductException {
        Users users = null;
        if (username != null) {
            users = userRepository.findByUsername(username).orElseThrow(() -> new ProductException("không tìm thấy use"));

        } else {
            throw new ProductException("token xác thực không hợp lệ");
        }
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new ProductException("Không tìm thấy đánh giá với ID: "));


        users.getReviewList().remove(review);
        reviewRepository.deleteById(id);
        userRepository.save(users);

    }

    public ReviewResponseDto reviewResponseDto(Review review) {
        ReviewResponseDto responseDto = new ReviewResponseDto();
        responseDto.setId(review.getId());
        responseDto.setComment(review.getComment());
        responseDto.setRating(review.getRating());
        responseDto.setProductId(review.getProduct().getId());
        responseDto.setProductname(review.getProduct().getName());
        responseDto.setUsername(review.getUser().getUsername());
        return responseDto;
    }
}
