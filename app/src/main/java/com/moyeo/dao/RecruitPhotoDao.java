package com.moyeo.dao;

import com.moyeo.vo.RecruitPhoto;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface RecruitPhotoDao {

  void add(RecruitPhoto photo);

  int addAll(List<RecruitPhoto> photos);

  RecruitPhoto findById(int recruitPhotoId);

  List<RecruitPhoto> findAllByBoardId(int recruitBoardId);

  int delete(int recruitPhotoId);

  void deleteAllPhotoByRecruitBoardId(int recruitBoardId);


}
