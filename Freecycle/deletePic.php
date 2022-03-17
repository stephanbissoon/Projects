<?php
	if(isSet($_POST['img_name']))
	{
		$img_name = $_POST["img_name"];
		$result = "false";
		
		require('dbConnect.php');
			
		if($stmt = mysqli_prepare($con, "DELETE FROM IMAGE WHERE NAME = ?"))
		{
			mysqli_stmt_bind_param($stmt, "s", $img_name);
			
			if(mysqli_stmt_execute($stmt))
			{
				unlink("images/post_images/$img_name");
				$result = "true";
			}
		}
		
		echo $result;
		
		mysqli_close($con);
	}
?>