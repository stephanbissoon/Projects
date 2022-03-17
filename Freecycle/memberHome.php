<?php
	header("Cache-Control: no-cache");
	header("Pragma: no-cache");
	
	session_start();
	
	if(!isSet($_SESSION['id']))
	{
		header('Location:index.php');
		exit();
	}
	
	$con = null;
	$stmt = null;
	$post_id = "";
	$title = "";
	$date = "";
	$table_data = "";
	$table_data_count = 0;
	
	require('dbConnect.php');
	
	if($stmt = mysqli_prepare($con, "SELECT POST_ID, TITLE, DATE_FORMAT(DATE_TIME, '%D %M %Y %h:%i %p') FROM POST WHERE MEMBER_ID = ? ORDER BY POST_ID DESC"))
	{
		mysqli_stmt_bind_param($stmt, "s", $_SESSION['id']);

		mysqli_stmt_execute($stmt);
		
		mysqli_stmt_bind_result($stmt, $post_id, $title, $date);
		
		while(mysqli_stmt_fetch($stmt))
		{
			$table_data .= "<tr>
								<td>$post_id</td>
								<td>$title</td>
								<td>$date</td>
								<td><a class='btn btn-default' href='viewAndUpdatePost.php?post_id=$post_id'><span class='glyphicon glyphicon-eye-open'></span> Select</a></td>
								<td><a class='btn btn-default' onclick='deletePostModal($post_id)'><span class='glyphicon glyphicon-remove'></span> Delete</a></td>
							</tr>";
			
			$table_data_count++;
		}
		
		mysqli_stmt_close($stmt);
	}
	
	else
	{
		echo "<p class='error'>Database processing error. Please contact your website administrator.</p>";
	}
	
	mysqli_close($con);
	
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
		
		<title>Home | Royal Borough Freecycle</title>
	</head>

	<body>
		<nav class="navbar navbar-default">
		  <div class="container-fluid">
			<div class="navbar-header">
			  <a class="navbar-brand" href="#">Royal Borough of Greenwich Freecycle</a>
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
		</nav>
			
		<div class="container">
		
			<h2>Welcome, <?php echo $_SESSION['username'] ?>!</h2>
			
			<p>Hey, <?php echo $_SESSION['username'] ?>, welcome to the Royal Borough of Greenwich's freecycle! Here you can trade the items you don't wish to possess anymore with other users on this website. You can start by creating a <a href="newPost.php">new</a> post. You can also <a href="search.php">search</a> for other user's items.</p>
			
			<?php
				if(isSet($_GET['feedback']))
				{
					echo $_GET['feedback'];
				}
				
				if($table_data_count > 0 && !empty($table_data))
				{
					echo 	"<h3>Your Posts</h3>
							<div class='table-responsive'>
								<table class='table table-bordered table-hover'>
									<thead>
										<tr>
											<th>Post ID</th>
											<th>Title</th>
											<th>Date</th>
											<th>&nbsp;</th>
											<th>&nbsp;</th>
										</tr>
									</thead>
									<tbody>
									$table_data
									</tbody>
								</table>
							</div>";
				}
			?>
			
			<div id="delete_modal" class="modal fade" role="dialog">
			  <div class="modal-dialog">
				<div class="modal-content">
				  <div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">&times;</button>
					<h4 class="modal-title">Confirmation</h4>
				  </div>
				  <div class="modal-body">
					<p>Are you sure you want to delete this post? This cannot be undone once completed.</p>
				  </div>
				  <div class="modal-footer">
					<button type="button" onclick='deletePost()' class="btn btn-default" data-dismiss="modal">Delete</button>
					<button type="button" class="btn btn-default" data-dismiss="modal">Cancel</button>
				  </div>
				</div>
			  </div>
			</div>
			
			<div id="post_modal" class="modal fade" role="dialog">
			  <div class="modal-dialog">
				<div class="modal-content">
				  <div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">&times;</button>
					<h4 class="modal-title">Post Failed To Delete</h4>
				  </div>
				  <div class="modal-body">
					<p>We have seem to encountered an issue when deleting your post. Please try again. If the problem persists, please contact your website administrator.</p>
				  </div>
				  <div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
				  </div>
				</div>
			  </div>
			</div>
		</div>
	</body>
</html>