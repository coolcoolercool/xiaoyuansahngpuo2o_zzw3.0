package com.zzw.o2o.service;

import com.zzw.o2o.dto.UserShopMapExecution;
import com.zzw.o2o.entity.UserShopMap;

public interface UserShopMapService {

	UserShopMapExecution listUserShopMap(UserShopMap userShopMapCondition,
                                         int pageIndex, int pageSize);

}
