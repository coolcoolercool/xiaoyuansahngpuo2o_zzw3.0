package com.zzw.o2o.web.frontend;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.zzw.o2o.util.HttPServletRequestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zzw.o2o.dto.UserProductMapExecution;
import com.zzw.o2o.entity.PersonInfo;
import com.zzw.o2o.entity.UserProductMap;
import com.zzw.o2o.service.UserProductMapService;
import com.zzw.o2o.util.HttPServletRequestUtil;

@Controller
@RequestMapping("/frontend")
public class MyProductController {
	@Autowired
	private UserProductMapService userProductMapService;

	@RequestMapping(value = "/listuserproductmapsbycustomer", method = RequestMethod.GET)
	@ResponseBody
	private Map<String, Object> listUserProductMapsByCustomer(
			HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		int pageIndex = HttPServletRequestUtil.getInt(request, "pageIndex");
		int pageSize = HttPServletRequestUtil.getInt(request, "pageSize");
		PersonInfo user = (PersonInfo) request.getSession()
				.getAttribute("user");
		if ((pageIndex > -1) && (pageSize > -1) && (user != null)
				&& (user.getUserId() != -1)) {
			UserProductMap userProductMapCondition = new UserProductMap();
			userProductMapCondition.setUserId(user.getUserId());
			long shopId = HttPServletRequestUtil.getLong(request, "shopId");
			if (shopId > -1) {
				userProductMapCondition.setShopId(shopId);
			}
			String productName = HttPServletRequestUtil.getString(request,
					"productName");
			if (productName != null) {
				userProductMapCondition.setProductName(productName);
			}
			UserProductMapExecution ue = userProductMapService
					.listUserProductMap(userProductMapCondition, pageIndex,
							pageSize);
			modelMap.put("userProductMapList", ue.getUserProductMapList());
			modelMap.put("count", ue.getCount());
			modelMap.put("success", true);
		} else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "empty pageSize or pageIndex or shopId");
		}
		return modelMap;
	}
}
