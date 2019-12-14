'use strict';

const Post = (post) => {
	let date = new Date(post.createdAt);
	const NOMATISM_URL = `https://nominatim.openstreetmap.org/reverse`;

	let locationStr = '';
	let dateStr = '';
	let hasImageUrl = false;
	let hasImageBase64 = false;
	let hasResourceUrl = false;
	let imageSrc = '';

	//TODO: check for image url not to be C:\
	if (
		post.imageURL !== '' &&
		post.imageURL !== 'null' &&
		post.imageURL !== 'empty' &&
		post.imageURL !== 'empty_url' &&
		!post.imageURL.startsWith('C:')
	)
		hasImageUrl = true;
	if (
		post.imageBase64 !== '' &&
		post.imageBase64 !== 'null' &&
		post.imageBase64 !== 'empty' &&
		post.imageBase64 !== 'empty_url' &&
		!post.imageBase64.startsWith('C:')
	)
		hasImageBase64 = true;
	
		
	if(
		post.resourceURL !== '' &&
		post.resourceURL !== 'null' &&
		post.resourceURL !== 'empty' &&
		post.resourceURL.startsWith("http")
	)
		hasResourceUrl = true;

	console.log(post.resourceURL,hasResourceUrl);
	if (hasImageUrl) imageSrc = post.imageURL;
	if (hasImageBase64 && post.imageBase64.startsWith('data:image/jpeg;')) imageSrc = post.imageBase64;

	if (post.latitude && post.longitude) {
		ajaxRequest(
			'GET',
			`${NOMATISM_URL}?lat=${post.latitude}&lon=${post.longitude}&format=json`,
			null,
			({ address, error }) => {
				if (error) locationStr = '';
				if (address === null || address === undefined) locationStr = '';
				else {
					console.log(post.userName, address);
					if (address.country == undefined) locationStr = '';
					else if (address.city && address.country) locationStr = `${address.city}, ${address.country}`;
					else if (address.town && address.country) locationStr = `${address.town}, ${address.country}`;
					else if (address.county && address.country) locationStr = `${address.county}, ${address.country}`;
					else if (address.state === undefined && address.country) locationStr = address.country;
					else locationStr = `${address.state}, ${address.country}`;
				}

				if (locationStr) {
					document
						.getElementById(post.postID)
						.getElementsByClassName(
							'timeline-post-location'
						)[0].innerHTML = `<img src="./assets/location.svg"/>
                    <div style="text-overflow: ellipsis;white-space: nowrap;overflow: hidden;">${locationStr}</div>`;
				}
			}
		);
	}

	dateStr = dateToString(date);
	return `
        <div id="${post.postID}" class="timeline-post">
            <div class="timeline-post-profile">
                <div class="image-placeholder">
                 ${post.userName[0]}${post.userName[1]}
								</div>
								<div style="display:flex; justify-content:space-between;">
									<div>
											<div class="timeline-post-profile-name">${post.userName}</div>
											<div class="timeline-post-date">${dateStr}</div>
									</div>
									
									<div class="timeline-post-location">
									</div>
								</div>
                
            </div>
            <div class="timeline-post-content">
                ${post.description}
            </div>
            
						${imageSrc !== '' ? `<img class="timeline-post-image" src="${imageSrc}"/>` : ``}
						
						${hasResourceUrl?`
							<div class="timeline-create-post-resource-container" style="display:block">
								<a href=${post.resourceURL} target="_blank">${post.resourceURL}</a>
							</div>
						`:``}

            ${post.userName === mySPA.getState('user').userName
				? `<div class="timeline-user-post-actions">
                <div class="timeline-user-post-action delete-action">
                    Delete
                </div>
            </div>`
				: ``}
        </div>
    `;
};
