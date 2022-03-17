<?php
	header("Cache-Control: no-cache");
	header("Pragma: no-cache");
	
	session_start();
	
	if(!isSet($_SESSION['id']))
	{
		header('Location:index.php');
	}

	require('functions.php');

	if(isSet($_POST['post_id']) && isSet($_POST['title']) && isSet($_POST['description']))
	{
		$post_id = $_POST['post_id'];
		$title = $_POST['title'];
		$body = $_POST['description'];
		$images = null;
		$image_alt = "";
		$con = null;
		$stmt = null;
		$feedback = "";
		
		if(validatePost($title, $body, $feedback))
		{
			$title = filter_var($title, FILTER_SANITIZE_STRING);
			$body = filter_var($body, FILTER_SANITIZE_STRING);
			
			require('dbConnect.php');
			
			if($stmt = mysqli_prepare($con, "UPDATE POST SET TITLE = ?, DESCRIPTION = ? WHERE POST_ID = ? AND MEMBER_ID = ?"))
			{
				mysqli_stmt_bind_param($stmt, "ssss", $title, $body, $post_id, $_SESSION['id']);
				
				if(mysqli_stmt_execute($stmt))
				{
					if(isSet($_FILES['images']) && isSet($_POST['alt_text']))
					{
						$images = $_FILES['images'];
						$alt_text = $_POST['alt_text'];
						
						if(is_uploaded_file($images['tmp_name'][0]))
						{
							if(substr($alt_text, -1) != ";")
							{
								$alt_text .= ";";
							}
							
							if(checkFileValidity($images, $alt_text, $feedback))
							{
								if($stmt = mysqli_prepare($con, "INSERT INTO IMAGE VALUES(NULL, ?, ?, ?)"))
								{
									foreach($images['name'] as $key => $name)
									{
										$cur_alt_text = filter_var(trim(substr($alt_text, 0, strpos($alt_text, ";"))), FILTER_SANITIZE_STRING);
										$alt_text = substr($alt_text, (strpos($alt_text, ";") + 1));
										
										move_uploaded_file($images['tmp_name'][$key], "images/post_images/$name");
										
										mysqli_stmt_bind_param($stmt, "sss", $name, $cur_alt_text, $post_id);
										
										if(mysqli_stmt_execute($stmt))
										{
											$feedback .= "<p class='success'>Image($name) uploaded successfully.</p>";
										}
										
										else
										{
											$feedback .= "<p class='error'>Image($name) failed to upload.</p>";
										}
									}
								}
								
								else
								{
									$feedback .= "<p class='error'>Database processing error occurred when publishing your post. Please contact your website administrator.</p>";
								}
							}
						}
					}
					
					$feedback .= "<p class='success'>Post title and description updated successfully.</p>";
				}
					
				else
				{
					$feedback .= "<p class='error'>Post failed to be published. Please contact your website administrator.</p>";
				}
				
				mysqli_stmt_close($stmt);
			}
			
			else
			{
				$feedback .= "<p class='error'>Database processing error for post publish. Please contact your website administrator.</p>";
			}
			
			mysqli_close($con);
		}
		
		header("Location:memberHome.php?feedback=$feedback");
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
		<title>View and Update Post | Royal Borough Freecycle</title>
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
				<li><a href="memberProfile.php"><span class="glyphicon glyphicon-user"></span> Member Profile</a></li>
				<li><a href="logout.php"><span class="glyphicon glyphicon-log-out"></span> Log Out</a></li>
			</ul>
		  </div>
		</nav>
	
		<div class="container">
			<h1>View And Update Post</h1>
			
			<?php
				
				if(isSet($_GET['post_id']))
				{
					$post_id = $_GET['post_id'];
					$con = null;
					$stmt = null;
					$title = "";
					$description = "";
					$img_name = "";
					$img_alt = "";
					
					require("dbConnect.php");
					if($stmt = mysqli_prepare($con, "SELECT TITLE, DESCRIPTION FROM POST WHERE POST_ID = ?"))
					{
						mysqli_stmt_bind_param($stmt, "s", $post_id);
						mysqli_stmt_execute($stmt);
						mysqli_stmt_bind_result($stmt, $title, $description);
						mysqli_stmt_fetch($stmt);
						mysqli_stmt_close($stmt);
					}
					
					else
					{
						echo "<p class='error'>Database processing error. Please contact your website administrator.</p>";
					}
					
					echo "<form method='post' action='viewAndUpdatePost.php' enctype='multipart/form-data' onsubmit='return validatePost(this)'>
							<fieldset>
								<input type='hidden' name='post_id' id='post_id' value='$post_id'/>
								<div class='form-group'>
								<label for='title'>Title</label>
								<input type='text' class='form-control' name='title' value='$title' id='title'/><span id='title_error' class='help-block'></span>
								</div>
								<div class='form-group'>
								<label for='description'>Description</label>
								<textarea class='form-control' name='description' id='description'  maxlength='2500' placeholder='Enter a description of what you are trading here...' rows='10' cols='50'>$description</textarea>
								<p id='description_error' class='help-block'></p>
								</div>
								<hr></hr>";
								
								if($stmt = mysqli_prepare($con, "SELECT NAME, ALT_TEXT FROM IMAGE WHERE POST_ID = ?"))
								{
									mysqli_stmt_bind_param($stmt, "s", $post_id);
									mysqli_stmt_execute($stmt);
									mysqli_stmt_bind_result($stmt, $img_name, $img_alt);
									
									while(mysqli_stmt_fetch($stmt))
									{
										echo "<div class='img'><img src='images/post_images/$img_name' alt='$img_alt' /><a class='delete' onclick='deletePicModal(".'"'.$img_name.'"'.")'>Delete</a></div>";
									}
									
									mysqli_stmt_close($stmt);
								}
								
								else
								{
									echo "<p class='error'>Database processing error. Please contact your website administrator.</p>";
								}
								
								
					echo		"<br></br>
								<div class='form-group'>
									<label for='images'>Add Image(s) - 10 MB Total Size Limit</label>
									<input type='file' name='images[]' id='images' accept='image/*' multiple='true' />
									<p id='img_error' class='help-block'></p>
								</div>
								<div class='form-group'>
									<label for='alt_text'>Image Description(s)</label>
									<p>Please enter mini descriptions for each of the images you have selected (if any) in the area below. Separate each description with a ".";"." (semi-colon). End the entire input with a ".";"." (semi-colon). Keep all mini descriptions to a maximum to 50 characters.</p>
									<textarea class='form-control' name='alt_text' id='alt_text' placeholder='Enter the mini description(s) for your additional image(s) here...' rows='1' cols='1'></textarea>
									<p id='alt_error' class='help-block'></p>
								</div>
								<input type='submit' class='btn btn-default' name='update' id='update' value='Update'/>
							</fieldset>
						</form>";
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
					<p>Are you sure you want to delete this image? This cannot be undone once completed.</p>
				  </div>
				  <div class="modal-footer">
					<button type="button" onclick='deletePic()' class="btn btn-default" data-dismiss="modal">Delete</button>
					<button type="button" class="btn btn-default" data-dismiss="modal">Cancel</button>
				  </div>
				</div>
			  </div>
			</div>
			
			<div id="image_modal" class="modal fade" role="dialog">
			  <div class="modal-dialog">
				<div class="modal-content">
				  <div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">&times;</button>
					<h4 class="modal-title">Image Failed To Delete</h4>
				  </div>
				  <div class="modal-body">
					<p>We have seem to encountered an issue when deleting your picture. Please try again. If the problem persists, please contact your website administrator.</p>
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