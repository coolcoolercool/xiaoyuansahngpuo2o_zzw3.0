package com.zzw.o2o.shop;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.zzw.o2o.entity.PersonInfo;

public class ShopLoginInterceptor extends HandlerInterceptorAdapter {
	/**
	 * 主要在做事前进行拦截,即用户操作发生前,改写preHandle里的逻辑,进行拦截
	 * @param request
	 * @param response
	 * @param handler
	 * @return
	 * @throws Exception
	 */
	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		//从session中获取用户信息
		Object userObj = request.getSession().getAttribute("user");
		if (userObj != null) {
			//若用户信息不为空,则将session里的用户信息转换成PersonInfo实体类对象
			PersonInfo user = (PersonInfo) userObj;
			//做空值判断,确保userId不为空并且该账号的可用状态为1,并且用户类型为店家
			if (user != null && user.getUserId() != null
					&& user.getUserId() > 0 && user.getEnableStatus() == 1
					/*&& user.getShopOwnerFlag() == 1*/)
				//若通过验证则返回true,拦截器返回true以后,用户接下来的操作得以正常执行
				return true;
		}
		//若不满足登陆验证,则直接跳转到账号登陆页面
		PrintWriter out = response.getWriter();
		out.println("<html>");
		out.println("<script>");
		out.println("window.open ('" + request.getContextPath()	+ "/shop/ownerlogin','_self')");
		out.println("</script>");
		out.println("</html>");
		return false;
	}
}
