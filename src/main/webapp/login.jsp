<!DOCTYPE html>
<html ng-app="hys1">
<head>
<%@ include file="imports/head.jsp"%>
<title>Login - Have You Seen?</title>
</head>
<body ng-controller="loginController">
	<%@ include file="imports/menu.jsp"%>
	<div class="intro-margin"></div>
	<div class="content-section-a">
		<div class="container">
			<div class="alert alert-danger" role="alert" ng-if="loginFail">
				<span class="glyphicon glyphicon-exclamation-sign"
					aria-hidden="true"></span> <span class="sr-only">Error:</span>
				Login failed. Invalid credentials. 
			</div>
			<form action="login.jsp" method="POST">
				<fieldset>
					<legend>Login</legend>
					<div class="row">
						<div class="form-group col-lg-3">
							<div class="input-group">
								<span class="input-group-addon"><i
									class="fa fa-user fa-fw"></i></span> <input class="form-control"
									type="text" name="login" placeholder="Enter username">
							</div>
						</div>
					</div>
					<div class="row">
						<div class="form-group col-lg-3">
							<div class="input-group">
								<span class="input-group-addon"><i
									class="fa fa-key fa-fw"></i></span> <input class="form-control"
									type="password" name="password" placeholder="Enter password">
							</div>
						</div>
					</div>
					<button type="submit" class="btn btn-primary">Login</button>
				</fieldset>
			</form>
		</div>
	</div>
	<%@ include file="imports/footer.jsp"%>
</body>
</html>