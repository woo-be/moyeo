package com.moyeo.vo;

import java.io.Serializable;
import java.sql.Date;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class RecruitBoard implements Serializable {

  private static final long serialVersionUID = 100L;

  private int recruitBoardId;           // 여행멤버모집게시글 식별자
  private Member writer;                // 회원 식별자
  private Region region;                // 지역 식별자
  private Theme theme;                  // 테마 식별자

  private String title;                 // 여행멤버모집 게시글 제목
  private String content;               // 여행멤버모집 게시글 내용

  private Date startDate;               // 여행 시작일
  private Date endDate;                 // 여행 종료일

  private int current;                  // 현재 인원
  private int recruitTotal;             // 모집 인원
  private Date deadline;                // 모집 마감일자
  private Boolean state;                // 모집 상태-> 0418 boolean에서 Boolean으로 변경 (null 처리 필요)

  private Date createdDate;             // 여행멤버모집게시물 작성일자

  private float latitude;               // 위도
  private float longitude;              // 경도
  private String address;

  private int views;                    // 여행멤버모집게시글 조회수

  private List<RecruitPhoto> photos;    // 여행멤버모집게시글 첨부사진
  private List<RecruitComment> comments;// 여행멤버모집게시글 댓글
  private List<RecruitMember> applicants;  // 여행멤버모집게시글 참여자
  private int waiting;                  // 대기인원
}
