<?php
	function validateRegistrationData($username, $password, $email, $user_captcha, $captcha_code, &$feedback)
	{
		$result = true;
		
		if($username == null || empty(trim($username)))
		{
			$feedback .= "<p class='error'>Please enter a username.</p>";
			$result = false;
		}
		
		if($password == null || empty(trim($password)))
		{
			$feedback .= "<p class='error'>Please enter a password.</p>";
			$result = false;
		}
		
		if($email == null || empty(trim($email)) || !filter_var($email, FILTER_VALIDATE_EMAIL))
		{
			$feedback .= "<p class='error'>Please enter a valid e-mail address.</p>";
			$result = false;
		}
		
		if($user_captcha == null || $captcha_code == null || empty(trim($user_captcha)) || $captcha_code != $user_captcha)
		{
			$feedback .= "<p class='error'>The code you have entered is invalid.</p>";
			$result = false;
		}
		
		require("dbConnect.php");

		if($stmt = mysqli_prepare($con, "SELECT USERNAME FROM MEMBER WHERE USERNAME = ?"))
		{
			mysqli_stmt_bind_param($stmt, "s", $username);
			
			mysqli_stmt_execute($stmt);
			
			if(mysqli_stmt_fetch($stmt))
			{
				$feedback .= "<p class='error'>$username is already taken, please choose another username.</p>";
				$result = false;
			}
		}
		
		else
		{
			$feedback .= "<p class='error'>An error occurred in attempting to check the username your have entered. Please contact your website administrator.</p>";
			$result = false;
		}
		
		return $result;
	}
	
	function getActivationCode()
	{
		$code = "";
		
		for($i = 0; $i < 5; $i++)
		{
			$code .= rand(0, 9);
		}
		
		return $code;
	}
	
	function validateVerify($email, $code, &$feedback)
	{
		$result = true;
		
		if($email == null || empty(trim($email)) || !filter_var($email, FILTER_VALIDATE_EMAIL))
		{
			$feedback .= "<p class='error'>Please enter an e-mail address.</p>";
			$result = false;
		}
		
		if($code == null || empty(trim($code)))
		{
			$feedback .= "<p class='error'>Please enter a code.</p>";
			$result = false;
		}

		return $result;
	}
	
	function validateLoginData($username, $password, &$feedback)
	{
		$result = true;
		
		if($username == null || empty(trim($username)))
		{
			$feedback .= "<p class='error'>Please enter a username.</p>";
			$result = false;
		}
		
		if($password == null || empty(trim($password)))
		{
			$feedback .= "<p class='error'>Please enter a password.</p>";
			$result = false;
		}

		return $result;
	}

	function validateCode($code, &$feedback)
	{
		$result = true;
		
		if($code == null || empty(trim($code)))
		{
			$feedback .= "<p class='error'>Please enter your verification code.</p>";
			$result = false;
		}

		return $result;
	}
	
	function validatePost($title, $description, &$feedback)
	{
		$result = true;
		
		if($title == null || empty(trim($title)))
		{
			$feedback .= "<p class='error'>Please enter a title.</p>";
			$result = false;
		}
		
		elseif(strlen($title) > 50)
		{
			$feedback .= "<p class='error'>Please enter a title that is equal to or less than 50 characters</p>";
			$result = false;
		}
			
		
		if($description == null || empty(trim($description)))
		{
			$feedback .= "<p class='error'>Please enter a description for the items being sold.</p>";
			$result = false;
		}
		
		elseif(strlen($description) > 2500)
		{
			$feedback .= "<p class='error'>Please enter a description that is equal to or less than 2500 characters</p>";
			$result = false;
		}
		
		return $result;
	}
	
	function checkFileValidity($images, $alt_text, &$feedback)
	{
		$result = true;
		$cur_alt_text = "";
		
		foreach($images['tmp_name'] as $key => $tmp_name)
		{
			$file_name = $images['name'][$key];
			$file_size = $images['size'][$key];
			$file_tmp_name = $images['tmp_name'][$key];
			$file_type = $images['type'][$key];	
			$file_err = $images['error'][$key];
			
			$location = "images/post_images/$file_name";
			$img_ext = pathinfo($file_name, PATHINFO_EXTENSION);
			
			switch ($file_err)
			{
				case 0:
					break;
				case 1:
					$feedback .= "<p class='error'>$file_name exceeds allowed 2MB size.</p>";
					$result = false;
					break;
				case 2:
					$feedback .= "<p class='error'>$file_name exceeds the file limit.</p>";
					$result = false;
					break;
				case 3:
					$feedback .= "<p class='error'>The file you are trying upload - $file_name - was only partially uploaded.</p>";
					$result = false;
					break;
				default:
					$feedback .= "<p class='error'>Unknown errors with selected files.</p>";
					$result = false;
					break;
			}
			
			if(($img_ext != 'gif') && ($img_ext != 'GIF')  && ($img_ext != 'jpeg') && ($img_ext != 'JPEG') && ($img_ext != 'jpg') && ($img_ext != 'JPG') && ($img_ext != 'png') && ($img_ext != 'PNG')) 
			{
				$feedback .= "<p class='error'>The file $file_name is of incorrect file type. Only image file types are allowed.</p>";
				$result = false;
			}
			
			if(file_exists($location))
			{
				$feedback .= "<p class='error'>The $file_name file already exists on this server.</p>";
				$result = false;
			}
			
			$cur_alt_text = trim(substr($alt_text, 0, strpos($alt_text, ";")));
			$alt_text = substr($alt_text, (strpos($alt_text, ";") + 1));
			
			if(empty($cur_alt_text))
			{
				$feedback .= "<p class='error'>No alternative text detected for $file_name. Please review your image mini-descriptions.</p>";
				$result = false;
			}
			
			elseif(strlen($cur_alt_text) > 50)
			{
				$feedback .= "<p class='error'>The alternative text for $file_name is longer than 50 characters. Please shorten this description.</p>";
				$result = false;
			}
		}
		
		return $result;
	}
?>