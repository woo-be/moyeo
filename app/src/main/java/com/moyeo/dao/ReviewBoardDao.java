package com.moyeo.dao;

import com.moyeo.vo.ReviewBoard;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ReviewBoardDao {

  void add(ReviewBoard reviewBoard);

  List<ReviewBoard> findAll();
}
