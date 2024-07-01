//app.js
const apiUrl = '/upload'; // 백엔드 API 엔드포인트

function uploadImage() {
    const fileInput = document.getElementById('fileInput');
    const file = fileInput.files[0];

    if (file) {
        const formData = new FormData();
        formData.append('file', file);

        fetch(apiUrl, {
            method: 'POST',
            body: formData
        })
            .then(response => response.json())
            .then(data => {
                console.log('이미지 업로드 성공❤️', data.url);
            })
            .catch(error => {
                console.error('❌에러발생❌', error);
            });
    } else {
        console.warn('이미지 파일 필요!⛔️');
    }
}