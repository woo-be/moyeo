package com.moyeo.dao;

import com.moyeo.vo.Region;
import com.moyeo.vo.Theme;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ThemeDao {

  Theme findBy(int themeId);
}
