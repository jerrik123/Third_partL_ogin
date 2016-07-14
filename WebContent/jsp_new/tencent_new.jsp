<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@page import="org.apache.commons.lang.StringUtils"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<!DOCTYPE html>
<html>
<head>
<%
	String jsStr = (String) request.getSession().getAttribute("jsStr");
	if (StringUtils.isNotEmpty(jsStr)) {
%>
<script>alert('<%=jsStr%>');</script>
<%
	request.getSession().removeAttribute("jsStr");
	}
%>
  <meta charset="utf-8">
  <meta name="renderer" content="webkit|ie-comp|ie-stand">
  <meta http-equiv="X-UA-Compatible" content="IE=edge">
  <meta name="format-detection" content="telephone=no" />
  <meta name="apple-mobile-web-app-capable" content="yes" />
  <meta name="viewport" content="width=device-width,user-scalable=yes" />
  <title>第三方登录_芒果网</title>
   <link type="text/css" rel="stylesheet" href="<%=request.getContextPath()%>/skin/css/global.css"/>
  <link type="text/css" rel="stylesheet" href="<%=request.getContextPath()%>/skin/css/login/third_register.css">
 <script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery-1.3.2.js"></script>
 <script type="text/javascript" src="<%=request.getContextPath()%>/js/validate.js"></script>
 <script type="text/javascript" src="<%=request.getContextPath()%>/js/common.js"></script>
 <script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.form.js"></script>
 
  <meta name="description" content="支持会员卡号、用户名、手机号、联名卡号等多种登录方式，非会员可直接进行机票、酒店、旅游度假预订，并在预订成功后自动注册成为会员" />
  <meta name="keywords" content="会员，登录，注册，忘记密码，直接预订，继续预订，会员卡号，用户名，手机号，联名卡号，证件，代理编号，代理卡号"/>
  <!--[if lt IE 10]>
   <link type="text/css" rel="stylesheet" href="../skin/css/login/login-ie.css">
<![endif]-->
</head>
<body>

  <!-- 登录头部 [[-->
  <div class="log_header">
    <a href="http://www.mangocity.com/">
      <h1>芒果网</h1>
      <img src="<%=request.getContextPath()%>/skin/img/login/logo.gif" alt="芒果网logo">
    </a>
    <span></span>
    <h2>会员登录</h2>
  </div>
  <!-- 登录头部 ]]-->
  <div class="log_content clear">
    <div class="log_con_l">
      <img src="<%=request.getContextPath()%>/skin/img/login/huiyuanka.png" alt="登录"></div>
    <div class="log_con_r">
      <div class="thirdparty clear">  
          <!-- 表单 [[ -->
        <div class="content " >
          <div class="con1">亲爱的QQ用户：你好,欢迎来到芒果网</div>
          <div class="con4">
            <div class="con5">我想绑定已有的芒果网账号</div>
            <form action="<%=request.getContextPath()%>/tencent_new/register.action" name="bindForm" id="bindForm" method="post" onsubmit="return false;">
            	<input type="hidden" name="nick" id="nick" value="${nick}"/>
				<input type="hidden" name="registerBean.registertype" value="1">
				<input type="hidden" name="registerBean.registermode" value="2">
            <div class="form_line">
              <label for="bindLoginType">登录名</label>
              <input name="registerBean.phonenumber" id="bindLoginType" type="text"  maxlength="32" onfocus="HideErrorNote(this)"/><!-- 默认是手机 -->
              <div class="error" id="userNote">请输入正确登录名</div>
             
              </div>
            <div class="form_line">
              <label for="bindBean.password">密码</label>
              <input name="registerBean.password" id="bindBean.password" onfocus="HideErrorNote(this)" type="password" maxlength="12" class="txtin w172" placeholder="6-20字母、数字和字符" />
              <div class="error" id="passwordNote">请输入正确密码</div>
            </div>
            <div class="form_line yan">
							<label for="name">验证码</label>
							<input name="randCode" id="bindRandCode" type="text" value=""  onfocus="HideErrorNote(this)" />
							<img id="image" onclick="changeImage('image');"  src="<%=request.getContextPath()%>/jsp/checkimage.jsp" />
							<a href="javascript:;" onclick="javascript:changeImage('image');">换一张</a>
							<div class="error" id="verfcodeNote">请输入正确登录名</div>
			</div>
			<div class="automatic_login">
				<input type="submit" value="登录并绑定" class="subt" onclick="bindAccount();"/>
				<a href="javascript:forgetPassword()" class="mi">忘记密码？</a>
			</div>
          </form>
          </div>
       </div>
       <!-- 表单 ]] -->
       <div class="con_r">
         <div class=" binding " onclick="javascript:imLogin();">不绑定，直接登录</div>
       </div>
        <!--  -->
      </div>
    </div>
  </div>
<!--#include virtual="/inc/footer.html"-->
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jQuery_1.8.3.js"></script>
<!--[if lt IE 10]>
<script type="text/javascript">
$(".form_line").click(function(){
  $(this).children('.tishi').hide();
  $(this).children('input').focus();
})
  </script>
<![endif]-->
<script type="text/javascript">
var isEnableOpen_mobile =false, isEnableOpen_email =false;	
window.onload=function () {
	$("div.error").hide();
}
//忘记密码
function forgetPassword()	{
    window.location.href='http://www.mangocity.com/mbrWebCenter/password/init.action';
}	
//判断是否是邮箱或者手机号
function isEmailOrPhone(id){
	var regexEmail= /^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+(.[a-zA-Z0-9_-])+/;
	var regexPhone = /^1\d{10}$/;
	var loginName = id;
	if(regexEmail.test(loginName)) return 'E';
	if(regexPhone.test(loginName)) return 'M';
	return '';
}
//验证码 换一张
function changeImage(id){
	var path='<%=request.getContextPath()%>/jsp/checkimage.jsp?';
	var image=document.getElementById(id);
	var now = new Date();
	var ss = now.getTime();
	image.src=path+ss;
}

//直接登陆
function imLogin(){
	window.location.href='<%=request.getContextPath()%>/tencent_new/register.action?registerBean.registermode=3&registerBean.registertype=1&nick=${nick}';
}
 
 //点击绑定登陆,触发事件
function bindAccount(){
	var flagNote = false;
	var result = isEmailOrPhone($("#bindLoginType").val());
	var password = $("input[type=password]").val();
	if(!password){
		//alert('密码不能为空');
		// passwordNote
		AddErrorNote("passwordNote" , "密码不能为空");
		flagNote = true;
		//return;
	}
	//根据输入登陆账户,判断是手机还是邮箱,设置隐藏域
    if(result == 'E'){
		document.getElementById("bindLoginType").setAttribute("name","registerBean.email");
    }else if(result == 'M'){//电话
		document.getElementById("bindLoginType").setAttribute("name","registerBean.phonenumber");
    }else{
    	//alert('请输入正确的邮箱或者手机号.');
    	AddErrorNote("userNote" , "请输入正确的邮箱或者手机号");
    	flagNote = true;
    	//return;
    }
	if(flagNote) return;
	document.forms['bindForm'].submit();
    <%-- var url="<%=request.getContextPath()%>/weibo/validateDoLogin.action?loginid="+loginid+'&selecttype='+selecttype+'&password='+password;
            $.post(url,function(responseText) {
                if('1N' == responseText ){
	    	    alert('手机号码或者密码错误，绑定失败');
                }else if ('6N' == responseText){
	    		alert('电子邮箱或者密码错误，绑定失败');
	    		rtnBoolean = false;
	    		return ;
                } else {
                  document.weiboform.submit();
                }
            }); --%>

}
 
  function register() {
    var mobile = document.getElementById('mobile').value;
    var email = document.getElementById('email').value;
   	if (IsStringNull(mobile)) {
	    	document.getElementById('validateMobile1').style.display='none';
			document.getElementById('validateMobile2').style.display='none';
			document.getElementById('validateMobile3').style.display='none';
			document.getElementById('validateMobile4').style.display='none';
			document.getElementById('validateMobile5').style.display='block';
	    	return;
  	} 
    
   	if (IsStringNull(email)) {
	     	document.getElementById('validateEmail1').style.display='none';
			document.getElementById('validateEmail2').style.display='none';
			document.getElementById('validateEmail3').style.display='none';
			document.getElementById('validateEmail4').style.display='none';
			document.getElementById('validateEmail5').style.display='block';
	    	return;
  	} 
  validateMobile(document.getElementById("mobile"));
  validateEmail(document.getElementById("email"));
  document.getElementById('loginType').value="lor";
  if(isEnableOpen_mobile&&isEnableOpen_email){
    document.weiboform.submit();
  }
   
 }
 
 
 function validateMobile(obj){
 var mobileTip = document.getElementById("validateMobile");
	if (!isMobile(obj.value)) {
	    		document.getElementById('validateMobile1').style.display='none';
				document.getElementById('validateMobile2').style.display='none';
				document.getElementById('validateMobile3').style.display='none';
				document.getElementById('validateMobile4').style.display='block';
				document.getElementById('validateMobile5').style.display='none';
	    		return false;
	    	}else{
	    	var  rtnBoolean = false;
	    	
         var url="<%=request.getContextPath()%>/validate/validateMobile.action?mobileNo="+$.trim(obj.value);
    
            $.post(url,function(responseText) {
                if('N' == responseText ){
                	isEnableOpen_mobile = true;
	                document.getElementById('validateMobile1').style.display='none';
					document.getElementById('validateMobile2').style.display='none';
					document.getElementById('validateMobile3').style.display='block';
					document.getElementById('validateMobile4').style.display='none';
					document.getElementById('validateMobile5').style.display='none';
					rtnBoolean = true;
                	return;
                }else{
	    		isEnableOpen_mobile = false;
	    		document.getElementById('validateMobile1').style.display='none';
				document.getElementById('validateMobile2').style.display='block';
				document.getElementById('validateMobile3').style.display='none';
				document.getElementById('validateMobile4').style.display='none';
				document.getElementById('validateMobile5').style.display='none';
	    		return ;
                }
                return rtnBoolean;
            }
            );
            return rtnBoolean;
	    	}
 }
 
 //  当用户输入电子邮件时进行验证
    function validateEmail(email){
    var emailTip = document.getElementById("validateEmail");
    	if (IsStringNull(email.value)) {
	     	    document.getElementById('validateEmail1').style.display='block';
				document.getElementById('validateEmail2').style.display='none';
				document.getElementById('validateEmail3').style.display='none';
				document.getElementById('validateEmail4').style.display='none';
				document.getElementById('validateEmail5').style.display='none';
	    		return false;
    	}
    	if (!IsStringNull(email.value)) {
        	if (!IsValidateEmail(email.value)) {
	     	    document.getElementById('validateEmail1').style.display='none';
				document.getElementById('validateEmail2').style.display='none';
				document.getElementById('validateEmail3').style.display='none';	
				document.getElementById('validateEmail4').style.display='block';
				document.getElementById('validateEmail5').style.display='none';		
            	return false;
        	}else{
        	    var rtnBoolean = false;
        	    var url="<%=request.getContextPath()%>/validate/validateEmail.action?emailNo="+$.trim(email.value);
               $.post(url,function(responseText) {
                //alert(responseText);
                if('N' == responseText){
                isEnableOpen_email=true;
	     	    document.getElementById('validateEmail1').style.display='none';
				document.getElementById('validateEmail2').style.display='none';
				document.getElementById('validateEmail3').style.display='block';
				document.getElementById('validateEmail4').style.display='none';
				document.getElementById('validateEmail5').style.display='none';
                 rtnBoolean = true;
                return ;
                }else{
                isEnableOpen_email = false;
	     	    document.getElementById('validateEmail1').style.display='none';
				document.getElementById('validateEmail2').style.display='block';
				document.getElementById('validateEmail3').style.display='none';
				document.getElementById('validateEmail4').style.display='none';
				document.getElementById('validateEmail5').style.display='none';
	    		return ;
                }
            }//end function;
            );
            return rtnBoolean;
        	}
        	//判断电子邮件是否重复
    	}
    }
 
    function loginMethod(selectit){
        document.getElementById("validateloginid").style.display="none";
    	var method = selectit.value;
    	if(method=="1"){
    	}else if(method=="6"){
    	}
     }
    
    //页面加载
    $(function(){
    	 var registerMode='${registerBean.registermode}';
    	var errorId='${errorId}';
    	var flagNote = false;
    	 if(registerMode==2){
    		 if(errorId==1){
    				var phoneBind=document.getElementById("phoneBind");
    				var panel=document.getElementById("requestTypePanel");
    				//alert("手机号或密码有误!!");
    				AddErrorNote("userNote" , "手机号或密码有误");
    				flagNote = true;
    		 }
    		 if(errorId==2){
    				//alert("邮箱或密码有误!!");
    				AddErrorNote("userNote" , "请输入正确的邮箱或者手机号");
    				flagNote = true;
    		 }
    		 if(errorId==6||errorId==7){
    			 //alert("验证码不能为空!!");
    			 AddErrorNote("verfcodeNote" , "验证码不能为空");
    			 flagNote = true;
    			 //return false;
    		 }
    		 if(errorId==8){
    			 //alert("验证码有误!!");
    			 AddErrorNote("verfcodeNote" , "验证码有误");
    			 flagNote = true;
    			// return false;
    	 
    		 }
    		 if(errorId==11){
    			 //alert("账号不能被重复绑定.");
    			 AddErrorNote("userNote" , "账号不能被重复绑定");
    			 flagNote = true;
    			// return false;
    		 }
    		 
    	 }else if(registerMode==1){
    		 if(errorId==3){
    			//alert("手机号已被使用!!");
    			AddErrorNote("userNote" , "手机号已被使用");
    			flagNote = true;
    		 }
    		if(errorId==4){
    			//alert("邮箱已被使用!!");
    			AddErrorNote("userNote" , "邮箱已被使用");
    			flagNote = true;
    		}
    		 if(errorId==6||errorId==7){
    			 //alert("验证码不能为空!!");
    			 AddErrorNote("verfcodeNote" , "验证码不能为空");
    			 flagNote = true;
    			// return false;
    		 }
    		 if(errorId==8){
    			 //alert("验证码有误!!");
    			 AddErrorNote("verfcodeNote" , "验证码有误");
    			 flagNote = true;
    			 //return false;
    		 }
    	 }else{
    	 } 
    	 $("div.error").hide();
    	 if(flagNote) return;
    	 $("div.error").hide();
    });
    function AddErrorNote(obj , msg) {
    	$("#" + obj).text(msg);
    	$("#" + obj).show();
    }
    function HideErrorNote(obj) {
    	$(obj).siblings("div.error").hide();
    }
</script>
</body>
</html>