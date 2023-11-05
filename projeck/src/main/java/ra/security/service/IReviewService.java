package ra.security.service;


import ra.security.exception.CartItemException;
import ra.security.exception.ProductException;
import ra.security.model.dto.request.ReviewRequestDto;
import ra.security.model.dto.response.ReviewResponseDto;

import java.util.List;

public interface IReviewService {
    List<ReviewResponseDto> getAllReviews(String username) throws ProductException;
    ReviewResponseDto addReview(ReviewRequestDto reviewRequestDto,String username) throws ProductException;

    ReviewResponseDto updateReview(Long reviewId, ReviewRequestDto reviewRequestDto,String username) throws ProductException;

    void deleteReview(Long reviewId,String username) throws ProductException;
}
