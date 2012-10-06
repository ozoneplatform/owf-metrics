<html>
<script id="owfTransport" type="text/javascript">
  window.name = document.getElementById("owfTransport").innerHTML.match(/temp\s*=([\w\W]*)/)[1];
  temp= {
      status:${status},
      data: ${value}
   }
</script>
	<body>
	    <h3>window.name Transport</h3>
		Value in window.name is <span style="font-weight:bold">${value?.encodeAsHTML()}</span><br/>
		HTTP Status Code is <span style="font-weight:bold">${status}</span>
	</body>
</html>