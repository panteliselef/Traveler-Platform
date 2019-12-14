'use strict';

function ajaxRequest(method, url, dataToServer, callback,isAsync=true) {
	let xmlReq = new XMLHttpRequest();
	// xmlReq.withCredentials = true;
	const onloadHandler = (e) => {
		if (xmlReq.status >= 400) {
			console.log('Status Code', xmlReq.status);
			console.log('Content', xmlReq.responseText);
			callback(JSON.parse(xmlReq.responseText));
		} else {
			console.log(xmlReq.responseText, xmlReq.status);
			callback(JSON.parse(xmlReq.responseText));
		}
	};
	xmlReq.open(method, url, isAsync);
	xmlReq.setRequestHeader('Access-Control-Allow-Origin','*');
	xmlReq.addEventListener('load', onloadHandler);
	xmlReq.addEventListener('error', onloadHandler);
	xmlReq.send(dataToServer);
}

function fetchImage(url,callback) {
	let xmlReq = new XMLHttpRequest();
	const onloadHandler = (e) => {

		callback(xmlReq.status);
		// if (xmlReq.status >= 400) {
		// 	console.log('Status Code', xmlReq.status);
		// 	console.log('Content', xmlReq.responseText);
		// 	callback(JSON.parse(xmlReq.responseText));
		// } else {
		// 	console.log(xmlReq.responseText, xmlReq.status);
		// 	callback(JSON.parse(xmlReq.responseText));
		// }
	};
	xmlReq.open('GET', url, true);
	// xmlReq.setRequestHeader('Access-Control-Allow-Origin','*');
	xmlReq.addEventListener('load', onloadHandler);
	xmlReq.addEventListener('error', onloadHandler);
	xmlReq.send(null);
}