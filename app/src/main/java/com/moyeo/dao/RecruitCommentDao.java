package com.moyeo.dao;

import com.moyeo.vo.RecruitComment;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface RecruitCommentDao {

  void add(RecruitComment recruitComment);

  RecruitComment findBy(int recruitCommentId);

  List<RecruitComment> findAllByRecruitBoardId(int recruitBoardId);

  int update(RecruitComment recruitComment);

  int delete(int recruitCommentId);

  int deleteAllCommentByRecruitBoardId(int recruitBoardId);
}
