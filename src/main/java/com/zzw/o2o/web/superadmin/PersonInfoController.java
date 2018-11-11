package com.zzw.o2o.web.superadmin;

import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zzw.o2o.dto.PersonInfoExecution;
import com.zzw.o2o.entity.ConstantForSuperAdmin;
import com.zzw.o2o.entity.PersonInfo;
import com.zzw.o2o.enums.PersonInfoStateEnum;
import com.zzw.o2o.service.PersonInfoService;
import com.zzw.o2o.util.HttPServletRequestUtil;

@Controller
@RequestMapping("/superadmin")
public class PersonInfoController {
	@Autowired
	private PersonInfoService personInfoService;

	@RequestMapping(value = "/listpersonInfos", method = RequestMethod.POST)
	@ResponseBody
	private Map<String, Object> listPersonInfos(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		PersonInfoExecution personInfos = null;
		int pageIndex = HttPServletRequestUtil.getInt(request,
				ConstantForSuperAdmin.PAGE_NO);
		int pageSize = HttPServletRequestUtil.getInt(request,
				ConstantForSuperAdmin.PAGE_SIZE);
		if (pageIndex > 0 && pageSize > 0) {
			try {
				PersonInfo personInfo = new PersonInfo();
				int enableStatus = HttPServletRequestUtil.getInt(request,
						"enableStatus");
				if (enableStatus > -1) {
					personInfo.setEnableStatus(enableStatus);
				}
				String name = HttPServletRequestUtil.getString(request, "name");
				if (name != null) {
					personInfo.setName(URLDecoder.decode(name, "UTF-8"));
				}
				personInfos = personInfoService.getPersonInfoList(personInfo,
						pageIndex, pageSize);
			} catch (Exception e) {
				modelMap.put("success", false);
				modelMap.put("errMsg", e.toString());
				return modelMap;
			}
			if (personInfos.getPersonInfoList() != null) {
				modelMap.put(ConstantForSuperAdmin.PAGE_SIZE,
						personInfos.getPersonInfoList());
				modelMap.put(ConstantForSuperAdmin.TOTAL,
						personInfos.getCount());
				modelMap.put("success", true);
			} else {
				modelMap.put(ConstantForSuperAdmin.PAGE_SIZE,
						new ArrayList<PersonInfo>());
				modelMap.put(ConstantForSuperAdmin.TOTAL, 0);
				modelMap.put("success", true);
			}
			return modelMap;
		} else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "空的查询信息");
			return modelMap;
		}
	}

	@RequestMapping(value = "/modifypersonInfo", method = RequestMethod.POST)
	@ResponseBody
	private Map<String, Object> modifyPersonInfo(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		long userId = HttPServletRequestUtil.getLong(request, "userId");
		int enableStatus = HttPServletRequestUtil.getInt(request,
				"enableStatus");
		if (userId > 0 && enableStatus > 0) {
			try {
				PersonInfo pi = new PersonInfo();
				pi.setUserId(userId);
				pi.setEnableStatus(enableStatus);
				PersonInfoExecution ae = personInfoService.modifyPersonInfo(pi);
				if (ae.getState() == PersonInfoStateEnum.SUCCESS.getState()) {
					modelMap.put("success", true);
				} else {
					modelMap.put("success", false);
					modelMap.put("errMsg", ae.getStateInfo());
				}
			} catch (RuntimeException e) {
				modelMap.put("success", false);
				modelMap.put("errMsg", e.toString());
				return modelMap;
			}

		} else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "请输入需要修改的帐号信息");
		}
		return modelMap;
	}

}
