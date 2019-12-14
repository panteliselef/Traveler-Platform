function FilterButton() {
	let index = 0;
	let options = [ 'time', 'rating', 'location' ];

	this.getState = () => {
		return options[index];
	};

	this.toggleState = () => {
		if (index < 2) index++;
		else index = 0;
	};
}
const filterButton = () => {
	return `
    <div id="fbutton" class="filter-button">
      <svg style="width:24px;height:24px" viewBox="0 0 24 24">
        <path fill="#000000" d="M6,13H18V11H6M3,6V8H21V6M10,18H14V16H10V18Z" />
      </svg>

      <span></span>
    
    </div>
  `;
};

const sortAndShowForRating = (posts, length) => {
	posts.sort(function(a, b) {
		return b.avg - a.avg;
	});

	document.getElementById('timeline-posts').innerHTML = posts
		.map((post) => {
			return Post(post);
		})
		.join('');
	// mySPA.setState({ posts: result });
	setDeleteListeners(posts);

	posts.forEach((post) => {
    document
					.getElementById(post.postID)
					.getElementsByClassName('timeline-post-ratings-container')[0].innerHTML = RatingSystem(post.postID);
		showCreateComment(post.postID);
		if (post.avg === 0) {
			document
				.getElementById(post.postID)
				.getElementsByClassName('timeline-post-ratings-score')[0].innerText = `No Ratings`;
		} else {
			document
				.getElementById(post.postID)
				.getElementsByClassName('timeline-post-ratings-score')[0].innerText = `${post.avg.toFixed(
				1
			)} out of ${posts.length} Ratings`;
		}
	});

	console.warn('POSTS', posts);
};

const showFilterButton = (elem) => {
	let fButton = new FilterButton();

	elem.insertAdjacentHTML('afterend', filterButton());

	document.getElementById('fbutton').children[1].innerText = fButton.getState();

	document.getElementById('fbutton').addEventListener('click', (e) => {
		fButton.toggleState();
		document.getElementById('fbutton').children[1].innerText = fButton.getState();
		let currentState = fButton.getState();

		let posts = document.getElementById('timeline-posts');

		// if (currentState === 'time') {
		// 	ajaxRequest(
		// 		'GET',
		// 		'/logbook/master?redirect=post&mode=top_ten',
		// 		null,
		// 		({ message, statusCode, result }) => {
		// 			if (statusCode === 200) {
		// 				posts.innerHTML = result
		// 					.map((post) => {
		// 						return Post(post);
		// 					})
		// 					.join('');
		// 				mySPA.setState({ posts: result });
		// 				setDeleteListeners(result);

		// 				result.forEach((post) => {
		// 					showCreateComment(post.postID);
		// 				});
		// 			}
		// 		}
		// 	);
		// } else if (currentState === 'rating') {
		// 	ajaxRequest(
		// 		'GET',
		// 		'/logbook/master?redirect=post&mode=top_ten_byRating',
		// 		null,
		// 		({ message, statusCode, result }) => {
		// 			if (statusCode === 200) {
		// 				posts.innerHTML = result
		// 					.map((post) => {
		// 						return Post(post);
		// 					})
		// 					.join('');
		// 				mySPA.setState({ posts: result });
		// 				setDeleteListeners(result);

		// 				result.forEach((post) => {
		// 					showCreateComment(post.postID);
		// 				});
		// 			}
		// 		}
		// 	);
		// }
		ajaxRequest('GET', '/logbook/master?redirect=post&mode=top_ten', null, ({ message, statusCode, result }) => {
			if (statusCode === 200) {
				if (currentState === 'time') {
          showPostsOnTimeline();
					// posts.innerHTML = result
					// 	.map((post) => {
					// 		return Post(post);
					// 	})
					// 	.join('');
					// mySPA.setState({ posts: result });
					// setDeleteListeners(result);

					// result.forEach((post) => {
					// 	showCreateComment(post.postID);
					// });
				} else if (currentState === 'location') {
				} else {
					let pairs = [];
					let invalidPairs = [];
					let posts = result;
					result.forEach((post, index) => {
						ajaxRequest('GET', `/logbook/rating?postID=${post.postID}`, null, ({ result }) => {
							let avg = 0;
							result.forEach((rating) => {
								avg += rating.rate;
							});
							avg = avg / result.length;
							if (!isNaN(avg)) {
								// console.log("Is not NAN",avg);
								post.avg = avg;
								// pairs.push({ postID: post.postID, avg });
							} else {
								post.avg = 0;
								// invalidPairs.push({ postID: post.postID});
							}
							sortAndShowForRating(posts, index);
						});
					});
					document.getElementById('timeline-posts').innerHTML = '';
				}
			}
		});
	});
};
