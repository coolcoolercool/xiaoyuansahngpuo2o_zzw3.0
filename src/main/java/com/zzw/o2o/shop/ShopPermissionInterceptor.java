package com.zzw.o2o.shop;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.zzw.o2o.entity.Shop;

/**
 * 店家管理系统操作验证拦截器
 */
public class ShopPermissionInterceptor extends HandlerInterceptorAdapter {
	/**
	 * 主要在做事前拦截,即用户操作发生前,改写preHandle里的逻辑,进行用户操作
	 * @param request
	 * @param response
	 * @param handler
	 * @return
	 * @throws Exception
	 */
	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		//从session中获取当前选择的店铺
		Shop currentShop = (Shop) request.getSession().getAttribute("currentShop");
		@SuppressWarnings("unchecked")
		//从session中获取当前用户课操作的店铺列表
		List<Shop> shopList = (List<Shop>) request.getSession().getAttribute("shopList");
		//空值判断
		if (currentShop != null && shopList != null) {
			for (Shop shop : shopList) {
				//如果当前店铺在可操作的列表里,则返回true,进行接下来的操作
				if (shop.getShopId() == currentShop.getShopId()) {
					return true;
				}
			}
		}
		//若不满足拦截器的验证则返回false,终止用户操作的执行
		return false;
	}
}
