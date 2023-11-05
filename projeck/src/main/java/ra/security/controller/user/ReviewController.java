package ra.security.controller.user;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import ra.security.exception.ProductException;
import ra.security.model.domain.Review;
import ra.security.model.dto.request.ReviewRequestDto;
import ra.security.model.dto.response.FavoriteProductResponseDto;
import ra.security.model.dto.response.ReviewResponseDto;
import ra.security.service.IReviewService;
import ra.security.service.impl.usersevice.UserServiceOnline;

import javax.validation.Valid;
import java.util.List;


@RestController
@RequestMapping("/api/user/reviews")
@CrossOrigin("*")
public class ReviewController {
    @Autowired
    private UserServiceOnline userServiceOnline;
    @Autowired
    private IReviewService reviewService;
    @GetMapping("/current-user")
    public ResponseEntity<String> getCurrentUser(@AuthenticationPrincipal UserDetails userDetails) {
//        @AuthenticationPrincipal trực tiếp trên tham số của phương thức controller để trích xuất thông tin người dùng.
//        Điều này giúp bạn tránh phải sử dụng SecurityContextHolder hoặc UserServiceOnline
//        trong mã nguồn
        String username = userDetails.getUsername();
        // Xử lý logic dựa trên tên người dùng
        return ResponseEntity.ok("Current user: " + username);
    }

    @GetMapping()
    public ResponseEntity<List<ReviewResponseDto>> getUserFavoriteProducts() throws ProductException {
        List<ReviewResponseDto> ReviewResponseDto = reviewService.getAllReviews(userServiceOnline.getCurrentUsername());
        return ResponseEntity.ok(ReviewResponseDto);
    }
    @PostMapping("/add")
    public ResponseEntity<ReviewResponseDto> addReview(@RequestBody @Valid ReviewRequestDto reviewDto) throws ProductException {

        ReviewResponseDto reviewResponseDto = reviewService.addReview(reviewDto,userServiceOnline.getCurrentUsername());
        return ResponseEntity.ok(reviewResponseDto);
    }
//    @PutMapping("/edit/{id}")
//    public ResponseEntity<ReviewResponseDto> updateReview(
//            @PathVariable Long id,
//            @RequestBody @Valid ReviewRequestDto reviewDto
//    ) throws ProductException {
//        ReviewResponseDto updatedReview = reviewService.updateReview(id, reviewDto,userServiceOnline.getCurrentUsername());
//        return ResponseEntity.ok(updatedReview);
//    }
//    @DeleteMapping("/delete/{id}")
//    public ResponseEntity<String> deleteReview(@PathVariable Long id) throws ProductException {
//        reviewService.deleteReview(id,userServiceOnline.getCurrentUsername());
//        return new ResponseEntity<>("Xóa thành công", HttpStatus.OK);
//
//    }
}
