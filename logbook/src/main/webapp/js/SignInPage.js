'use strict';
const SignInPage = () => {
	return `
    <div id="form-container">

    <img class="shape-aqua" alt="aqua-shape-cover" src="./assets/shape-aqua.svg" />
    <section id="sign-in-container" class="main-section light">
      <div style="position: absolute;top:0;right:0;padding: 1rem;"onclick="mySPA.goToSignUp();">Sign Up</div>
        <h2>Sign in 😁</h2>
				<div class="grid-layout-3-col">
					<div class="col"></div>
          <div class="col">
            <div class="input-item">
              <label>
                Username
              </label>
              <input type="text" id="signin_username" required minlength="8" pattern="[A-Za-z]{8,}" title="8 or more latin characters">
            </div>
            <div class="input-item">
              <label>
                Password
              </label>
              <input id="signin_password" required type="password" pattern="(?=.*[A-Za-z])(?=.*\d)[A-Za-z\d]{8,10}">
            </div>
          </div>
				</div>
				<div class="grid-layout-2-col"></div>
      <button id="btnSubmit" class="enroll-button">Sign In</button>
    </section>
    <img class="shape-somon" alt="somon-shape-cover" src="./assets/shape-somon.svg" />
    </div>
    `;
};

const showSignIn = () => {
	document.getElementsByTagName('body')[0].style.background = '#2e282a';
	const rootDiv = document.getElementById('root');
	rootDiv.innerHTML = SignInPage();

	// document.getElementById('signin_username').value = 'elefcodes';
	// document.getElementById('signin_password').value = 'elef123#';

	document.getElementById('signin_username').value = 'whynotdoit';
	document.getElementById('signin_password').value = 'aaZZa44@';

	document.getElementById('btnSubmit').addEventListener('click', (e) => {

		let text = e.target.textContent;
		e.target.innerHTML = `
			${text}<div class="loading-spinner visible"></div>
		`;
		e.preventDefault();
		cleanErrorMessages();
		let username = document.getElementById('signin_username').value;
		let password = document.getElementById('signin_password').value;
		let data = new FormData();
		data.append('username', username);
		data.append('password', password);
		ajaxRequest('POST', 'http://localhost:8080/logbook/master?redirect=signin', data, ({ statusCode, message, result }) => {
			if (statusCode === 200) {
				mySPA.setState({ user: result });
				// window.localStorage.setItem('user', JSON.stringify(result));
				mySPA.goToTimelinePage();
			} else if (statusCode === 400) {
				if (message.includes('Password')) {
					showErrorOnInput(document.getElementById('signin_password'), message);
				} else if (message.includes('or')) {
					showErrorOnInput(document.getElementById('signin_username'), message);
					showErrorOnInput(document.getElementById('signin_password'), message);
				}
			}
			e.target.innerHTML = `${text}`;
		});
	});
};
