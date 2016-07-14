package com.mangocity.thirdparty.login.utils;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;

public class CommonUtil {

	/*public static String getOperator() {
		//默认取MBRADMIN
		String operator = "MBRADMIN";
		Object object = SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();

		if (object instanceof UserWithDomain) {
			UserWithDomain userWithDomain = (UserWithDomain) object;
			User user = userWithDomain.getDomainUser();
			if (null != user && null != user.getLoginName()
					&& !user.getLoginName().equals("")) {
				operator = user.getLoginName();
			}
		}
		return operator;
	}*/

	public static String getOpIp(HttpServletRequest httpServletRequest) {
		String ipAddress = null;
		// ipAddress = this.getRequest().getRemoteAddr();
		ipAddress = httpServletRequest.getHeader("x-forwarded-for");
		if (ipAddress == null || ipAddress.length() == 0
				|| "unknown".equalsIgnoreCase(ipAddress)) {
			ipAddress = httpServletRequest.getHeader(
					"Proxy-Client-IP");
		}
		if (ipAddress == null || ipAddress.length() == 0
				|| "unknown".equalsIgnoreCase(ipAddress)) {
			ipAddress = httpServletRequest.getHeader(
					"WL-Proxy-Client-IP");
		}
		if (ipAddress == null || ipAddress.length() == 0
				|| "unknown".equalsIgnoreCase(ipAddress)) {
			ipAddress = httpServletRequest.getRemoteAddr();
			if (ipAddress.equals("127.0.0.1")) {
				// 根据网卡取本机配置的IP
				InetAddress inet = null;
				try {
					inet = InetAddress.getLocalHost();
				} catch (UnknownHostException e) {
					e.printStackTrace();
				}
				ipAddress = inet.getHostAddress();
			}

		}

		// 对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
		if (ipAddress != null && ipAddress.length() > 15) { // "***.***.***.***".length()
															// = 15
			if (ipAddress.indexOf(",") > 0) {
				ipAddress = ipAddress.substring(0, ipAddress.indexOf(","));
			}
		}
		return ipAddress;
	}
	
	/**
	 * 获取随机密码6位数字
	 * @return
	 */
	public static String getRandomPwd(){
		String password = "";
		for (int mm = 0; mm < 100; mm++) {
            Random rdm = new Random();
            String temp = Integer.toString(Math.abs(rdm.nextInt()));
            if (temp.trim().length() > 6) {
                password = temp.substring(0, 6);
            } else {
                password = temp;
            }
            if (!"111111".equals(password) && !"222222".equals(password)
                && !"333333".equals(password) && !"444444".equals(password)
                && !"555555".equals(password) && !"666666".equals(password)
                && !"777777".equals(password) && !"888888".equals(password)
                && !"999999".equals(password) && !"000000".equals(password)
                && !"123456".equals(password) && !"654321".equals(password)) {
                break;
            }
        }
		return password;
	}
	
}
