'use strict';
const TimelinePage = (currentUser) => {
	return `
    <div id="dashboard-container" class="dashboard-container">
    <div class="header">
      <div id="header-username">${currentUser.userName}</div>
      <ul class="header-options">
        <li id="btnLogout">Logout</li>
      </ul>
    </div>
<!--    <div class="timeline-modal-view">-->
<!--        <div id="timeline-modal-close">X</div>-->
<!--        <div id="timeline-post" style="display: flex">-->
<!--        -->
<!--        </div>-->
<!--        <div class="timeline-modal-view-post timeline-post">-->
<!--            <div class="timeline-post-profile">-->
<!--                <div class="image-placeholder">-->
<!--                 PE-->
<!--                </div>-->
<!--                <div>-->
<!--                    <div class="timeline-post-profile-name">elefcodes</div>-->
<!--                    <div class="timeline-post-date">Yesterday</div>-->
<!--                </div>-->
<!--                <div class="timeline-post-location">-->
<!--                    <img src="./assets/location.svg"/>-->
<!--                    <div style="text-overflow: ellipsis;white-space: nowrap;overflow: hidden;">Greece</div>-->
<!--                </div>-->
<!--            </div>-->
<!--            <div class="timeline-post-content">-->
<!--                test me datafsdsdf test me datafsdsdf test me datafsdsdf test me datafsdsdf test me datafsdsdf test me datafsdsdf test me datafsdsdf test me datafsdsdf test me datafsdsdf test me datafsdsdf-->
<!--            </div>-->
<!--            <img class="timeline-post-image" src="https://images.unsplash.com/photo-1575859225486-f377a3f867bf?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=634&q=80"/>-->
<!--        </div>-->
<!--    </div>-->
    <div class="timeline-layout">
      <aside>
        <div style="position: sticky;top:15px" id="timeline-users">
            <div style="font-weight: 500;font-size: 1.2em;padding-left: 1rem">All users</div>
            <div id="timeline-users-list" class="timeline-users-list">
            </div>
        </div>
      </aside>
      <main>
        <div id="timeline-posts" class="timeline-posts">
<!--            <div class="timeline-post">-->
<!--                <div class="timeline-post-profile">-->
<!--                    <div class="image-placeholder">-->
<!--                     PE-->
<!--                    </div>-->
<!--                    <div>-->
<!--                        <div class="timeline-post-profile-name">Pantelis Elef</div>-->
<!--                        <div class="timeline-post-date">34 minutes ago</div>-->
<!--                    </div>-->
<!--                    <div class="timeline-post-location">-->
<!--                        <img src="./assets/location.svg"/>-->
<!--                        <div>Heraklion, Greece</div>-->
<!--                    </div>-->
<!--                </div>-->
<!--                <div class="timeline-post-content">-->
<!--                    Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s.-->
<!--                </div>-->
<!--                <img class="timeline-post-image" src="https://images.unsplash.com/photo-1575535135032-0045eaea7d62?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=934&q=80"/>-->
<!--            </div>-->
        </div>
      </main>
      <aside>
        <div style="position: sticky;top: 20px;margin .5rem 0">
            <div onclick="mySPA.goToEditPage();" class="cta">Edit Profile</div>
            <div onclick="mySPA.goToProfilePage('${currentUser.userName}');" class="cta">Show Profile</div>
        </div>
      </aside>
    </div>
    </div>
    `;
};

const ModalPost = (post, image, date, location) => {
	return `
    <div class="timeline-modal-view-post timeline-post">
        <div class="timeline-post-profile">
            <div class="image-placeholder">
                ${post.userName[0]}${post.userName[1]}
            </div>

            <div style="display:flex; justify-content:space-between;">
                <div>
                    <div class="timeline-post-profile-name">${post.userName}</div>
                    <div class="timeline-post-date">${date}</div>
                </div>
                
                ${location != null
					? `
                    <div class="timeline-post-location" style="max-width:300px;">
                        <img src="./assets/location.svg"/>
                        <div style="text-overflow: ellipsis;white-space: nowrap;overflow: hidden;">${location.locationStr}</div>
                    </div>
                `
					: ``}
            </div>
            
        </div>
        <div class="timeline-post-content">
            ${post.description}
        </div>
        ${image != null
			? `
            <img class="timeline-post-image" src="${image}"/>
        `
      : ``}
      
        <div class="timeline-post-comments-container">
          
        </div>
    </div>
    ${location != null ? `<div class="timeline-modal-view-map"></div>` : ``}
    `;
};

const PostPlaceholder = () => {
  return `
    <div class="timeline-post facebook-animation">
      <div class="container">
        <div id="one"></div>
        <div id="two"></div>
        <div id="three"></div>
      </div>
      <div id="four"></div>
      <div id="five"></div>
      <div id="six"></div>
    </div>
`;
}

const showTimelinePage = () => {
	let user = mySPA.getState('user');
	document.getElementsByTagName('body')[0].style.background = '#fbfbfb';

	const rootDiv = document.getElementById('root');
    rootDiv.innerHTML = TimelinePage(user);
    
    let posts = document.getElementById('timeline-posts');

    posts.innerHTML = `${PostPlaceholder().repeat(3)}`

  showCreatPost(document.getElementsByClassName('timeline-layout')[0].children[1]);
  
  showFilterButton(document.getElementsByClassName("timeline-create-post")[0]);


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
    
  showPostsOnTimeline();

	// document.getElementById('timeline-modal-close').addEventListener('click',()=>{
	//     document.getElementsByClassName("timeline-modal-view")[0].style.display = 'none';
	//     document.getElementById('timeline-post').innerHTML = '';
	// })

	let data = new FormData();
	let currentUser = mySPA.getState('user');
	data.append('username', currentUser.userName);
	data.append('password', currentUser.password);
	ajaxRequest('POST', 'http://localhost:8080/logbook/master?redirect=getUsers', data, ({ result }) => {
		if (!result) return;
		document.getElementById('timeline-users-list').innerHTML = '';
		let str = '';
		let users = result
			.map((user) => {
				// console.warn("User",user);
				str += `<a href="/logbook/#/profile/${user.userName}">${user.userName}</a>`;
				mySPA.setState({ [user.userName]: user });
			})
			.join('');

		document.getElementById('timeline-users-list').innerHTML = str;
	});
};
