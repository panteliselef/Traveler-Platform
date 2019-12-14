'use strict';
function App() {
	let state = {};

	const router = new Router(window.location.pathname);


	router.registerRoute('/editPage', () => {
		this.showLoadingPage();
		ajaxRequest('GET', 'http://localhost:8080/logbook/signin', null, ({ statusCode, message, result }) => {
			if (statusCode === 200) {
				this.setState({ user: result });
				showEditPage();
			} else {
				this.goToSignIn();
			}
			console.log(message);
		});
	});
	router.registerRoute('/signIn', () => {
		this.showLoadingPage();
		ajaxRequest('GET', 'http://localhost:8080/logbook/signin', null, ({ statusCode, message, result }) => {
			if (statusCode === 200) {
				this.setState({ user: result });
				this.goToTimelinePage();
			} else {
				showSignIn();
			}
			console.log(message);
		});
	});
	router.registerRoute('/signUp', () => {
		this.showLoadingPage();
		ajaxRequest('GET', 'http://localhost:8080/logbook/signin', null, ({ statusCode, message, result }) => {
			if (statusCode === 200) {
				this.setState({ user: result });
				this.goToTimelinePage();
			} else {
				showSignUp();
			}
			console.log(message);
		});
	});

	router.registerRoute('/', () => {
		this.showLoadingPage();
		ajaxRequest('GET', 'http://localhost:8080/logbook/signin', null, ({ statusCode, message, result }) => {
			if (statusCode === 200) {
				this.setState({ user: result });
				showTimelinePage();
			} else {
				this.goToSignIn();
			}
			console.log(message);
		});
	});
	router.registerRoute('/dashboard', () => {
		this.showLoadingPage();
		ajaxRequest('GET', 'http://localhost:8080/logbook/signin', null, ({ statusCode, message, result }) => {
			if (statusCode === 200) {
				this.setState({ user: result });
				showDashboard();
			} else {
				this.goToSignIn();
			}
			console.log(message);
		});
	});
	router.registerRoute('/profile/:username', (username) => {
		this.showLoadingPage();
		ajaxRequest('GET', 'http://localhost:8080/logbook/signin', null, ({ statusCode, message, result }) => {
			if (statusCode === 200) {
				this.setState({ user: result });
				showProfilePage(username);
			} else {
				this.goToSignIn();
			}
			console.log(message);
		});
	});
	router.setProtectedRoutes([ '/editPage', '/dashboard', '/profile/:username', '/' ]);

	router.setSafeFunction(() => {
		// console.log(window.localStorage.getItem('user'))
		if (mySPA.getState('user')) return true;
		else return false;
	});


	this.dev = () => {
		document.getElementById('to_dashboard').addEventListener('click', this.goToDashBoard);
		document.getElementById('to_signup').addEventListener('click', this.goToSignUp);
	};
	this.showLoadingPage = () => {
		const rootDiv = document.getElementById('root');

		let html = `
			<div class="loading-page">
				<div class="loading-spinner visible"></div>
			</div>
		`;
		rootDiv.innerHTML = html;
	};
	this.goToProfilePage = (username) => {
		router.navigateTo('/profile/:username', username);
	};
	this.goToDashBoard = function() {
		router.navigateTo('/dashboard');
	};
	this.goToSignUp = function() {
		router.navigateTo('/signUp');
	};
	this.goToSignIn = function() {
		router.navigateTo('/signIn');
	};
	this.goToEditPage = () => {
		router.navigateTo('/editPage');
	};
	this.goToTimelinePage = () => {
		router.navigateTo('/');
	}
	this.resetState = () => {
		state = {};
	};
	this.setState = function(obj) {
		state = {
			...state,
			...obj
		};
	};
	this.getState = function(username = null){
		if (username === null) return state;
		return state[username];
	};

	this.init = () => {
		console.log('Start');
		let user = window.localStorage.getItem('user');
		if (!user) {
			return;
		}
		user = JSON.parse(user);
		this.setState({ user });
	};
}
