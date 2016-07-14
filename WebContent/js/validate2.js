	var fileds={
		type:'N',//N reg Y bind
		successClass:'suctip',
		errorClass:'errtip',
		mobile:{filed:'',panel:''},
		mail:{filed:'',panel:''},
		password:{filed:'',panel:''},
		repassword:{filed:'',panel:''}
	};
	function validateRepassword(){
	//alert(document.getElementById("loyaltyaccount.customer.password").value);
	var pwd = document.getElementById(fileds.password.filed).value;
	if (!IsStringNull(document.getElementById(fileds.password.filed).value)){
    	if (IsStringNull(document.getElementById(fileds.repassword.filed).value)) {
        	document.getElementById(fileds.repassword.panel).style.display="none";
	    	document.getElementById(fileds.repassword.panel).style.color="#ff6600";
	    	document.getElementById(fileds.repassword.panel).innerText=msg_14;
	    	$("#"+fileds.repassword.panel).fadeIn("slow");
        	//document.getElementById("repassword").focus();
        	return false;
    	}else{
    	   if(document.getElementById(fileds.password.filed).value != document.getElementById(fileds.repassword.filed).value){
    		document.getElementById(fileds.repassword.filed).style.display="none";
	    	document.getElementById(fileds.repassword.filed).style.color="#ff6600";
	    	document.getElementById(fileds.repassword.filed).innerText=msg_15;
	    	$("#"+fileds.repassword.filed).fadeIn("slow");
        	//document.getElementById("repassword").focus();
        	return false;
        	}else{
	    	document.getElementById(fileds.repassword.filed).style.display="";
	    	document.getElementById(fileds.repassword.filed).innerHTML = "<img src='"+m_v_uri+"/images/newMember/right.jpg' width='19' height='19' hspace='10' />";
         	return true;
        	}
    	}
	}else{
	return validatePassword();
	}
}
   
      function validateMobile(obj){
    	  
    	  
        var mobileTip = document.getElementById(fileds.mobile.panel);
        mobileTip.style.display="none";
        mobileTip.innerHTML="";
	    	if (!isMobile(obj.value)) {
	        	mobileTip.style.display="none";
	    		mobileTip.style.color="#ff6600";
	    		mobileTip.appendChild(document.createTextNode(msg_16));
	    		mobileTip.className=fileds.errorClass;
	    		mobileTip.style.display="";
	    		//$("#"+fileds.mobile.panel).fadeIn("slow");
	        	return false;
	    	}else{
	var  rtnBoolean = false;
    var url=m_v_uri+"/validate/validateMobile.action?mobileNo="+$.trim(obj.value);
            //alert(url);
            $.post(url,function(responseText) {
                //alert(responseText);
                if(fileds.type == responseText ){
                isEnableOpen_mobile = true;
                mobileTip.style.display="";
                mobileTip.innerHTML="&nbsp;";
                mobileTip.className=fileds.successClass;
                rtnBoolean = true;
                return;
                }else{
                isEnableOpen_mobile = false;
	        	mobileTip.style.display="";
	    		mobileTip.style.color="#ff6600";
	    		//mobileTip.innerText=msg_17;
	    		mobileTip.appendChild(document.createTextNode(msg_17));
	    		//document.createTextNode
	    		//alert("1:"+msg_17);
	    		mobileTip.className=fileds.errorClass;
	    		
	    		//$("#"+fileds.mobile.filed).fadeIn("slow");
	    		return ;
                }
            }
            );
            //alert(rtnBoolean);
            return rtnBoolean;
	    	}
	    	
		//}
		//return true;
	}


    function validateEmail(){
    
    	var email = document.getElementById(fileds.mail.filed);
    	var emailTip = document.getElementById(fileds.mail.panel);
    	emailTip.style.display="none";
    	if (IsStringNull(email.value)) {
        		emailTip.style.display="none";
	    		emailTip.style.color="#ff6600";
	    		emailTip.innerHTML=msg_23;
	    		emailTip.className=fileds.errorClass;
	    		emailTip.style.display="";
	    		//$("#"+fileds.mail.panel).fadeIn("slow");
	    		return false;
    	}
    	if (!IsStringNull(email.value)) {
        	if (!Trim(email.value).isEmail()) {
            	emailTip.style.display="none";
				emailTip.style.color="#ff6600";
	    		emailTip.innerHTML=msg_24;
	    		emailTip.className=fileds.errorClass;
	    		emailTip.style.display="";
	    		//$("#"+fileds.mail.panel).fadeIn("slow");
            	return false;
        	}else{
        	    var rtnBoolean = false;
        	    var url=m_v_uri+"/validate/validateEmail.action?emailNo="+$.trim(email.value);
               $.post(url,function(responseText) {
                //alert(responseText);
                if(fileds.type == responseText){
                emailTip.style.display="none";
                emailTip.className=fileds.errorClass;
	    	    //emailTip.innerHTML = "<img src='"+m_v_uri+"/images/newMember/right.jpg' width='19' height='19' hspace='10' />";
                //document.getElementById("submitButton").src="../images/newMember/tiaokuan.jpg";
                //isEnableOpen_email = true;
                rtnBoolean = true;
                return ;
                }
            }//end function;
            );
            return rtnBoolean;
        	}
    	}
    }
    function validatePassword(){

      var password = document.getElementById(fileds.password.filed).value;
      var validatePasswordTip = document.getElementById(fileds.password.panel);
    	if (IsStringNull(password)) {
        	validatePasswordTip.style.display="none";
	    	validatePasswordTip.style.color="ff6600";
	    	validatePasswordTip.innerHTML=msg_27;
	    	$("#"+fileds.password.panel).fadeIn("slow");
        	return false;
    	}
        if (!checkIsNumber(password)) {
        	validatePasswordTip.style.display="none";
	    	validatePasswordTip.style.color="#ff6600";
	    	validatePasswordTip.innerHTML=msg_28;
	    	$("#"+fileds.password.panel).fadeIn("slow");
       	 	return false;
    	}
    	if (!IsStringNull(password)) {
    		if (password.length < 6 || password.length > 12) {
        		validatePasswordTip.style.display="none";
	    		validatePasswordTip.style.color="#ff6600";
	    		validatePasswordTip.innerHTML=msg_29;
	    		$("#"+fileds.password.panel).fadeIn("slow");
        		return false;
    		}
   	 	}
    	if(password=="111111" || password=="222222" || password=="333333" || password=="444444" || password=="555555"
    		|| password=="666666" || password=="777777" || password=="888888" || password=="999999" || password=="000000"
    		|| password=="123456" || password=="654321") {
    		validatePasswordTip.style.display="none";
	    	validatePasswordTip.style.color="#ff6600";
	    	validatePasswordTip.innerHTML=msg_30;
	    	$("#"+fileds.password.panel).fadeIn("slow");
       	 	return false;
    	}
    	if(!IsStringNull(password)){
    	   // document.getElementById("loyaltyaccount.customer.password").value = password;
	    	validatePasswordTip.style.display="";
	    	validatePasswordTip.innerHTML = "<img src='"+m_v_uri+"/images/newMember/right.jpg' width='19' height='19' hspace='10' />";
    	    document.getElementById(fileds.password.filed).value = password;	
    	}
      return true;
    }
