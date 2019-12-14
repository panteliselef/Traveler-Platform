'use strict';
const DashboardPage = (currentUser) => {
	return `
  <div id="dashboard-container" class="dashboard-container">
  <div class="header">
    <div id="header-username">${currentUser.userName}</div>
    <ul class="header-options">
      <li id="btnLogout">Logout</li>
    </ul>
  </div>
  <div class="dashboard">
    <section>
      <div style="display:flex;justify-content:space-between;align-items:center">
        <h3>Dashboard</h3>
        <div id="refresh-btn">
          <img src="./assets/refresh.svg" width="28px"/>
        </div>
      </div>
      <div id="dashboard-members" class="dashboard-members"></div>
    </section>
    <section id="about-me">
			<h3>About Me</h3>
			<div style="position: sticky;top: 20px;">
				<div onclick="mySPA.goToEditPage();" class="cta">Edit Profile</div>
				<div id="user-info" class="user-info">
					${Object.keys(currentUser)
						.map((key) => {
							return `<p>${key} ${currentUser[key]}</p>`;
						})
						.join('')}
				</div>
			</div>
    </section>
  </div>
</div>`;
};

const DashMember = (member) => {
	let hasAttacked = checkXSSAttack(member);
	return `
		<div class="dashboard-member">
			<div class="top-section" style="background-color: ${hasAttacked ? 'red' : '#17bebb'}">
				<div class="">${member.userName}</div>
			</div>

			<div class="middle-section">
				${hasAttacked
					? `<div id="f-line">User has been under XSS Attack, for safety reason his info is not visible</div>`
					: `<div id="f-line">
						<div>${member.firstName} ${member.lastName}</div>
						<div>${member.gender === 'MALE' ? `♂️` : (member.gender === 'FEMALE' ? `♀️`: `🤷🏼‍♂️`)}</div>
					</div>
					<div>${member.occupation}</div>
					<div>${member.town},${member.country}</div>`}
			</div>

			${!hasAttacked
				? `<div class="bottom-section">
				<a class="cta" href="mailto:${member.email}">Send Email</a>
				<a class="cta" onclick="mySPA.goToProfilePage('${member.userName}');">Profile</a>
			</div>`
				: ``}
			
		</div>`;
};

const fetchUsers = (currentUser, elementId, callback = null) => {
	let data = new FormData();
	console.log(currentUser.userName);
	console.log(currentUser.password);
	data.append('username', currentUser.userName);
	data.append('password', currentUser.password);
	ajaxRequest('POST', 'http://localhost:8080/logbook/getUsers', data, ({ result }) => {
		if (!result) return;
		let users = result
			.map((user) => {
				mySPA.setState({ [user.userName]: user });
				return DashMember(user);
			})
			.join('');
		document.getElementById(elementId).innerHTML = users;
		if (callback != null && typeof callback === 'function') callback();
	});
};

const showUsersData = (currentUser, elementId) => {
	if (!currentUser) throw new Error('No user specified');

	if (Object.keys(mySPA.getState()).length > 1) {
		let users = Object.keys(mySPA.getState())
			.map((username) => {
				if (username === 'user') return;
				let user = mySPA.getState(username);
				return DashMember(user);
			})
			.join('');
		document.getElementById(elementId).innerHTML = users;
	} else {
		document.getElementById('refresh-btn').classList.add('spinning');
		fetchUsers(currentUser, elementId, () => {
			document.getElementById('refresh-btn').classList.remove('spinning');
		});
	}
};

const showDashboard = () => {
	let user = mySPA.getState('user');
	console.log('User', user);
	document.getElementsByTagName('body')[0].style.background = '#fff';

	const rootDiv = document.getElementById('root');
	rootDiv.innerHTML = DashboardPage(user);
	showUsersData(user, 'dashboard-members');

	document.getElementById('btnLogout').addEventListener('click', (e) => {
		mySPA.showLoadingPage();
		ajaxRequest('GET', 'http://localhost:8080/logbook/logout', null, ({ statusCode, message }) => {
			if (statusCode === 200) {
				mySPA.setState({ user: null });
				mySPA.goToSignIn();
			} else {
				console.log('back to dashboard');
				mySPA.goToDashBoard();
			}
		});
	});

	document.getElementById('refresh-btn').addEventListener('click', () => {
		document.getElementById('dashboard-members').innerHTML = `
			<div class="loading-spinner visible" style="justify-self: flex-end;"></div>
		`;
		document.getElementById('refresh-btn').classList.add('spinning');
		fetchUsers(user, 'dashboard-members', () => {
			document.getElementById('refresh-btn').classList.remove('spinning');
		});
	});

	console.log('Cookies', document.cookie);
};
