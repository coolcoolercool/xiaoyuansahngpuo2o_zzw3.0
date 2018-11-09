package com.zzw.o2o.service;

import com.zzw.o2o.exception.LocalAuthOperationException;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.zzw.o2o.dto.LocalAuthExecution;
import com.zzw.o2o.entity.LocalAuth;

public interface LocalAuthService {
	/**
	 * 通过账号和密码获取平台账号信息
	 * @param userName
	 * @return
	 */
	LocalAuth getLocalAuthByUserNameAndPwd(String userName, String password);

	/**
	 * 通过userId获取平台账号信息
	 * @param userId
	 * @return
	 */
	LocalAuth getLocalAuthByUserId(long userId);

	/**
	 *
	 * @param localAuth
	 * @param profileImg
	 * @return
	 * @throws RuntimeException
	 */
	LocalAuthExecution register(LocalAuth localAuth,
                                CommonsMultipartFile profileImg) throws RuntimeException;

	/**
	 * 绑定微信,生成平台专属的账号(这里尚未开发)
	 * @param localAuth
	 * @return
	 * @throws RuntimeException
	 */
	LocalAuthExecution bindLocalAuth(LocalAuth localAuth)
			throws RuntimeException;

	/**
	 * 修改平台账号的登陆密码
	 * @param userId
	 * @param userName
	 * @param password
	 * @param newPassword
	 * @return
	 */
	LocalAuthExecution modifyLocalAuth(Long userId,
                                       String userName,
                                       String password,
                                       String newPassword) throws LocalAuthOperationException;
}
