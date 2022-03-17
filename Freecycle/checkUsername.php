<?php
	if(isSet($_POST['username']))
	{
		$username = $_POST["username"];
		
		require('dbConnect.php');

		if($stmt = mysqli_prepare($con, "SELECT USERNAME FROM MEMBER WHERE USERNAME = ?"))
		{
			mysqli_stmt_bind_param($stmt, "s", $username);
			
			mysqli_stmt_execute($stmt);
			
			if(mysqli_stmt_fetch($stmt))
			{
				echo "$username is already taken, please choose another username.";
			}
		}
		
		else
		{
			echo "An error occurred in attempting to check the username your have entered. Please contact your website administrator.";
		}
		
		mysqli_close($con);
	}
?>