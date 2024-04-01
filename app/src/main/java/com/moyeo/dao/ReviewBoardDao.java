package com.moyeo.dao;

import com.moyeo.vo.ReviewBoard;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ReviewBoardDao {
List<ReviewBoard> findAll();
ReviewBoard findBy(@Param("reviewBoardId")int reviewBoardId);
}
