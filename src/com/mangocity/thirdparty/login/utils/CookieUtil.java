package com.mangocity.thirdparty.login.utils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

public class CookieUtil {
    /**
     * 设置cookie
     * 
     * @param key
     *            键
     * @param value
     *            值
     * @param maxAge
     *            有效时间
     * @param domain
     *            域
     * @param path
     *            路径
     * @param response
     *            响应
     */
    public static void writeCookie(String key, String value, Integer maxAge, String domain,
        String path, HttpServletResponse response) {
        Cookie namecookie = new Cookie(key, value);
        if (maxAge != null) {
            namecookie.setMaxAge(maxAge);
        }
        if (StringUtils.isNotBlank(domain)) {
            namecookie.setDomain(domain);
        }
        if (StringUtils.isNotBlank(path)) {
            namecookie.setPath(path);
        }
        response.addCookie(namecookie);

    }

    /**
     * @param key
     *            主键
     * @param value
     *            值
     * @param maxAge
     *            有效时间
     * @return void
     */
    public void writeCookie(String key, String value, HttpServletResponse response) {
        Cookie namecookie = new Cookie(key, value);
        namecookie.setMaxAge(60 * 60 * 24 * 365);
        response.addCookie(namecookie);
    }

    /**
     * @param request
     *            请求
     * @param key
     *            主键
     * @return String 读取key 对应的值
     */
    public static String readCookie(String key, HttpServletRequest request) {
        String value = null;
        Cookie[] cookies = request.getCookies();
        Cookie c = null;
        if (cookies != null) {
            for (int i = 0; i < cookies.length; i++) {
                c = cookies[i];
                if (c.getName().equalsIgnoreCase(key)) {
                    value = c.getValue();
                    return value;
                }
            }
        }
        return value;
    }

    public static void removeCookie(String key, String domain, String path,
        HttpServletRequest request, HttpServletResponse response) {

        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (int i = 0; i < cookies.length; i++) {
                if (cookies[i].getName().equals(key)) {
                    cookies[i].setValue("0");
                    cookies[i].setMaxAge(0);
                    if (StringUtils.isNotEmpty(domain)) {
                        cookies[i].setDomain(domain);
                    }
                    cookies[i].setPath("/");
                    response.addCookie(cookies[i]);
                }

            }
        }
    }
}