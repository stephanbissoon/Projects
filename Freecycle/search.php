<?php
	if (isSet($_GET['query']) && !isSet($_GET['page']))
	{
		header("Location:".$_SERVER['REQUEST_URI']."&page=1");
		exit();
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
		
		<title>Search | Royal Borough Freecycle</title>
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
							  <li><a href="#"><span class="glyphicon glyphicon-search"></span> Search Posts</a></li>
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
			<h1>Search</h1>
			
			<form method="get" action="search.php" onsubmit="return validateSearch(this)">
				<div class="checkbox">
					<label for="description"><input type="checkbox" name="description" id="description"/> Include Description Search</label>
				</div>
				
				<div class="checkbox">
					<label for="images"><input type="checkbox" name="images" id="images"/> Include Posts With Images</label>
				</div>
				
				<div class="form-group">
				  <label for="price">Price Sort</label>
				  <select class="form-control" id="price">
					<option>High to Low</option>
					<option>Low to High</option>
				  </select>
				</div>
				
				<div class="input-group">
					<input type="text" class="form-control" name="query" id="query" placeholder="Search..."/>
					<div class="input-group-btn">
						<button class="btn btn-default" id='search' type="submit"><span class="glyphicon glyphicon-search"></span> Search</button>
					</div>
				</div>
				<p id="search_error" class="help-block"></p>
			</form>
			
			<br></br>
			
			<?php
				if(isSet($_GET['query']) && isSet($_GET['page']))
				{
					$query = $_GET['query'];
					$images = false;
					$description = false;
					$sql_query = "";
					$con = null;
					$stmt = null;
					$post_id = "";
					$title = "";
					$date_time = "";
					$username = "";
					$records_per_page = 5;
					$page = $_GET['page'];
					$offset = 0;
					$record_amount = 0;
					$total_pages = 0;
					
					if(isSet($_GET['images']))
					{
						$images = true;
					}
					
					if(isSet($_GET['description']))
					{
						$description = true;
					}
					
					$sql_query = "SELECT POST_ID, TITLE, DATE_FORMAT(DATE_TIME, '%D %M %Y %h:%i %p'), USERNAME FROM POST, MEMBER WHERE MEMBER.MEMBER_ID = POST.MEMBER_ID AND TITLE LIKE CONCAT('%',?,'%')";
				
					if($description) //add description to query if it was selected
					{
						$sql_query .= " AND DESCRIPTION LIKE CONCAT('%',?,'%')";
					}
					
					if($images) //add image to query if it was selected
					{
						$sql_query .= " AND POST_ID IN (SELECT POST_ID FROM IMAGE)";
					}
					
					else
					{
						$sql_query .= " AND POST_ID NOT IN (SELECT POST_ID FROM IMAGE)";
					}
					
					$offset = ($page - 1) * $records_per_page; //set which page to start from
					
					$sql_query .= " ORDER BY DATE_TIME LIMIT $records_per_page OFFSET $offset";
					
					require("dbConnect.php");
					
					if($stmt = mysqli_prepare($con, $sql_query))
					{
						if($description)
						{
							mysqli_stmt_bind_param($stmt, "ss", $query, $query);
						}
						
						else
						{
							mysqli_stmt_bind_param($stmt, "s", $query);
						}

						mysqli_stmt_execute($stmt);
						
						mysqli_stmt_store_result($stmt);
						
						mysqli_stmt_bind_result($stmt, $post_id, $title, $date_time, $username);
						
						echo "<table class='table table-bordered table-hover'>
								<thead>
									<tr>
										<th>Title</th>
										<th>Posting Date/Time</th>
										<th>Author</th>
										<th>&nbsp;</th>
									</tr>
								</thead>
								<tbody>";
						
						while(mysqli_stmt_fetch($stmt)) //output data for query
						{
							echo 	"<tr>
										<td>$title</td>
										<td>$date_time</td>
										<td>$username</td>
										<td><a class='btn btn-default' href='viewPost.php?post_id=$post_id'><span class='glyphicon glyphicon-eye-open'></span> View</a></td>
									</tr>";
						}
						
						echo "</tbody>
							</table>";
							
							
						mysqli_stmt_close($stmt);
							
						$sql_query = "SELECT POST_ID, TITLE, DATE_FORMAT(DATE_TIME, '%D %M %Y %h:%i %p'), USERNAME FROM POST, MEMBER WHERE MEMBER.MEMBER_ID = POST.MEMBER_ID AND TITLE LIKE CONCAT('%',?,'%')";	
						
						if($description)
						{
							$sql_query .= " AND DESCRIPTION LIKE CONCAT('%',?,'%')";
						}
						
						if($images)
						{
							$sql_query .= " AND POST_ID IN (SELECT POST_ID FROM IMAGE)";
						}
						
						else
						{
							$sql_query .= " AND POST_ID NOT IN (SELECT POST_ID FROM IMAGE)";
						}
						
						$sql_query .= " ORDER BY DATE_TIME";
						
						if($stmt = mysqli_prepare($con, $sql_query))
						{
							if($description)
							{
								mysqli_stmt_bind_param($stmt, "ss", $query, $query);
							}
							
							else
							{
								mysqli_stmt_bind_param($stmt, "s", $query);
							}

							mysqli_stmt_execute($stmt);
							
							mysqli_stmt_store_result($stmt);
							
							$record_amount = mysqli_stmt_num_rows($stmt);
							
							mysqli_stmt_free_result($stmt);
								
							mysqli_stmt_close($stmt);
							
							$total_pages = ceil($record_amount / $records_per_page); //get total amount of pages
							
							echo("<p>Records Retrieved: $record_amount</p>");
							
							if($total_pages > 1) //check if total amount of pages is greater than one
							{
								echo "<ul class='pagination'>"; //output pagination
							
								for ($i = 1; $i <= $total_pages; $i++)
								{
									if($i == $page)
									{
										if($description == true && $images == true)
										{
											echo "<li class='active'><a href='search.php?description=on&images=on&query=$query&page=$i'>$i</a></li>";
										}
										
										elseif($description == true && $images == false)
										{
											echo "<li class='active'><a href='search.php?description=on&query=$query&page=$i'>$i</a></li>";
										}
										
										elseif($description == false && $images == true)
										{
											echo "<li class='active'><a href='search.php?images=on&query=$query&page=$i'>$i</a></li>";
										}
										
										else
										{
											echo "<li class='active'><a href='search.php?query=$query&page=$i'>$i</a></li>";
										}
									}
									
									else
									{
										if($description == true && $images == true)
										{
											echo "<li><a href='search.php?description=on&images=on&query=$query&page=$i'>$i</a></li>";
										}
										
										elseif($description == true && $images == false)
										{
											echo "<li><a href='search.php?description=on&query=$query&page=$i'>$i</a></li>";
										}
										
										elseif($description == false && $images == true)
										{
											echo "<li><a href='search.php?images=on&query=$query&page=$i'>$i</a></li>";
										}
										
										else
										{
											echo "<li><a href='search.php?query=$query&page=$i'>$i</a></li>";
										}
									}
								}
								
								echo "</ul>";
							}
						}
						
						else
						{
							echo "<p class='error'>Database processing error. Please contact your website administrator.</p>";
						}
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