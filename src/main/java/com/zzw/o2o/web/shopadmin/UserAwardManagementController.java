package com.zzw.o2o.web.shopadmin;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zzw.o2o.dto.ShopAuthMapExecution;
import com.zzw.o2o.dto.UserAwardMapExecution;
import com.zzw.o2o.entity.PersonInfo;
import com.zzw.o2o.entity.Shop;
import com.zzw.o2o.entity.ShopAuthMap;
import com.zzw.o2o.entity.UserAwardMap;
import com.zzw.o2o.enums.UserAwardMapStateEnum;
import com.zzw.o2o.service.AwardService;
import com.zzw.o2o.service.PersonInfoService;
import com.zzw.o2o.service.ShopAuthMapService;
import com.zzw.o2o.service.UserAwardMapService;
import com.zzw.o2o.util.HttPServletRequestUtil;
import com.zzw.o2o.util.weixin.message.req.WechatInfo;

@Controller
@RequestMapping("/shop")
public class UserAwardManagementController {
	@Autowired
	private UserAwardMapService userAwardMapService;
	@Autowired
	private AwardService awardService;
	@Autowired
	private PersonInfoService personInfoService;
	@Autowired
	private ShopAuthMapService shopAuthMapService;

	@RequestMapping(value = "/listuserawardmapsbyshop", method = RequestMethod.GET)
	@ResponseBody
	private Map<String, Object> listUserAwardMapsByShop(
			HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		Shop currentShop = (Shop) request.getSession().getAttribute(
				"currentShop");
		int pageIndex = HttPServletRequestUtil.getInt(request, "pageIndex");
		int pageSize = HttPServletRequestUtil.getInt(request, "pageSize");
		if ((pageIndex > -1) && (pageSize > -1) && (currentShop != null)
				&& (currentShop.getShopId() != null)) {
			UserAwardMap userAwardMap = new UserAwardMap();
			userAwardMap.setShopId(currentShop.getShopId());
			String awardName = HttPServletRequestUtil.getString(request,
					"awardName");
			if (awardName != null) {
				userAwardMap.setAwardName(awardName);
			}
			UserAwardMapExecution ue = userAwardMapService.listUserAwardMap(
					userAwardMap, pageIndex, pageSize);
			modelMap.put("userAwardMapList", ue.getUserAwardMapList());
			modelMap.put("count", ue.getCount());
			modelMap.put("success", true);
		} else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "empty pageSize or pageIndex or shopId");
		}
		return modelMap;
	}

	@RequestMapping(value = "/exchangeaward", method = RequestMethod.POST)
	@ResponseBody
	private Map<String, Object> exchangeAward(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		PersonInfo user = (PersonInfo) request.getSession()
				.getAttribute("user");
		String qrCodeinfo = HttPServletRequestUtil.getString(request, "state");
		ObjectMapper mapper = new ObjectMapper();
		WechatInfo wechatInfo = null;
		try {
			wechatInfo = mapper.readValue(qrCodeinfo, WechatInfo.class);
		} catch (Exception e) {
			modelMap.put("success", false);
			modelMap.put("errMsg", e.toString());
			return modelMap;
		}
		if (!checkQRCodeInfo(wechatInfo)) {
			modelMap.put("success", false);
			modelMap.put("errMsg", "二维码信息非法！");
			return modelMap;
		}
		Long userAwardId = wechatInfo.getUserAwardId();
		Long customerId = wechatInfo.getCustomerId();
		UserAwardMap userAwardMap = compactUserAwardMap4Exchange(customerId,
				userAwardId);
		if (userAwardMap != null) {
			try {
				if (!checkShopAuth(user.getUserId(), userAwardMap)) {
					modelMap.put("success", false);
					modelMap.put("errMsg", "无操作权限");
					return modelMap;
				}
				UserAwardMapExecution se = userAwardMapService
						.modifyUserAwardMap(userAwardMap);
				if (se.getState() == UserAwardMapStateEnum.SUCCESS.getState()) {
					modelMap.put("success", true);
				} else {
					modelMap.put("success", false);
					modelMap.put("errMsg", se.getStateInfo());
				}
			} catch (RuntimeException e) {
				modelMap.put("success", false);
				modelMap.put("errMsg", e.toString());
				return modelMap;
			}

		} else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "请输入领取信息");
		}
		return modelMap;
	}

	private boolean checkQRCodeInfo(WechatInfo wechatInfo) {
		if (wechatInfo != null && wechatInfo.getUserAwardId() != null
				&& wechatInfo.getCustomerId() != null
				&& wechatInfo.getCreateTime() != null) {
			long nowTime = System.currentTimeMillis();
			if ((nowTime - wechatInfo.getCreateTime()) <= 5000) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	private UserAwardMap compactUserAwardMap4Exchange(Long customerId,
			Long userAwardId) {
		UserAwardMap userAwardMap = null;
		if (customerId != null && userAwardId != null) {
			userAwardMap = userAwardMapService.getUserAwardMapById(userAwardId);
			userAwardMap.setUsedStatus(0);
			userAwardMap.setUserId(customerId);
		}
		return userAwardMap;
	}

	private boolean checkShopAuth(long userId, UserAwardMap userAwardMap) {
		ShopAuthMapExecution shopAuthMapExecution = shopAuthMapService
				.listShopAuthMapByShopId(userAwardMap.getShopId(), 1, 1000);
		for (ShopAuthMap shopAuthMap : shopAuthMapExecution
				.getShopAuthMapList()) {
			if (shopAuthMap.getEmployeeId() == userId) {
				return true;
			}
		}
		return false;
	}
}
