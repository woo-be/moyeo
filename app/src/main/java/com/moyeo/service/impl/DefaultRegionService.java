package com.moyeo.service.impl;

import com.moyeo.dao.RegionDao;
import com.moyeo.service.RegionService;
import com.moyeo.vo.Region;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DefaultRegionService implements RegionService {

  private final RegionDao regionDao;

  @Override
  public Region get(int regionID) {
    return regionDao.findBy(regionID);
  }
}
