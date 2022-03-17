<?php echo '<?xml version="1.0" encoding="utf-8"?>'; ?>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">

<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<!-- Bootstrap Framework zip file retrieved from http://getbootstrap.com/docs/3.3/getting-started/#download on 17th February 2018 Created by Twitter, Inc. -->
		<!-- jQuery retrieved from https://jquery.com/ on 17th February 2018 Created by jQuery Org -->
		<script src="js/jquery.min.js" type="text/javascript"></script>
		<script src="js/bootstrap.min.js" type="text/javascript"></script>
		<script src="js/main.js" type="text/javascript"></script>
		<link rel="stylesheet" href="css/bootstrap.min.css" type="text/css"/>
		<link href="css/style.css" rel="stylesheet" type="text/css" />
		<title>Login | Royal Borough Freecycle</title>
	</head>

	<body>
		<?php
			require('functions.php');
			
			$cookie_user = "";
		
			if(isSet($_POST['username']) && isSet($_POST['password']))
			{
				$username = $_POST['username'];
				$password = $_POST['password'];
				$con;
				$stmt;
				$member_id = "";
				$status = "";
				$email = "";
				$feedback = "";
				
				if(validateLoginData($username, $password, $feedback))
				{
					$username = filter_var($username, FILTER_SANITIZE_STRING);
					$password = hash("sha512", $password);
					
					require('dbConnect.php');
					
					if($stmt = mysqli_prepare($con, "SELECT MEMBER_ID, ACCOUNT_STATUS, EMAIL FROM MEMBER WHERE USERNAME = ? AND PASSWORD = ?"))
					{
						mysqli_stmt_bind_param($stmt, "ss", $username, $password);
						
						mysqli_stmt_execute($stmt);

						mysqli_stmt_bind_result($stmt, $member_id, $status, $email);
						
						if(mysqli_stmt_fetch($stmt))
						{
							if(isSet($_POST['remember']))
							{
								setcookie("cookie_user", $username, time() + 60*60*24*30);
							}
							
							if($status == "A")
							{
								session_start();
							
								$_SESSION['id'] = $member_id;
								$_SESSION['username'] = $username;
								
								header("Location:memberHome.php");
							}
							
							else
							{
								header("Location:verify.php?email=$email");
							}
						}
							
						else
						{
							$feedback = "<p class='error'>Unidentified username OR Incorrect username and password. Please try again. Kindly click the 'register' link below if you currently don't have an account.</p>";
							echo $feedback;
						}	
					}
					
					else
					{
						$feedback ="<p class='error'>Database processing error. Please contact your website administrator.</p>";
						echo $feedback;
					}
					
					mysqli_close($con);
				}
				
				else
				{
					echo $feedback;
				}
			}
			
			if(isSet($_COOKIE['cookie_user']))
			{
				$cookie_user = $_COOKIE['cookie_user'];
			}
		?>
		<div class="container">
			<h1>Log into Royal Borough Freecycle</h1>
		
			<fieldset>
				<form method="post" action="index.php" onsubmit="return validateLogin(this)">
					<div class="input-group">
						<span class="input-group-addon"><i class="glyphicon glyphicon-user"></i></span>
						<input type="text" class="form-control" name="username" id="username" placeholder="Username" value="<?php echo $cookie_user ?>"/>
					</div>
					<p id="username_error" class="help-block"></p>
					<div class="input-group">
						<span class="input-group-addon"><i class="glyphicon glyphicon-lock"></i></span>
						<input type="password" class="form-control" name="password" placeholder="Password" id="password"/>
					</div>
					<p id="password_error" class="help-block"></p>
					<div class="checkbox">
						<label for="remember"><input type="checkbox" name="remember" id="remember"/> Remember Me</label>
					</div>
					<div>
						<input type="submit" class="btn btn-default" name="login" id="login" value="Log In"/>
					</div>
					<p>By using the 'Remember Me' option, you agree that this website uses cookies to store your username and only your username.</p>
					<p>Don't have an account? Register <a href="register.php">here</a></p>
					<p>Just looking? <a href="search.php">Search</a> our database and see what we have to offer.</p>
				</form>
			</fieldset>
		</div>
	</body>
</html>