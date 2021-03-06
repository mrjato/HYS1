<!DOCTYPE html>
<html ng-app="hys1">
<head>
<%@ include file="imports/head.jsp"%>
<title>Home - Have You Seen?</title>
</head>
<body ng-controller="eventController">
	<%@ include file="imports/menu.jsp"%>
	<div class="intro-header intro-margin" ng-show="!loading">
		<div class="container">
			<div class="row">
				<div class="col-lg-12">
					<div class="intro-message" ng-repeat="e in events.slice(0, 1) | filter: {status: 'PROGRAMMED'}">
						<h2>{{e.title}}</h2>
						<h3>{{e.description}}</h3>
						<hr class="intro-divider">
						<ul class="list-inline intro-social-buttons">
							<li><a href="#" class="btn btn-default btn-lg"><i
									class="fa fa-facebook-square fa-fw"></i> <span
									class="network-name">Facebook</span></a></li>
							<li><a href="#" class="btn btn-default btn-lg"><i
									class="fa fa-twitter-square fa-fw"></i> <span
									class="network-name">Twitter</span></a></li>
						</ul>
					</div>
				</div>
			</div>
		</div>
	</div>
	<div class="content-section-a">
		<div class="container">
			<div ng-show="loading">
				<div id="loading">
					<img class="img-responsive" src="img/loading.gif" alt="">
				</div>
			</div>
			<div ng-show="!loading">
				<div id="products" class="row list-group">
					<div class="item  col-xs-4 col-lg-4" ng-repeat="e in events.slice(1, 7) | filter: {status: 'PROGRAMMED'}" on-finish-render="ngRepeatFinished">
						<div class="thumbnail">
							<img class="group list-group-image"
								src="http://lorempixel.com/400/250/" alt="" />
							<div class="caption">
								<h4 class="group inner list-group-item-heading">{{e.title}}</h4>
								<p class="group inner list-group-item-text">{{e.description}}</p>
								<div class="row">
									<div class="col-xs-12 col-md-6">
										<p class="lead"><small>{{e.date | date: "MMM d, yyyy 'at' HH:mm"}}</small></p>
									</div>
									<div class="col-xs-12 col-md-6">
										<a class="btn btn-primary" href="#">See details</a>
									</div>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
	<a name="about"></a>
	<div class="content-section-b">
		<div class="container">
			<div class="row">
				<div class="col-lg-5 col-sm-6">
					<hr class="section-heading-spacer">
					<div class="clearfix"></div>
					<h2 class="section-heading">Deciding what to read next?</h2>
					<p class="lead">You're in the right place. Tell us what titles
						or genres you've enjoyed in the past, and we'll give you
						surprisingly insightful recommendations.</p>
				</div>
				<div class="col-lg-5 col-lg-offset-2 col-sm-6">
					<img class="img-responsive" src="img/reading.jpg" alt="">
				</div>
			</div>
		</div>
	</div>
	<div class="content-section-a">
		<div class="container">
			<div class="row">
				<div class="col-lg-5 col-lg-offset-1 col-sm-push-6 col-sm-6">
					<hr class="section-heading-spacer">
					<div class="clearfix"></div>
					<h2 class="section-heading">Looking to sign up?</h2>
					<p class="lead">Connect with your friends and other fascinating
						people to take part of this amazing community.</p>
				</div>
				<div class="col-lg-5 col-sm-pull-6  col-sm-6">
					<img class="img-responsive" src="img/signup.png" alt="">
				</div>
			</div>
		</div>
	</div>
	<div class="content-section-b">
		<div class="container">
			<div class="row">
				<div class="col-lg-5 col-sm-6">
					<hr class="section-heading-spacer">
					<div class="clearfix"></div>
					<h2 class="section-heading">Share with other people and enjoy
						your free time</h2>
					<p class="lead">Find a common meeting time with other people
						without the tedious back-and-forth of scheduling over email.</p>
				</div>
				<div class="col-lg-5 col-lg-offset-2 col-sm-6">
					<img class="img-responsive" src="img/meeting.png" alt="">
				</div>
			</div>
		</div>
	</div>
	<%@ include file="imports/footer.jsp"%>
</body>
</html>