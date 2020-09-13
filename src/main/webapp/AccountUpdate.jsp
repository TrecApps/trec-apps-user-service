<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<title>Trec-Apps Login</title>
		<link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.1.3/css/bootstrap.min.css" integrity="sha384-MCw98/SFnGE8fJT3GXwEOngsV7Zt27NXFoaoApmYm81iuXoPkFOJwJ8ERdknLPMO" crossorigin="anonymous">
    	<script src="https://code.jquery.com/jquery-3.3.1.slim.min.js" integrity="sha384-q8i/X+965DzO0rT7abK41JStQIAqVgRVzpbzo5smXKp4YfRvH+8abtTE1Pi6jizo" crossorigin="anonymous"></script>
    	<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.3/umd/popper.min.js" integrity="sha384-ZMP7rVo3mIykV+2+9J3UJ46jBk0WLaUAdn689aCwoqbBJiSnjAK/l8WvCWPIPm49" crossorigin="anonymous"></script>
		<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.1.3/js/bootstrap.min.js" integrity="sha384-ChfqqxuZUCnJSK3+MXmPNIyE6ZbWh2IMqE241rYiqJxyMiZ6OW/JmZQ5stwEULTy" crossorigin="anonymous"></script>
	</head>
	<body>
	
		<script type="application/JavaScript">
		
		var passwordsMatch = false;
		
		function comparePasswords()	{
			let p1 = document.getElementById("passNew1").value;
			let p2 = document.getElementById("passNew2").value;
			
			let passBanner = document.getElementById("passWarning");
			
			if(p1 === p2 && p1.length > 8) {
				passwordsMatch = true;
				passBanner.hidden = true;
			} else {
				passwordsMatch = false;
				passBanner.hidden = false;
			}
		}
		
		
		</script>
	
		<div class="container">
			<div id="messageJumbo" class="jumbotron">${message}
			</div>
			
			<form width="100%" class="form" id="Passwordform" action="/Secure/UpdateUser_" method="post" onsubmit="return passwordsMatch;">
			
				<input type="number" readonly="readonly" name="accountID" value="${account_id}">
				
				<label id="oldPassLabel">Old Password:</label>
				<input id="passOld" type="password" name="oldPassword"><br>
				
				<label id="newPassLabel1">New Password:</label>
				<input id="passNew1" type="password" name="newPassword" oninput="comparePasswords()"><br>
				
				<label id="newPassLabel">Old Password:</label>
				<input id="passNew2" type="password" name="password" oninput="comparePasswords()"><br>
				<h5 class="bg-danger" id="passWarning" hidden> Passwords must match and be 8+ characters in length!</h5>
			
				<input type="submit" class="btn btn-submit" value="Submit">
			</form>
		</div>
	</body>
</html>