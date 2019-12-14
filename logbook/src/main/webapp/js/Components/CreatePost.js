const CreatePost = () => {

	let user = mySPA.getState('user').userName;
	return `
        <div class="timeline-create-post">
            <div class="timeline-post-about">
                <div class="image-placeholder">
                 ${user[0]}${user[1]}
                </div>
                <textarea id="post-area" placeholder="What's on your mind, ${mySPA.getState('user').firstName} ?"></textarea> 
            </div>
            <div id="timeline-create-post-image-container" class="timeline-create-post-image-container">
<!--                <div class="image-overlay"></div>-->
<!--                <img id="create-post-image" class="timeline-create-post-image"  src="https://images.unsplash.com/photo-1575535135032-0045eaea7d62?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=3365&q=80"/>-->
<!--                <div id="remove-image">X</div>-->
            </div>
            <div id="timeline-create-post-resource-container" class="timeline-create-post-resource-container">
<!--            	<div>-->
<!--            		https://stackoverflow.com/questions/35911262/fetching-metadata-from-url-->
<!--				</div>-->
            </div>
<!--            <img id="timeline-create-post-image" class="timeline-create-post-image" src="https://images.unsplash.com/photo-1575535135032-0045eaea7d62?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=3365&q=80"/>-->
            <div id="timeline-post-action-inputfields" class="timeline-post-action-inputfields">
                <div id="image-url-input" style="display: none" class="timeline-post-action-input">
                    <label>Image Url</label>
                    <input id="photo-input" type="text"/>
								</div>
								<div id="resource-url-input" style="display: none" class="timeline-post-action-input">
                    <label>Url Resource</label>
                    <input id="resource-input" type="text"/>
                </div>
            </div>
            <div class="timeline-post-actions">
                <div style="display: flex">
                    <div id="upload-action" class="timeline-post-action">
                        <img style="margin-right: .5rem" height="20px" src="./assets/upload.svg"/>
                        <div>Upload</div>
                        <input style="display:none" id="upload-photo-input" placeholder="upload somthing" type="file" accept="image/*"/>
                    </div>
                    <div id="camera-action" class="timeline-post-action">
                        <img style="margin-right: .5rem" height="20px" src="./assets/photo_camera-24px.svg"/>
                        <div>Camera</div>
                    </div>
                    <div class="timeline-post-action-group">
                        <div class="timeline-post-action">
                            <img height="20px" src="./assets/dots-horizontal.svg"/>
                        </div>
                        <div class="timeline-post-action-menu">
                            <p id="photo-action">Add photo from URL</p>
														<p id="resource-action">Add resource from URL</p>
												</div>
                    </div>
                </div>
                <div>
                    <div id="share-action" class="timeline-post-action primary">
                        Share
                    </div>
                </div>
            </div>
        </div>
    `;
};

const hideImage = () => {
	document.getElementById('timeline-create-post-image-container').innerHTML = ``;
	document.getElementById('timeline-create-post-image-container').style.display = 'none';
};

const hideResource = () => {
	document.getElementById('timeline-create-post-resource-container').innerHTML = '';
	document.getElementById('timeline-create-post-resource-container').style.display = 'none';
};

const showImage = (url) => {
	const imgeElems = `
			<div class="image-overlay"></div>
			<img id="create-post-image" class="timeline-create-post-image"  src="${url}"/>
			<div id="remove-image">X</div>
    `;

	document.getElementById('timeline-create-post-image-container').innerHTML = imgeElems;
	document.getElementById('timeline-create-post-image-container').style.display = 'block';

	document.getElementById('remove-image').addEventListener('click', (e) => {
		hideImage();
	});
};

const showResource = (url) => {
	const resourceElems = `
			<a href="${url}" target="_blank">${url}</a>
			<div id="remove-resource">X</div>
    `;

	document.getElementById('timeline-create-post-resource-container').innerHTML = resourceElems;
	document.getElementById('timeline-create-post-resource-container').style.display = 'block';

	document.getElementById('remove-resource').addEventListener('click', (e) => {
		hideResource();
	});
};

const showCreatPost = (elem) => {
	let photoButton = new SwitchButton();
	let resourceButton = new SwitchButton();
	photoButton.turnOff();
	resourceButton.turnOff();

	elem.insertAdjacentHTML('afterbegin', CreatePost());

	document.getElementById('upload-photo-input').addEventListener('change', (e) => {
		const file = e.target.files[0];
		if (file.size > 720000) {
			alert('File is too big, max 720KB');
			return;
		}
		var reader = new FileReader();
		reader.onloadend = function() {
			toDataURL(reader.result, (base64) => {
				// saveImage(base64);
				// console.log(base64);
				showImage(base64);
			});
		};
		reader.readAsDataURL(file);
	});

	document.getElementById('resource-action').addEventListener('click', (e) => {
		if (!resourceButton.isOn()) {
			//open it
			// document.getElementById('timeline-post-action-inputfields').style.display = 'block';
			document.getElementById('resource-url-input').style.display = 'flex';
			resourceButton.turnOn();
		} else {
			//close it
			// document.getElementById('timeline-post-action-inputfields').style.display = 'none';
			document.getElementById('resource-url-input').style.display = 'none';
			resourceButton.turnOff();
		}
	});

	document.getElementById('upload-action').addEventListener('click', (e) => {
		document.getElementById('upload-photo-input').click();
	});

	document.getElementById('camera-action').addEventListener('click', (e) => {
		faceRec.resume();
		document.getElementsByClassName('video-section')[0].style.display = 'block';
		document.body.scrollTop = 0; // For Safari
		document.documentElement.scrollTop = 0;
		document.getElementsByTagName('body')[0].style.overflowY = 'hidden';
	});

	document.getElementById('photo-action').addEventListener('click', (e) => {
		if (!photoButton.isOn()) {
			//open it
			// document.getElementById('timeline-post-action-inputfields').style.display = 'block';
			document.getElementById('image-url-input').style.display = 'flex';
			photoButton.turnOn();
		} else {
			//close it
			// document.getElementById('timeline-post-action-inputfields').style.display = 'none';
			document.getElementById('image-url-input').style.display = 'none';
			photoButton.turnOff();
		}
	});

	document.getElementById('photo-input').addEventListener('blur', (e) => {
		let url = e.target.value;
		if (url === '' || url == null) return;
		fetchImage(url, (statusCode) => {
			if (statusCode >= 400) {
				console.log('Not a image url');
				return;
			}
			photoButton.turnOff();
			document.getElementById('timeline-post-action-inputfields').style.display = 'none';
			showImage(url);
			e.target.value = ``;
		});
	});

	document.getElementById('resource-input').addEventListener('blur', (e) => {
		let url = e.target.value;
		if (url === '' || url == null) return;
		if (!url.startsWith('http')) {
			alert('This is not a valid URL');
			return;
		}
		resourceButton.turnOff();
		document.getElementById('resource-url-input').style.display = 'none';
		showResource(url);
		e.target.value = ``;
	});

	function SwitchButton() {
		let isOn = true;

		this.turnOn = () => {
			isOn = true;
		};
		this.turnOff = () => {
			isOn = false;
		};
		this.toggle = () => {
			isOn = !isOn;
		};
		this.isOn = () => {
			return isOn === true;
		};
	}

	function Post() {
		let description = null;
		let lon = null;
		let lat = null;
		let image = null;
		let createdAt = Date.now();
		let username = mySPA.getState('user').userName;

		this.getUsername = () => {
			return username;
		};
		this.setUsername = (name) => {
			username = name;
		};
	}

	function saveImage(image) {
		let fileName = 'image';
		const link = document.createElement('a');
		link.download = fileName + '.jpeg';
		// link.href = URL.createObjectURL(image.src);
		link.href = image;
		link.click();
	}

	function toDataURL(src, callback, outputFormat = 'image/jpeg') {
		let img = new Image();
		img.crossOrigin = 'Anonymous';
		img.addEventListener('load', function() {
			let canvas = document.createElement('CANVAS');
			let ctx = canvas.getContext('2d');
			let dataURL;
			// let resolution = 480;
			// let ratio = this.naturalHeight / resolution;
			canvas.height = this.naturalHeight;
			canvas.width = this.naturalWidth;
			ctx.drawImage(this, 0, 0);
			ctx.drawImage(this, 0, 0, this.naturalWidth, this.naturalHeight);
			dataURL = canvas.toDataURL(outputFormat, 1);
			callback(dataURL);
		});
		img.src = src;
		if (img.complete || img.complete === undefined) {
			img.src = 'data:image/gif;base64,R0lGODlhAQABAIAAAAAAAP///ywAAAAAAQABAAACAUwAOw==';
			img.src = src;
		}
	}

	document.getElementById('share-action').addEventListener('click', (e) => {
		// let post = new Post();
		let description = document.getElementById('post-area').value;
		if (description.trim() === '') {
			alert('Write a description first');
			return;
		}

		e.target.innerHTML = `<div class="loading-spinner inverted visible small"></div>`;

		e.target.classList.add('disable');
		navigator.geolocation.getCurrentPosition(({ coords }) => {
			e.target.classList.remove('disable');
			e.target.innerHTML = `Share`;
			let { latitude, longitude } = coords;
			let user = mySPA.getState('user').userName;
			let description = document.getElementById('post-area').value;
			let image = '';
			if (document.getElementById('create-post-image') != null) {
				image = document.getElementById('create-post-image').src;
			}

			let resource_URL = '';
			if (document.getElementById('timeline-create-post-resource-container').children.length != 0) {
				resource_URL = document.getElementById('timeline-create-post-resource-container').children[0].innerText;
			}
			// let imageBase64 =  document.getElementById('create-post-image').src;
			let createdAt = new Date();

			if (image.startsWith('data:image/jpeg;')) {
				console.log('We have a base64 image');
			} else console.log('image from url');

			// toDataURL(imageURL,(base64)=>{
			//     console.log("NIce",base64);
			//     saveImage(base64);
			// });

			console.log('Username', user);
			console.log('Content', description);
			console.log('Image', image);
			console.log('resource_URL', resource_URL);
			// console.log('ImageBase64',imageBase64);
			console.log('Location', latitude, longitude);
			console.log('Created at', createdAt);

			let data = new FormData();

			data.append('username', user);
			data.append('description', description);
			data.append('resource_URL', resource_URL);
			if (image.startsWith('data:image/jpeg;')) {
				data.append('image_base64', image);
				data.append('image_URL', '');
			} else {
				data.append('image_URL', image);
				data.append('image_base64', '');
			}
			data.append('latitude', latitude);
			data.append('longitude', longitude);
			data.append('created_at', createdAt);

			ajaxRequest('POST', '/logbook/master?redirect=post', data, (res) => {
				console.log('POST', res);
			});
		});
	});
};
