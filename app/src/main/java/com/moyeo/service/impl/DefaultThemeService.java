package com.moyeo.service.impl;

import com.moyeo.dao.RegionDao;
import com.moyeo.dao.ThemeDao;
import com.moyeo.service.RegionService;
import com.moyeo.service.ThemeService;
import com.moyeo.vo.Region;
import com.moyeo.vo.Theme;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DefaultThemeService implements ThemeService {

  private final ThemeDao themeDao;

  @Override
  public Theme get(int regionID) {
    return themeDao.findBy(regionID);
  }
}
