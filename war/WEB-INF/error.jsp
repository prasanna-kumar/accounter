<html>
    <head>
       <title>Accounter</title>
       <meta http-equiv="content-type" content="text/html; charset=UTF-8">
       <link type="text/css" rel="stylesheet" href="../css/Finance.css?version=<%= version%>">
    </head>
    <body style="background:none repeat scroll 0 0 #D8DCE0;">
      <div class="error_screen">
         <div class="error_vimukti_logo"></div>
         <div class="exception_box">
            <div class="main_data_box">
                 <h4>Exception in database</h4>
                 <div><%= request.getParameter("message") %></div>
                 <a href="/site/home">Go to Home page</a>
            </div>
         </div>
      </div>   
      
      <script type="text/javascript" charset="utf-8">
			var is_ssl = ("https:" == document.location.protocol);
			var asset_host = is_ssl ? "https://s3.amazonaws.com/getsatisfaction.com/" : "http://s3.amazonaws.com/getsatisfaction.com/";
			document.write(unescape("%3Cscript src='" + asset_host + "javascripts/feedback-v2.js' type='text/javascript'%3E%3C/script%3E"));
		</script>
		<script type="text/javascript" charset="utf-8">
			var feedback_widget_options = {};
			
			feedback_widget_options.display = "overlay";  
  			feedback_widget_options.company = "vimukti";
			feedback_widget_options.placement = "left";
			feedback_widget_options.color = "#222";
			feedback_widget_options.style = "idea";
		
			var feedback_widget = new GSFN.feedback_widget(feedback_widget_options);
		</script>   
    </body>
</html>

