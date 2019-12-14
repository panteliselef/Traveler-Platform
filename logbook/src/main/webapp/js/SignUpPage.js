'use strict';

const SignUpPage = () => {
	return `<div id="form-container">

<img class="shape-aqua" alt="aqua-shape-cover" src="./assets/shape-aqua.svg" />

<section id="sign-up-container" class="main-section light">
  <div style="position: absolute;top:0;right:0;padding: 1rem;" onclick="mySPA.goToSignIn();">Sign In</div>
  <div style="display: flex;justify-content: space-between;align-items: flex-end;flex-wrap: wrap;">
    <h2>Let’s Sign you up 😆</h2>
    <div id="location-switch-container" class="switch-button">
      <label class="switch">
        <input id="location-switch" type="checkbox">
        <span class="slider round"></span>
      </label>
      <p>Use location</p>
      <span class="loading-spinner"></span>
    </div>
  </div>
  <div class="grid-layout-3-col">
    <div class="col">
      <div class="input-item">
        <label>
          Username *
        </label>
        <input required type="text" id="username" minlength="8" pattern="[A-Za-z]{8,}"
          title="8 or more latin characters">
      </div>
      <div class="input-item">
        <label>
          Email *
        </label>
        <input required id="email" type="email" pattern="[a-z0-9._%+-]+@[a-z0-9.-]+\.[a-z]{2,}$">
      </div>
      <div class="input-item">
        <label>
          Password *
        </label>
        <input id="password" required type="password" 
          pattern="(?=.*[A-Za-z])(?=.*\d)[A-Za-z\d]{8,10}">
      </div>
      <div class="input-item">
        <label>
          Confirm Password *
        </label>
        <input id="inputPasswordConfirm" required type="password" minlength="8" maxlength="10"
          pattern="(?=.*[A-Za-z])(?=.*\d)[A-Za-z\d]">
      </div>
    </div>
    <div class="col">
      <div class="input-item">
        <label>
          First Name *
        </label>
        <input type="text" id="firstname" required minlength="3" maxlength="15" pattern="([a-zA-Z]){3,15}$">
      </div>
      <div class="input-item">
        <label>
          Last Name *
        </label>
        <input type="text" id="lastname" required minlength="3" maxlength="15" pattern="([a-zA-Z]){3,15}$">
      </div>
      <div class="input-item">
        <label>
          Birthday *
        </label>
        <div class="datepicker">

          <select required id="dates-menu">
            <option value="none" selected disabled>DD</option>

          </select>
          <select required id="months-menu">
            <option value="none" selected disabled>MM</option>

          </select>
          <select required id="years-menu">
            <option value="none" selected disabled>YY</option>

          </select>
        </div>


      </div>
      <div class="input-item">
        <label>
          Gender
        </label>
        <div class="radio-button-collection">
          <div class="radio-button-container">

            <input type="radio" name="gender" value="male" checked>
            <span class="custom-radio">👱🏼</span>
          </div>
          <div class="radio-button-container">

            <input type="radio" name="gender" value="female">
            <span class="custom-radio">👱🏼‍♀️</span>
          </div>
          <div class="radio-button-container">

            <input type="radio" name="gender" value="Undefined">
            <span class="custom-radio">❓</span>
          </div>


        </div>

      </div>
    </div>
    <div class="col">
      <div class="input-item">
        <label>
          Country *
        </label>
        <select required id="country">

        </select>
      </div>
      <div class="input-item">
        <label>
          City *
        </label>
        <input id="town" required type="text" minlength="2" maxlength="20" pattern="([a-zA-Z]){2,20}$">
      </div>
      <div class="input-item">
        <label>
          Address
        </label>
        <input id="address" type="text" minlength="2">
      </div>
      <div class="input-item">
        <label>
          Occupation *
        </label>
        <input required type="text" id="occupation" minlength="3" maxlength="15" pattern="([a-zA-Z]){3,15}$">
      </div>
    </div>
  </div>
  <div class="map-info">
    <div id="location-from-inputs" class="">
      No location Found
    </div>
    <div class="buttons-for-map">
      <div id="show-map-btn" class="primary-btn">Show Map</div>
    </div>
  </div>
  <div id="map-container">
    <div id="mapdiv" class=""></div>
  </div>
  <div class="grid-layout-2-col">
    <div class="col">
      <div class="input-item">
        <label>
          Hobbies
        </label>
        <textarea id="interests" maxlength="100"></textarea>
      </div>
    </div>
    <div class="col">
      <div class="input-item">
        <label>
          More Info
        </label>
        <textarea id="info" maxlength="500"></textarea>
      </div>
    </div>
  </div>
  <button id="btnSubmit" class="enroll-button"> Sign Up</button>
</section>
<img class="shape-somon" alt="somon-shape-cover" src="./assets/shape-somon.svg" />
</div>`;
};

const toggleLocationLoadingSpinner = (mode) => {
	let loadingSpinnerElem = document.getElementById('location-switch-container').children[2];
	loadingSpinnerElem.style.display = mode;
	console.log(loadingSpinnerElem);
};

const showMap = async () => {
	let NOMATISM_URL = `https://nominatim.openstreetmap.org/search`;

	const countryCode = document.getElementById('country').value;
	const city = document.getElementById('town').value;
	const address = document.getElementById('address').value;
	const addressElem = document.getElementById('address');
	const locationFromInputsElem = document.getElementById('location-from-inputs');

	if (city === '' || city === ' ') {
		showErrorOnInput(document.getElementById('city'));
		return;
	}

	locationFromInputsElem.innerText = `${address ? address + ',' : ''} ${city},${countryCode}`;

	ajaxRequest('GET', `${NOMATISM_URL}?q=${city},${address},${countryCode}&format=json`, null, (res) => {
		if (res.length > 0) {
			// at least a match has been found
			let selected = res[0];
			console.log('SELECTED', selected);
			createMap(selected.lon, selected.lat);
		} else {
			showErrorOnInput(addressElem);
		}
	});
};

const handleSignUp = ({ statusCode, message, result }) => {
	if (statusCode == 200) {
		mySPA.setState({ user: result });
		// window.localStorage.setItem('user', JSON.stringify(result));
		console.log('GOING to dashboard');
		mySPA.goToTimelinePage();
	} else if (statusCode == 400) {
		if (message.includes('username')) {
			showErrorOnInput(document.getElementById('username'), message);
		} else if (message.includes('email')) {
			showErrorOnInput(document.getElementById('email'), message);
		} else if (message.toLowerCase().includes('missing')) {
			cleanErrorMessages();
			let obj = getLowerCaseKeyObject(result);
			let keys = Object.keys(obj);
			let fieldIds = getRequiredFieldIds().map((id) => id.toLowerCase());
			keys = keys.filter((key) => fieldIds.includes(key));

			keys.forEach((key) => {
				if (!obj[key]) {
					showErrorOnInput(document.getElementById(key));
				}
			});
		}
	}
};

const showSignUp = () => {
	document.getElementsByTagName('body')[0].style.background = '#2e282a';
	const rootDiv = document.getElementById('root');
	rootDiv.innerHTML = SignUpPage();

	let testOne = {
		username: 'elefcodes',
		email: 'csd3942@csd.uoc.gr'
	};
	let testTwo = {
		username: 'starbucks',
		email: 'elefcodes@elefcodes.com'
	};

	let testThree = {
		username: 'whynotdoit',
		email: 'why@why.com'
	};

	initInputfields(testThree);

	generateDates();
	getLocation();
	generateDropDown(`https://restcountries.eu/rest/v2/all`);

	document.getElementById('btnSubmit').addEventListener('click', (e) => {
		e.preventDefault();

		if (!validateInputs()) return;

		let data = getFormData();
		ajaxRequest('POST', 'http://localhost:8080/logbook/master?redirect=SignUpServlet', data, handleSignUp);
	});

	document.getElementById('username').addEventListener('blur', (e) => {
		let params = `&username=${e.target.value}`;
		ajaxRequest(
			'GET',
			`http://localhost:8080/logbook/master?redirect=checkExistence${params}`,
			null,
			({ message, statusCode, result }) => {
				cleanErrorMessages(e.target);
				if (statusCode == 400) {
					showErrorOnInput(e.target, message);
				}
			}
		);
	});

	document.getElementById('email').addEventListener('blur', (e) => {
		let params = `&email=${e.target.value}`;
		ajaxRequest(
			'GET',
			`http://localhost:8080/logbook/master?redirect=checkExistence${params}`,
			null,
			({ message, statusCode, result }) => {
				cleanErrorMessages(e.target);
				if (statusCode == 400) {
					showErrorOnInput(e.target, message);
				}
			}
		);
	});

	// Show Map to User
	document.getElementById('show-map-btn').addEventListener('click', () => {
		cleanErrorMessages();
		showMap();
	});

	// Get live location of user
	document.getElementById('location-switch').addEventListener('click', (e) => {
		if (e.target.disabled) return;
		const NOMATISM_URL = `https://nominatim.openstreetmap.org/reverse`;
		if (!e.target.checked) {
			toggleLocationLoadingSpinner('none');
			return;
		}
		toggleLocationLoadingSpinner('block');
		navigator.geolocation.getCurrentPosition(async ({ coords }) => {
			const { latitude, longitude } = coords;
			console.log(coords);

			ajaxRequest('GET', `${NOMATISM_URL}?lat=${latitude}&lon=${longitude}&format=json`, null, ({ address }) => {
				console.log(address);
				let { bus_stop, city, country_code, town, suburb } = address;
				console.log(bus_stop, city, country_code, town, suburb);
				if (!city) city = town;
				if (!bus_stop) {
					if (!suburb) bus_stop = '';
					else bus_stop = suburb;
				}

				const countryCode = document.getElementById('country');
				const cityElem = document.getElementById('town');
				const addressElem = document.getElementById('address');
				cityElem.value = city;
				addressElem.value = bus_stop;
				countryCode.value = country_code.toUpperCase();
				toggleLocationLoadingSpinner('none');
			});
		});
	});
};
