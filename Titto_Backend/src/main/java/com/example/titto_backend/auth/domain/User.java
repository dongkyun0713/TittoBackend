package com.example.titto_backend.auth.domain;

import com.example.titto_backend.auth.dto.request.SignUpDTO;
import com.example.titto_backend.common.BaseEntity;
import com.example.titto_backend.matchingBoard.domain.matchingBoard.MatchingPost;
import com.example.titto_backend.matchingBoard.domain.review.MatchingPostReview;
import com.example.titto_backend.message.domain.Message;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotBlank;
import java.util.List;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class User extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "user_id")
  private Long id;

  @Column(name = "email")
  @NotBlank(message = "이메일은 필수!")
  private String email;

  @Column(name = "profile", nullable = true)
  private String profile;

  @Setter
  @Column(name = "name")
  private String name;

  @Setter
  @Column(name = "nickname")
  private String nickname;

  @Setter
  @Column(name = "student_no")
  private String studentNo;

  @Setter
  @Column(name = "department")
  private String department;

  @Column(name = "social_id")
  private String socialId;

  @Enumerated(EnumType.STRING)
  private SocialType socialType;

  @Setter
  @Column(name = "one_line_intro")
  private String oneLineIntro;

  @Setter
  @Column(name = "self_intro", columnDefinition = "TEXT")
  private String selfIntro;

  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  private Set<Badge> badges;

  @JsonIgnore
  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  private List<MatchingPost> matchingPosts;

  @JsonIgnore
  @OneToMany(mappedBy = "reviewAuthor", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  private List<MatchingPostReview> matchingPostReviews;

  @JsonIgnore
  @OneToMany(mappedBy = "sender", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  private List<Message> sentMessages;

  @JsonIgnore
  @OneToMany(mappedBy = "receiver", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  private List<Message> receivedMessages;

  @Builder
  public User(String email, String profile, String socialId, SocialType socialType) {
    this.email = email;
    this.profile = profile;
    this.socialId = socialId;
    this.socialType = socialType;
  }

  public void signupUser(SignUpDTO signUpDTO) {
    this.setName(signUpDTO.getName());
    this.setNickname(signUpDTO.getNickname());
    this.setStudentNo(signUpDTO.getStudentNo());
    this.setDepartment(signUpDTO.getDepartment());
  }
}