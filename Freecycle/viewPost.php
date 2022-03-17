<?php echo '<?xml version="1.0" encoding="utf-8"?>'; ?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">

<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<script src="js/jquery.min.js" type="text/javascript"></script>
		<script src="js/bootstrap.min.js" type="text/javascript"></script>
		<script src="js/main.js" type="text/javascript"></script>
		<link rel="stylesheet" href="css/bootstrap.min.css" type="text/css"/>
		<link href="css/style.css" rel="stylesheet" type="text/css" />
		<title>View Post | Royal Borough Freecycle</title>
	</head>

	<body>
		<?php
			session_start();
		
			if(isSet($_SESSION['id']))
			{
				echo '<nav class="navbar navbar-default">
						<div class="container-fluid">
							<div class="navbar-header">
							  <a class="navbar-brand" href="memberHome.php">Royal Borough of Greenwich Freecycle</a>
							</div>
							<ul class="nav navbar-nav">
							  <li><a href="newPost.php"><span class="glyphicon glyphicon-pencil"></span> New Post</a></li>
							  <li><a href="search.php"><span class="glyphicon glyphicon-search"></span> Search Posts</a></li>
							</ul>
							<ul class="nav navbar-nav navbar-right">
								<li><a href="memberProfile.php"><span class="glyphicon glyphicon-user"></span> Member Profile</a></li>
								<li><a href="logout.php"><span class="glyphicon glyphicon-log-out"></span> Log Out</a></li>
							</ul>
						  </div>
						</nav>';
			}
			
			else
			{
				echo '<nav class="navbar navbar-default">
						<div class="container-fluid">
							<div class="navbar-header">
							  <a class="navbar-brand" href="#">Royal Borough of Greenwich Freecycle</a>
							</div>
							<ul class="nav navbar-nav navbar-right">
							  <li><a href="register.php"><span class="glyphicon glyphicon-user"></span> Register</a></li>
							  <li><a href="index.php"><span class="glyphicon glyphicon-log-in"></span> Log In</a></li>
							</ul>
						  </div>
						</nav>';
			}
		?>
	
		<div class="container">
			<h1>View Post</h1>
			
			<hr></hr>
			
			<?php
				if(isSet($_GET['post_id']))
				{
					$post_id = $_GET['post_id'];
					$con = null;
					$stmt = null;
					$username = "";
					$bio = "";
					$title = "";
					$description = "";
					$date = "";
					$img_name = "";
					$img_alt = "";
					
					require('dbConnect.php');
					
					if($stmt = mysqli_prepare($con, "SELECT USERNAME, BIOGRAPHY FROM MEMBER, POST WHERE MEMBER.MEMBER_ID = POST.MEMBER_ID AND POST_ID = ?"))
					{
						mysqli_stmt_bind_param($stmt, "s", $post_id);
						mysqli_stmt_execute($stmt);
						mysqli_stmt_bind_result($stmt, $username, $bio);
						mysqli_stmt_fetch($stmt);
						mysqli_stmt_close($stmt);
						
						if(empty($bio))
						{
							$bio = "No introduction defined by this trader.";
						}
					}
					
					else
					{
						echo "<p class='error'>Database processing error. Please contact your website administrator.</p>";
					}
					
					if($stmt = mysqli_prepare($con, "SELECT TITLE, DESCRIPTION, DATE_FORMAT(DATE_TIME, '%D %M %Y %h:%i %p') FROM POST WHERE POST_ID = ?"))
					{
						mysqli_stmt_bind_param($stmt, "s", $post_id);
						mysqli_stmt_execute($stmt);
						mysqli_stmt_bind_result($stmt, $title, $description, $date);
						mysqli_stmt_fetch($stmt);
						
						echo 		"<h3>$title</h3>
									<p><strong>Trader</strong></p>
									<p>$username</p>
									<p><strong>Trader's Introduction</strong></p>
									<p>$bio</p>
									<p><strong>Details</strong></p>
									<p>$description</p>
									<p><strong>Posting Date</strong></p>
									<p>$date</p>
									<hr></hr>";
									
						mysqli_stmt_close($stmt);
					}
					
					else
					{
						echo "<p class='error'>Database processing error. Please contact your website administrator.</p>";
					}
								
					if($stmt = mysqli_prepare($con, "SELECT NAME, ALT_TEXT FROM IMAGE WHERE POST_ID = ?"))
					{
						mysqli_stmt_bind_param($stmt, "s", $post_id);
						mysqli_stmt_execute($stmt);
						mysqli_stmt_bind_result($stmt, $img_name, $img_alt);
						
						while(mysqli_stmt_fetch($stmt))
						{
							echo "<div class='img'><img src='images/post_images/$img_name' alt='$img_alt' /></div>";
						}
						
						mysqli_stmt_close($stmt);
					}
					
					else
					{
						echo "<p class='error'>Database processing error. Please contact your website administrator.</p>";
					}
					
					mysqli_close($con);
				}
			?>
		</div>
	</body>
</html>