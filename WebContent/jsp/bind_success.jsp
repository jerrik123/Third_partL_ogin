<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>会员绑定成功</title>
<link rel="stylesheet" type="text/css" href="http://wres.mangocity.com/css/w/memberbind/memberbind.css" />
<script type="text/javascript" src="http://www.mangocity.com/include/css/tel.js"></script>
<script type="text/javascript" src="http://www.mangocity.com/ThirdPartyLogin/js/jquery-1.3.2.js"></script>
<script type="text/javascript" src="http://www.mangocity.com/ThirdPartyLogin/js/jquery.form.js"></script>
<script type="text/javascript">
	$(document).ready(function(){
		var nickValue = document.getElementById("nick").value;
		if(nickValue != null && typeof(nickValue) != 'undefined' && nickValue!=""){
			nickValue= nickValue.replace(/</g,"&lt;").replace(/>/g,"&gt;");
		}
		document.getElementById("nickValue").innerHTML = nickValue;
	});
	
	//base cookie name get cookie value
    function getUserName(name) 
    {         
        var arr,reg=new RegExp("(^| )"+name+"=([^;]*)(;|$)");
        if(arr=document.cookie.match(reg))
        return arr[2]; 
        else 
        return null; 
    }
</script>
</head>

<body>

<!--=S head-->


<div id="head_mango">
	<jsp:include page="common/topmenu.jsp" flush="true" />
</div>

<!--=E head-->

<div class="w968">
    <div class="bindtit"><h1>绑定芒果会员,享受一站式在线旅游生活!</h1></div>
    <div class="feedback">
        <div class="welcomeinfo"><em><span id="nickValue"></span></em><br /><span>来自QQ</span></div>
        <input type="hidden" name="nick" id="nick" value="${nick}"/>
        <div class="successinfo">
            <strong>恭喜您绑定成功</strong>

            <p class="notice">您以后也可以通过QQ账号登录“芒果网会员”了</p>
            <p><a href="http://www.mangocity.com/">返回芒果网首页&gt;&gt;</a></p>
        </div>
    </div>
</div>

<!--=S footer-->
<div class="footer w970"><iframe scrolling="no" frameborder="no" width="100%" id="footer2010" marginheight="0" marginwidth="0" border="0" src="http://www.mangocity.com/include/foot.htm" name="framename"></iframe></div>
<!--=E footer -->

<script type="text/javascript">
	var userName_99 = getUserName("userName");
	var _ozprm="username="+userName_99;//明细数据
</script>
<script type="text/javascript" src="http://wres.mangocity.com/js/w/common/js/o_code.js"></script>
</body>
</html>
