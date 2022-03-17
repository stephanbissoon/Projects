<?php
	require('functions.php');

	if(isSet($_GET['email']) && isSet($_GET['code']))
	{
		$email = $_GET['email'];
		$code = $_GET['code'];
		$con = null;
		$stmt = null;
		$member_id = "";
		$username = "";
		$db_code = "";
		$status = "A";
		$feedback = "";
		
		if(validateVerify($email, $code, $feedback)) //verify input
		{
			$code = filter_var($code, FILTER_SANITIZE_STRING);
			
			require('dbConnect.php');
			
			if($stmt = mysqli_prepare($con, "SELECT MEMBER_ID, USERNAME, ACCOUNT_STATUS FROM MEMBER WHERE EMAIL = ?"))
			{	
				mysqli_stmt_bind_param($stmt, "s", $email);
					
				mysqli_stmt_execute($stmt);

				mysqli_stmt_bind_result($stmt, $member_id, $username, $db_code);
				
				if(mysqli_stmt_fetch($stmt))
				{
					if($code == $db_code) //check if entered code is the database code
					{	
						mysqli_stmt_close($stmt);
				
						if($stmt = mysqli_prepare($con, "UPDATE MEMBER SET ACCOUNT_STATUS = ? WHERE MEMBER_ID = ?")) //update member data to active once code entered matches database code
						{	
							mysqli_stmt_bind_param($stmt, "ss", $status, $member_id);
							
							mysqli_stmt_execute($stmt);
							
							mysqli_stmt_close($stmt);
							
							session_start();
							
							$_SESSION['id'] = $member_id; //create session variables
							$_SESSION['username'] = $username;
							
							header("Location:memberHome.php");
						}
						
						else
						{
							$feedback .= "<p class='error'>Database processing error when updating member information. Please contact your website administrator.</p>";
							echo $feedback;
						}
					}
					
					else
					{
						$feedback .= "<p class='error'>An incorrect activation code was entered. Please try again.</p>";
						echo $feedback;
					}
				}
				
				else
				{
					$feedback .= "<p class='error'>Member does not exist. Please register for this website using the following link: <a href='register.php'>Register</a></p>";
					echo $feedback;
				}
			}
			
			else
			{
				$feedback .= "<p class='error'>Database processing error for retrieving member information. Please contact your website administrator.</p>";
				echo $feedback;
			}
			
			mysqli_close($con);
		}
		
		else
		{
			echo $feedback;
		}
	}
	
	echo '<?xml version="1.0" encoding="utf-8"?>';
?>

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
		<title>Verify | Royal Borough Freecycle</title>
	</head>

	<body>
		<div class="container">
			<h1>Verify Your Account</h1>
			
			<p>You are required to verify your Royal Borough of Greenwich freecycle account. Please check your e-mail for a verification code link.</p>
			
			<fieldset>
				<form method="get" action="verify.php" onsubmit="return validateVerify(this)">
					<div class="form-group">
						<input type="hidden" name="email" value="<?php echo $_GET['email'] ?>"/>
						<label for="code">Verification Code</label>
						<input type="text" class="form-control" name="code" id="code"/><span id="code_error" class="help-block"></span>
					</div>
					<div>
						<input type="submit" class="btn btn-default" id="verify" value="Verify"/>
					</div>
				</form>
			</fieldset>
		</div>
	</body>
</html>