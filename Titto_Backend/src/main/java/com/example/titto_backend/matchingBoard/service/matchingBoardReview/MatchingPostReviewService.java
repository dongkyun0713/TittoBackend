package com.example.titto_backend.matchingBoard.service.matchingBoardReview;

import com.example.titto_backend.auth.domain.User;
import com.example.titto_backend.auth.repository.UserRepository;
import com.example.titto_backend.common.exception.CustomException;
import com.example.titto_backend.common.exception.ErrorCode;
import com.example.titto_backend.matchingBoard.domain.matchingBoard.MatchingPost;
import com.example.titto_backend.matchingBoard.domain.review.MatchingPostReview;
import com.example.titto_backend.matchingBoard.dto.request.matchingPostReviewRequest.MatchingPostReviewCreateRequestDto;
import com.example.titto_backend.matchingBoard.dto.request.matchingPostReviewRequest.MatchingPostReviewUpdateRequestDto;
import com.example.titto_backend.matchingBoard.dto.response.matchingPostReviewResponse.MatchingPostReviewCreateResponseDto;
import com.example.titto_backend.matchingBoard.dto.response.matchingPostReviewResponse.MatchingPostReviewDeleteResponseDto;
import com.example.titto_backend.matchingBoard.dto.response.matchingPostReviewResponse.MatchingPostReviewResponseDto;
import com.example.titto_backend.matchingBoard.dto.response.matchingPostReviewResponse.MatchingPostReviewUpdateResponseDto;
import com.example.titto_backend.matchingBoard.repository.matchingBoard.MatchingPostRepository;
import com.example.titto_backend.matchingBoard.repository.review.MatchingPostReviewRepository;
import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MatchingPostReviewService {
    private final MatchingPostReviewRepository matchingPostReviewRepository;
    private final UserRepository userRepository;
    private final MatchingPostRepository matchingPostRepository;

    // 생성
    @Transactional
    public MatchingPostReviewCreateResponseDto createReview(Principal principal,
                                                            MatchingPostReviewCreateRequestDto matchingPostReviewCreateRequestDto) {
        User user = getCurrentUser(principal);

        MatchingPostReview matchingPostReview = MatchingPostReview.builder()
                .matchingPost(matchingPostRepository.findById(matchingPostReviewCreateRequestDto.getPostId())
                        .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND)))
                .reviewAuthor(user)
                .content(matchingPostReviewCreateRequestDto.getContent())
                .build();
        return new MatchingPostReviewCreateResponseDto(matchingPostReviewRepository.save(matchingPostReview));
    }

    // 조회
    public List<MatchingPostReviewResponseDto> getAllMatchingBoardReviewsByPostId(Long postId) {
        MatchingPost matchingPost = matchingPostRepository.findById(postId)
                .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));

        List<MatchingPostReview> matchingPostReviews = matchingPostReviewRepository.findAllByMatchingPost(matchingPost);

        return matchingPostReviews.stream()
                .map(MatchingPostReviewResponseDto::new)
                .collect(Collectors.toList());
    }


    // 수정
    @Transactional
    public MatchingPostReviewUpdateResponseDto updateReview(Principal principal,
                                                            MatchingPostReviewUpdateRequestDto matchingPostReviewUpdateRequestDto) {
        User user = getCurrentUser(principal);

        MatchingPostReview matchingPostReview = MatchingPostReview.builder()
                .review_id(matchingPostReviewUpdateRequestDto.getReviewId())
                .matchingPost(matchingPostRepository.findById(matchingPostReviewUpdateRequestDto.getPostId())
                        .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND)))
                .reviewAuthor(user)
                .content(matchingPostReviewUpdateRequestDto.getContent())
                .build();
        return new MatchingPostReviewUpdateResponseDto(matchingPostReviewRepository.save(matchingPostReview));
    }


    // 삭제
    @Transactional
    public MatchingPostReviewDeleteResponseDto deleteReviewByReviewId(Long reviewId) {
        MatchingPostReview matchingPostReview = matchingPostReviewRepository.findById(reviewId).orElseThrow(
                () -> new CustomException(ErrorCode.REVIEW_NOT_FOUND));
        matchingPostReviewRepository.delete(matchingPostReview);
        return MatchingPostReviewDeleteResponseDto.of(reviewId);
    }

    private User getCurrentUser(Principal principal) {
        String userEmail = principal.getName();
        return userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
    }
}
