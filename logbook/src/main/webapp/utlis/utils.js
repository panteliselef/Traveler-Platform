'use strict';

let url = `https://restcountries.eu/rest/v2/all`;

let months = [ 'Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec' ];
const checkXSSAttack = (obj) => {
	let hasHTML = false;

	Object.keys(obj).map((key) => {
		const test = new RegExp('<[^>]*>').test(obj[key]);

		if (typeof obj[key] === 'string' && test) {
			hasHTML = true;
			console.log(obj.userName);
			console.log(key);
			console.log(obj[key]);
		}
	});
	return hasHTML;
};

const inList = (str, list) => {
	let isInList = false;
	list.forEach((element) => {
		if (element === str) isInList = true;
	});
	return isInList;
};

const findParentWithClass = (cssClass, child) => {
	let target = child;
	while (!inList(cssClass, target.classList)) {
		target = target.parentElement;
		if (target == null) return null;
	}
	return target;
};

const fetchComments = (postID) => {
	ajaxRequest('GET', `/logbook/comments?postID=${postID}`, null, ({ statusCode, result, message }) => {
		if (statusCode != 200);
		let commentsHTML = result
			.map((comment) => {
				return `
						<div id="${comment.ID}" class="timeline-post-comment">
							<div class="timeline-post-comment-username">${comment.userName}
								<span>created at ${new Date(comment.createdAt).toLocaleString()}</span>
							</div>
							<div class="timeline-post-comment-msg">${comment.comment}
							</div>
							<div class="timeline-comment-actions">
								<p class="comment-action-delete">Delete</p>
								<p class="comment-action-edit">Edit</p>
							</div> 
						</div>
						
					`;
			})
			.join('');

		document
			.getElementById('timeline-post')
			.getElementsByClassName('timeline-post-comments-container')[0].innerHTML = commentsHTML;

		result.forEach((comment) => {
			document
				.getElementById(comment.ID)
				.getElementsByClassName('comment-action-delete')[0]
				.addEventListener('click', (e) => {
					let data = new FormData();
					data.append('commentID', comment.ID);
					ajaxRequest('DELETE', `/logbook/comments`, data, ({ statusCode, result }) => {
						if (statusCode != 200) return;
						fetchComments(postID);
					});
					console.log(comment.ID);
				});

			document
				.getElementById(comment.ID)
				.getElementsByClassName('comment-action-edit')[0]
				.addEventListener('click', (e) => {
					var newcomment = prompt('Edit comment:', document.getElementById(comment.ID).getElementsByClassName('timeline-post-comment-msg')[0].innerText);
					if (newcomment == null || newcomment == '') {
						console = 'User cancelled the prompt.';
					} else {

						let data = new FormData();
						data.append('ID',comment.ID);
						data.append('comment',newcomment);
						data.append('modifiedAt',new Date());
						ajaxRequest("PUT",`/logbook/comments`,data,({result})=>{
							console.log(result);
						});
					}
					
				});
		});

		console.log(result);
	});
};

const setDeleteListeners = (arr) => {
	arr.map(({ postID }) => {
		document.getElementById(postID).addEventListener('click', (e) => {
			// event.stopPropagation();
			// event.preventDefault();
			if (e.target.classList[1] === 'delete-action') {
				// Do delete here

				let target = findParentWithClass('timeline-post', e.target);
				ajaxRequest('DELETE', `/logbook/post?id=${postID}`, null, ({ statusCode }) => {
					if (statusCode === 200) {
						target.classList.add('deleted');
						target.innerHTML = `
						This post is now deleted
				`;
					}
				});

				return;
			}

			if (findParentWithClass('timeline-post-comments-container', e.target) != null) {
				return;
			}

			if (findParentWithClass('timeline-post-ratings-container', e.target) != null) {
				return;
			}
			console.log(e.target, e.target.tagName);
			if (e.target.tagName === 'A') return;
			let target = findParentWithClass('timeline-post', e.target);
			let id = target.id;
			let post = mySPA.getState('posts').filter((post) => post.postID == id)[0];
			// console.log(post);

			let locationTag = document.getElementById(post.postID).getElementsByClassName('timeline-post-location')[0];

			console.log;
			let location = null;
			if (locationTag.children.length > 0) {
				location = {
					locationStr: locationTag.children[1].innerText,
					lon: post.longitude,
					lat: post.latitude
				};
			}
			// locationTag? locationStr = locationTag.children[1].innerText : locationStr = null;
			let dateStr = document.getElementById(post.postID).getElementsByClassName('timeline-post-date')[0]
				.innerText;
			let imageTag = document.getElementById(post.postID).children[2];
			let imageSrc = null;
			imageTag ? (imageSrc = imageTag.src) : (imageSrc = null);

			document.getElementById('timeline-post').innerHTML = ModalPost(post, imageSrc, dateStr, location);

			fetchComments(post.postID);

			document.getElementsByClassName('timeline-modal-view')[0].style.display = 'flex';

			if (location) {
				console.log(document.getElementsByClassName('timeline-modal-view-map')[0]);
				createMap(location.lon, location.lat, document.getElementsByClassName('timeline-modal-view-map')[0]);
			}
		});
	});
};

const showPostsOnTimeline = () => {
	let posts = document.getElementById('timeline-posts');
	ajaxRequest('GET', '/logbook/master?redirect=post&mode=top_ten', null, ({ message, statusCode, result }) => {
		if (statusCode === 200) {
			posts.innerHTML = result
				.map((post) => {
					return Post(post);
				})
				.join('');
			mySPA.setState({ posts: result });
			setDeleteListeners(result);

			result.forEach((post) => {
				ajaxRequest('GET', `/logbook/rating?postID=${post.postID}`, null, ({ result }) => {
					let avg = 0;
					result.forEach((rating) => {
						avg += rating.rate;

						if (rating.userName === mySPA.getState('user').userName) {
							document.getElementById(`option-${rating.rate}-${post.postID}`).classList.add('selected');
						}
					});
					avg = avg / result.length;

					if (`${avg}` === 'NaN') {
						document
							.getElementById(post.postID)
							.getElementsByClassName('timeline-post-ratings-score')[0].innerText = `No Ratings`;
					} else {
						document
							.getElementById(post.postID)
							.getElementsByClassName('timeline-post-ratings-score')[0].innerText = `${avg.toFixed(
							1
						)} out of ${result.length} Ratings`;
					}
				});
				document
					.getElementById(post.postID)
					.getElementsByClassName('timeline-post-ratings-container')[0].innerHTML = RatingSystem(post.postID);
				Array.from(
					document.getElementById(post.postID).getElementsByClassName('timeline-post-ratings-option')
				).forEach((option) => {
					option.addEventListener('click', (e) => {
						let data = new FormData();
						data.append('userName', mySPA.getState('user').userName);
						data.append('rate', e.target.innerText);
						data.append('postID', post.postID);
						ajaxRequest('POST', '/logbook/rating', data, ({ result }) => {
							console.log(result);
						});
						console.log(e.target.innerText, post.postID);
					});
				});
				showCreateComment(post.postID);
			});
		}
	});
};

const dateToString = (date) => {
	let today = new Date();
	if (date.getDate() === today.getDate()) {
		if (date.getHours() < today.getHours()) {
			return `About ${today.getHours() - date.getHours()} hours ago`;
		} else {
			return `${today.getMinutes() - date.getMinutes()} minutes ago`;
		}
	}
	if (today.getDate() - date.getDate() == 1) {
		return 'Yesterday';
	} else {
		return `${date.getDate()} ${months[date.getMonth()]} at ${date.toLocaleTimeString()}`;
	}
};

const initInputfields = ({ username, email }) => {
	document.getElementById('username').value = username;
	document.getElementById('email').value = email;
	document.getElementById('password').value = 'aaZZa44@';
	document.getElementById('inputPasswordConfirm').value = 'aaZZa44@';
	document.getElementById('firstname').value = 'Pantelis';
	document.getElementById('lastname').value = 'Elef';
	document.getElementById('dates-menu').value = 22;
	document.getElementById('months-menu').value = 12;
	document.getElementById('years-menu').value = 1975;
	document.getElementById('town').value = 'Heraklion';
	document.getElementById('address').value = 'Kalokairinou 800';
	document.getElementById('occupation').value = 'Web Developer';
};

const getRequiredFieldIds = () => {
	return [ 'username', 'email', 'password', 'firstname', 'lastname', 'country', 'town', 'occupation' ];
};
// Function Expressions
const getFormData = () => {
	let formData = new FormData();
	let username = document.getElementById('username').value;
	let email = document.getElementById('email').value;
	let password = document.getElementById('password').value;
	let gender = document.querySelector('input[name="gender"]').value;
	let fName = document.getElementById('firstname').value;
	let lName = document.getElementById('lastname').value;
	let bdate = document.getElementById('dates-menu').value;
	let bmonth = document.getElementById('months-menu').value;
	let byear = document.getElementById('years-menu').value;
	let city = document.getElementById('town').value;
	let country = document.getElementById('country').value;
	let address = document.getElementById('address').value;
	let job = document.getElementById('occupation').value;
	let interests = document.getElementById('interests').value;
	let info = document.getElementById('info').value;

	let birthday = new Date();
	birthday.setDate(bdate);
	birthday.setMonth(bmonth - 1);
	birthday.setFullYear(byear);

	let registeredSince = new Date().toLocaleDateString();
	formData.append('username', username);
	formData.append('email', email);
	formData.append('password', password);
	formData.append('firstname', fName);
	formData.append('lastname', lName);
	formData.append('birthday', birthday.toLocaleDateString());
	formData.append('registeredSince', registeredSince);
	formData.append('gender', gender);
	formData.append('country', country);
	formData.append('city', city);
	formData.append('address', address);
	formData.append('job', job);
	formData.append('interests', interests);
	formData.append('info', info);
	return formData;
};

const cleanErrorMessages = (elem = null) => {
	console.warn('CLEANING ERROR MESSAGES');
	if (!elem) {
		let inputs = document.getElementsByClassName('input-item');
		Array.from(inputs).forEach((inputCont) => {
			let children = inputCont.children;
			if (children.length === 3) {
				children[0].classList.remove('error');
				children[1].classList.remove('error');
				inputCont.removeChild(children[2]);
			}
		});
		return;
	}

	let inputContainer = elem.parentElement;
	let children = inputContainer.children;
	if (children.length === 3) {
		children[0].classList.remove('error');
		children[1].classList.remove('error');
		inputContainer.removeChild(children[2]);
	}
};

const getLowerCaseKeyObject = (obj) => {
	let key,
		keys = Object.keys(obj);
	let n = keys.length;
	const result = {};
	while (n--) {
		key = keys[n];
		result[key.toLowerCase()] = obj[key];
	}
	return result;
};

const validatePasswords = () => {
	let password = document.getElementById('password').value;
	let inputPassConfirm = document.getElementById('inputPasswordConfirm').value;

	cleanErrorMessages();
	if (password != inputPassConfirm) {
		showErrorOnInput(document.getElementById('inputPasswordConfirm'), 'Passwords do not match');
		return false;
	}
	return true;
};

const showErrorOnInput = (element, msg = '') => {
	element.classList.add('error');
	let inputContainer = element.parentElement;
	inputContainer.children[0].classList.add('error');
	let labelText = inputContainer.children[0].textContent.replace('*', '');
	console.log(inputContainer.children);

	let errorContainer = inputContainer.children[2];

	if (errorContainer) {
		msg === ''
			? (errorContainer.textContent = `${labelText.toLowerCase()} is required`)
			: (errorContainer.textContent = msg);
	} else {
		let elem = document.createElement('p');
		elem.classList.add('error-msg');
		if (msg === '') {
			elem.textContent = `${labelText.toLowerCase()} is required`;
		} else {
			elem.textContent = msg;
		}
		inputContainer.appendChild(elem);
	}
};

const validateInputs = () => {
	if (!validatePasswords()) return false;
	let username = document.getElementById('username').value;
	let email = document.getElementById('email').value;
	let password = document.getElementById('password').value;
	let firstname = document.getElementById('firstname').value;
	let lastname = document.getElementById('lastname').value;
	let city = document.getElementById('town').value;
	let job = document.getElementById('occupation').value;
	let validator = new Validator();
	let isCorrect = true;

	cleanErrorMessages();

	if (!validator.testUsername(username)) {
		showErrorOnInput(document.getElementById('username'), 'Username should contain only letter (min. 8)');
		console.error('username', username);
		isCorrect = false;
	}
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

function getLocation() {
	if (navigator.geolocation) {
		console.log('GEOLOCATION EXISTS');
	} else {
		document.getElementById('location-switch').disabled = true;
		document.getElementById('location-switch-container').classList.add('disabled');
		console.log('not supported');
	}
}

function generateDropDown(url) {
	fetch(url)
		.then((res) => res.json())
		.then((data) => {
			return data.map((x) => {
				return {
					code: x.alpha2Code,
					name: x.name
				};
			});
		})
		.then((trimedData) => {
			let dropdown = document.getElementById('country');

			trimedData.forEach((d) => {
				let option = document.createElement('option');
				if (d.code === 'GR') {
					option.defaultSelected = true;
				}
				option.value = d.code;
				option.text = d.name;
				dropdown.appendChild(option);
			});
		})
		.catch((error) => {
			console.error(error);
		});
}

function generateDates() {
	let today = new Date();
	let past = new Date();
	let future = new Date();

	let betweenYears = [];
	let betweenDates = [];
	let betweenMonths = [];

	past.setFullYear(today.getFullYear() - 100);
	while (past < today) {
		betweenYears.push(past.getFullYear());
		past.setFullYear(past.getFullYear() + 1);
	}
	console.log(betweenYears);

	today = new Date();
	future.setFullYear(future.getFullYear() + 1);
	while (future > today) {
		betweenMonths.push(today.getMonth() + 1);
		today.setMonth(today.getMonth() + 1);
	}
	console.log(betweenMonths);

	today = new Date();
	future = new Date();
	future.setMonth(future.getMonth() + 1);
	while (future > today) {
		betweenDates.push(today.getDate());
		today.setDate(today.getDate() + 1);
	}
	console.log(betweenDates);

	let datesMenu = document.getElementById('dates-menu');
	let monthsMenu = document.getElementById('months-menu');
	let yearsMenu = document.getElementById('years-menu');

	let selectMenus = [ datesMenu, monthsMenu, yearsMenu ];
	[ betweenDates, betweenMonths, betweenYears.reverse() ].forEach((arr, i) => {
		arr.forEach((item) => {
			let elem = document.createElement('option');
			elem.value = item;
			elem.text = item;
			selectMenus[i].appendChild(elem);
		});
	});
}

const createMap = (lon, lat, elem = null) => {
	console.log(lon + ' , ' + lat);
	let mapContainer;
	if (elem == null) {
		mapContainer = document.getElementById('map-container');
	} else {
		mapContainer = elem;
	}

	console.log(mapContainer.children);
	Array.from(mapContainer.children).map((element) => {
		console.log('Removing');
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

	// var zoom = 8;

	var markers = new OpenLayers.Layer.Markers('Markers');
	map.addLayer(markers);

	markers.addMarker(new OpenLayers.Marker(lonLat));

	map.setCenter(lonLat, 10);
};
