'use strict';
function Router(startingPath) {
	const routes = {};
	let protectedRoutes = [];
	const hasParams = (routeName) => {
		let tester = new RegExp(/:[^\s/]+/g);
		return tester.test(routeName);
	};

	let isSafe;

	this.registerRoute = (routeName, fn) => {
		routes[routeName] = fn;
	};

	this.setSafeFunction = (fn) => {
		if (typeof fn !== 'function') {
			console.error('Parameter is not a function');
			return;
		}
		isSafe = fn;
	};

	this.setProtectedRoutes = (arr) => {
		protectedRoutes = arr.slice(); // copy array
	};

	this.navigateTo = (routeName, param = null, fallback) => {
		if (hasParams(routeName)) {
			if (!param) {
				console.error('Parameter not found');
				return;
			}
			let name = routeName.replace(/:[^\s/]+/g, param);
			window.history.pushState({}, routeName, window.location.origin + startingPath + '#' + name);
			routes[routeName](param);
		} else {
			window.history.pushState({}, routeName, window.location.origin + startingPath + '#' + routeName);
			routes[routeName]();
		}
	};
	this.getRoutes = () => {
		return Object.assign({}, routes);
	};

	const onCreate = () => {
		const goToRightPage = () => {
			if (window.location.hash.slice(1).includes('profile')) {
				let username = window.location.hash.slice(1).split('/')[2];
				let route = '/profile/:username';
				routes[route](username);
			} else {
				let route = window.location.hash.slice(1) || '/';
				this.navigateTo(route);
				// if (protectedRoutes.includes(route)) {
				// 	if (isSafe()) {
				// 		// routes[route]();
				// 		this.navigateTo(route);
				// 	} else {
				// 		// routes['/signIn']();
				// 		console.log("niceeeee");
				// 		this.navigateTo('/signIn');
				// 	}
				// } else {
				// 	this.navigateTo(route);
				// 	// if (!isSafe() || ( route != '/signIn' && route != '/signUp')) {
				// 	// 	this.navigateTo(route);
				// 	// } else {
				// 	// 	this.navigateTo('/dashboard');
				// 	// }
				// }
			}
		};
		window.onpopstate = () => {
			goToRightPage();
		};

		window.addEventListener('load', () => {
			goToRightPage();
		});
	};
	onCreate();
}
