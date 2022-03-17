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
		<title>Register | Royal Borough Freecycle</title>
	</head>

	<body>
		<div class="container">
			<h1>Register for Royal Borough Freecycle</h1>
			
			<?php
				require('functions.php');
				
				session_start();

				if(isSet($_POST['username']) && isSet($_POST['email']) && isSet($_POST['password']) && isSet($_POST['captcha']))
				{
					$username = $_POST['username'];
					$email = $_POST['email'];
					$password = $_POST['password'];
					$bio = "";
					$user_captcha = $_POST['captcha'];
					$captcha_code = $_SESSION['captcha']['code'];
					$con = null;
					$stmt = null;
					$activation_code = "";
					$message = "";
					$header = "";				
					$feedback = "";
					
					if(validateRegistrationData($username, $password, $email, $user_captcha, $captcha_code, $feedback))
					{
						$username = filter_var($username, FILTER_SANITIZE_STRING);
						$email = filter_var($email, FILTER_SANITIZE_EMAIL);
						$password = hash("sha512", $password);
						$activation_code = getActivationCode();
						
						require('dbConnect.php');
						
						if($stmt = mysqli_prepare($con, "INSERT INTO MEMBER VALUES (NULL, ?, ?, ?, ?, NOW(), ?)"))
						{
							mysqli_stmt_bind_param($stmt, "sssss", $username, $password, $email, $bio, $activation_code);
							
							if(mysqli_stmt_execute($stmt)) //insert into db and send activation email to user
							{
								$message = "
								<html>
								<head>
								<title>Account Activation</title>
								</head>
								<body style='background-color:lightblue'>
									<h3 bgcolor='whitesmoke'>Your Activation Link</h3>
									<p>Please click on the link below to activate your account status</p>
									<a href='http://localhost/assign1/verify.php?email=$email'>Click Here</a> To activate your account.
									<p>Please use '$activation_code' as your activation code.</p>
								</body>
								</html>";
								
								$header = "MIME-Version: 1.0" . "\r\n";
								$header .= "Content-type:text/html;charset=iso-8859-1" . "\r\n";
								
								echo "<p class='success'>Add Student Successful</p>";
								
								if(mail($email, "Activate Your Royal Borough of Greenwich Freecycle Account", $message, $header))
								{
									header("Location:verify.php?email=$email");
								}
								
								else
								{
									echo "<p class='error'>Verification Failed To Send</p>";
								}
							}
							
							else
							{
								echo "<p class='error'>Database processing error. Add Student Unsuccessful</p>";
							}
						}
						
						else
						{
							echo "<p class='error'>Database processing error. Please contact your website administrator.</p>";
						}
						
						mysqli_close($con);
					}
					
					else
					{
						echo $feedback;
					}
				}
				
				require("simple-php-captcha.php");
				$_SESSION['captcha'] = simple_php_captcha();
			?>
			
			<fieldset>
				<form method="post" action="register.php" onsubmit="return validateRegistration(this)">
					<div class="form-group">
						<label for="username">Username</label>
						<input type="text" class="form-control" placeholder="Username" name="username" id="username" onblur="checkUsername(this.value)"/><span id="username_error" class="help-block"></span>
					</div>
					<div class="form-group">
						<label for="email">E-mail Address</label>
						<input type="email" class="form-control" name="email" placeholder="john@example.com" id="email"/><span id="email_error" class="help-block"></span>
					</div>
					<div class="form-group">
						<label for="password">Password</label>
						<input type="password" class="form-control" name="password" placeholder="Password" id="password"/><span id="password_error" class="help-block"></span>
					</div>
					<div class="form-group">
						<img src="<?php echo $_SESSION['captcha']['image_src'] ?>" alt="Captcha Code: <?php echo $_SESSION['captcha']['code'] ?>"/> <!-- Use of the captcha code as seen in simple-captcha.php -->
						<br></br>
						<label for="captcha">Captcha (case sensitive)</label>
						<input type="text" class="form-control" name="captcha" id="captcha"/><span id="captcha_error" class="help-block"></span>
					</div>
					<div>
						<input type="submit" class="btn btn-default" name="register" id="register" value="Register"/>
					</div>
					<p>By registering for this website, you agree to our <a href="terms_of_use.html">terms of use</a> and our <a href="privacy_policy.html">privacy policy.</a></p>
					<p>Already have an account? Log In <a href="index.php">here</a></p>
				</form>
			</fieldset>
		</div>
	</body>
</html>