<?php
	header("Cache-Control: no-cache");
	header("Pragma: no-cache");
	
	session_start();
	
	if(!isSet($_SESSION['id']))
	{
		header('Location:index.php');
		exit();
	}
	
	$feedback = "";
	
	if(isSet($_POST['update_bio']))
	{
		$stmt = "";
		$bio = $_POST['bio'];
		
		if(!empty(trim($bio)))
		{
			$bio = filter_var($bio, FILTER_SANITIZE_STRING);
			
			require('dbConnect.php');
			
			if($stmt = mysqli_prepare($con, "UPDATE MEMBER SET BIOGRAPHY = ? WHERE MEMBER_ID = ?"))
			{
				mysqli_stmt_bind_param($stmt, "ss", $bio, $_SESSION['id']);
				
				if(mysqli_stmt_execute($stmt))
				{
					$feedback = "<p class='success'>Your biography has been successfully updated.</p>";
				}
				
				else
				{
					$feedback = "<p class='error'>Your biography failed to update.</p>";
				}
			}
			
			else
			{
				$feedback = "<p class='error'>Database processing error. Please contact your website administrator.</p>";
			}
			
			mysqli_close($con);
		}
	}
	
	echo '<?xml version="1.0" encoding="utf-8"?>';
?>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">

<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<script src="js/jquery.min.js" type="text/javascript"></script>
		<script src="js/bootstrap.min.js" type="text/javascript"></script>
		<script src="js/main.js" type="text/javascript"></script>
		<link rel="stylesheet" href="css/bootstrap.min.css" type="text/css"/>
		<link href="css/style.css" rel="stylesheet" type="text/css" />
		
		<title>Member Profile | Royal Borough Freecycle</title>
	</head>

	<body>
		<nav class="navbar navbar-default">
		  <div class="container-fluid">
			<div class="navbar-header">
			  <a class="navbar-brand" href="memberHome.php">Royal Borough of Greenwich Freecycle</a>
			</div>
			<ul class="nav navbar-nav">
			  <li><a href="newPost.php"><span class="glyphicon glyphicon-pencil"></span> New Post</a></li>
			  <li><a href="search.php"><span class="glyphicon glyphicon-search"></span> Search Posts</a></li>
			</ul>
			<ul class="nav navbar-nav navbar-right">
				<li><a href="#"><span class="glyphicon glyphicon-user"></span> Member Profile</a></li>
				<li><a href="logout.php"><span class="glyphicon glyphicon-log-out"></span> Log Out</a></li>
			</ul>
		  </div>
		</nav>
			
		<div class="container">
			<?php
				if(!empty($feedback))
				{
					echo $feedback;
				}
				
				$con = null;
				$stmt = null;
				$username = "";
				$email = "";
				$date_reg = "";
				$bio = "";
				
				require('dbConnect.php');
				
				if($stmt = mysqli_prepare($con, "SELECT USERNAME, EMAIL, BIOGRAPHY, DATE_FORMAT(DATE_REGISTERED, '%D %M %Y %h:%i %p') FROM MEMBER WHERE MEMBER_ID = ?"))
				{
					mysqli_stmt_bind_param($stmt, "s", $_SESSION['id']);

					mysqli_stmt_execute($stmt);
					
					mysqli_stmt_bind_result($stmt, $username, $email, $bio, $date_reg);
					
					if(mysqli_stmt_fetch($stmt))
					{
						echo "<fieldset>
								<form method='post' action='memberProfile.php'>
									<div class='form-group'>
										<label for='username'>Username</label>
										<input type='text' class='form-control' id='username' name='username' disabled='disabled' value='$username'/>
									</div>
									<div class='form-group'>
										<label for='email'>E-mail Address</label>
										<input type='text' class='form-control' id='email' name='email' disabled='disabled' value='$email'/>
									</div>
									<div class='form-group'>
										<label for='date'>Date Registered</label>
										<input type='text' class='form-control' id='date' name='date_reg' disabled='disabled' value='$date_reg'/>
									</div>
									<div class='form-group'>
										<label for='bio'>Biography</label>
										<textarea id='bio' name='bio' maxlength='2500' class='form-control' rows='3' cols='1' placeholder='Type a biography of yourself here such as your name and location. This information is shown on all your posts. Please refrain from entering any highly sensitive data which can be used to identify you.'>$bio</textarea>
									</div>
									<div>
										<input type='submit' class='btn btn-default' name='update_bio' value='Update'/>
									</div>
								</form>
							</fieldset>";
					}
					
					mysqli_stmt_close($stmt);
				}
				
				else
				{
					echo "<p class='error'>Database processing error. Please contact your website administrator.</p>";
				}
				
				mysqli_close($con);
			?>
		</div>
	</body>
</html>