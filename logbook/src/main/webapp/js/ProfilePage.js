'use strict';
const ProfilePage = (user) => {
	return `
  <div id="dashboard-container" class="dashboard-container">
    <div class="header">
      <div style="display:flex;justify-items:flex-start;align-items:center;">
        <img id="back-btn" src="./assets/arrow-left.svg" />
        <div id="header-username">${user.userName}</div>
      </div>
      <ul class="header-options">
        <li onclick="mySPA.goToDashBoard()">Dashboard</li>
        <li id="btnLogout">Logout</li>
      </ul>
		</div>
		<div class="profile-container">
			<div class="profile-whois">
				<div style="display:flex;align-items:center">
					<div class="image-placeholder big">
							${user.userName[0]}${user.userName[1]}
					</div>
					<div class="profile-username">${user.userName}</div>
				</div>
				<div id="travel-distance">
				</div>
			</div>
			<div class="profile">				
				${Object.keys(user)
					.map((key) => {
						return `
							<div class="profile-info-field">
								<div class="field-name">${key}</div>
								<div class="field-value">${user[key]}</div>
							</div>
						`;
					})
					.join('')}


			</div>
			<main>
				<div id="profile-timeline-posts" class="timeline-posts"></div>
			</main>
		</div>
  </div>
  `;
};

const showProfilePage = (username) => {
	console.log('Profile Page', username);
	document.getElementsByTagName('body')[0].style.background = 'rgb(251, 251, 251)';
	const rootDiv = document.getElementById('root');
	let user = mySPA.getState(username);

	const registerEventListeners = () => {
		document.getElementById('back-btn').addEventListener('click', () => {
			window.history.back();
		});

		document.getElementById('btnLogout').addEventListener('click', (e) => {
			mySPA.showLoadingPage();
			ajaxRequest(
				'GET',
				'http://localhost:8080/logbook/master?redirect=logout',
				null,
				({ statusCode, message }) => {
					if (statusCode === 200) {
						mySPA.setState({ user: null });
						mySPA.goToSignIn();
					} else {
						console.log('back to dashboard');
						mySPA.goToDashBoard();
					}
				}
			);
		});
	};

	const fetchPosts = () => {
		const NOMATISM_URL = `https://nominatim.openstreetmap.org/search`;
		ajaxRequest(
			'GET',
			`/logbook/master?redirect=post&mode=top_ten&username=${username}`,
			null,
			({ message, statusCode, result }) => {
				console.log('MICE', result);
				document.getElementById('profile-timeline-posts').innerHTML = result
					.map((post) => {
						return Post(post);
					})
					.join('');

				let user = mySPA.getState('user');
				console.warn('Country', user.town, user.country);

				ajaxRequest('GET', `${NOMATISM_URL}?q=${user.town},${user.country}&format=json`, null, (res) => {
					if (res.length > 0) {
						// at least a match has been found
						let { lat, lon } = res[0];

						let posts = [];
						result.forEach((post) => {
							if (
								post.latitude &&
								post.latitude != 'null' &&
								post.longitude &&
								post.longitude != 'null'
							) {
								posts.push(post);
							}
						});

						let str = posts
							.map((post) => {
								return `${post.longitude},${post.latitude}`;
							})
							.join(';');

						ajaxRequest(
							'GET',
							`http://router.project-osrm.org/route/v1/driving/${lon},${lat};${str}?overview=false`,
							null,
							({ code, routes, waypoints }) => {
								if (code != 'Ok') return;

								let distance = '';
								if(routes[0].distance*2 > 1000){
									distance = `${(routes[0].distance*2/1000).toFixed(1)}km`
								}else{
									distance = `${(routes[0].distance*2).toFixed(1)}m`;
								}
								document.getElementById('travel-distance').innerText = distance
							}
						);

						// fetch(`http://router.project-osrm.org/route/v1/driving/${lon},${lat};${str}?overview=false`)
						// 	.then((res) => res.json())
						// 	.then(() => {});
						// result.forEach((post) => {
						// 	console.log('LOCATION', post.latitude, post.longitude);

						// 	// ajaxRequest(
						// 	// 	'GET',
						// 	// 	`http://router.project-osrm.org/route/v1/driving/${lon},${lat};${post.latitude},${post.latitude}?overview=false`,null,(res)=>{
						// 	// 		console.log(res);
						// 	// 	}
						// 	// );
						// });
					} else {
						console.warn('No home');
					}
				});
				mySPA.setState({ posts: result });
				setDeleteListeners(result);
			}
		);
	};

	if (user) {
		// user exists on state
		rootDiv.innerHTML = ProfilePage(user);
		document.getElementById('profile-timeline-posts').innerHTML = PostPlaceholder().repeat(3);
		registerEventListeners();
		fetchPosts();

		if (username === mySPA.getState('user').userName) {
			let mainTag = document.getElementsByClassName('profile-container')[0].children[2];
			showCreatPost(mainTag);
		}
	} else {
		ajaxRequest(
			'GET',
			`http://localhost:8080/logbook/master?redirect=user&username=${username}`,
			null,
			({ statusCode, message, result }) => {
				if (statusCode === 200) {
					rootDiv.innerHTML = ProfilePage(result);

					document.getElementById('profile-timeline-posts').innerHTML = PostPlaceholder().repeat(3);
					if (username === mySPA.getState('user').userName) {
						let mainTag = document.getElementsByClassName('profile-container')[0].children[2];
						showCreatPost(mainTag);
					}
					registerEventListeners();
					fetchPosts();
				}
			}
		);
	}
};
