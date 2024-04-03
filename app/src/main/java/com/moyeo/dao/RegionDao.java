package com.moyeo.dao;

import com.moyeo.vo.Region;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface RegionDao {

  Region findBy(int regionId);
}
