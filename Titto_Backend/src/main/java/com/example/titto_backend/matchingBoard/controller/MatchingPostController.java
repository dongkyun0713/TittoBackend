package com.example.titto_backend.matchingBoard.controller;

import com.example.titto_backend.matchingBoard.dto.request.MatchingPostRequest.MatchingPostCreateRequestDto;
import com.example.titto_backend.matchingBoard.dto.request.MatchingPostRequest.MatchingPostUpdateRequestDto;
import com.example.titto_backend.matchingBoard.dto.response.matchingPostResponse.MatchingPostCreateResponseDto;
import com.example.titto_backend.matchingBoard.dto.response.matchingPostResponse.MatchingPostDeleteResponseDto;
import com.example.titto_backend.matchingBoard.dto.response.matchingPostResponse.MatchingPostResponseDto;
import com.example.titto_backend.matchingBoard.dto.response.matchingPostResponse.MatchingPostUpdateResponseDto;
import com.example.titto_backend.matchingBoard.service.matchingBoard.MatchingPostService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AuthorizationServiceException;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/matchingposts")
@RequiredArgsConstructor
public class MatchingPostController {
    private final MatchingPostService matchingPostService;

    @PostMapping
    public ResponseEntity<MatchingPostCreateResponseDto> createMatchingPost(Principal principal, @RequestBody MatchingPostCreateRequestDto matchingPostCreateRequestDto) {
        MatchingPostCreateResponseDto responseDto = matchingPostService.createMatchingPost(principal, matchingPostCreateRequestDto);
        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }

    @GetMapping("/{matchingPostId}")
    public ResponseEntity<MatchingPostResponseDto> getMatchingPostByMatchingPostId(@PathVariable Long matchingPostId, HttpServletRequest request, HttpServletResponse response) {
        MatchingPostResponseDto responseDto = matchingPostService.getMatchingPostByMatchingPostId(matchingPostId, request, response);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @DeleteMapping("/{matchingPostId}")
    public ResponseEntity<MatchingPostDeleteResponseDto> deleteMatchingPostByMatchingPostId(@PathVariable Long matchingPostId) {
        MatchingPostDeleteResponseDto responseDto = matchingPostService.deleteMatchingPostByMatchingPostId(matchingPostId);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @PutMapping("/{matchingPostId}")
    public ResponseEntity<MatchingPostUpdateResponseDto> updateMatchingPost(@PathVariable Long matchingPostId, Principal principal, @RequestBody MatchingPostUpdateRequestDto matchingPostUpdateRequestDto) {
        MatchingPostUpdateResponseDto responseDto = matchingPostService.updateMatchingPost(matchingPostId, principal, matchingPostUpdateRequestDto);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @ExceptionHandler(value = AuthorizationServiceException.class)
    public ResponseEntity<String> handleAuthorizationException(AuthorizationServiceException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);
    }
}
