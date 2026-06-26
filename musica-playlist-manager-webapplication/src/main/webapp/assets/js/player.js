const audioPlayer = new Audio();
let isPlaying = false;
let isShuffle = false;

// Cache DOM elements (các phần tử cố định, không nằm trong vùng dynamic content)
const progressBar = document.getElementById('progress-bar');
const timeCurrent = document.getElementById('time-current');
const timeTotal = document.getElementById('time-total');

// Định dạng thời gian giây sang "m:ss" (ví dụ: 2:00, 3:45)
function formatTime(secs) {
    if (isNaN(secs) || secs < 0) return '0:00';
    const totalSeconds = Math.floor(secs);
    const minutes = Math.floor(totalSeconds / 60);
    const seconds = totalSeconds % 60;
    return minutes + ':' + (seconds < 10 ? '0' : '') + seconds;
}

// 1. Phát một bài hát cụ thể theo ID (gọi khi click nút [Play] trên danh sách)
function playSong(songId, title, artist) {
    fetch('player?action=play&songId=' + encodeURIComponent(songId))
        .then(response => response.json())
        .then(data => {
            if (data.status === 'success') {
                document.getElementById('player-status').innerText = 'Playing';
                document.getElementById('current-track').innerText = title || data.title;
                document.getElementById('current-artist').innerText = artist || data.artist;
                
                // Reset thanh tiến trình
                progressBar.value = 0;
                progressBar.max = data.duration || 100;
                timeCurrent.innerText = '0:00';
                timeTotal.innerText = formatTime(data.duration);
                
                // Khởi tạo audio và tự động phát
                audioPlayer.src = data.filePath;
                isPlaying = true;
                audioPlayer.play().catch(err => console.log("Lỗi phát nhạc: " + err));
            }
        });
}

// 2. Chuyển sang bài tiếp theo (Next) hoặc lùi bài (Prev)
function controlPlayer(action) {
    if (action === 'shuffle') {
        toggleShuffle();
        return;
    }

    fetch('player?action=' + action)
        .then(response => response.json())
        .then(data => {
            if (data.status === 'success') {
                document.getElementById('current-track').innerText = data.title;
                document.getElementById('current-artist').innerText = data.artist;
                
                // Reset thanh tiến trình cho bài mới
                progressBar.value = 0;
                progressBar.max = data.duration || 100;
                timeCurrent.innerText = '0:00';
                timeTotal.innerText = formatTime(data.duration);
                
                // Đổi nguồn nhạc bài mới
                audioPlayer.src = data.filePath;
                
                // Nếu đang phát thì tiếp tục phát, nếu đang pause thì giữ pause
                if (isPlaying) {
                    document.getElementById('player-status').innerText = 'Playing';
                    audioPlayer.play().catch(err => console.log("Lỗi phát nhạc: " + err));
                } else {
                    document.getElementById('player-status').innerText = 'Paused';
                }
            } else {
                // Trạng thái dừng phát (Idle)
                document.getElementById('player-status').innerText = 'Idle';
                document.getElementById('current-track').innerText = 'None';
                document.getElementById('current-artist').innerText = 'None';
                audioPlayer.src = '';
                isPlaying = false;
                
                progressBar.value = 0;
                progressBar.max = 100;
                timeCurrent.innerText = '0:00';
                timeTotal.innerText = '0:00';
            }
        });
}

// 3. Tạm dừng / Phát tiếp (Play / Pause)
function togglePlay() {
    if (!audioPlayer.src || audioPlayer.src === window.location.href) {
        // Nếu chưa chọn bài nào, phát bài đầu tiên trong bảng thư viện
        const firstPlayBtn = document.querySelector('table button');
        if (firstPlayBtn) {
            firstPlayBtn.click();
        }
        return;
    }

    if (isPlaying) {
        audioPlayer.pause();
        document.getElementById('player-status').innerText = 'Paused';
        isPlaying = false;
    } else {
        audioPlayer.play().then(() => {
            document.getElementById('player-status').innerText = 'Playing';
            isPlaying = true;
        }).catch(err => console.log("Lỗi phát nhạc: " + err));
    }
}

// 4. Bật / Tắt Shuffle
function toggleShuffle() {
    isShuffle = !isShuffle;
    const btn = document.getElementById('btn-shuffle');
    if (isShuffle) {
        btn.style.color = '#fff';
        btn.style.backgroundColor = '#000';
    } else {
        btn.style.color = '';
        btn.style.backgroundColor = '';
    }
}

// --- Xử lý sự kiện Audio Player thực tế ---

// Tự động chạy thanh tiến trình theo nhạc
audioPlayer.addEventListener('timeupdate', () => {
    if (audioPlayer.duration && !isNaN(audioPlayer.duration)) {
        progressBar.value = Math.floor(audioPlayer.currentTime);
        timeCurrent.innerText = formatTime(audioPlayer.currentTime);
    }
});

// Cập nhật tổng thời lượng bài hát khi load xong metadata (giá trị THỰC từ file audio)
audioPlayer.addEventListener('loadedmetadata', () => {
    if (audioPlayer.duration && !isNaN(audioPlayer.duration)) {
        progressBar.max = Math.floor(audioPlayer.duration);
        timeTotal.innerText = formatTime(audioPlayer.duration);
    }
});

// Tự động chuyển bài tiếp theo khi hết bài
audioPlayer.addEventListener('ended', () => {
    isPlaying = true; 
    controlPlayer('next');
});

// Tua nhạc khi người dùng kéo/tua trên thanh tiến trình
progressBar.addEventListener('input', (e) => {
    if (audioPlayer.src && audioPlayer.duration && !isNaN(audioPlayer.duration)) {
        const newTime = parseInt(e.target.value);
        audioPlayer.currentTime = newTime;
        timeCurrent.innerText = formatTime(newTime);
    }
});
