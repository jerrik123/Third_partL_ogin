<%@ page language="java" contentType="text/html; charset=utf-8" import="com.mangocity.thirdparty.login.utils.CookieUtil"
    pageEncoding="utf-8"%>
<%@page import="java.net.URLDecoder"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
<title>无标题文档</title>
<link href="http://wres.mangocity.com/css/promotion/h111118zhifubao.css" rel="stylesheet" type="text/css" />
<script type="text/javascript">
	function getCookie(objName){//获取指定名称的cookie的值
		var arrStr = document.cookie.split("; ");
		for(var i = 0;i < arrStr.length;i ++){
		var temp = arrStr[i].split("=");
		if(temp[0] == objName) return unescape(temp[1]);
		} 
	}
	function initpage()
	{
		var mbrID = getCookie("mbrID");
		var logoImg = document.getElementById("logoImg");
		var displayname = document.getElementById("displayname");
		if(mbrID == undefined)
		{
			//未登陆
			document.getElementById("logindiv").style.display = "none";
			document.getElementById("notlogin").style.display = "block";
		}else
		{
			//已登陆
			document.getElementById("logindiv").style.display = "block";
			document.getElementById("notlogin").style.display = "none";
			var user_id = getCookie("user_id");
			var fmalipay = document.getElementById("fmalipay");
			if(user_id != undefined && user_id.length == 16)
			{
				//支付宝
				logoImg.src="http://wimg.mangocity.com/img/promotion/h111118zhifubao/logo_zhifubao.jpg";
				//alert(decodeURIComponent(escape(getCookie("real_name"))));
				displayname.innerHTML = decodeURIComponent(escape(getCookie("real_name")));
				fmalipay.style.display="block";
			}else
			{
				//非支付宝
				logoImg.src="img/logo_mango.jpg";
				displayname.innerHTML = getCookie("membercd");
				fmalipay.style.display="none";
			}
		}
	}
</script>
</head>
<body onload="initpage()">
<div class="member" id="logindiv">
          <div class="list_t">
          	<img src="http://wimg.mangocity.com/img/promotion/h111118zhifubao/list_t.jpg" />
          </div>
          <div class="list_main">
            <h2>
				<img id="logoImg"/>
            </h2>
            <p>欢迎
            <strong><span id="displayname"></span>
            </strong>来到<span class="orange">芒果网</span><br /><span id="fmalipay">来自支付宝</span></p>
            <dl>
              <dt>芒果网提供以下服务，满足您的在线旅游生活：</dt>
              <dd class="tb_fj"><span>机票</span>上芒果，订机票，让您飞翔无限！</dd>
              <dd class="tb_jd"><span>酒店</span>上芒果，订酒店，给您家一般的感觉！</dd>
              <dd class="tb_ly"><span>度假</span>上芒果，来旅游，你我的轻松时刻！</dd>
            </dl>
          </div>
          <div class="list_b">
          	<img src="http://wimg.mangocity.com/img/promotion/h111118zhifubao/list_b.jpg" />
          </div>
</div>
<div class="member_login" id="notlogin">
         <div class="list_t"><img src="http://wimg.mangocity.com/img/promotion/h111118zhifubao/list_t.jpg" /></div>
         <div class="login_main">
           <h2><a href="<%=request.getContextPath() %>/alipay/login.action" target="_top"><img src="http://wimg.mangocity.com/img/promotion/h111118zhifubao/login_zfb.jpg" /></a></h2>
           <form action="<%=request.getContextPath() %>/alipay/mbrlogin.action" method="post" target="_top">
	           <div class="meb_login">
	              <ul>
	                <li><label>登录账号：</label><input name="name" id="name" type="text"  value="用户名/手机号码/会员卡号/邮箱"/></li>
	                <li><label>登录密码：</label><input type="password" name="password" id="password" /></li>
	                <li><label>&nbsp;</label><input type="submit"  value="登 录" class="meb_login_btn"/></li>
	              </ul>
	           </div>
           </form>
         </div>
         <div class="list_b"><img src="http://wimg.mangocity.com/img/promotion/h111118zhifubao/list_b.jpg" /></div>
</div>
<script type="text/javascript">
var loginName = "用户名/手机号码/会员卡号/邮箱";

	String.prototype.trim=function(){
　　    return this.replace(/(^\s*)|(\s*$)/g, "");
　　 }
	var input_name = document.getElementById("name");
	input_name.onfocus = function()
	{
		if(input_name.value.trim() == loginName)
		{
			input_name.value = "";
			input_name.style.color = "black";
		}
	}
	input_name.onblur = function()
	{
		if(input_name.value.trim() == "")
		{
			input_name.value = loginName;
			input_name.style.color = "gray";
		}
	}
	function valideForm()
	{
		var flag = true;
		if(input_name.value.trim() == loginName || input_name.value.trim() == "")
		{
			flag = false; 
			alert("请输入登录名称");
		}
		if(document.getElementById("password").value=="")
		{
			flag = false;
			alert("请输入密码");
		}
		return flag;
	}
</script>
</body>
</html>
