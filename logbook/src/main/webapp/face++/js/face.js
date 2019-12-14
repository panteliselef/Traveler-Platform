'use strict';

/*
    Author: Panagiotis Papadakos papadako@csd.uoc.gr

    Try to read this file and understand what the code does...
    Then try to complete the missing functionality

    For the needs of the hy359 2019 course
    University of Crete

    At the end of the file there are some comments about our trips

*/

/*  face recognition that is based on faceplusplus service */
var faceRec = (function() {
	// Object that holds anything related with the facetPlusPlus REST API Service
	var faceAPI = {
		apiKey: 'l2jNgKbk1HXSR4vMzNygHXx2g8c_xT9c',
		apiSecret: '2T6XdZt4EYw-I7OhmZ6g1wtECl81e_Ip',
		app: 'hy359',
		// Detect
		// https://console.faceplusplus.com/documents/5679127
		detect: 'https://api-us.faceplusplus.com/facepp/v3/detect', // POST
		// Set User ID
		// https://console.faceplusplus.com/documents/6329500
		setuserId: 'https://api-us.faceplusplus.com/facepp/v3/face/setuserid', // POST
		// Get User ID
		// https://console.faceplusplus.com/documents/6329496
		getDetail: 'https://api-us.faceplusplus.com/facepp/v3/face/getdetail', // POST
		// addFace
		// https://console.faceplusplus.com/documents/6329371
		addFace: 'https://api-us.faceplusplus.com/facepp/v3/faceset/addface', // POST
		// Search
		// https://console.faceplusplus.com/documents/5681455
		search: 'https://api-us.faceplusplus.com/facepp/v3/search', // POST
		// Create set of faces
		// https://console.faceplusplus.com/documents/6329329
		create: 'https://api-us.faceplusplus.com/facepp/v3/faceset/create', // POST
		// update
		// https://console.faceplusplus.com/documents/6329383
		update: 'https://api-us.faceplusplus.com/facepp/v3/faceset/update', // POST
		// removeface
		// https://console.faceplusplus.com/documents/6329376
		removeFace: 'https://api-us.faceplusplus.com/facepp/v3/faceset/removeface' // POST
	};

	// Object that holds anything related with the state of our app
	// Currently it only holds if the snap button has been pressed
	var state = {
		photoSnapped: false,
		videoStream: null
	};

	// function that returns a binary representation of the canvas
	function getImageAsBlobFromCanvas(canvas) {
		// function that converts the dataURL to a binary blob object
		function dataURLtoBlob(dataUrl) {
			// Decode the dataURL
			var binary = atob(dataUrl.split(',')[1]);

			// Create 8-bit unsigned array
			var array = [];
			for (var i = 0; i < binary.length; i++) {
				array.push(binary.charCodeAt(i));
			}

			// Return our Blob object
			return new Blob([ new Uint8Array(array) ], {
				type: 'image/jpg'
			});
		}

		var fullQuality = canvas.toDataURL('image/jpeg', 1.0);
		return dataURLtoBlob(fullQuality);
	}

	// function that returns a base64 representation of the canvas
	function getImageAsBase64FromCanvas(canvas) {
		// return only the base64 image not the header as reported in issue #2
		// return canvas.toDataURL('image/jpeg', 1.0).split(',')[1];
		return canvas.toDataURL('image/jpeg', 1.0);
	}

	function searchImage() {
		if (state.photoSnapped) {
			var canvas = document.getElementById('canvas');
			var image = getImageAsBlobFromCanvas(canvas);

			let data = new FormData();
			data.append('api_key', faceAPI.apiKey);
			data.append('api_secret', faceAPI.apiSecret);
			data.append('image_file', getImageAsBlobFromCanvas(canvas));
			data.append('outer_id', 'hy359');

			ajaxRequest('POST', faceAPI.search, data, (res) => {
				console.log(res);
				document.getElementsByClassName('video-section')[0].style.display = 'none';
				document.getElementsByTagName('body')[0].style.overflowY = 'unset';
				faceRec.pause();
				const { results } = res;
				results.sort((a, b) => b.confidence - a.confidence);
				const username = results[0].user_id;
				document.getElementById('username').value = username;
				document.getElementById('video-spinner').style.display = 'none';
			});
		} else {
			alert('No image has been taken!');
		}
	}

	function selectImage() {
		if (!state.photoSnapped) {
			alert('Take a photo first!');
			return;
		}

		var canvas = document.getElementById('canvas');
		let image = getImageAsBase64FromCanvas(canvas);
		// mySPA.setState({ cameraPhoto: image });
		document.getElementsByClassName('video-section')[0].style.display = 'none';
		document.getElementsByTagName('body')[0].style.overflowY = 'unset';
		faceRec.pause();
		showImage(image);
	}

	// Function called when we upload an image
	function uploadImage() {
		if (state.photoSnapped) {
			var canvas = document.getElementById('canvas');
			var image = getImageAsBlobFromCanvas(canvas);

			// ============================================

			// TODO!!! Well this is for you ... YES you!!!
			// Good luck!

			// Create Form Data. Here you should put all data
			// requested by the face plus plus services and
			// pass it to ajaxRequest
			var data = new FormData();
			data.append('api_key', faceAPI.apiKey);
			data.append('api_secret', faceAPI.apiSecret);
			data.append('image_file', getImageAsBlobFromCanvas(canvas));
			// data.append('image_file', faceAPI.apiSecret);
			// add also other query parameters based on the request
			// you have to send

			// You have to implement the ajaxRequest. Here you can
			// see an example of how you should call this
			// First argument: the HTTP method
			// Second argument: the URI where we are sending our request
			// Third argument: the data (the parameters of the request)
			// ajaxRequest function should be general and support all your ajax requests...
			// Think also about the handler
			// ajaxRequest('POST', faceAPI.search, data);
			ajaxRequest('POST', faceAPI.detect, data, (jsonData) => {
				console.log(jsonData);
				const { faces } = jsonData;
				const faceToken = faces[0].face_token;
				console.log(faces[0].face_rectangle);
				showBoxBoundsInCanvas(canvas, faces[0].face_rectangle);
				let tmpData = new FormData();
				tmpData.append('api_key', faceAPI.apiKey);
				tmpData.append('api_secret', faceAPI.apiSecret);
				tmpData.append('face_token', faceToken);
				tmpData.append('user_id', document.getElementById('username').value);

				// perform another ajax request
				ajaxRequest('POST', faceAPI.setuserId, tmpData, (jsonResponse) => {
					console.log(jsonResponse);
					let tmpData2 = new FormData();
					tmpData2.append('api_key', faceAPI.apiKey);
					tmpData2.append('api_secret', faceAPI.apiSecret);
					tmpData2.append('face_tokens', faceToken);
					tmpData2.append('outer_id', 'hy359');

					// perform another ajax request
					ajaxRequest('POST', faceAPI.addFace, tmpData2, (jsonRes) => {
						document.getElementById('video-spinner').style.display = 'none';
						console.log(jsonRes);
					});
				});
			});
		} else {
			alert('No image has been taken!');
		}
	}

	function showBoxBoundsInCanvas(canvas, { top, left, width, height }) {
		console.log(canvas);
		console.log('to canvas', left, top, width, height);
		const context = canvas.getContext('2d');
		context.save();
		// Red rectangle
		context.beginPath();
		context.lineWidth = '6';
		context.strokeStyle = 'red';
		context.rect(left, top, width, height); //because i've translated the image
		context.stroke();
		context.restore();
	}

	function pause() {
		console.log('pause');
		let video = document.getElementById('video');
		if (state.stream) {
			console.log(state.stream.getTracks());
			// state.stream.getTracks().forEach(function(track) {
			//   track.stop();
			// });
			state.stream.getVideoTracks().forEach((track) => track.stop());
		}
		state.stream = null;
		video.srcObject = null;
		video.pause();
		document.getElementById('video-spinner').style.display = 'block';
	}

	function resume() {
		console.log('resume');
		var video = document.getElementById('video');
		if (navigator.mediaDevices && navigator.mediaDevices.getUserMedia) {
			navigator.mediaDevices.getUserMedia({ video: true }).then(function(stream) {
				// stream.getVideoTracks().forEach(e=>e.stop());
				setTimeout(() => {}, 2000);
				document.getElementById('video-spinner').style.display = 'none';
				state.stream = stream;
				video.srcObject = state.stream;
				video.onloadedmetadata = function(e) {
					video.play();
				};
			});
		}
	}
	// Function for initializing things (event handlers, etc...)
	function init() {
		console.log('init');
		// Put event listeners into place
		// Notice that in this specific case handlers are loaded on the onload event
		window.addEventListener(
			'DOMContentLoaded',
			function() {
				// Grab elements, create settings, etc.
				var canvas = document.getElementById('canvas');
				var context = canvas.getContext('2d');
				// context.transform(3,0,0,1,canvas.width,canvas.heigth);
				var video = document.getElementById('video');
				var mediaConfig = {
					video: true
				};
				var errBack = function(e) {
					console.log('An error has occurred!', e);
				};

				let aspectRatio = 2;

				// Put video listeners into place
				if (navigator.mediaDevices && navigator.mediaDevices.getUserMedia) {
					navigator.mediaDevices.getUserMedia(mediaConfig).then(function(stream) {
						aspectRatio = stream.getVideoTracks()[0].getSettings().aspectRatio;
						document.getElementById('video-spinner').style.display = 'block';
					});
				}

				// Trigger photo take
				document.getElementById('snap').addEventListener('click', function() {
					document.getElementsByClassName('canvas-container')[0].style.visibility = 'visible';
					let heigth = 480;

					context.save();
					context.translate(480 * aspectRatio, 0);
					context.scale(-1, 1);
					context.drawImage(video, 0, 0, aspectRatio * heigth, heigth);
					context.restore();
					state.photoSnapped = true; // photo has been taken
				});

				// Trigger when upload button is pressed
				if (document.getElementById('upload'))
					document.getElementById('upload').addEventListener('click', () => {
						document.getElementById('video-spinner').style.display = 'block';
						// uploadImage();
						selectImage();
					});

				if (document.getElementById('upload_search'))
					document.getElementById('upload_search').addEventListener('click', () => {
						document.getElementById('video-spinner').style.display = 'block';
						// searchImage();
					});
			},
			false
		);
	}

	// ============================================

	// !!!!!!!!!!! ================ TODO  ADD YOUR CODE HERE  ====================
	// From here on there is code that should not be given....

	// You have to implement the ajaxRequest function!!!!

	// function ajaxRequest(method, url, fData, callback) {
	// 	let xmlReq = new XMLHttpRequest();
	// 	const onloadHandler = (e) => {
	// 		callback(JSON.parse(xmlReq.responseText));
	// 	};
	// 	xmlReq.addEventListener('load', onloadHandler);
	// 	xmlReq.open(method, url, true);
	// 	xmlReq.send(fData);
	// }

	// !!!!!!!!!!! =========== END OF TODO  ===============================

	// Public API of function for facet recognition
	// You might need to add here other methods based on your implementation
	return {
		init: init,
		pause: pause,
		resume: resume
	};
})();
