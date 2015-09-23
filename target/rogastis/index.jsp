<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<script type="text/javascript" src="scripts/jquery-2.1.3.js"></script>
<script type="text/javascript" src="scripts/jquery.json.js"></script>
<script type="text/javascript" src="scripts/jquery.session.js"></script>
<title>Welcome to Rogastis !</title>
<link rel="shortcut icon" type="image/ico" href="favicon.ico" />
<link rel="stylesheet" href="style.css" type="text/css">
<link href="jquery-ui.css" rel="stylesheet">
<style>
body {
	font: 62.5% "Trebuchet MS", sans-serif;
	margin: 50px;
}

.demoHeaders {
	margin-top: 2em;
}

#login-dialog-link {
	padding: .4em 1em .4em 20px;
	text-decoration: none;
	position: relative;
}

#login-dialog-link span.ui-icon {
	margin: 0 5px 0 0;
	position: absolute;
	left: .2em;
	top: 50%;
	margin-top: -8px;
}

#question-dialog-link {
	padding: .4em 1em .4em 20px;
	text-decoration: none;
	position: relative;
}

#question-dialog-link span.ui-icon {
	margin: 0 5px 0 0;
	position: absolute;
	left: .2em;
	top: 50%;
	margin-top: -8px;
}

#register-dialog-link {
	padding: .4em 1em .4em 20px;
	text-decoration: none;
	position: relative;
}

#register-dialog-link span.ui-icon {
	margin: 0 5px 0 0;
	position: absolute;
	left: .2em;
	top: 50%;
	margin-top: -8px;
}

#icons {
	margin: 0;
	padding: 0;
}

#icons li {
	margin: 2px;
	position: relative;
	padding: 4px 0;
	cursor: pointer;
	float: left;
	list-style: none;
}

#icons span.ui-icon {
	float: left;
	margin: 0 4px;
}

.fakewindowcontain .ui-widget-overlay {
	position: absolute;
}

select {
	width: 200px;
}
</style>
</head>
<body>
	<div class="ui-widget">
		<div id="userAddSuccess" class="ui-state-highlight ui-corner-all"
			style="margin-top: 20px; padding: 0 .7em;" hidden="true">
			<p>
				<span class="ui-icon ui-icon-info"
					style="float: left; margin-right: .3em;"></span> <strong>User
					Registered Successfully !!</strong>
			</p>
		</div>
	</div>

	<div class="ui-widget">
		<div id="userAddFailure" class="ui-state-error ui-corner-all"
			style="padding: 0 .7em;" hidden="true">
			<p>
				<span class="ui-icon ui-icon-alert"
					style="float: left; margin-right: .3em;"></span> <strong>User
					Registration Failure</strong>
			</p>
		</div>
	</div>

	<div id="header">
		<div class="section">
			<div class="logo">
				<a href="index.html"><img src="images/rogastis.png"></img></a>
			</div>
			<ul>
				<li><a href="#" id="question-dialog-link"
					class="ui-state-default ui-corner-all"> <span
						class="ui-icon ui-icon-newwin"> </span>Post Question
				</a></li>
				<li><a href="#" id="login-dialog-link"
					class="ui-state-default ui-corner-all"> <span
						class="ui-icon ui-icon-newwin"> </span>Login
				</a></li>
				<li><a href="#" id="register-dialog-link"
					class="ui-state-default ui-corner-all"> <span
						class="ui-icon ui-icon-newwin"></span>Register
				</a></li>
			</ul>
		</div>

		<div id="login-dialog" title="Login">
			<p>
				<label>Username</label>&nbsp;&nbsp;<input id="loginusername"
					title="type &quot;a&quot;" type="text">
			<p>
				<label>Password</label>&nbsp;&nbsp;&nbsp;<input id="loginpassword"
					title="type &quot;a&quot;" type="password">
		</div>

		<div id="register-dialog" title="Register">
			<p>
				<label>Username</label>&nbsp;<font size="2" color="red">*</font>&nbsp;<input
					id="username" title="type &quot;a&quot;" type="text">
			<p>
				<label>Password</label>&nbsp;<font size="2" color="red">*</font>&nbsp;&nbsp;<input
					id="password" title="type &quot;a&quot;" type="password">
			<p>
				<label>First Name</label>&nbsp;<font size="2" color="red">*</font>&nbsp;<input
					id="firstname" title="type &quot;a&quot;" type="text">
			<p>
				<label>Last Name</label>&nbsp;<font size="2" color="red">*</font>&nbsp;<input
					id="lastname" title="type &quot;a&quot;" type="text">
			<p>
				<font size="2" color="red">* indicates mandatory field</font>
		</div>

		<div id="question-dialog" title="Post your question">
			<p>
				<label>Enter a title for you question: </label>&nbsp;&nbsp;<font
					size="2" color="red">*</font><br> <br> <input
					id="question" title="type &quot;a&quot;" type="text"
					style="width: 600px; height: 20px;" required="required">
			<p>
				<label>Description: </label>&nbsp;&nbsp;<font size="2" color="red">*</font><br>
				<br>

				<textarea id="description" name="description" maxlength="3000"
					required="required" style="width: 600px; height: 200px;"></textarea>
			<p>
				<font size="2" color="red">* indicates mandatory field</font>
		</div>

	</div>

	<div id="questionList"></div>

	<script src="external/jquery/jquery.js"></script>
	<script src="jquery-ui.js"></script>
	<script type="text/javascript" src="scripts/rogastis.js"></script>
	<script>
		$("#questionList").accordion();

		$("#login-dialog").dialog({
			autoOpen : false,
			width : 400,
			buttons : [ {
				text : "Submit",
				click : function() {
					$(this).dialog("close");
				}
			}, {
				text : "Cancel",
				click : function() {
					$(this).dialog("close");
				}
			} ]
		});

		// Link to open the dialog
		$("#login-dialog-link").click(function(event) {
			$("#login-dialog").dialog("open");
			event.preventDefault();
		});

		$("#user-info-dialog").dialog({
			autoOpen : false,
			width : 400,
			buttons : [ {
				text : "OK",
				click : function() {
					$(this).dialog("close");
				}
			} ]
		});

		// Link to open the dialog
		$("#user-info-dialog-link").click(function(event) {
			$("#user-info-dialog").dialog("open");
			event.preventDefault();
		});

		$("#question-dialog").dialog({
			autoOpen : false,
			width : 800,
			buttons : [ {
				text : "Post",
				click : function() {
					$(this).dialog("close");
				}
			}, {
				text : "Cancel",
				click : function() {
					$(this).dialog("close");
				}
			} ]
		});

		// Link to open the dialog
		$("#question-dialog-link").click(function(event) {
			$("#question-dialog").dialog("open");
			event.preventDefault();
		});

		$("#register-dialog").dialog({
			autoOpen : false,
			width : 400,
			buttons : [ {
				text : "Submit",
				click : function() {
					if ($('#username').val() == '')
						alert('User Name cannot be empty');
					else if ($('#password').val() == '')
						alert('Password cannot be empty');
					else if ($('#firstName').val() == '')
						alert('First Name cannot be empty');
					else if ($('#lastName').val() == '')
						alert('Last Name cannot be empty');
					else {
						addUser();
					}
				}
			}, {
				text : "Cancel",
				click : function() {
					$(this).dialog("close");
				}
			} ]
		});

		// Link to open the dialog
		$("#register-dialog-link").click(function(event) {
			$("#register-dialog").dialog("open");
			event.preventDefault();
		});

		// Hover states on the static widgets
		$("#login-dialog-link, #icons li").hover(function() {
			$(this).addClass("ui-state-hover");
		}, function() {
			$(this).removeClass("ui-state-hover");
		});

		//Hover states on the static widgets
		$("#register-dialog-link, #icons li").hover(function() {
			$(this).addClass("ui-state-hover");
		}, function() {
			$(this).removeClass("ui-state-hover");
		});
	</script>
</body>
</html>