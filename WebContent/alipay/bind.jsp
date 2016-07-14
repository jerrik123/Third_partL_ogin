<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>会员绑定</title>
<link rel="stylesheet" type="text/css" href="http://wres.mangocity.com/css/w/memberbind/memberbind.css" />
<script type="text/javascript" src="http://www.mangocity.com/ThirdPartyLogin/js/validate.js"></script>
<script type="text/javascript" src="http://www.mangocity.com/ThirdPartyLogin/js/common.js"></script>
<script type="text/javascript" src="http://www.mangocity.com/ThirdPartyLogin/js/prototype.js"></script>
<script type="text/javascript" src="http://www.mangocity.com/ThirdPartyLogin/js/jquery-1.3.2.js"></script>
<script type="text/javascript" src="http://www.mangocity.com/ThirdPartyLogin/js/jquery.form.js"></script>
<script type="text/javascript">
	$(document).ready(function(){
		var nickValue = document.getElementById("nick").value;
		if(nickValue != null && nickValue!=""){
			nickValue= nickValue.replace(/</g,"&lt;").replace(/>/g,"&gt;");
		}
		document.getElementById("nickValue").innerHTML = nickValue;
	});
	<%
		String message = (String)request.getAttribute("message");
		if(message != null && !"".equals(message))
		{
	%>
		alert('<%=message %>');
	<% }%>
	
	$(document).ready(function(){
		var mobile = '<%=request.getAttribute("mobile")%>';
		if(mobile != null && mobile != "" && mobile != 'null')
		{
			document.getElementById("phoneBind").click();
			document.getElementById("bindLoginType").value = mobile;
			return;
		}
		var email = '<%=request.getAttribute("email")%>';
		if(email != null && email != "" && email != 'null')
		{
			document.getElementById("mailBind").click();
			document.getElementById("bindLoginType").value = email;
			return;
		}
	});
</script>
</head>

<body>

<!--=S head-->
<script type="text/javascript" src="http://www.mangocity.com/include/css/tel.js"></script>

<div id="head_mango">

	<jsp:include page="common/topmenu.jsp" flush="true" />

</div>

<!--=E head-->

<div class="w968">
    <div class="bindtit"><h1>绑定芒果会员,享受一站式在线旅游生活!</h1></div>
    <div class="statement">
        <div class="welcomeinfo" style="background:url(img/alipay_logo.jpg) no-repeat;padding-left:110px;">欢迎<em><span id="nickValue"></span></em>来到芒果网<br /><span>来自支付宝</span></div>
        <p class="servicetit">芒果网提供以下服务，满足您的在线旅游生活：</p>

        <div class="serviceList">
            <ul>
                <li><em>机票</em>上芒果,订机票,让您飞翔无限!</li>
                <li><em>酒店</em>上芒果,订酒店,给您家一般的感觉!</li>
                <li><em>度假</em>上芒果,来旅游,你我的轻松时刻!</li>
                <li><em>企业</em>为您提供最专业、专注、高效的差旅管理服务!</li>

            </ul>
        </div>
    </div>
    <div class="bindform">
        <div class="bindNav">
            <ul id="bindNav">
                <li><a href="javascript:void(0);">我是芒果网会员</a></li>
                <li><a href="javascript:void(0);">还不是芒果网会员</a></li>

            </ul>
        </div>
        <div id="bindpanel">
            <div class="bindpanel" id="bindArea">
                <p class="bindNote">您正在使用芒果网的合作方--支付宝的账号登录“芒果网会员”，为方便您日后登录，您可以将支付宝账号与芒果网会员进行绑定。绑定后，您可以用邮箱或手机直接登录芒果网会员：</p>
                <div class="formWrap">
                <form action="<%=request.getContextPath()%>/alipay/loginBand.action" method="post" id="bindForm">
                    <ul><input type="hidden" name="nick" id="nick" value="${nick}"/>
						<input type="hidden" name="registerBean.registertype" value="1"/>
						<input type="hidden" name="registerBean.registermode" value="2"/>
					    <li><label>登录方式：</label>
					    <input id="phoneBind" name="logintype" type="radio" value="1" class="iradio"/> <label class="tordo" for="phone">手机登录</label>
					    <input id="mailBind" name="logintype" type="radio" value="2" class="iradio" /><label class="tordo" for="mail">邮箱登录</label></li>
                        <li><label id="bindLoginText">手机号：</label>
                        <input name="registerBean.phonenumber" id="bindLoginType" type="text" class="txtin w172"  maxlength="32" onfocus="changeType(2)" />
                        <span ><em type="msg" class="errtip" style="display:none;" id="requestTypePanel"></em></span></li>
                        <li><label>您的密码：</label><input name="registerBean.password" id="bindBean.password" type="password" maxlength="12" class="txtin w172" />
                        <span><em type="msg" class="errtip" style="display:none;" id="bindPasswordPanel"></em></span></li>
                        <li><label>验证码:</label><input name="randCode" id="bindRandCode" style="width:60px;" class="txtin" maxlength="6"></input>
                        <img height="25" width="80" id="bindRandImage" onclick="changeImage('bindRandImage');" style="float:left; cursor:pointer;" src="<%=request.getContextPath()%>/jsp/checkimage.jsp" title="看不清,请点击图片"/>
                        <span><em><a href="javascript:;" onclick="javascript:changeImage('bindRandImage');">换一张</a></em><em type="msg" class="errtip" style="display:none;" id="bindRandPanel"></em></span>
                        <div style="width:0;height:0;clear:both;"></div>
                        </li>
                       
                    </ul>
                    <div class="btnWrap"><a href="javascript:void(0);" onclick="javascript:submitForm('bindForm');" class="btn97x27">绑 定</a><a href="http://www.mangocity.com/mbrweb/jsp/member/member_pwd_forgot.jsp" class="fwd">忘记密码&gt;&gt;</a></div>

                </form>
                </div>
            </div>
            
            <div class="bindpanel" style="height:281px;" id="registArea">
                <p class="bindNote">您正在使用芒果网的合作方--支付宝的账号登录“芒果会员”，为方便您日后登录，您可以注册成为芒果网会员，享受更多的会员服务：</p>
                <div class="formWrap">
                <form action="<%=request.getContextPath()%>/alipay/registerBand.action" method="post" id="registForm">
                    <ul>
                    <input type="hidden" name="nick" value="${nick}"/>
                    	<input type="hidden" name="registerBean.registertype" value="1"/>
						<input type="hidden" name="registerBean.registermode" value="1"/>
                        <li><label>手机：</label>
                        <input name="registerBean.phonenumber" id="registerBeanphonenumber" type="text" class="txtin w172" maxlength="11" value="${registerBean.registermode==2?registerBeanphonenumber:''}"/>
                        <span><em type="msg" style="display:none;" class="errtip" id="registerBeanphonenumberpanel"></em></span></li>
                        <li><label>邮箱：</label><input name="registerBean.email" id="registerBeanemail" type="text" maxlength="32" class="txtin w172" length="32" value="${registerBean.registermode==2?registerBeanemail:''}"/>
                        <span><em type="msg" style="display:none;" class="errtip" id="registerBeanemailpanel"></em></span></li>
                        <li><label>密码：</label><input name="registerBean.password" id="registerBeanpassword3" type="password" maxlength="12" class="txtin w172" title="密码必需为6至12位的数字"/>
                        <span><em type="msg" style="display:none;" class="errtip" id="registerBeanpasswordpanel"></em><em id="registerBeanpasswordpanelnote">密码由6-12位数字组成</em></span></li>
                        <li><label>确认密码：</label><input name="registerBean.password2" id="registerBeanpassword2" type="password" maxlength="12" class="txtin w172"  />
                        <span><em type="msg" class="errtip" style="display:none;" id="registerBeanpassword2panel"></em></span></li>
                        
                        <li><label>验证码:</label><input name="randCode" id="registerRandCode" style="width:60px;" class="txtin" maxlength="6"></input>
                        <img height="25" width="80" id="registerRandImage" onclick="changeImage('registerRandImage');" style="float:left; cursor:pointer" src="<%=request.getContextPath()%>/jsp/checkimage.jsp" title="看不清,请点击图片"/>
                        <span><em><a href="javascript:;" onclick="javascript:changeImage('registerRandImage');">换一张</a></em><em type="msg" class="errtip" style="display:none;" id="registerRandPanel"></em></span>
                        <div style="width:0;height:0;clear:both;"></div>
                        </li>
                        
                    </ul>

                    <div class="btnWrap"><a href="javascript:void(0);" class="btn97x27"  onclick="javascript:submitForm('registForm');" >注册并绑定</a></div>
                </form>
                </div>
            </div>
            </div>
    </div>
    <div class="passto"><a href="<%=request.getContextPath()%>/alipay/derectLogin.action">直接进入芒果网&gt;&gt;</a></div>
</div>

<!--=S footer-->
<div class="footer w970">
<%-- 
<jsp:include page="../../headmango/footer.jsp" flush="true" />
--%>

<iframe scrolling="no" frameborder="no" width="100%" id="footer2010" marginheight="0" marginwidth="0" border="0" src="http://www.mangocity.com/include/foot.htm" name="framename"></iframe>
</div>
<!--=E footer -->

<div id="mango-greybox" class="greybox">
    <div class="greycont">
       <strong>您确认要离开本页吗？</strong>
       <p>不注册芒果会员 将无法享受芒果网提供的会员服务<br />您也可以重新进入芒果网 <a href="http://www.mangocity.com/mbrweb/login/init.action">会员登录页</a> 进行注册</p>

       <div class="greybtn"><a href="<%=request.getContextPath()%>/tencent/register.action?registerBean.registermode=3&registerBean.registertype=1" class="btn75x21">确 定</a> <a id="close" href="#" class="btn75x21">取 消</a></div>
    </div>
</div>


<script type="text/javascript" src="http://wres.mangocity.com/js/home/cloud/js/jquery-1.3.2.js"></script>
<script type="text/javascript" src="http://wres.mangocity.com/js/w/widget/mango.greybox.js"></script>
<script type="text/javascript">
var registerMode='${registerBean.registermode}';
var errorId='${errorId}';

function changeImage(id){
	var path='<%=request.getContextPath()%>/jsp/checkimage.jsp?';
	var image=document.getElementById(id);
	var now = new Date();
	var ss = now.getTime();

	image.src=path+ss;
	
}

var fileds={
		type:'N',//N reg Y bind
		successClass:'suctip',
		errorClass:'errtip',
		mobile:{filed:'',panel:''},
		mail:{filed:'',panel:''},
		password:{filed:'',panel:''},
		repassword:{filed:'',panel:''}
	};

function changeType(type){
	if(type==2){
		fileds.type='Y';
		fileds.mobile={filed:"registerType",panel:"requestTypePanel"};
	}else{
		fileds.type='N';
		fileds.mobile={filed:"registerBeanphonenumber",panel:"registerBeanphonenumberpanel"};
		fileds.mail={filed:"registerBeanemail",panel:"registerBeanemailpanel"};
		fileds.password={filed:"registerBeanpassword3",panel:"registerBeanpasswordpanel"};
		fileds.repassword={filed:'registerBeanpassword2',panel:'registerBeanpassword2panel'};
	}
}

$(document).ready(function(){
     $('#click').greybox({overlayclose : false, callback : null});
});

var BIND_AREA="bindArea";
var REGIST_AREA="registArea";
(function(){
	var dtab = function(navid, panelid, tabtag, paneltag, index){
		 var navid = (typeof navid == "string") ? document.getElementById(navid) : navid;
		 var panelid = (typeof panelid == "string") ? document.getElementById(panelid) : panelid;
		 var tabs = navid.getElementsByTagName(tabtag);
		 var panels = getbyClass(panelid, 'div', 'bindpanel');
		 var cindex = index;
		 addClass(tabs[cindex].parentNode, 'on');
		 
		 for(var i=0, j=tabs.length; i<j; i++){
		    if(i!=cindex) addClass(panels[i], 'hidepanel');
		    (function(){
			   var p=i;
			   addEvent(tabs[p], "mouseover", function(){
			         if(p!=cindex){
			        	
			        	 var area=panels[p].getAttribute("id");
			             addClass(tabs[p].parentNode, 'on');
						 removeClass(tabs[cindex].parentNode, 'on');
						 
						 removeClass(panels[p], 'hidepanel');
						 addClass(panels[cindex], 'hidepanel');
						 
						 if(area==BIND_AREA){
							 changeImage('bindRandImage');
							 var bindLoginType=document.getElementById("bindLoginType");
							 //if(bindLoginType)
							 //	bindLoginType.focus();
						 }else{
							 changeImage('registerRandImage');
							 var phoneNum=document.getElementById("registerBeanphonenumber");
							 //if(phoneNum)
							 //	phoneNum.focus();
						 }
						 
					 }
					 cindex = p;
					 return false;
			   });
			})();
		 }
		

		 
	}
	
	function getbyClass(parent, tag, name){
	   parent = parent || document;
	   tag = tag || "*";
	   var arr = [];
	   var reg = new RegExp("(^|\\s)" +name+ "(\\s|$)");
	   var ts = parent.getElementsByTagName(tag);
	   for(var i=0, j=ts.length; i<j; i++){
	         if(reg.test(ts[i].className)){
			    arr.push(ts[i]);
			 }
	   }
	   return arr;
	}

	function addClass(elem, name){
	   if(elem.className.length){
		   var classnames = elem.className.split(" ");
		   classnames.push(name);
		   elem.className = classnames.join(" ");
	   }else{
	       elem.className = name;
	   }
	}
	
	function removeClass(elem, name){
	    var reg = new RegExp("(^|\\s)" +name+ "(\\s|$)");
		elem.className = elem.className.replace(reg, '');
	}
	
	function addEvent(obj, type, fn){
		if(obj.addEventListener){
			obj.addEventListener(type, fn, false);
		}else if(obj.attachEvent){
			obj.attachEvent("on" + type, fn);
		}else{
			obj["on" + type] = fn;
		}
	}
	 
	
	 if(registerMode==1){
		 dtab('bindNav','bindpanel', 'a', 'div', 1); //0是默认第一个选项 1是默认第二个选项		 
		 changeImage('registerRandImage');
	 }else{
		 dtab('bindNav','bindpanel', 'a', 'div', 0); //0是默认第一个选项 1是默认第二个选项
		 changeImage('bindRandImage');
	 }
	
	
	
	//选择登录方式
	var logintype = {
	    init : 'phone',
	    phone : {
		    id : 'phone',
			txt: '手机号：',
			nam:'registerBeanphonenumber'
		},
		mail : {
		    id : 'mail',
			txt:'邮箱：',
			nam:'registerBean.mail'
		}
	};
	
	var getlogintype = document.getElementById('getlogintype');
	var errorId='${errorId}';
	
	//set register field name
	var registerType=document.getElementById('registerType');


	addEvent(document.getElementById("phoneBind"),"click",function(){
		var textPanel=document.getElementById("bindLoginText");
		textPanel.innerHTML="手机号：";
		document.getElementById("bindLoginType").value="";
		document.getElementById("bindLoginType").setAttribute("name","registerBean.phonenumber");
		var form=document.getElementById("bindForm");
		var ems=form.getElementsByTagName("EM");
		for(var i=0;i<ems.length;i++){
			ems[i].style.display="none";
		}
	});

	addEvent(document.getElementById("mailBind"),"click",function(){
		var textPanel=document.getElementById("bindLoginText");
		textPanel.innerHTML="邮箱：";
		document.getElementById("bindLoginType").value="";
		document.getElementById("bindLoginType").setAttribute("name","registerBean.email");
		var form=document.getElementById("bindForm");
		var ems=form.getElementsByTagName("EM");
		for(var i=0;i<ems.length;i++){
			ems[i].style.display="none";
		}
		
	});
	
	addEvent(document.getElementById("bindLoginType"),'blur',function(){
		if(document.getElementById("phoneBind").checked==true){
			validateMobile(document.getElementById("bindLoginType"),document.getElementById("requestTypePanel"));
		}else{
			validateEmail(document.getElementById("bindLoginType"),document.getElementById("requestTypePanel"));
		}
	});


/*	
	addEvent(document.getElementById("bindLoginType"), 'blur', function(){
		//bindForm
		var frm=document.getElementById("bindForm");
		
	});
*/
	
	//bind password validate
	addEvent(document.getElementById("bindBean.password"),'blur',function(){
		var panel=document.getElementById("bindPasswordPanel");
		var password=document.getElementById("bindBean.password").value;
		if(password==null||password==""){
			panel.innerHTML="密码不能为空!!";
			panel.style.display="";
		}else{
			panel.style.display="none";
		}
		
		//validateBindPassword(document.getElementById("bindBean.password"),document.getElementById("bindPasswordPanel"));
		
	});
	//bind validate code validate
	addEvent(document.getElementById("bindRandCode"),'blur',function(){
		validateBindCode(document.getElementById("bindRandCode"),document.getElementById("bindRandPanel"));
	});

	//add register filed events.
	addEvent(document.getElementById("registerBeanphonenumber"),'blur',function(){
		validateMobile(document.getElementById("registerBeanphonenumber"),document.getElementById("registerBeanphonenumberpanel"));
	});
	
	addEvent(document.getElementById("registerBeanemail"),'blur',function(){
		validateEmail(document.getElementById("registerBeanemail"),document.getElementById("registerBeanemailpanel"));
	});
	
	addEvent(document.getElementById("registerBeanpassword3"),'blur',function(){
		var s=document.getElementById("registerBeanpassword3");
		var panel=document.getElementById("registerBeanpasswordpanel");
		return validateBindPassword(s,panel);
	});
	
	addEvent(document.getElementById("registerBeanpassword2"),'blur',function(){
		var s=document.getElementById("registerBeanpassword2").value;
		var s1=document.getElementById("registerBeanpassword3").value;
		var panel=document.getElementById("registerBeanpassword2panel");
		
		
		if(s==null||s==''){
			panel.style.display="";
			panel.innerHTML="密码不能为空!!";
			return false;
		}else if(s!=s1){
			panel.style.display="";
			panel.innerHTML="确认密码有误!!";
			return false;
		}else{
			panel.style.display="none";
			return true;
		}
	});
	
	addEvent(document.getElementById("registerRandCode"),'blur',function(){
		validateBindCode(document.getElementById("registerRandCode"),document.getElementById("registerRandPanel"));
	});
//开始初始化操作
//1 phone not exists 2 mail not exists 3 phone exists 4 mail is exists 5 repassword is wrong.
 if(registerMode==2){
	 
	 if(errorId==1){
			var phoneBind=document.getElementById("phoneBind");
			var panel=document.getElementById("requestTypePanel");
			phoneBind.checked="true";
			alert("手机号或密码有误!!");
			panel.innerHTML="手机号或密码有误!!";
			panel.style.display="";
			var textPanel=document.getElementById("bindLoginText");
			textPanel.innerHTML="手机：";
	 }
	 if(errorId==2){
		var mailBind=document.getElementById("mailBind");
		mailBind.checked="true";
		var panel=document.getElementById("bindPasswordPanel");
		alert("邮箱或密码有误!!");
		panel.innerHTML="邮箱或密码有误!!";
		panel.style.display="";
		var textPanel=document.getElementById("bindLoginText");
		textPanel.innerHTML="邮箱：";
	 }
	 
	 if(errorId==6||errorId==7){
		 var panel=document.getElementById("bindRandPanel");
		 alert("验证码不能为空!!");
		 panel.innerHTML="验证码不能为空!!";
		 panel.style.display="";
		 return false;
	 }
	 if(errorId==8){
		 var panel=document.getElementById("bindRandPanel");
		 alert("验证码有误!!");
		 panel.innerHTML="验证码有误!!";
		 panel.style.display="";
		 return false;
 
	 }
	 
 }else if(registerMode==1){
	 var phoneBind=document.getElementById("phoneBind");
	 if(errorId==3){
			var panel=document.getElementById("registerBeanphonenumberpanel");
			phoneBind.checked="true";
			alert("手机号已被使用!!");
			panel.innerHTML="手机号已被使用!!";
			panel.style.display="";
	 }
	if(errorId==4){
		var panel=document.getElementById("registerBeanemailpanel");
		phoneBind.checked="true";
		alert("邮箱已被使用!!");
		panel.innerHTML="邮箱已被使用!!";
		panel.style.display="";
	}
	
	 if(errorId==6||errorId==7){
		 var panel=document.getElementById("registerRandPanel");
		 alert("验证码不能为空!!");
		 panel.innerHTML="验证码不能为空!!";
			panel.style.display="";
			return false;
	 }
	 
	 if(errorId==8){
		 var panel=document.getElementById("registerRandPanel");
		 alert("验证码有误!!");
		 panel.innerHTML="验证码有误!!";
			panel.style.display="";
			return false;
	 }
	 
	
 }else{
	 var phoneBind=document.getElementById("phoneBind");
	 phoneBind.checked="true";
	var textPanel=document.getElementById("bindLoginText");
	textPanel.innerHTML="手机：";
 }

	
})();

function validateMobile(el,panel){
	var s=el.value;
	//var panel=document.getElementById(fileds.mobile.panel);
	if(s==null||s==""){
		panel.innerHTML="手机号不能为空!!";
		panel.style.display="";
		return false;
	}
	var regu =/^[1][3,5,8][0-9]{9}$/; 
	var re = new RegExp(regu); 
	if (re.test(s)) {
		panel.style.display="none";
		return true; 
	}else{
		panel.innerHTML="手机号不正确!!";
		panel.style.display="";
		return false;
	}
}

function validateEmail(el,panel){
	var s=el.value;
 	//var panel=document.getElementById(fileds.mobile.panel);
 	
 	if(s==null||s==""){
 		panel.innerHTML="邮箱地址不能为空!!";
 		panel.style.display="";
 		return false;
 	}
 	var regu =/^[\w-]+(\.[\w-]+)*@[\w-]+(\.[\w-]+)+$/; 
 	var re = new RegExp(regu); 
 	if (re.test(s)) {
 		panel.style.display="none";
 		return true; 
 	}else{ 
 		panel.innerHTML="格式xx@xxx.xxx!!";
 		panel.style.display="";
 		return false; 
 	}
 	
 }


function validateBindPassword(el,panel){
	//var panel=document.getElementById(panel);
	var notepanel=document.getElementById("registerBeanpasswordpanelnote");
	var viewNote=function(note){
		if(note)
			note.style.display="";
	};
	var closeNote=function(note){
		if(note)
			note.style.display="none";
	};
	if(el.value!=null&&el.value!=""){
		panel.style.display="none";
		viewNote(notepanel);
		var passd=el.value;
		var regu=/^[0-9]{6,12}$/;
		var regu1=/^[0]{6}|[1]{6}|[2]{6}|[3]{6}|[4]{6}|[5]{6}|[6]{6}|[7]{6}|[8]{6}|[9]{6}$/;
		if (regu.test(passd)){
			panel.style.display="none";
		}else{
			closeNote(notepanel);
			panel.innerHTML="密码由6-12位数字组成!!";
			panel.style.display="";
			return false;
		}
		
		if (!validatePasswordHelper(passd)){
			panel.style.display="none";
		}else{
			closeNote(notepanel);
			panel.innerHTML="密码太简单!!";
			panel.style.display="";
			
			return false;
		}
		return true;
	}else{
		panel.innerHTML="密码不能为空!!";
		closeNote();
		panel.style.display="";
		
		return false;
	}
}

function validatePasswordHelper(password){
	var sum=0;
	for(var i=0;i<password.length;i++){
		sum+=password.charAt(i)*1;
	}
	if(password[0]*password.length==sum
			&&password[2]*password.length==sum
			&&password[3]*password.length==sum
			&&password[4]*password.length==sum)
		return true;
	if(password.length%2==0){
		return sum==(password.charAt(0)*1+password.charAt(password.length-1)*1)*(password.length/2);
	}else{
		return sum==((password.charAt(0)*1+password.charAt(password.length-1)*1)/2)*password.length;
	}
}

function validateBindCode(el,panel){
	//var panel=document.getElementById(panel);
	if(el.value!=null&&el.value!=""){
		panel.style.display="none";
		return true;
	}else{
		panel.innerHTML="验证码不能为空!!";
		panel.style.display="";
		return false;
	}

	
}



function submitForm(frm){
	//validate
	var frmObj=document.getElementById(frm);
	if(frm=='bindForm'){
		var password=document.getElementById("bindBean.password");
		var passwordPanel=document.getElementById("bindPasswordPanel");
		var randCodePanel=document.getElementById("bindRandPanel");
		var code=document.getElementById("bindRandCode");
		if(password==null||password==""){
			passwordPanel.innerHTML="密码不能为空!!";
			
			passwordPanel.style.display="";
			return;
		}
//		if(!validateBindPassword(password,passwordPanel)){
//			return;
//		}
		if(!validateBindCode(code,randCodePanel)){
			return;
		}
	}
	if(frm=='registForm'){
		var mobile=document.getElementById("registerBeanphonenumber");
		var mobilePanel=document.getElementById("registerBeanphonenumberpanel");
		var mail=document.getElementById("registerBeanemail");
		var mailPanel=document.getElementById("registerBeanemailpanel");
		var password=document.getElementById("registerBeanpassword3");
		var passwordPanel=document.getElementById("registerBeanpasswordpanel");
		var password2=document.getElementById("registerBeanpassword2");
		var passwordPanel2=document.getElementById("registerBeanpassword2panel");
		
		var randCodePanel=document.getElementById("registerRandPanel");
		var code=document.getElementById("registerRandCode");
	
		if(!validateMobile(mobile,mobilePanel))
			return false;
		if(!validateEmail(mail,mailPanel))
			return false;
		if(!validateBindPassword(password,passwordPanel))
			return false;
		if(!validateBindPassword(password2,passwordPanel2))
			return;
		if(!validateBindCode(code,randCodePanel)){
			return false;
		}
		
		
		
	}
	if(frmObj){
		var ems = frmObj.getElementsByTagName("EM");//document.getElementByTagName("EM");
		//alert("ok1");
		for(var i=0;i<ems.length;i++){
			if(ems[i].style.display!='none'&&ems[i].className=="errtip")//如果有错则取消提交
				return false;
		}
		//return true;
		//alert("ok");
		frmObj.submit();
	}
}
	//$(document).ready(function(){
		//var regist = '<%=request.getAttribute("regist")%>';
		//if(regist != null && regist != "" && regist != 'null')
		//{
			//dtab('bindNav','bindpanel', 'a', 'div', 1); //0是默认第一个选项 1是默认第二个选项		 
		 	//changeImage('registerRandImage');
			//return;
			//jQuery(jQuery('#bindNav').children('li')[1]).mouseover();
			//dtab('bindNav','bindpanel', 'a', 'div', 1); //0是默认第一个选项 1是默认第二个选项		 
		 	//changeImage('registerRandImage');
		//}
	//});
</script>

</body>
</html>
