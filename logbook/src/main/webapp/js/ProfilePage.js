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
				<div class="image-placeholder big">
						${user.userName[0]}${user.userName[1]}
				</div>
				<div class="profile-username">${user.userName}</div>
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
		console.log('fetching posts');
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
				mySPA.setState({ posts: result });
				setDeleteListeners(result);
			}
		);
	};

	if (user) {
		// user exists on state
		rootDiv.innerHTML = ProfilePage(user);
		document.getElementById("profile-timeline-posts").innerHTML = PostPlaceholder().repeat(3);
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

					document.getElementById("profile-timeline-posts").innerHTML = PostPlaceholder().repeat(3);
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
