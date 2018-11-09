package com.zzw.o2o.dao;

import java.util.Date;

import org.apache.ibatis.annotations.Param;

import com.zzw.o2o.entity.LocalAuth;

public interface LocalAuthDao {

	/**
     * 根据userName和password查询用户
	 * @param userName
	 * @param password
	 * @return
	 */
	LocalAuth queryLocalByUserNameAndPwd(@Param("userName") String userName,
                                         @Param("password") String password);

	/**
     * 根据userId查询用户
	 * @param userId
	 * @return
	 */
	LocalAuth queryLocalByUserId(@Param("userId") long userId);

	/**
	 * 添加平台账号
	 * @param localAuth
	 * @return
	 */
	int insertLocalAuth(LocalAuth localAuth);

    /**
     * 通过userId,userName,password更改密码
     * @param userId
     * @param userName
     * @param password
     * @param newPassword
     * @param lastEditTime
     * @return
     */
	int updateLocalAuth(@Param("userId") Long userId,
                        @Param("userName") String userName,
                        @Param("password") String password,
                        @Param("newPassword") String newPassword,
                        @Param("lastEditTime") Date lastEditTime);
}
