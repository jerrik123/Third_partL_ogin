<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">

<head>

<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />

<title>QQ OPENID</title>
</head>
<body>
<form name="form1" method="post" action="<%=request.getContextPath()%>/tencent/login.action">
<table>
  <tr>
    <td><input type="submit" name="submit" value="Login By QQ"></td>
    
  </tr>
</table>
<!--  
</form>
<form name="form2" method="post" action="<%=request.getContextPath()%>/tencent/register.action">
<input type="hidden" name="registerBean.registertype" value="1">
<input type="hidden" name="registerBean.registermode" value="1">
<table>
  <tr>
    <td>phonenumber:<input type="text" name="registerBean.phonenumber"></td>
     <td>email:<input type="text" name="registerBean.email"></td>
    <td>password:<input type="text" name="registerBean.password"></td>  
  </tr>
  <tr>
    
    <td><input type="submit" name="submit" value="Submit"></td>
    
  </tr>
</table>
</form>
-->
</body>

</html>