//调用webCheck方法如果返回true,表示页面中有不符合要求的数据
//empty=false 表示该项不能为空
//len=10      表示该项最大长度为10(区分中英文，一个中文算两个字符)
//isNum=true  表示必须输入数字
//date=true   表示将会转化为日期格式 'yyyy-mm-dd hh:mm:ss'
//isPhone=true  表示必须输入电话
//propStr     表示提示信息
//begin=true     表示时间开始
//end=true     表示时间结束
//areaempty=false   表示国家区域城市不能为空
//isInteger=true 表示为整数
//isIdCard=true   为有效身分证
//isEMail=true 表示为有效e-mail
//isZip=true 表示为有效邮编
//
function webCheck(formName){
	 
	  for(i = 0; i < formName.length; i++){
	  
	  	 var ob = formName.elements(i);
	  	 
       if(ob.empty == 'false'){
           debug(ob.value);
            debug(ob.disabled);
          if(ob.value==""&& ob.disabled == false){
            var alertValue = '[' + ob.propStr + ']' + '不能为空';
            alert(alertValue); 
             try{
              ob.focus();
             }catch(Exception){
             
             }
            return true;
           }         
       }
      
       if(ob.value!=undefined){
       
       if(!len(ob.value,ob.len)){
       	   var alertValue = '[' + ob.propStr + ']' + '超过最大长度';
            alert(alertValue); 
            ob.focus();
            return true;
       }
      }
       
       if(ob.isNum == 'true'){
       	  var temp = ob.value;
       	  if(temp!=""&&isNaN(temp)){
       	  	var alertValue = '[' + ob.propStr + ']' + '必须输入数字';
            alert(alertValue); 
            ob.focus();
            return true;
       	  }
       }  
        if(ob.date == 'true'){
          if(!checkCommonIsValidDate(ob.value)){
              var alertValue = '[' + ob.propStr + ']' + '必须是合法的日期YYYY/MM/DD';
              alert(alertValue);
              ob.focus();
              return true;
          }
       	  convertDate(ob);
       } 
       if(ob.isPhone == 'true'){
       	  var temp = ob.value;
       	  if(!is_phone(temp)){
       	  	var alertValue = '[' + ob.propStr + ']' + '必须输入合法电话';
            alert(alertValue); 
            ob.focus();
            return true;
       	  }
       }
      
      //判断开始时间是否小于时间
      if(ob.begin == 'true'){
       	  var begintime = ob.value;
       	  var endtime = getEndTime(formName);
       	  if(!checkDateTime(begintime,endtime)){
       	  	var alertValue = '开始时间必须小于结束时间';
            alert(alertValue); 
            ob.focus();
            return true;
       	  }
       }
       
    
      if(ob.areaempty == 'false'){
       	  var temp = ob.value;
       	  if(!area(ob.value)){
       	  	var alertValue = '[' + ob.propStr + ']' + '不能为空';
            alert(alertValue); 
            ob.focus();
            return true;
       	  }
       }
       if(ob.isInteger == 'true'){
       	  var temp = ob.value;
       	  if(!is_integer(temp)){
       	  	var alertValue = '[' + ob.propStr + ']' + '必须输入整数';
            alert(alertValue); 
            ob.focus();
            return true;
       	  }
       }
       
       if(ob.isIdCard == 'true'){
       	  var temp = ob.value;
       	  if(!is_id_card(temp)){
       	  	var alertValue = '[' + ob.propStr + ']' + '格式不对，应为15位或者18位，15位全数字，18位的最后一位可能是字母！';
            alert(alertValue); 
            ob.focus();
            return true;
       	  }
       }
       
       
       if(ob.isEMail == 'true'){
       	  var temp = ob.value;
       	  if(!is_email(temp)){
       	  	var alertValue = '[' + ob.propStr + ']' + '格式不对，应为XXX@XXX.XXX';
            alert(alertValue); 
            ob.focus();
            return true;
       	  }
       }
       
       
       if(ob.isZip == 'true'){
       	  var temp = ob.value;
       	  if(!is_zip(temp)){
       	  	var alertValue = '[' + ob.propStr + ']' + '格式不对';
            alert(alertValue); 
            ob.focus();
            return true;
       	  }
       }
       
    }
    return false;
	
 }
 
 function debug(message){
   if(false){
     alert(message);
   }
 }
 
 //开始时间小于结束时间,开始或者结束时间为空的时候返回true
 //开始时间大于结束时间返回false
 function checkDateTime(beginDate,endDate) {

   if(trim(beginDate)==""||trim(endDate)=="")

   {
      return true;

   }

    var beginTime =beginDate.split("/");

   var start = new
   Date(beginTime[0],beginTime[1],beginTime[2],0,0,0,0);

   var endTime=endDate.split("/");

   var end = new
   Date(endTime[0],endTime[1],endTime[2],0,0,0,0);


   if (start.getTime() <
     end.getTime()||start.getTime()==end.getTime()) {
     return true;

   }else{
     return false;

   }

}
 
 //得到结束时间
  function getEndTime(formName) {
    for(k = 0; k < formName.length; k++){
	  	 var ob = formName.elements(k);
	  	 if(ob.end=='true'){
	  	   return ob.value;
	  	 }
	}
	return "";
 }
 
 
  //电话校验
  function is_phone(js_value) {
   var re = /^[0-9\*\-( )]*$/;
   if(js_value.match(re))
   return true;
   return false;
 }
  
  function delSubmit(id){	
      if(confirm("是否确定删除!")){	
		document.getElementById("delId").value = id;		
		
		delForm.submit();
	  }
	}
	
	function editSubmit(id){		
		document.getElementById("editId").value = id;		
		editForm.submit();
	}	
	

	
	function operSubmitSession(id,actionValue,formName){	
		//alert('sdfsd');	
		document.getElementById("operId").value = id;		
		//alert('sdfsd');	
		formName.action.value=actionValue;
		checkDate();
		//alert('sdfsd');	
		formName.submit();
	}	
	
	//判断长度
	function len(val,asklength){	
	  var valLength = DataLength(val);
	  
	  if(valLength==null){
	    return true;
	  }else if(valLength>parseInt(asklength)){
	    return false;
	  }
	  return true;
	  
	}	
	//校验区域
	function area(value){
	    if(value == "" ||value <= 0){
	         return false;
	     }
	     return true;
	     
	}
	//校验整数
	function is_integer(js_value) {
   var re;
   re = /^\s*$/;
   if(js_value.match(re)) {
      return true;
   }
   if(isNaN(js_value) || js_value.indexOf('.', 0) >= 0) {
      return false;
   }
   return true;
   }
   
   //校验身份证
   function is_id_card(js_value) {
   var re;
   re = /^\s*$/;
   reg1=/\d{17}\w{1}/;
   reg2=/\d{15}/;
   if(js_value.match(re)) {
      return true;
   }
   if(js_value.match(reg1)&&js_value.length==18) {
      return true;
   }
   
   if(js_value.match(reg2)&&js_value.length==15) {
      return true;
   }
   return false;
   }
   
   //校验e-mail
   function is_email(js_value) {
   var pos;
   var re;
   re = /^\s*$/;
   if(js_value.match(re)) {
      return true;
   }
   pos = js_value.indexOf('@', 0);
   if(js_value.length <= 5)
   return false;
   if(pos == - 1 || pos == 0 || pos ==(js_value.length - 1))
   return false;
   pos = js_value.indexOf('.', 0);
   if(pos <= 0 || pos ==(js_value.length - 1))
   return false;
   return true;
  }
  
  //校验邮编
  function is_zip(js_value) {
   var re;
   re = /^\s*$/;
   if(js_value.match(re)) {
      return true;
   }
   if(!is_natural(js_value) || js_value.length != 6) {
      return false;
   }
   return true;
 }
 
 function is_natural(js_value) {
   var re;
   re = /^\s*$/;
   if(js_value.match(re)) {
      return true;
   }
   re = /^\+{0,1}[0-9]*$/;
   if(!js_value.match(re))
   return false;
   return true;
 }
	
	  //****************************************************************
//* 名　　称：DataLength
//* 功    能：计算数据的长度
//* 入口参数：fData：需要计算的数据
//* 出口参数：返回fData的长度(Unicode长度为2，非Unicode长度为1)
//*****************************************************************
function DataLength(fData)
{
    var intLength=0
    for (var i=0;i<fData.length;i++)
    {
        if ((fData.charCodeAt(i) < 0) || (fData.charCodeAt(i) > 255))
            intLength=intLength+4
        else
            intLength=intLength+1    
    }
    return intLength
}

	function formSubmit(formName){	 
			//alert('1111');
	  if(webCheck(formName)){
	    return false;
	  }
	  checkDate();
		//alert(formName.action.value);
		formName.submit();
	}
	
	//带提示信息的提交，当confirmInfo为true的时候先提示再提交
	function confirmSubmit(formName,confirmInfo){	 
			//alert('1111');
		if(confirmInfo){
		  if(confirm("是否确认提交！")){
		    if(webCheck(formName)){
	        return false;
	      }
	      checkDate();
		    formName.submit();
		  }
		}else{
	 
		    if(webCheck(formName)){
	        return false;
	      }
	      checkDate();
		    formName.submit();

	  }
	
	}
	
	
	function checkDate(){ 
	 	
	  for(i = 0; i < document.all.length; i++){	
	  	 var ob = document.all(i);  	
      if(ob.date == 'true'){
       	  convertDate(ob);
       }       
    }
	}
	
function convertDate(elementValue){	 
	
}

	//去左空格; 
	function ltrim(s){ 
	 return s.replace( /^\s*/, ""); 
	} 
	//去右空格; 
	function rtrim(s){ 
	 return s.replace( /\s*$/, ""); 
	} 
	//去左右空格; 
	function trim(s){ 
	 return rtrim(ltrim(s)); 
	} 
	
	//将数字转换为汉字
	function transNum(num)
   {  
	//转换整数部分
	var i=1;
	var len = num.length;

	var dw2 = new Array("","万","亿");//大单位
	var dw1 = new Array("十","百","千");//小单位
	var dw = new Array("零","一","二","三","四","五","六","七","八","九");//整数部分用
	var k1=0;//计小单位
	var k2=0;//计大单位
	var str="";

	for(i=1;i<=len;i++)
	{
	var n = num.charAt(len-i);
	if(n=="0")
	{
	if(k1!=0)
	str = str.substr( 1, str.length-1);
	}

	str = dw[Number(n)].concat(str);//加数字

	if(len-i-1>=0)//在数字范围内
	{
	if(k1!=3)//加小单位
	{
	str = dw1[k1].concat(str);
	k1++;
	}
	else//不加小单位，加大单位
	{
	k1=0;
	var temp = str.charAt(0);
	if(temp=="万" || temp=="亿")//若大单位前没有数字则舍去大单位
	str = str.substr( 1, str.length-1);
	str = dw2[k2].concat(str);
	}
	}
	if(k1==3)//小单位到千则大单位进一
	{
	k2++;
	}
	 }
	 return str;
	}

//字符串型数字转换为字符串时间(HH:dd)
	function convertStrNumberToStrTime(strNumber){
		if(strNumber.search(/^(-|\+)?\d+(\.\d+)?$/) != -1){
			for(var i=0;i<4;i++){
				if(strNumber.length<4){
					strNumber='0'+strNumber;
				}
			}
			if(strNumber.substring(0,1)=="0"){
				return strNumber.substring(1,2)+":"+strNumber.substring(2,4);
			}
			else{
				return strNumber.substring(0,2)+":"+strNumber.substring(2,4);
			}
		}
		else{
			return strNumber;
		}
	}

	//判断时间格式（HH:dd||H:dd）是否合法并转换为字符串型数字
	//格式错误返回'ErrorFormat'
	function convertStrTimeToStrNumber(strtime){
	     if(strtime.length !=0 && strtime!=""){
			  	var regexp = /^([0-9]|1[0-9]|2[0-3]):[0-5][0-9]$/
			  	if(regexp.test(strtime)== false){
			  	     return "ErrorFormat";
			  	}	
			  	var array = strtime.split(":");
			  	var ar1 = array[0];
			  	var ar2 = array[1];
			  	if(ar1 == 0){
			  		if(ar2 == 00){
			  			return "0";
			  		}else if(ar2.indexOf('0')== 0 ){
			  			return ar2.substring(1);
			  		}else{
			  			return ar2;
			  		}
			  	}else{
			  	  	return ar1+ar2;
			  	}

		}else{
		  	return "0";
		}	
     }
     
     
function checkCommonIsValidDate(str){
    //如果为空，则通过校验
    if(str == "")
        return true;
    var pattern = /^\d{4}\/\d{1,2}\/\d{1,2}$/g;
    if(!pattern.test(str))
        return false;
    //alert("【" +str+"】1");
    var arrDate = str.split("/");
    var date =  new Date(arrDate[0],(parseInt(arrDate[1],10) -1) +"",parseInt(arrDate[2],10) +"");
    //alert("a:【" +date.getFullYear()+"】【" + date.getMonth() + "】【" + date.getDate() + "】");
    //alert("b:【" +arrDate[0]+"】【" + parseInt(arrDate[1],10) + "】【" + parseInt(arrDate[2],10) + "】");
    if(date.getFullYear() == arrDate[0]
       && date.getMonth() == (parseInt(arrDate[1],10)-1) +""
       && date.getDate() == parseInt(arrDate[2],10) +"")
        return true;
    else
    	//alert("【" +str+"】2");
        return false;
}

/*****************************************
	设置form中名称为checkBoxName的checkBox的checked属性
******************************************/
function setChecked(form,checkBoxName,isChecked){
	for (var i=0;i<form.elements.length;i++)  {
		var e = form.elements[i];
		if (e.name == checkBoxName && e.type == "checkbox"){
		    e.checked = isChecked;       			
		}
	}
}

function myConfirm(urlStr){
  if(confirm('是否确定删除!')){
    window.location=urlStr;
  }
}

/*****************************************
	//根据value选择select选项
******************************************/	
	function selectOptionByValue(SelectForm,optionValue)
	{
		for(i=0;i<SelectForm.length;i++)
		{
			if(SelectForm.options[i].value==optionValue)
			{
				SelectForm.options[i].selected=true;
			}
		}
	}

/*****************************************
	//比较两个date的间隔天数
******************************************/	
function daysElapsed(date1,date2) {
    var difference = Date.UTC(date1.getYear(),date1.getMonth(),date1.getDate(),0,0,0)
                   - Date.UTC(date2.getYear(),date2.getMonth(),date2.getDate(),0,0,0);
    var difdays=difference/(1000*60*60*24);
    if(difdays<0){
    	return 0-difdays;
    }else{
    	return difdays;
    }
}

function setCheckedValue(radioName, newValue) {  
       alert();
	if(!radioName) return;  
	var radios = document.getElementsByName(radioName);     
	for(var i=0; i<radios.length; i++) {  
		radios[i].checked = false;  
		if(radios[i].value == newValue.toString()) {  
			radios[i].checked = true;  
			}  
	}  
}  
//=========================add by Mingyu.li start===================================
//checkbox处理，全选和部分选中
// 全选事件
function clickAll(e, itemName) {
	if ($(e).attr("checked") == true) {
		$("input[name=\"checkItem\"]").attr("checked", true);
	} else {
		$("input[name=\"checkItem\"]").attr("checked", false);
	}
}
//选中一个事件
function clickItem(e, allName) {
	if ($(e).attr("checked") == false) {
		$("input[name=\"" + allName + "\"]").attr("checked", false);
	} else {
		var aa = $("[name=checkItem]");
		for (i = 0; i < aa.length; i++) {
			if (!aa[i].checked) {
				return;
			}
		}
		$("input[name=\"selectAll\"]").attr("checked", true);
	}
}
//获取 一组radio 选中值
function getSelRadioValueByName(rediosName)
{
 	var o=document.getElementsByName(rediosName);
   var len=o.length;  
   for (var i=0;i<len;i++ )
   { if( o[i].checked==true )
    {
     	return o[i].value;
    }
   }
   return null;
}
//判断复选框有没有被选中的
function checkChoose() {
	var item = $("[name=checkItem]");
	var objYN;
	var i;
	objYN = false;
	for (i = 0; i < item.length; i++) {
		if (item[i].checked) {
			if (confirm("\u4f60\u786e\u5b9a\u5220\u9664\u5417\uff1f")) {
				objYN = true;
			} else {
				objYN = false;
			}
			return objYN;
		}
	}
	alert("\u8bf7\u9009\u62e9\u8981\u64cd\u4f5c\u7684\u8bb0\u5f55\uff01");
	return objYN;
}
//获取 cookie
function getCookie(Name) {
var search = Name + "=";
if (document.cookie.length > 0) {
    offset = document.cookie.indexOf(search);
    if (offset != -1) {
     offset += search.length;
     end = document.cookie.indexOf(";", offset);
     if (end == -1) {
      end = document.cookie.length;
     }
     return unescape(document.cookie.substring(offset, end));
    } else {
     return ("");
    }
} else {
    return ("");
}
}	
//=========================add by Mingyu.li  end===================================



     	