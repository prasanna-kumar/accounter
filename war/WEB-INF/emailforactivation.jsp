<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
 "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="i18n" uri="/WEB-INF/i18n.tld"%>
<%@page pageEncoding="UTF-8" %>
<html>
  <head>
  <meta http-equiv="content-type" content="text/html; charset=UTF-8" />
  <title><i18n:i18n msg='emailForActivation'/> | Accounter
  </title>
        <meta content="IE=100" http-equiv="X-UA-Compatible" />
		<link rel="shortcut icon" href="/images/favicon.ico" />
		
		<%@ include file="./feedback.jsp" %>
		<link type="text/css" href="../css/ss.css" rel="stylesheet" />
		<link type="text/css" href="../css/cmxform.css?version=<%= version%>" rel="stylesheet" />
		<script  type="text/javascript" >
			$(document).ready(function() {
				$('#submitButton').click(function() {
					$("#emailActivationForm").validate({
						rules: {
						emailid:{ 
						required :true,
						email: true
						}},
						messages: {
						emailid: "<i18n:i18n msg='pleaseenteranvalidemailaddress'/>" 
						}
					});
				});
				$('#emailidBox').keydown(function(e) {
					  if (e.keyCode == '9' || e.which =='9' ) {
					     e.preventDefault();
					    $('#submitButton').focus();
					   }
					});
			});
		</script>
  </head>
    <body>
	    <div id="commanContainer">
		 <img src="/images/Accounter_logo_title.png" class="accounterLogo" alt="Accounter logo" />
		  <c:if test="${errormessage!=null}">
			<div id="login_success" class="common-box">
				<span>${errormessage}</span>
			</div>
  		  </c:if>
  		  
		 <form id = "emailActivationForm" action="/main/emailforactivation" method="post">
		    <div class="reset-header">
			   <h2><i18n:i18n msg='resendActivationcode'/></h2>
			</div>
			<div>
			  <label><i18n:i18n msg='registeredEmailId'/></label>
			  <input id = "emailidBox" type="text" name="emailid" />
			</div>
			<div class="reset-button">
			   <input type="submit" tabindex="3" value="<i18n:i18n msg='resendActivationcode'/>" name="resend" class="allviews-common-button" id="submitButton" />
			</div>
		 </form>
		 
     </div>
     
     <!-- Footer Section-->
     
     <div id="mainFooter"  >
	    <div>
	       <span><i18n:i18n msg='atTherateCopy'/></span> |
	       <a target="_blank" href="/site/termsandconditions"><i18n:i18n msg='termsConditions'/></a> |
	       <a target="_blank" href="/site/privacypolicy"><i18n:i18n msg='privacyPolicy'/></a> |
	       <a target="_blank" href="/site/support"><i18n:i18n msg='support'/></a>
	    </div>
	</div>
     
     
	<%@ include file="./scripts.jsp" %>
	</body>
</html>