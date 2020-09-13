
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
	
		
	
		<div class="container">
			<div id="messageJumbo" class="jumbotron">${message}
			</div>

			<div width="100%" class="form" id="Userform">
				<label id="Label">Username:</label>
				<input id="input" type="text"><br>
				<button class="btn btn-submit" onclick="submitUsername()">Submit</button>
				<button class="btn btn-info" onclick="redirect()" id="redirectButton">Create Trec-Account</button>
			</div>
			
			<form hidden width="100%" class="form" id="Passwordform" action="/LogIn" method="post" onsubmit="return true;">
				<input type="text" id="userLabel" readonly="readonly" name="username">
				<label id="Label2">Password:</label>
				<input id="input2" type="password" name="password"><br>
				<input type="submit" class="btn btn-submit" value="Submit">
			</form>
		</div>
		
		<script type="application/JavaScript">
		
		var username;
		
		function submitUsername() {
			let xhr = (new XMLHttpRequest() || new ActiveXObject("Microsoft.HTTPRequest"));
			
			let initUrl = window.location.href;
			username = document.getElementById("input").value;
			
			initUrl += '/UserExists/' + username;
			
			xhr.onreadystatechange = function() {
				
				if(this.readyState === 4 && this.status == 200){
					let result = this.responseText;
					result = result.toUpperCase();
					
					if(result == "TRUE") {
						// Here, the user name DOES exist, so activate the Password form
						let pForm = document.getElementById("Passwordform");
						pForm.hidden = false;
						let pLabel = document.getElementById("userLabel");
						pLabel.value = username;
						
						// Now Deactivate the User Form
						let uForm = document.getElementById("Userform");
						uForm.hidden = true;
						
					} else{
						let reButton = document.getElementById("redirectButton");
						reButton.hidden = false;
						
						let jumbo = document.getElementById("messageJumbo");
						jumbo.innerHTML += "<br><h3>Username does not exist! Please pick a username or Create a Trec-Account!</h3>";
					}
				}
			}
			initUrl = initUrl.replace("/**", "");
			initUrl = initUrl.replace("/LogIn", "");
			initUrl = initUrl.replace("/failed", "");
			initUrl = initUrl.replace("/LogoutSuccess", "");
			console.log("initUrl=", initUrl);
			xhr.open("GET", initUrl)
			xhr.send();
		}
		
		function redirect() {
			
			console.log("Redirect Funciton called");
			
			let colonSep = window.location.href.indexOf('://');
			
			// add 5 to colonSep! If '://' is absent, value will be 4. Either way, the 
			// main .com url will be at least 5 characters
			colonSep = window.location.href.indexOf('/', colonSep + 5);
			
			let newUrl = window.location.href.indexOf(0, colonSep);
			console.log("About to Transfer to New User page! At:");
			console.log("/NewUser/")
			window.location.href = "/NewUser";
		}
		
		function blockSubmit() {
			return false;
		}
		
		</script>
	</body>
</html>