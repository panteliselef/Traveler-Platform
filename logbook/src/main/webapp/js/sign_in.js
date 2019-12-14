'use strict';
document.addEventListener('DOMContentLoaded', init);

function init() {

  const closeVideo =(e) =>{
    document.getElementsByClassName('video-section')[0].style.display = 'none';
    document.getElementsByTagName('body')[0].style.overflowY = 'unset';
    faceRec.pause();
  }

	document.getElementById('close-video').addEventListener('click', closeVideo);
  
	document.querySelectorAll(`input[name="face_recogn"]`).forEach((inputElem) => {
		inputElem.addEventListener('click', (e) => {
			if (e.target.checked && e.target.value === 'yes') {
				faceRec.resume();
				document.getElementsByClassName('video-section')[0].style.display = 'block';
				document.body.scrollTop = 0; // For Safari
				document.documentElement.scrollTop = 0;
				document.getElementsByTagName('body')[0].style.overflowY = 'hidden';
				console.log('can happen');
			} else {
				console.log('cannot happen');
			}
		});
	});
}
