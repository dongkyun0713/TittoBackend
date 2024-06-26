package com.example.titto_backend.matchingBoard.controller;


import com.example.titto_backend.matchingBoard.dto.request.matchingPostReviewRequest.MatchingPostReviewCreateRequestDto;
import com.example.titto_backend.matchingBoard.dto.request.matchingPostReviewRequest.MatchingPostReviewDeleteRequestDto;
import com.example.titto_backend.matchingBoard.dto.request.matchingPostReviewRequest.MatchingPostReviewUpdateRequestDto;
import com.example.titto_backend.matchingBoard.dto.response.matchingPostReviewResponse.MatchingPostReviewCreateResponseDto;
import com.example.titto_backend.matchingBoard.dto.response.matchingPostReviewResponse.MatchingPostReviewDeleteResponseDto;
import com.example.titto_backend.matchingBoard.dto.response.matchingPostReviewResponse.MatchingPostReviewResponseDto;
import com.example.titto_backend.matchingBoard.dto.response.matchingPostReviewResponse.MatchingPostReviewUpdateResponseDto;
import com.example.titto_backend.matchingBoard.service.matchingBoardReview.MatchingPostReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.security.Principal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/matching-board-review")
@RequiredArgsConstructor
@Tag(name = "Matching Post Review Controller", description = "매칭 게시글 리뷰 관련 API")
public class MatchingPostReviewController {

    private final MatchingPostReviewService matchingBoardReviewService;

    @PostMapping("/create")
    @Operation(
            summary = "매칭 게시글 리뷰 작성",
            description = "매칭 게시글에 리뷰를 작성합니다",
            responses = {
                    @ApiResponse(responseCode = "201", description = "요청 성공"),
                    @ApiResponse(responseCode = "400", description = "잘못된 요청"),
                    @ApiResponse(responseCode = "500", description = "관리자 문의")
            })
    public ResponseEntity<MatchingPostReviewCreateResponseDto> createReview(Principal principal,
                                                                            @RequestBody MatchingPostReviewCreateRequestDto matchingPostReviewCreateRequestDto) {
        MatchingPostReviewCreateResponseDto responseDto = matchingBoardReviewService.createReview(principal,
                matchingPostReviewCreateRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    @GetMapping("/get/{postId}")
    @Operation(
            summary = "매칭 게시글 리뷰 전체 조회",
            description = "특정 매칭 게시글에 대한 전체 리뷰를 조회합니다",
            responses = {
                    @ApiResponse(responseCode = "200", description = "요청 성공"),
                    @ApiResponse(responseCode = "400", description = "잘못된 요청"),
                    @ApiResponse(responseCode = "500", description = "관리자 문의")
            })
    public ResponseEntity<List<MatchingPostReviewResponseDto>> getAllMatchingBoardReviewsByPostId(
            @PathVariable Long postId) {
        List<MatchingPostReviewResponseDto> responseDtoList = matchingBoardReviewService.getAllMatchingBoardReviewsByPostId(
                postId);
        return ResponseEntity.ok(responseDtoList);
    }

    @PutMapping("/update/{reviewId}")
    @Operation(
            summary = "매칭 게시글 리뷰 수정",
            description = "매칭 게시글에 작성된 리뷰를 수정합니다",
            responses = {
                    @ApiResponse(responseCode = "200", description = "요청 성공"),
                    @ApiResponse(responseCode = "400", description = "잘못된 요청"),
                    @ApiResponse(responseCode = "500", description = "관리자 문의")
            })
    public ResponseEntity<MatchingPostReviewUpdateResponseDto> updateReview(Principal principal,
                                                                            @RequestBody MatchingPostReviewUpdateRequestDto matchingPostReviewUpdateRequestDto) {
        MatchingPostReviewUpdateResponseDto responseDto = matchingBoardReviewService.updateReview(principal,
                matchingPostReviewUpdateRequestDto);
        return ResponseEntity.ok(responseDto);
    }

    @DeleteMapping("/delete/{reviewId}")
    @Operation(
            summary = "매칭 게시글 리뷰 삭제",
            description = "매칭 게시글에 작성된 리뷰를 삭제합니다",
            responses = {
                    @ApiResponse(responseCode = "200", description = "요청 성공"),
                    @ApiResponse(responseCode = "400", description = "잘못된 요청"),
                    @ApiResponse(responseCode = "500", description = "관리자 문의")
            })
    public ResponseEntity<MatchingPostReviewDeleteResponseDto> deleteReviewByReviewId(
            @RequestBody MatchingPostReviewDeleteRequestDto matchingPostReviewDeleteRequestDto,
            Principal principal) {
        MatchingPostReviewDeleteResponseDto responseDto = matchingBoardReviewService.deleteReviewByReviewId(
                matchingPostReviewDeleteRequestDto, principal);
        return ResponseEntity.ok(responseDto);
    }

}