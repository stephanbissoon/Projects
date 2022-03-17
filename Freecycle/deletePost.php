<?php
	if(isSet($_POST['post_id']))
	{
		$post_id = $_POST["post_id"];
		$con = null;
		$stmt = null;
		$img_name = "";
		$result = "true";
			
		require('dbConnect.php');
			
		if($stmt = mysqli_prepare($con, "SELECT NAME FROM IMAGE WHERE POST_ID = ?"))
		{
			mysqli_stmt_bind_param($stmt, "s", $post_id);
			
			mysqli_stmt_execute($stmt);
			
			mysqli_stmt_bind_result($stmt, $img_name);
			
			while(mysqli_stmt_fetch($stmt))
			{
				unlink("images/post_images/$img_name");
			}
			
			mysqli_stmt_close($stmt);
			
			if($stmt = mysqli_prepare($con, "DELETE FROM IMAGE WHERE POST_ID = ?"))
			{
				mysqli_stmt_bind_param($stmt, "s", $post_id);
				
				if(!mysqli_stmt_execute($stmt))
				{
					$result = "false";
				}
				
				mysqli_stmt_close($stmt);
			}
		}
		
		if($stmt = mysqli_prepare($con, "DELETE FROM POST WHERE POST_ID = ?"))
		{
			mysqli_stmt_bind_param($stmt, "s", $post_id);
				
			if(!mysqli_stmt_execute($stmt))
			{
				$result = "false";
			}
			
			mysqli_stmt_close($stmt);
		}
		
		mysqli_close($con);
		echo $result;
	}
?>