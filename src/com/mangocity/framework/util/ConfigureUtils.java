package com.mangocity.framework.util;

import java.util.Locale;
import java.util.ResourceBundle;


/**
 * 读取属性文件工具类
 * 
 * @author chenjun
 * @since 1.0
 */
public class ConfigureUtils {

    private static String hsqlResName = "config.hsql";

    private static String messageResName = "config.Message";

    private static String errorResName = "config.error";

    private static String sysConfig = "config.SysConfig";

    /**
     * 根据proKey读取在hsql文件中对应的属性值，文件路径/config/hsql_zh_CN.properties
     * 
     * @param proKey
     *            hsql文件中的key值
     * @return proKey在hsql文件中对应的属性值
     */
    public static String getHSqlConfig(String proKey, Locale locale) {
        ResourceBundle rb = ResourceBundle.getBundle(hsqlResName, locale);
        return rb.getString(proKey);
    }

    /**
     * 根据messageKey读取在msg文件中对应的属性值，文件路径/config/msg_zh_CN.properties
     * 
     * @param messageKey
     *            msg文件中的messageKey值
     * @return messageKey在msg文件中对应的属性值
     */
    public static String getMessageConfig(String messageKey, Locale locale) {
        ResourceBundle rb = ResourceBundle.getBundle(messageResName, locale);
       
        return rb.getString(messageKey);

    }
    
    public static String getMessage(String key,Object[] params,Locale locale){
    	ResourceBundle rb = ResourceBundle.getBundle(messageResName, locale);
    	StringBuffer builder =new StringBuffer(rb.getString(key));
    	for(int i=0;i<params.length;i++){
    		builder.replace(builder.indexOf("{"+i+"}"),builder.indexOf("{"+i+"}")+("{"+i+"}").length(),params[i].toString());
    	}
    	return builder.toString();
    }

    /**
     * 根据messageKey读取在msg文件中对应的属性值，文件路径/config/msg_zh_CN.properties
     * 
     * @param messageKey
     *            msg文件中的messageKey值
     * @return messageKey在msg文件中对应的属性值
     */
    public static String getSysConfig(String messageKey) {
        ResourceBundle rb = ResourceBundle.getBundle(sysConfig, Locale.getDefault());
        return rb.getString(messageKey);

    }

    /**
     * 根据errorKey读取在error文件中对应的属性值，文件路径/config/error_zh_CN.properties
     * 
     * @param errorKey
     *            error文件中的errorKey值
     * @return errorKey在error文件中对应的属性值
     */
    public static String getErrorConfig(String errorKey, Locale locale) {
        ResourceBundle rb = ResourceBundle.getBundle(errorResName, locale);
        return rb.getString(errorKey);
    }
}
