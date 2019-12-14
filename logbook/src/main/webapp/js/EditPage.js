'use strict';

const EditPage = (user) => {
	return `
  <div id="dashboard-container" class="dashboard-container">
  <div class="header">
    <div id="back-btn">
      <img src="./assets/arrow-left.svg" />
    </div>
    <div id="header-username">${user.userName}</div>
    <ul class="header-options">
      <li id="btnLogout">Logout</li>
    </ul>
  </div>
  <div class="edit">
    <section class="edit-section">
      <div style="display:flex;justify-content:space-between;align-items:center;flex-wrap:wrap;">
        <h3>Edit</h3>
        <div id="delete-profile-btn" class="cta red" style="max-width: 200px;">Delete Profile</div>
      </div>
      <div class="edit-info-container">
        <div class="grid-layout-3-col">
          <div class="col">
      <div class="input-item">
        <label>
          Email *
        </label>
        <input required value="${user.email}" id="email" type="email" pattern="[a-z0-9._%+-]+@[a-z0-9.-]+\.[a-z]{2,}$">
      </div>
      <div class="input-item">
        <label>
          Password *
        </label>
        <input id="password" value="${user.password}" required type="password" minlength="8" maxlength="10"
          pattern="(?=.*[A-Za-z])(?=.*\d)[A-Za-z\d]">
      </div>
      <div class="input-item">
        <label>
          Confirm Password *
        </label>
        <input id="inputPasswordConfirm" value="${user.password}" required type="password" minlength="8" maxlength="10"
          pattern="(?=.*[A-Za-z])(?=.*\d)[A-Za-z\d]">
      </div>
          </div>

          <div class="col">
      <div class="input-item">
        <label>
          First Name *
        </label>
        <input type="text" value="${user.firstName}" id="firstname" required minlength="3" maxlength="15" pattern="([a-zA-Z]){3,15}$">
      </div>
      <div class="input-item">
        <label>
          Last Name *
        </label>
        <input type="text" value="${user.lastName}" id="lastname" required minlength="3" maxlength="15" pattern="([a-zA-Z]){3,15}$">
      </div>
    </div>
    <div class="col">

      <div class="input-item">
        <label>
          City *
        </label>
        <input id="town" value="${user.town}" required type="text" minlength="2" maxlength="20" pattern="([a-zA-Z]){2,20}$">
      </div>
      <div class="input-item">
        <label>
          Address
        </label>
        <input id="address" value="${user.address}" type="text" minlength="2">
      </div>
      <div class="input-item">
        <label>
          Occupation *
        </label>
        <input required value="${user.occupation}" type="text" id="occupation" minlength="3" maxlength="15" pattern="([a-zA-Z]){3,15}$">
      </div>
    </div>
        </div>
        <div class="grid-layout-2-col">
    <div class="col">
      <div class="input-item">
        <label>
          Hobbies
        </label>
        <textarea id="interests" maxlength="100">${user.interests}</textarea>
      </div>
    </div>
    <div class="col">
      <div class="input-item">
        <label>
          More Info
        </label>
        <textarea id="info"  maxlength="500">${user.info}</textarea>
      </div>
    </div>
  </div>
      </div>
      <button id="btnSubmit" class="enroll-button relative"> Update Profile</button>
    </section>
  </div>
</div>`;
};
const validateEditInpits = () => {
	if (!validatePasswords()) return false;
	let email = document.getElementById('email').value;
	let password = document.getElementById('password').value;
	let firstname = document.getElementById('firstname').value;
	let lastname = document.getElementById('lastname').value;
	let city = document.getElementById('town').value;
	let job = document.getElementById('occupation').value;
	let validator = new Validator();
	let isCorrect = true;

	cleanErrorMessages();

	if (!validator.testEmail(email)) {
		showErrorOnInput(document.getElementById('email'), 'Email address is invalid');
		isCorrect = false;
	}
	if (!validator.testPassword(password)) {
		showErrorOnInput(
			document.getElementById('password'),
			'8 - 10 characters, at least one letter, one number, one symbol'
		);
		isCorrect = false;
	}

	if (!validator.testName(firstname)) {
		showErrorOnInput(document.getElementById('firstname'), 'First Name should be 3 - 15 letters (only)');
		isCorrect = false;
	}
	if (!validator.testName(lastname)) {
		showErrorOnInput(document.getElementById('lastname'), 'Last Name should be 3 - 15 letters (only)');
		isCorrect = false;
	}

	if (!validator.testCity(city)) {
		showErrorOnInput(document.getElementById('town'), 'City should be 3 - 20 letters (only)');
		isCorrect = false;
	}

	if (!validator.testOccupation(job)) {
		showErrorOnInput(document.getElementById('occupation'), 'Occupation should be 3 - 15 letters (only)');
		isCorrect = false;
	}

	return isCorrect;
};

const showEditPage = () => {
	console.log('nice');
	document.getElementsByTagName('body')[0].style.background = '#fff';
	const rootDiv = document.getElementById('root');
	rootDiv.innerHTML = EditPage(mySPA.getState('user'));

	document.getElementById('back-btn').addEventListener('click', () => {
		window.history.back();
	});

	document.getElementById('btnLogout').addEventListener('click', (e) => {
		mySPA.showLoadingPage();
		ajaxRequest('GET', 'http://localhost:8080/logbook/master?redirect=logout', null, ({ statusCode, message }) => {
			if (statusCode === 200) {
				mySPA.setState({ user: null });
				mySPA.goToSignIn();
			} else {
				console.log('back to dashboard');
				mySPA.goToDashBoard();
			}
		});
	});

	document.getElementById('delete-profile-btn').addEventListener('click', (e) => {
		if (window.confirm('This action is permanent\nAre you sure you want to delete your account?')) {
			let data = new FormData();
			data.set('username', mySPA.getState('user').userName);
			data.set('password', mySPA.getState('user').password);
			ajaxRequest(
				'DELETE',
				'http://localhost:8080/logbook/master?redirect=user',
				data,
				({ statusCode, message, result }) => {
					if (statusCode === 200) {
						window.localStorage.removeItem('user');
						mySPA.resetState();
						mySPA.goToSignUp();
					} else {
						alert('Something went try again later!');
					}
				}
			);
		} else {
			console.log('no');
		}
	});

	document.getElementById('btnSubmit').addEventListener('click', () => {
		let formData = new FormData();
		let email = document.getElementById('email').value;
		let password = document.getElementById('password').value;
		let inputPasswordConfirm = document.getElementById('inputPasswordConfirm').value;
		let fName = document.getElementById('firstname').value;
		let lName = document.getElementById('lastname').value;
		let city = document.getElementById('town').value;
		let address = document.getElementById('address').value;
		let job = document.getElementById('occupation').value;
		let interests = document.getElementById('interests').value;
		let info = document.getElementById('info').value;

		cleanErrorMessages();
		if (!validateEditInpits()) return;

		let obj = getLowerCaseKeyObject(mySPA.getState('user'));

		Object.keys(obj).forEach((key) => {
			formData.set(key, obj[key]);
		});

		formData.set('email', email);
		formData.set('password', password);
		formData.set('firstname', fName);
		formData.set('lastname', lName);
		formData.set('city', city);
		formData.set('address', address);
		formData.set('job', job);
		formData.set('interests', interests);
		formData.set('info', info);

		for (var pair of formData.entries()) {
			console.log(pair[0] + ', ' + pair[1]);
		}

		ajaxRequest(
			'POST',
			'http://localhost:8080/logbook/master?redirect=updateUser',
			formData,
			({ statusCode, message, result }) => {
				console.log(result);
				if (statusCode === 200) {
					let user = mySPA.getState('user');
					result['userID'] = user['userID'];
					mySPA.setState({ user: result });
					// window.localStorage.setItem('user', JSON.stringify(result));
					alert('You updated your info successfully');
				} else if (statusCode == 400) {
					if (message.includes('username')) {
						showErrorOnInput(document.getElementById('username'), message);
					} else if (message.includes('email')) {
						showErrorOnInput(document.getElementById('email'), message);
					} else if (message.toLowerCase().includes('missing')) {
						cleanErrorMessages();
						let obj = getLowerCaseKeyObject(result);
						keys = Object.keys(obj);
						console.log(keys);
						let fieldIds = [
							'email',
							'password',
							'firstname',
							'lastname',
							'town',
							'occupation'
						].map((id) => id.toLowerCase());
						keys = keys.filter((key) => fieldIds.includes(key));

						console.log(keys);

						keys.forEach((key) => {
							if (!obj[key]) {
								showErrorOnInput(document.getElementById(key));
							}
						});
					}
				}
			}
		);
	});
};
