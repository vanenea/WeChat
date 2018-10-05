package com.chen.utils;

/**
 * 响应命令
 * @author Administrator
 *
 */
public class ResponseCommand {

	/**
	 * 注册响应
	 */
	public static final short REG_RESPONSE = 1;
	/**
	 * 登陆响应
	 */
	public static final short LOGIN_RESPONSE = REG_RESPONSE + 1;
	/**
	 * 刷新用户列表
	 */
	public static final short USER_LIST_RESPONSE = LOGIN_RESPONSE + 1;
	/**
	 * 私聊响应
	 */
	public static final short MESSAGE_TO_ONE_RESPONSE = USER_LIST_RESPONSE + 1;
	/**
	 * 群聊响应
	 */
	public static final short MESSAGE_TO_ALL_RESPONSE = MESSAGE_TO_ONE_RESPONSE + 1;
	/**
	 * 私人发送文件求情响应
	 */
	public static final short FILE_TO_ONE_RESPONSE = MESSAGE_TO_ALL_RESPONSE + 1;
	/**
	 * 收件人回执响应
	 */
	public static final short FILE_TO_ONE_1_RESPONSE = FILE_TO_ONE_RESPONSE + 1;
	/**
	 * 执行私人发送文件响应
	 */
	public static final short FILE_TO_ONE_2_RESPONSE = FILE_TO_ONE_1_RESPONSE + 1;
}
