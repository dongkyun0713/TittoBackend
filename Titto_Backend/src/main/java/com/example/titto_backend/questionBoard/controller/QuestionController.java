package com.example.titto_backend.questionBoard.controller;

import com.example.titto_backend.auth.domain.User;
import com.example.titto_backend.auth.repository.UserRepository;
import com.example.titto_backend.common.exception.CustomException;
import com.example.titto_backend.common.exception.ErrorCode;
import com.example.titto_backend.questionBoard.dto.QuestionDTO;
import com.example.titto_backend.questionBoard.service.QuestionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.security.Principal;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/questions")
@Tag(name = "Question Controller", description = "질문 게시판 관련 API")
public class QuestionController {

    private final QuestionService questionService;
    private final UserRepository userRepository;

    @PostMapping("/create")
    @Operation(
            summary = "질문 작성",
            description = "질문을 작성합니다",
            responses = {
                    @ApiResponse(responseCode = "201", description = "질문 작성 성공"),
                    @ApiResponse(responseCode = "500", description = "관리자 문의")
            })
    public ResponseEntity<String> createQuestion(@RequestBody QuestionDTO.Request request,
                                                 Principal principal) {
        String email = principal.getName();
        return ResponseEntity.status(HttpStatus.CREATED).body(questionService.save(email, request));
    }

    @GetMapping("/posts")
    @Operation(
            summary = "전체 질문 조회",
            description = "전체 질문을 조회합니다",
            responses = {
                    @ApiResponse(responseCode = "200", description = "요청 성공"),
                    @ApiResponse(responseCode = "500", description = "관리자 문의")
            })
    public ResponseEntity<Page<QuestionDTO.Response>> getAllQuestions(@RequestParam(defaultValue = "0") int page) {
        Page<QuestionDTO.Response> questions = questionService.findAll(page);
        return ResponseEntity.ok(questions);
    }

    @GetMapping("/{postId}")
    @Operation(
            summary = "질문 상세 조회",
            description = "질문 상세 내용을 조회합니다",
            responses = {
                    @ApiResponse(responseCode = "200", description = "요청 성공"),
                    @ApiResponse(responseCode = "404", description = "질문을 찾을 수 없음")
            })
    public ResponseEntity<QuestionDTO.Response> getQuestionById(Principal principal,
                                                                @PathVariable Long postId) {
        QuestionDTO.Response question = questionService.findById(principal, postId);
        return new ResponseEntity<>(question, HttpStatus.OK);
    }

    @GetMapping("/category/{category}")
    @Operation(
            summary = "카테고리별 질문 조회",
            description = "카테고리별 질문을 조회합니다",
            responses = {
                    @ApiResponse(responseCode = "200", description = "요청 성공"),
                    @ApiResponse(responseCode = "404", description = "질문을 찾을 수 없음")
            })
    public ResponseEntity<Page<QuestionDTO.Response>> getQuestionsByCategory(@PathVariable("category") String category,
                                                                             @RequestParam(defaultValue = "0") int page) {
        Page<QuestionDTO.Response> questions = questionService.findByCategory(page, category);
        return ResponseEntity.ok(questions);
    }

    @GetMapping("/status/{status}")
    @Operation(
            summary = "상태별 질문 조회",
            description = "상태별 질문을 조회합니다",
            responses = {
                    @ApiResponse(responseCode = "200", description = "요청 성공"),
                    @ApiResponse(responseCode = "404", description = "질문을 찾을 수 없음")
            })
    public ResponseEntity<Page<QuestionDTO.Response>> getQuestionsByStatus(@PathVariable("status") String status,
                                                                           @RequestParam(defaultValue = "0") int page) {
        Page<QuestionDTO.Response> questions = questionService.findByStatus(page, status);
        return ResponseEntity.ok(questions);
    }

    @GetMapping("/search")
    @Operation(
            summary = "질문 게시판 검색",
            description = "키워드로 검색하여 질문을 조회합니다",
            responses = {
                    @ApiResponse(responseCode = "200", description = "요청 성공"),
                    @ApiResponse(responseCode = "404", description = "질문을 찾을 수 없음")
            })
    public ResponseEntity<Page<QuestionDTO.Response>> searchByKeyWord(@RequestParam("page") int page,
                                                                      @RequestParam String keyWord) {
        Page<QuestionDTO.Response> questions = questionService.searchByKeyword(keyWord, page);
        return ResponseEntity.ok(questions);
    }

    @PutMapping("/{postId}")
    @Operation(
            summary = "질문 수정",
            description = "질문을 수정합니다",
            responses = {
                    @ApiResponse(responseCode = "200", description = "질문 수정 성공"),
                    @ApiResponse(responseCode = "404", description = "질문을 찾을 수 없음")
            })
    public ResponseEntity<String> updateQuestion(@PathVariable("postId") Long postId,
                                                 @RequestBody QuestionDTO.Update update) {

        String currentEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        User currentUser = userRepository.findByEmail(currentEmail)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        questionService.update(update, postId, currentUser);

        return ResponseEntity.ok("질문 수정 성공");
    }

    @DeleteMapping("/{postId}")
    @Operation(
            summary = "질문 삭제",
            description = "질문을 삭제합니다",
            responses = {
                    @ApiResponse(responseCode = "204", description = "질문 삭제 성공"),
                    @ApiResponse(responseCode = "404", description = "질문을 찾을 수 없음")
            })
    public ResponseEntity<Void> deleteQuestion(@PathVariable("postId") Long postId) {
        String currentEmail = SecurityContextHolder.getContext().getAuthentication().getName(); // 현재 사용자의 이메일 주소 가져오기
        User currentUser = userRepository.findByEmail(currentEmail)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        questionService.delete(postId, currentUser); // 현재 사용자의 ID를 전달하여 삭제 메소드 호출
        return ResponseEntity.noContent().build();
    }

}