package com.mangocity.thirdparty.login.utils;

/**
 * 对特殊字符进行转义
 * @author chenjun
 *
 */
public class CodeFilter {
	/**  
	 * to db  
	 * @param s  
	 * @return  
	 */
	public static String toHtml(String s) {

		s = Replace(s, "&", "&amp;");
		s = Replace(s, "<", "&lt;");
		s = Replace(s, ">", "&gt;");
		s = Replace(s, "\t", "&nbsp;&nbsp;&nbsp;&nbsp;");
		s = Replace(s, "\r\n", "\n");
		s = Replace(s, "\n", "<br>");
		s = Replace(s, "  ", "&nbsp;&nbsp;");
		s = Replace(s, "'", "&#39;");
		s = Replace(s, "\\", "&#92;");
		
		/*s = Replace(s, "&", "\\&");
		s = Replace(s, "<", "\\<");
		s = Replace(s, ">", "\\>");
		s = Replace(s, "\t", "&nbsp;&nbsp;&nbsp;&nbsp;");
		s = Replace(s, "\r\n", "\\\r\\\n");
		s = Replace(s, "\n", "\\\n");
		s = Replace(s, "  ", "&nbsp;&nbsp;");
		s = Replace(s, "'", "&#39;");
		s = Replace(s, "\\", "&#92;");*/

		if (s == null) {
			s = "";
		}
		if (s != null && !s.equals("")) {
			s = s.trim();
		}
		
		return s;
	}

	/**  
	 * to front  
	 * @param s  
	 * @return  
	 */
	public static String unHtml(String s) {
		s = Replace(s, "&amp;", "&");
		s = Replace(s, "&nbsp;", " ");
		s = Replace(s, "&#39;", "'");
		s = Replace(s, "&lt;", "<");
		s = Replace(s, "&gt;", ">");
		s = Replace(s, "<br>", "\n");
		s = Replace(s, "?D", "—");
		return s;
	}

	private static String Replace(String s, String s1, String s2) {
		if (s == null) {
			return null;
		}
		StringBuffer stringbuffer = new StringBuffer();
		int i = s.length();
		int j = s1.length();
		int k;
		int l;
		for (k = 0; (l = s.indexOf(s1, k)) >= 0; k = l + j) {
			stringbuffer.append(s.substring(k, l));
			stringbuffer.append(s2);
		}

		if (k < i) {
			stringbuffer.append(s.substring(k));
		}
		return stringbuffer.toString();
	}
}
