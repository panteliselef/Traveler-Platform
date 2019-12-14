const RatingSystem = (postID) => {
	return `
    <div class="timeline-post-ratings-score"></div>
    <div class="timeline-post-ratings-options">
      <div id="option-1-${postID}" class="timeline-post-ratings-option">1</div>
      <div id="option-2-${postID}" class="timeline-post-ratings-option">2</div>
      <div id="option-3-${postID}" class="timeline-post-ratings-option">3</div>
      <div id="option-4-${postID}" class="timeline-post-ratings-option">4</div>
      <div id="option-5-${postID}" class="timeline-post-ratings-option">5</div>
    </div>
  `;
};
