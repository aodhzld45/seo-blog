// 삭제 기능
const deleteButton = document.getElementById('delete-btn');

if (deleteButton) {
    deleteButton.addEventListener('click', event => {
        alert('삭제 버튼 클릭됨');
        let ano = document.getElementById('article-ano').value;
         fetch(`/api/articles/${ano}` , {
             method: 'DELETE'
         })
         .then(() => {
             alert('삭제가 완료되었습니다.');
             location.replace('/articles');
         });
    });
}

// 수정 기능
const modifyButton = document.getElementById('modify-btn');

if (modifyButton) {
/* 클릭 이벤트가 발생하면 수정 API 요청*/
    modifyButton.addEventListener('click', event => {
        alert('수정 버튼 클릭됨');
        let params = new URLSearchParams(location.search);
        let ano = params.get('ano');

        fetch(`/api/articles/${ano}` ,{
            method: 'PUT',
            headers: {
                "Content-Type" : "application/json",
            },
            body: JSON.stringify({
                title: document.getElementById('title').value,
                content: document.getElementById('content').value
            })
        })
        .then(() => {
            alert('수정이 완료되었습니다.');
            location.replace(`/articles/${ano}`);
        });

    });
}

// 생성 기능
const createButton = document.getElementById('create-btn');

if (createButton) {
    createButton.addEventListener('click', event => {
        fetch('/api/articles', {
            method: 'POST',
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify({
                title: document.getElementById('title').value,
                content: document.getElementById('content').value,
            }),
        })
            .then(() => {
                alert('등록 완료되었습니다.');
                location.replace('/articles');
            });
    });
}


