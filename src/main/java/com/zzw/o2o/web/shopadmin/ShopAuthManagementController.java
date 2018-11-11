package com.zzw.o2o.web.shopadmin;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sun.jnlp.CheckServicePermission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zzw.o2o.dto.ShopAuthMapExecution;
import com.zzw.o2o.entity.PersonInfo;
import com.zzw.o2o.entity.Shop;
import com.zzw.o2o.entity.ShopAuthMap;
import com.zzw.o2o.enums.ShopAuthMapStateEnum;
import com.zzw.o2o.service.ShopAuthMapService;
import com.zzw.o2o.util.CodeUtil;
import com.zzw.o2o.util.HttPServletRequestUtil;

@Controller
@RequestMapping("/shopadmin")
public class ShopAuthManagementController {
	@Autowired
	private ShopAuthMapService shopAuthMapService;

	@RequestMapping(value = "/listshopauthmapsbyshop", method = RequestMethod.GET)
	@ResponseBody
	private Map<String, Object> listShopAuthMapsByShop(
			HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		//取出分页信息
		int pageIndex = HttPServletRequestUtil.getInt(request, "pageIndex");
		int pageSize = HttPServletRequestUtil.getInt(request, "pageSize");
		//从session中取出分页信息
		Shop currentShop = (Shop) request.getSession().getAttribute("currentShop");
		//控制判断
		if ((pageIndex > -1) && (pageSize > -1) && (currentShop != null)
				&& (currentShop.getShopId() != null)) {
			//分页取出该店铺下的授权信息列表
			ShopAuthMapExecution se = shopAuthMapService
					.listShopAuthMapByShopId(currentShop.getShopId(),
							pageIndex, pageSize);
			modelMap.put("shopAuthMapList", se.getShopAuthMapList());
			modelMap.put("count", se.getCount());
			modelMap.put("success", true);
		} else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "empty pageSize or pageIndex or shopId");
		}
		return modelMap;
	}

	@RequestMapping(value = "/getshopauthmapbyid", method = RequestMethod.GET)
	@ResponseBody
	private Map<String, Object> getShopAuthMapById(@RequestParam Long shopAuthId) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		//非空判断
		if (shopAuthId != null && shopAuthId > -1) {
			//根据前台传入的shopAuthId查找对应的授权信息
			ShopAuthMap shopAuthMap = shopAuthMapService
					.getShopAuthMapById(shopAuthId);
			modelMap.put("shopAuthMap", shopAuthMap);
			modelMap.put("success", true);
		} else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "empty shopAuthId");
		}
		return modelMap;
	}

	@RequestMapping(value = "/addshopauthmap", method = RequestMethod.POST)
	@ResponseBody
	private Map<String, Object> addShopAuthMap(String shopAuthMapStr,
			HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		ObjectMapper mapper = new ObjectMapper();
		ShopAuthMap shopAuthMap = null;
		try {
			shopAuthMap = mapper.readValue(shopAuthMapStr, ShopAuthMap.class);
		} catch (Exception e) {
			modelMap.put("success", false);
			modelMap.put("errMsg", e.toString());
			return modelMap;
		}
		if (shopAuthMap != null) {
			try {
				Shop currentShop = (Shop) request.getSession().getAttribute(
						"currentShop");
				PersonInfo user = (PersonInfo) request.getSession()
						.getAttribute("user");
				if (currentShop.getOwnerId() != user.getUserId()) {
					modelMap.put("success", false);
					modelMap.put("errMsg", "无操作权限");
					return modelMap;
				}
				shopAuthMap.setShopId(currentShop.getShopId());
				shopAuthMap.setEmployeeId(user.getUserId());
				ShopAuthMapExecution se = shopAuthMapService
						.addShopAuthMap(shopAuthMap);
				if (se.getState() == ShopAuthMapStateEnum.SUCCESS.getState()) {
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
			modelMap.put("errMsg", "请输入授权信息");
		}
		return modelMap;
	}

	@RequestMapping(value = "/modifyshopauthmap", method = RequestMethod.POST)
	@ResponseBody
	private Map<String, Object> modifyShopAuthMap(String shopAuthMapStr,HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();

		//是授权编辑时候调用还是删除/恢复授权的时候调用
		//若是为前者则进行验证码判断,后者则跳过验证码判断
		//todo 这里类似于之前的部分,如果出错可以参考以前是怎么做的时候,继续修改
		boolean statusChange = HttPServletRequestUtil.getBoolean(request, "statusChange");

		if (/*!statusChange &&*/ !CodeUtil.checkVerifyCode(request)) {
			modelMap.put("success", false);
			modelMap.put("errMsg", "输入了错误的验证码");
			return modelMap;
		}
		ObjectMapper mapper = new ObjectMapper();
		ShopAuthMap shopAuthMap = null;
		try {
			shopAuthMap = mapper.readValue(shopAuthMapStr, ShopAuthMap.class);
		} catch (Exception e) {
			modelMap.put("success", false);
			modelMap.put("errMsg", e.toString());
			return modelMap;
		}
		if (shopAuthMap != null && shopAuthMap.getShopAuthId() != null) {
			try {
				//看看被操作的对方是否为店家本人,店家本人不支持被修改
				/*if(!checkPermission(shopAuthMap.getShopAuthId())){
					modelMap.put("success",false);
					modelMap.put("errMsg","无法对店家本人权限操作");
					return modelMap;
				}*/

				//todo 这里我不清楚要不要这么做,还是直接放入shopAuthMap比较好
				Shop currentShop = (Shop) request.getSession().getAttribute("currentShop");
				PersonInfo user = (PersonInfo) request.getSession().getAttribute("user");
				shopAuthMap.setShopId(currentShop.getShopId());
				shopAuthMap.setEmployeeId(user.getUserId());
				ShopAuthMapExecution se = shopAuthMapService.modifyShopAuthMap(shopAuthMap);
				if (se.getState() == ShopAuthMapStateEnum.SUCCESS.getState()) {
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
			modelMap.put("errMsg", "请输入要修改的授权信息");
		}
		return modelMap;
	}

	@RequestMapping(value = "/removeshopauthmap", method = RequestMethod.GET)
	@ResponseBody
	private Map<String, Object> removeShopAuthMap(Long shopAuthId) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		if (shopAuthId != null && shopAuthId > 0) {
			try {
				ShopAuthMapExecution se = shopAuthMapService
						.removeShopAuthMap(shopAuthId);
				if (se.getState() == ShopAuthMapStateEnum.SUCCESS.getState()) {
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
			modelMap.put("errMsg", "请至少选择一个授权进行删除");
		}
		return modelMap;
	}

	private boolean checkPermission(Long shopAuthId){
		ShopAuthMap grantedPerson = shopAuthMapService.getShopAuthMapById(shopAuthId);
		if(grantedPerson.getTitleFlag() == 0){
			//若是店家本人,则不能操作
			return false;
		}else{
			return true;
		}
	}
}
