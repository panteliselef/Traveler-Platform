const CreateComment = (postId) => {
	return `
  
    <div class="timeline-comments-create">

      <input type="text" placeholder="Write a comment ..."/>
      <div class="timeline-comments-button-container">
        <svg style="width:24px;height:24px" viewBox="0 0 24 24">
            <path fill="#17bebb" d="M2,21L23,12L2,3V10L17,12L2,14V21Z" />
        </svg>
      </div>
    </div>

  `;
};


const showCreateComment = (postId) => {

  document.getElementById(postId).getElementsByClassName('timeline-post-comments-container')[0].innerHTML = CreateComment(postId);

  document.getElementById(postId).getElementsByClassName('timeline-comments-button-container')[0].addEventListener('click',(e)=>{

    let comment = document.getElementById(postId).getElementsByTagName('input')[0].value
    if (!comment) return;


    let data = new FormData();

    data.append('userName',mySPA.getState('user').userName);
    data.append('postID',postId);
    data.append('comment',comment);
    data.append('createdAt',new Date());
    ajaxRequest('POST','/logbook/comments',data,({statusCode,message,result})=>{
      if(statusCode!=200)return;
      console.log(result);
      document.getElementById(postId).getElementsByTagName('input')[0].value = '';
    })

  })

  

}