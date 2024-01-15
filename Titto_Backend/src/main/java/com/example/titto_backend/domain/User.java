package com.example.titto_backend.domain;

import com.example.titto_backend.domain.MatchingBoard.MatchingPost;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@RequiredArgsConstructor
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String nickname;

  private String studentNumber;
  private String department;
  private String profile_image;
  private LocalDateTime createDate;
  private Level level;
  //post count;
  private Integer postCount;
  //badge
  private Badge badge;
  //available point
  private Integer availablePoint;

  @JsonIgnore
  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  private List<MatchingPost> matchingPosts;

  @JsonIgnore
  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  private List<Review> reviews;
}