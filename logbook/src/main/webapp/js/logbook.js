'use strict';


const toggleLocationLoadingSpinner = (mode) => {
	let loadingSpinnerElem = document.getElementById('location-switch-container').children[2];
	loadingSpinnerElem.style.display = mode;
	console.log(loadingSpinnerElem);
};



const createMap = (lon, lat) => {
	console.log(lon + ' , ' + lat);
	let mapContainer = document.getElementById('map-container');

	console.log(mapContainer.children);
	Array.from(mapContainer.children).map((element) => {
		mapContainer.removeChild(element);
	});
	let mapDiv = document.createElement('div');
	mapDiv.classList.add('map-show');
	mapDiv.id = 'mapdiv';
	mapContainer.appendChild(mapDiv);
	let map = new OpenLayers.Map('mapdiv');
	map.addLayer(new OpenLayers.Layer.OSM());

	var lonLat = new OpenLayers.LonLat(lon, lat).transform(
		new OpenLayers.Projection('EPSG:4326'), // transform from WGS 1984
		map.getProjectionObject() // to Spherical Mercator Projection
	);

	var zoom = 8;

	var markers = new OpenLayers.Layer.Markers('Markers');
	map.addLayer(markers);

	markers.addMarker(new OpenLayers.Marker(lonLat));

	map.setCenter(lonLat, zoom);
};

const showMap = async () => {
	let NOMATISM_URL = `https://nominatim.openstreetmap.org/search`;
	let anotherUrl = `${NOMATISM_URL}`;

	const countryCode = document.getElementById('countries-dp').value;
	const city = document.getElementById('city').value;
	const address = document.getElementById('address').value;
	const addressElem = document.getElementById('address');
	const locationFromInputsElem = document.getElementById('location-from-inputs');

	if (city === '' || city === ' ') {
		showErrorOnInput(document.getElementById('city'));
		return;
	}

	locationFromInputsElem.innerText = `${address ? address + ',' : ''} ${city},${countryCode}`;

	try {
		let res = await fetchUrl(`${NOMATISM_URL}?q=${city},${address},${countryCode}&format=json`);
		// console.log(res + ' dwa');
		if (res.length > 0) {
			// at least a match has been found
			let selected = res[0];
			console.log('SELECTED', selected);
			createMap(selected.lon, selected.lat);
		} else {
			showErrorOnInput(addressElem);
		}
	} catch (error) {
		console.error(error);
	}
};

//Event Listeners

// For the "upload" radio buttons
document.querySelectorAll(`input[name="upload_photo"]`).forEach((inputElem) => {
	inputElem.addEventListener('click', (e) => {
		cleanErrorMessages();
		if (e.target.checked && e.target.value === 'yes') {
			if (/^[A-Za-z]{8,}$/.test(document.getElementById('username').value)) {
				//valid username
				faceRec.resume();
				document.getElementsByClassName('video-section')[0].style.display = 'block';
				document.body.scrollTop = 0; // For Safari
				document.documentElement.scrollTop = 0;
				document.getElementsByTagName('body')[0].style.overflowY = 'hidden';
			} else {
				showErrorOnInput(document.getElementById('username'));
			}
		} else {
			console.log('cannot happen');
		}
	});
});

// Close video
document.getElementById('close-video').addEventListener('click',(e)=>{
	document.getElementsByClassName('video-section')[0].style.display = 'none';
	document.getElementsByTagName("body")[0].style.overflowY = "unset";
	faceRec.pause();
})

// Validate username before taking a photo of user
document.getElementById('username').addEventListener('keyup', (e) => {
	let container = document.getElementById('upload_photo_container');
	if (e.target.value === '' || e.target.value === ' ') {
		container.classList.add('disabled');
		Array.from(container.children[1].children).forEach((child) => {
			child.children[0].disabled = true;
		});
	} else {
		container.classList.remove('disabled');
		Array.from(container.children[1].children).forEach((child) => {
			child.children[0].disabled = false;
		});
	}
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
		try {
			const { address } = await fetchUrl(`${NOMATISM_URL}?lat=${latitude}&lon=${longitude}&format=json`);
			console.log(address);
			let { bus_stop, city, country_code, town, suburb } = address;
			console.log(bus_stop, city, country_code, town, suburb);
			if (!city) city = town;
			if (!bus_stop) {
				if (!suburb) bus_stop = '';
				else bus_stop = suburb;
			}

			const countryCode = document.getElementById('countries-dp');
			const cityElem = document.getElementById('city');
			const addressElem = document.getElementById('address');
			cityElem.value = city;
			addressElem.value = bus_stop;
			countryCode.value = country_code.toUpperCase();
			toggleLocationLoadingSpinner('none');
		} catch (error) {
			console.log(error);
			toggleLocationLoadingSpinner('none');
		}
	});
});


document.getElementById('btnSubmit').addEventListener('click', (e) => {
	// e.preventDefault();
	// let inputPass = document.getElementById('inputPassword');
	// let inputPassConfirm = document.getElementById('inputPasswordConfirm');
	// cleanErrorMessages();
	// if (inputPass.value != inputPassConfirm.value) {
	// 	console.log('Invalid password');
	// 	inputPassConfirm.classList.add('error');
	// 	let inputContainer = inputPassConfirm.parentElement;
	// 	inputContainer.children[0].classList.add('error');
	// 	let elem = document.createElement('p');
	// 	elem.classList.add('error-msg');
	// 	elem.textContent = 'The two passwords do not match';
	// 	inputContainer.appendChild(elem);
	// }

	// let data = getFormData();
	// ajaxRequest('POST', 'http://localhost:8080/logbook/SignUpServlet', data, ({ statusCode, message, user }) => {
	// 	if (statusCode == 200) {
	// 		mySPA.setState({ user });
	// 		window.localStorage.setItem('user', JSON.stringify(user));
	// 		mySPA.goToDashBoard();
	// 	}else if(statusCode == 400) {
	// 		if(message.includes("username")){
	// 			showErrorOnInput(document.getElementById('username'),message)
	// 		}else if(message.includes("email")){
	// 			showErrorOnInput(document.getElementById('email'),message)
	// 		}
	// 	}
	// });
});

//Function Declarations

function fetchUrl(
	url,
	options = {
		method: 'GET',
		async: true
	}
) {
	return new Promise((resolve, reject) => {
		if (url.isEmpty || url === '' || url === ' ') {
			reject(new Error('The provided url is empty'));
			return;
		}
		let xmlReq = new XMLHttpRequest();

		const loadHandler = () => {
			if (xmlReq.status == 200) {
				console.log(JSON.parse(xmlReq.responseText));
				resolve(JSON.parse(xmlReq.responseText));
			}
		};

		const errorHandler = () => {
			reject(new Error());
		};
		xmlReq.addEventListener('load', loadHandler);
		xmlReq.addEventListener('error', errorHandler);
		xmlReq.open(options.method, url, options.async);
		xmlReq.setRequestHeader('Access-Control-Allow-Origin', '*');
		xmlReq.send();
	});
}
