function validateLogin(form)
{
	var flag = true;
	
	if(form.username.value.trim() === "")
	{
		document.getElementById("username_error").innerHTML = "Please enter your username.";
		flag = false;
	}
	
	if(form.password.value.trim() === "")
	{
		document.getElementById("password_error").innerHTML = "Please enter your password.";
		flag = false;
	}
	
	if(flag)
	{
		$('#login').attr("disabled", "true");
		form.submit();
	}
	
	return flag;
}

function validateRegistration(form)
{
	var flag = true;
	
	if(form.username.value.trim() === "")
	{
		document.getElementById("username_error").innerHTML = "Please enter a username.";
		flag = false;
	}
	
	if(form.email.value.trim() === "")
	{
		document.getElementById("email_error").innerHTML = "Please enter your e-mail address.";
		flag = false;
	}
	
	if(form.password.value.trim() === "")
	{
		document.getElementById("password_error").innerHTML = "Please enter a password.";
		flag = false;
	}
	
	if(form.captcha.value.trim() === "")
	{
		document.getElementById("captcha_error").innerHTML = "Please enter the captcha string.";
		flag = false;
	}
	
	if(flag)
	{
		$('#register').attr("disabled", "true");
		form.submit();
	}
	
	return flag;
}

function validateVerify(form)
{
	var flag = true;
	
	if(form.email.value.trim() === "")
	{
		document.getElementById("email_error").innerHTML = "Please enter your e-mail address.";
		flag = false;
	}
	
	if(form.code.value.trim() === "")
	{
		document.getElementById("code_error").innerHTML = "Please enter your verification code.";
		flag = false;
	}
	
	if(flag)
	{
		$('#verify').attr("disabled", "true");
		form.submit();
	}
	
	return flag;
}

function validatePost(form)
{
	var flag = true;
	
	if(form.title.value.trim() === "")
	{
		document.getElementById("title_error").innerHTML = "Please enter a suitable title.";
		flag = false;
	}
	
	if(form.description.value.trim() === "")
	{
		document.getElementById("description_error").innerHTML = "Please enter a description for the item(s).";
		flag = false;
	}
	
	if(form.images.value !== "" && form.alt_text.value.trim() === "")
	{
		document.getElementById("alt_error").innerHTML = "You have select image(s) but no alt text was detected.";
		flag = false;
	}
	
	if(form.images.value === "" && form.alt_text.value.trim() !== "")
	{
		document.getElementById("img_error").innerHTML = "You entered some alternative text for your image(s) but no image(s) were selected.";
		flag = false;
	}
	
	if(flag)
	{
		if($('#publish') !== null)
		{
			$('#publish').attr("disabled", "true");
		}
		
		if($('#update') !== null)
		{
			$('#update').attr("disabled", "true");
		}
		
		form.submit();
	}
	
	return flag;
}

function validateSearch(form)
{
	var flag = true;
	
	if(form.query.value.trim() === "")
	{
		document.getElementById("search_error").innerHTML = "Please enter a search term.";
		flag = false;
	}
	
	if(flag)
	{
		$("#search").attr("disabled", "true");
		form.submit();
	}
	
	return flag;
}

function checkUsername(username) //ajax to check username availability
{
	var requestObj = new XMLHttpRequest();
	
	requestObj.onreadystatechange = function()
	{
		if(this.readyState === 4 && this.status === 200)
		{
			if(this.responseText.trim() !== "")
			{
				document.getElementById("username_error").innerHTML = this.responseText.trim();
				document.getElementById("register").disabled = true;
			}
			
			else
			{
				document.getElementById("username_error").innerHTML = "";
				document.getElementById("register").disabled = false;
			}
		}
	};
	
	requestObj.open("POST", "checkUsername.php", true);
	requestObj.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
	requestObj.send("username=" + username);
}

function deletePicModal(image_name) //show pic delete modal
{
	window.localStorage.setItem("image_name", image_name);
	
	$("#delete_modal").modal();
}


function deletePic() //ajax to delete picture
{
	var requestObj = new XMLHttpRequest();
	
	$("#update").attr("disabled", "true");
	
	requestObj.onreadystatechange = function()
	{
		if(this.readyState === 4 && this.status === 200)
		{	
			if(this.responseText.trim() === "true")
			{
				window.localStorage.removeItem("image_name");
				window.location.reload();
			}
			
			else
			{
				$('#image_modal').modal();
				$("#update").attr("disabled", "false");
			}
		}
	};
	
	requestObj.open("POST", "deletePic.php", true);
	requestObj.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
	requestObj.send("img_name=" + window.localStorage.getItem("image_name"));
}

function deletePostModal(post_id) //show delete modal
{
	window.localStorage.setItem("post_id", post_id);
	
	$("#delete_modal").modal();
}

function deletePost() //ajax to delete post
{
	var requestObj = new XMLHttpRequest();
	
	$(".btn").attr("disabled", "true");
	
	requestObj.onreadystatechange = function()
	{
		if(this.readyState === 4 && this.status === 200)
		{
			if(this.responseText.trim() === "true")
			{
				window.localStorage.removeItem("post_id");
				window.location.reload();
			}
			
			else
			{
				$('#post_modal').modal();
				$(".btn").attr("disabled", "false");
			}
		}
	};
	
	requestObj.open("POST", "deletePost.php", true);
	requestObj.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
	requestObj.send("post_id=" + window.localStorage.getItem("post_id"));
}
