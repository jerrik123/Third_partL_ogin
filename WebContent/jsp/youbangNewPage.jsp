<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta content="免费意外险 最高保障60万元—芒果网" name="description"/>
<meta content="免费意外险 最高保障60万元—芒果网" name="keywords"/>
<title>免费意外险 最高保障60万元—芒果网</title>
<link href="http://wres.mangocity.com/css/promotion/album_top.css" rel="stylesheet" type="text/css"/>
<link type="text/css" href="http://wres.mangocity.com/css/promotion/m20130105tiaokuan.css"  rel="stylesheet"/>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery-1.3.2.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.form.js"></script>
</head>
<body>
<div class="header1">
  <iframe name=framename src="http://www.mangocity.com/promotion/album_top.html" width="100%" height="38" frameborder="no" border="0"  marginwidth="0" marginheight="0" scrolling="no"></iframe>
</div>
<div class="mg_banner"><img src="http://wimg.mangocity.com/img/promotion/m20130105tiaokuan/mg_banner1.jpg"/></div>
<div class="mg_banner"><img border="0" usemap="#Map" src="http://wimg.mangocity.com/img/promotion/m20130105tiaokuan/mg_banner2.jpg"/><map id="Map" name="Map"><area href="#a1" coords="834,172,946,203" shape="rect"/></map></div>
<div class="mg_cot"><span class="bold">1.</span>&nbsp;&nbsp;本次活动由芒果网和友邦保险联合主办,赠险由友邦保险承保。<br />
<span class="bold">2.</span>&nbsp;&nbsp;参与本次活动者即表示接受本活动条款和声明。<br />
<span class="bold">3.</span>&nbsp;&nbsp;活动的参与者必须在<strong style="color:#FF0000">广东省、深圳市、上海市、北京市</strong>或<strong  style="color:#FF0000">江苏省</strong>工作或常住。<br />
<span class="bold">4.&nbsp;&nbsp;活动时间：</span>2013年1月18日—2013年4月17日<br />
<span class="bold">5.&nbsp;&nbsp;赠险保险利益：</span> <br />
&nbsp;&nbsp;&nbsp;&nbsp;营运汽车意外保险金：10万元。 <br />
&nbsp;&nbsp;&nbsp;&nbsp;轨道交通及轮船意外保险金：20万元。<br />
&nbsp;&nbsp;&nbsp;&nbsp;航空意外保险金：60万元。<br />
&nbsp;&nbsp;&nbsp;&nbsp;详细内容以保险合同的保险条款为准，欢迎您登录友邦保险的公司网站<br />
&nbsp;&nbsp;&nbsp;&nbsp;<a href="http://www.aia.com.cn" target="_blank">www.aia.com.cn</a>阅读相关的<a href="http://www.aia.com.cn/zh-cn/resources/6d7ceb804d5f5c4fb2d6fa0f90884aae/cyb_20121108.pdf" target="_blank">合同条款</a>并了解具体的保险权益及责任免除事项等重要内容。<br /> 
<span class="bold">6.</span>&nbsp;&nbsp;活动参与者同意友邦保险致电以核实其所填写信息的真实性，以便激活赠险，另同意将<br />
&nbsp;&nbsp;&nbsp;&nbsp;其个人信息用于友邦保险产品的推广和服务之用。<br />
<span class="bold">7.</span>&nbsp;&nbsp;成功获得赠险者将获得抽取<strong style="color:#FF0000">IPad4</strong>大奖的资格，大奖的得主由随即抽取产生,中奖者由主<br/>
&nbsp;&nbsp;&nbsp;&nbsp;办方专人通知领奖。<br />
<span class="bold">8.</span>&nbsp;&nbsp;在法律允许的范围内，芒果网和友邦保险拥有对于本次活动的最终解释权。<br />
&nbsp;&nbsp;&nbsp;&nbsp;<input name="checkbox" type="checkbox"  id="checkbox" value="" /><span class="bold">&nbsp;我已阅读并同意<a href="#" target="_blank">活动条款和声明</a></span><br /><br />
<center>
	<a id="a1" name="a1"></a>
  	<a href="javascript:void(0)" onclick="ddt(this)" id="submitbutton" name="submitbutton"><img src="http://wimg.mangocity.com/img/promotion/m20130105tiaokuan/mg_button.jpg" name="button" /></a>
</center>

</div>
<div class="openmu" id="openmu" style="display:none;"></div>
<div class="suc_tj" id="suc_tj" style="display:none;">
    	<a class="close_tj" href="javascript:void(0);" onclick="closebox()" title="关闭"></a>
        <div><span class="text_tj" ></span></div>
</div>

<script>
var myitem = document.getElementById('checkbox');
var Openmu = document.getElementById("openmu");
var Opensuc = document.getElementById("suc_tj");
	function ddt(dt){
		if(myitem.checked){
			$("#submitbutton").attr("disabled",true);
			var url = "<%=request.getContextPath()%>/youbang/youbangRecord.action";
			$.post(url,'',function(data){
				if(data.result=="1"){
					alert("请登录您的芒果网账户!");
					window.location.href ="http://www.mangocity.com/mbrweb/login/init.action";				
				}else if(data.result=="2"){
					alert("系统异常,请重试!");
				}else if(data.result=="0"){
					Openmu.style.display ="block";
					Opensuc.style.display ="block";
				}else{
					alert("系统异常,请重试!!!");
				}
				$("#submitbutton").attr("disabled",false);		
			},"json");	
		}else{
			alert('你必须同意条款才能申请!')	
		}
	}

	function closebox(){
		Openmu.style.display ="none";
		Opensuc.style.display ="none";
		window.location.href ="http://www.mangocity.com";
	}
	
</script>
<a href="http://e.weibo.com/mangocity20060331?ref=http%3A%2F%2Fwidget.weibo.com%2Frelationship%2Ffollowbutton.php%3Flanguage%3Dzh_cn%26width%3D136%26height%3D24%26uid%3D1785833173%26style%3D2%26btn%3Dred%26dpc%3D1" target="_blank" class="kissguanzhu">&nbsp;</a>
<a class="bshareDiv" href="http://www.bshare.cn/share">分享按钮</a>
<script type="text/javascript" charset="utf-8" src="http://static.bshare.cn/b/buttonLite.js#uuid=01bbdbce-9f67-413e-9886-cf768d61c435&amp;style=3&amp;fs=4&amp;textcolor=#FFFFFF&amp;bgcolor=#FF6600&amp;bp=sinaminiblog,qqmb,qzone,renren,douban,qqxiaoyou,kaixin001,taojianghu,favorite,baiduhi,blogsina,blog163,blogsohu&amp;text=分享到...&amp;logo=false"></script>
<script type="text/javascript" src="http://wres.mangocity.com/js/w/build/mgtool.js"></script>
<script type="text/javascript" src="http://wres.mangocity.com/js/w/mgtool/cookie/cookie.js"></script>
<script type="text/javascript" src="http://wres.mangocity.com/js/w/mgtool/cookie/albumCookie.js"></script>
<script type="text/javascript">
MGTOOL.setCookie('zjcode', '',{'domain':'.mangocity.com'});
</script>
<script language="javascript" src="http://wres.mangocity.com/js/w/common/js/o_code.js"></script>
</body>
</html>

