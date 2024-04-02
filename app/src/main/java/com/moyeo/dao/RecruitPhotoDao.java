package com.moyeo.dao;

import com.moyeo.vo.RecruitPhoto;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface RecruitPhotoDao {
// add, list, delete
  void add(RecruitPhoto photo);

  int addAll(List<RecruitPhoto> photos);

  int delete(int no);

  int deleteAll(int recruitBoardNo);



}
