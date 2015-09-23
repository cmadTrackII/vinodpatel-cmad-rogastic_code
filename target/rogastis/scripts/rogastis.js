(function() {

	var app = angular.module("rogastis", [ 'ngRoute', 'ui.bootstrap',
			'ngCookies', 'anguFixedHeaderTable' ]);

	angular.module('Session', [ 'ngCookies' ]).controller('InitController',
			[ '$cookies', function($cookies) {
				if (!$cookies.get('loggedinuser')) {
					$cookies.put('loggedinuser', 'null');
					$scope.showFailureAlertCtrl = false;
					$scope.showSuccessAlertCtrl = false;
					$scope.showRegisterButton = true;
					$scope.showLoginButton = true;
					$scope.showLogoutButton = false;
					$scope.showPostButton = false;
					$scope.showAlerts = false;
					$scope.WelcomePanel = false;
				} else {
					$scope.showLogoutButton = true;
					$scope.showPostButton = true;
					$scope.showLoginButton = false;
					$scope.showRegisterButton = false;
					$scope.WelcomePanel = true;
				}
				$scope.showRegAlert = false;
			} ]);

	app.config([ '$routeProvider', function($routeProvider) {
		$routeProvider.when('/questions', {
			templateUrl : 'questions.html',
			controller : 'DefaultQuestionController'
		}).when('/questiondetail/:qid', {
			templateUrl : 'questiondetails.html',
			controller : 'QuestionDetailsController'
		}).when('/', {
			templateUrl : 'header.html',
			controller : 'HeaderLoginController'
		}).when('/search/:searchstring', {
			templateUrl : 'search.html',
			controller : 'SearchController'
		}).otherwise({
			redirectTo : '/'
		});
	} ]);

	app.controller("UserWelcomeController", function($scope, $cookies) {
		console.log('Current User ID: ' + $cookies.get('loggedinuser'));
		console.log('Current User Name: ' + $cookies.get('loggedinusername'));
		if ($cookies.get('loggedinusername') == 'null') {
			$scope.WelcomePanel = false;

		} else {
			$scope.currentUser = {};
			$scope.currentUser = $cookies.get('loggedinusername');
			$scope.WelcomePanel = true;
		}
	});

	app
			.controller(
					"DefaultQuestionController",
					function($http, $log, $scope) {
						$scope.questions = [];
						$scope.loadingquestions = true;

						console.log("Getting default set of questions...");
						var promise = $http
								.get('http://localhost:8080/rogastis/service/question/count/0');
						promise
								.success(function(data, status, headers, config) {
									$scope.loadingquestions = false;
									$scope.questions = data;
								});
						promise.error(function(data, status, headers, config) {
							$scope.loadingquestions = false;
							console.log('Error fetching questions: ' + status);
							$scope.error = status;
						});

					});

	app
			.controller(
					"SearchController",
					function($http, $log, $scope) {
						$scope.questions = [];
						$scope.loadingquestions = true;
						console.log("Searching for questions...");

						$scope.searchquestions = function(search) {
							var promise = $http
									.get('http://localhost:8080/rogastis/service/question/search/'
											+ $scope.search.string);
							promise.success(function(data, status, headers,
									config) {
								$scope.loadingquestions = false;
								$scope.questions = data;
							});
							promise.error(function(data, status, headers,
									config) {
								$scope.loadingquestions = false;
								console.log('Error fetching questions: '
										+ status);
								$scope.error = status;
							});
						}

					});

	app.controller("QuestionDetailsController", function($http, $log, $scope,
			$routeParams, $cookies, $modal, $route) {
		$scope.question = [];

		console.log("Getting question headline...");
		var promise = $http
				.get('http://localhost:8080/rogastis/service/question/single/'
						+ $routeParams.qid);
		promise.success(function(data, status, headers, config) {
			$scope.question.push(data);
			$scope.question.question = data.question;
			$scope.question.questionId = data.questionId;
		});
		promise.error(function(data, status, headers, config) {
			console.log('Inside failure block');
			$scope.loading = false;
			console.log('Error fetching question headline details: ' + status);
			$scope.error = status;
		});

		$scope.posts = '';
		$scope.users = '';
		$scope.postslist = [];

		$scope.loadingposts = true;

		console.log("Getting posts for question " + $routeParams.qid + "...");
		var promisePosts = $http
				.get('http://localhost:8080/rogastis/service/post/'
						+ $routeParams.qid);
		promisePosts.success(function(data, status, headers, config) {
			console.log('Complete JSON Data: ' + angular.toJson(data));
			$scope.posts = data;
			console.log('Length of posts: ' + ($scope.posts).length);

			for (var i = 0; i < ($scope.posts).length; i++) {

				var currentpost = $scope.posts[i];

				var postentry = {
					postId : '',
					postContent : '',
					username : '',
					postdate : '',
					likes : '',
					dislikes : ''
				};

				console.log('Post JSON Data: ' + angular.toJson(currentpost));

				console.log('Fetching details of post: ' + currentpost.postId);

				postentry.postId = currentpost.postId;
				postentry.postContent = currentpost.post;
				postentry.postdate = currentpost.postedOn;
				postentry.likes = currentpost.likes;
				postentry.dislikes = currentpost.dislikes;
				postentry.username = currentpost.userName;

				console.log("Post entry to be pushed: "
						+ angular.toJson(postentry));
				$scope.postslist.push(postentry);

			}

			$scope.loadingposts = false;
			console
					.log('Final posts lits: '
							+ angular.toJson($scope.postslist));
		});
		promisePosts.error(function(data, status, headers, config) {
			console.log('Inside failure block');
			$scope.loading = false;
			console.log('Error fetching posts: ' + status);
			$scope.error = status;
		});

		$scope.animationsEnabled = true;

		if ($cookies.get('loggedinuser') == 'null')
			$scope.showCommentButton = false;
		else
			$scope.showCommentButton = true;

		$scope.post = {
			comment : '',
			qstnId : ''
		};

		$scope.open = function(size) {

			var modalInstance = $modal
					.open({
						animation : $scope.animationsEnabled,
						templateUrl : 'PostCommentModal.html',
						size : size,
						controller : function($scope, $modalInstance, $log,
								$http, post, $routeParams) {

							$scope.post = post;

							$scope.ok = function() {

								$scope.newpost = {
									post : '',
									userId : ''
								};

								$scope.newpost.post = $scope.post.comment;
								$scope.newpost.userId = $cookies
										.get('loggedinuser');

								console.log('Comment: ' + $scope.newpost.post);
								console.log('Posted by: '
										+ $scope.newpost.userId);
								console.log('Posting questionId: '
										+ $routeParams.qid);
								console.log("Adding comment...");

								var promise = $http.post(
										'http://localhost:8080/rogastis/service/post/'
												+ $routeParams.qid, angular
												.toJson($scope.newpost));
								promise.success(function(data, status, headers,
										config) {
									console.log('New comment posted with ID: '
											+ data.postId);
									$modalInstance.close();
									$route.reload();

								});
								promise.error(function(data, status, headers,
										config) {
									console.log('Error posting new comment: '
											+ status);
									$scope.error = status;
									alert('Error posting new comment: '
											+ status);
								});
							};

							$scope.cancel = function() {
								$modalInstance.dismiss('cancel');
							};
						},
						resolve : {
							post : function() {
								return $scope.post;
							}
						}
					});

			modalInstance.result.then(function() {

			}, function() {
				console.log('Modal dismissed at: ' + new Date());
			});
		};

		$scope.toggleAnimation = function() {
			$scope.animationsEnabled = !$scope.animationsEnabled;
		};

		$scope.updateLike = function(postentry) {
			var promise = $http.post(
					'http://localhost:8080/rogastis/service/post/'
							+ $scope.postentry.postId + 'search/likes', '');
			promise.success(function(data, status, headers, config) {
				console.log('Successfully updated like for post: ' + $scope.postentry.postId);
			});
			promise.error(function(data, status, headers, config) {
				console.log('Error updating like: ' + status);
				$scope.error = status;
			});
		}
		
		$scope.updateDislike = function(postentry) {
			var promise = $http.post(
					'http://localhost:8080/rogastis/service/post/'
							+ $scope.postentry.postId + 'search/dislikes', '');
			promise.success(function(data, status, headers, config) {
				console.log('Successfully updated dislikes for post: ' + $scope.postentry.postId);
			});
			promise.error(function(data, status, headers, config) {
				console.log('Error updating dislike: ' + status);
				$scope.error = status;
			});
		}


	});

	angular
			.module('ui.bootstrap')
			.controller(
					'HeaderRegisterController',
					function($scope, $modal, $log, $cookies) {

						$scope.animationsEnabled = true;

						console.log("User: " + $cookies.get('loggedinuser'));
						if ($cookies.get('loggedinuser') == 'null')
							$scope.showRegisterButton = true;
						else
							$scope.showRegisterButton = false;

						$scope.register = {
							loginId : '',
							password : '',
							firstName : '',
							lastName : ''
						};

						$scope.open = function(size) {

							var modalInstance = $modal
									.open({
										animation : $scope.animationsEnabled,
										templateUrl : 'RegisterModalContent.html',
										size : size,
										controller : function($scope,
												$modalInstance, $log, $http,
												register) {

											$scope.register = register;

											$scope.ok = function() {

												console
														.log('Username: '
																+ $scope.register.username);
												console
														.log('Password: '
																+ $scope.register.password);
												console
														.log('Firstname: '
																+ $scope.register.firstname);
												console
														.log('Lastname: '
																+ $scope.register.lastname);

												console
														.log("Adding user..."
																+ $scope.register.username);
												var promise = $http
														.post(
																'http://localhost:8080/rogastis/service/user',
																register);
												promise
														.success(function(data,
																status,
																headers, config) {
															$scope.userId = data.userId
															console
																	.log('New user created with ID: '
																			+ $scope.userId);
															alert('New User Registration Successful !! Please login to proceed further.');
															$modalInstance
																	.close();
														});
												promise
														.error(function(data,
																status,
																headers, config) {
															console
																	.log('Error adding user: '
																			+ status);
															$scope.error = status;
															if (status == 502) {
																alert('User already exists !!');
															}
														});
											};

											$scope.cancel = function() {
												$modalInstance
														.dismiss('cancel');
											};
										},
										resolve : {
											register : function() {
												return $scope.register;
											}
										}
									});

							modalInstance.result.then(function() {

							}, function() {
								console
										.log('Modal dismissed at: '
												+ new Date());
							});
						};

						$scope.toggleAnimation = function() {
							$scope.animationsEnabled = !$scope.animationsEnabled;
						};

					});

	angular
			.module('ui.bootstrap')
			.controller(
					'HeaderPostController',
					function($scope, $modal, $log, $cookies, $route) {

						$scope.animationsEnabled = true;

						if ($cookies.get('loggedinuser') == 'null')
							$scope.showPostButton = false;
						else
							$scope.showPostButton = true;

						$scope.post = {
							headline : '',
							description : ''
						};

						$scope.open = function(size) {

							var modalInstance = $modal
									.open({
										animation : $scope.animationsEnabled,
										templateUrl : 'PostModalContent.html',
										size : size,
										controller : function($scope,
												$modalInstance, $log, $http,
												post, postheadline, firstpost) {

											$scope.post = post;

											$scope.ok = function() {

												console.log('Headline: '
														+ $scope.post.headline);
												console
														.log('Description: '
																+ $scope.post.description);
												console
														.log("Posting question...");

												$scope.postheadline = {
													question : ''
												};

												$scope.postheadline.question = $scope.post.headline;

												console.log('Headline data: '
														+ $scope.postheadline);
												console
														.log('Headline JSON payload: '
																+ angular
																		.toJson($scope.postheadline));

												var promise = $http
														.post(
																'http://localhost:8080/rogastis/service/question/'
																		+ $cookies
																				.get('loggedinuser'),
																angular
																		.toJson($scope.postheadline));
												promise
														.success(function(data,
																status,
																headers, config) {
															$scope.questionId = data.questionId
															console
																	.log('New question posted with ID: '
																			+ $scope.questionId);

															$scope.firstpost = {
																post : '',
																userId : ''
															};

															$scope.firstpost.post = $scope.post.description;
															$scope.firstpost.userId = $cookies
																	.get('loggedinuser');

															console
																	.log('First Post: '
																			+ $scope.firstpost);
															console
																	.log('First Post JSON payload: '
																			+ angular
																					.toJson($scope.firstpost));

															var newpost = $http
																	.post(
																			'http://localhost:8080/rogastis/service/post/'
																					+ $scope.questionId,
																			angular
																					.toJson($scope.firstpost));
															newpost
																	.success(function(
																			data,
																			status,
																			headers,
																			config) {
																		$scope.postId = data.postId
																		console
																				.log('First post added with ID: '
																						+ $scope.postId);
																		$modalInstance
																				.close();
																		$route
																				.reload();
																	});
															newpost
																	.error(function(
																			data,
																			status,
																			headers,
																			config) {
																		console
																				.log('Error adding first post: '
																						+ status);
																		$scope.error = status;
																	});
														});
												promise
														.error(function(data,
																status,
																headers, config) {
															console
																	.log('Error posting new question: '
																			+ status);
															$scope.error = status;
														});
											};

											$scope.cancel = function() {
												$modalInstance
														.dismiss('cancel');
											};
										},
										resolve : {
											post : function() {
												return $scope.post;
											},
											postheadline : function() {
												return $scope.postheadline;
											},
											firstpost : function() {
												return $scope.firstpost;
											}
										}
									});

							modalInstance.result.then(function() {

							}, function() {
								console
										.log('Modal dismissed at: '
												+ new Date());
							});
						};

						$scope.toggleAnimation = function() {
							$scope.animationsEnabled = !$scope.animationsEnabled;
						};

					});

	angular
			.module('ui.bootstrap')
			.controller(
					'HeaderLoginController',
					function($scope, $modal, $log, $cookies, $route) {

						$scope.animationsEnabled = true;

						if ($cookies.get('loggedinuser') == 'null')
							$scope.showLoginButton = true;
						else
							$scope.showLoginButton = false;

						$scope.login = {
							username : '',
							password : ''
						};

						$scope.open = function(size) {

							var modalInstance = $modal
									.open({
										animation : $scope.animationsEnabled,
										templateUrl : 'LoginModalContent.html',
										size : size,
										controller : function($scope,
												$modalInstance, $log, login,
												$http, $cookies) {

											$scope.login = login;

											$scope.ok = function() {

												console
														.log('Username: '
																+ $scope.login.username);
												console
														.log('Password: '
																+ $scope.login.password);

												console
														.log("Validating login details for username..."
																+ $scope.login.username);
												var promise = $http
														.get('http://localhost:8080/rogastis/service/user/loginId/'
																+ $scope.login.username);
												promise
														.success(function(data,
																status,
																headers, config) {
															console
																	.log('username: '
																			+ data.loginId);
															console
																	.log('password: '
																			+ data.password);
															console
																	.log('userId: '
																			+ data.userId);
															if ($scope.login.password == data.password) {
																$cookies
																		.put(
																				'loggedinuser',
																				data.userId);
																$cookies
																		.put(
																				'loggedinusername',
																				data.firstName
																						+ ' '
																						+ data.lastName);
																$scope.showRegisterButton = false;
																$scope.showLogoutButton = true;
																$scope.showLoginButton = false;
																console
																		.log('User Login Successful !!');
																console
																		.log('Session Cookie: '
																				+ $cookies
																						.get('loggedinuser'));
																$modalInstance
																		.close();
																$route.reload();
															} else {
																console
																		.log('Invalid username/password');
																alert('Invalid username/password !!');
															}
														});
												promise
														.error(function(data,
																status,
																headers, config) {
															console
																	.log('Response code: '
																			+ status);
															if (status == 503) {
																alert('User does not exist !!');
															}
														});
											};

											$scope.cancel = function() {
												$modalInstance
														.dismiss('cancel');
											};

										},
										size : size,
										resolve : {
											login : function() {
												return $scope.login;
											}
										}
									});
						};

						$scope.toggleAnimation = function() {
							$scope.animationsEnabled = !$scope.animationsEnabled;
						};

					});

	angular.module('ui.bootstrap').controller('HeaderLogoutController',
			function($scope, $modal, $log, $cookies, $route) {

				$scope.animationsEnabled = true;

				if ($cookies.get('loggedinuser') == 'null')
					$scope.showLogoutButton = false;
				else
					$scope.showLogoutButton = true;

				$scope.open = function(size) {

					var modalInstance = $modal.open({
						animation : $scope.animationsEnabled,
						templateUrl : 'LogoutModalContent.html',
						size : size,
						controller : function($scope, $modalInstance, $log) {
							$scope.ok = function() {
								$cookies.put('loggedinuser', 'null');
								$cookies.put('loggedinusername', 'null');
								$route.reload();
								$modalInstance.close();
							};
							$scope.cancel = function() {
								$modalInstance.dismiss('cancel');
							};
						},
						resolve : {
							logout : function() {

							}
						}
					});

					modalInstance.result.then(function() {

					}, function() {
						console.log('Modal dismissed at: ' + new Date());
					});
				};

				$scope.toggleAnimation = function() {
					$scope.animationsEnabled = !$scope.animationsEnabled;
				};

			});

	angular
			.module('ui.bootstrap')
			.controller(
					'Alertcontroller',
					function($scope) {

						$scope.alerts = [ {
							type : 'success',
							msg : 'New User Registration Successful !! Login to proceed further.'
						} ];
						$scope.showRegAlert = true;

						$scope.$on('publishAlert', function(event, data) {
							$scope.showRegAlert = true;
							console.log(data);
							$scope.alerts.push(data);
						});

						$scope.closeAlert = function(index) {
							$scope.alerts.splice(index, 1);
						};
					});
})();
