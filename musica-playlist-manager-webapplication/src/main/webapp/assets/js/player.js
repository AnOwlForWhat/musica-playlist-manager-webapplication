let isPlaying = false;
let isShuffle = false;

// Simulated logic since actual audio file isn't hooked up to JS Audio API yet
// But we'll build out the UI logic as requested
function playSong(songId, title, artist) {
    fetch('player?action=play&songId=' + encodeURIComponent(songId))
        .then(response => response.json())
        .then(data => {
            if (data.status === 'success') {
                document.getElementById('player-status').innerText = 'Playing';
                document.getElementById('current-track').innerText = title || data.title;
                document.getElementById('current-artist').innerText = artist || data.artist;
                isPlaying = true;
                
                // Demo progress bar animation
                startDemoProgress();
            }
        });
}

function controlPlayer(action) {
    if (action === 'shuffle') {
        toggleShuffle();
        return;
    }

    fetch('player?action=' + action)
        .then(response => response.json())
        .then(data => {
            if (data.status === 'success') {
                document.getElementById('player-status').innerText = 'Playing';
                document.getElementById('current-track').innerText = data.title;
                document.getElementById('current-artist').innerText = data.artist;
                isPlaying = true;
                startDemoProgress();
            } else {
                document.getElementById('player-status').innerText = 'Idle';
                document.getElementById('current-track').innerText = 'None';
                document.getElementById('current-artist').innerText = 'None';
                isPlaying = false;
                stopDemoProgress();
            }
        });
}

function togglePlay() {
    if (isPlaying) {
        document.getElementById('player-status').innerText = 'Paused';
        isPlaying = false;
        stopDemoProgress();
    } else {
        document.getElementById('player-status').innerText = 'Playing';
        isPlaying = true;
        startDemoProgress();
    }
}

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

// --- Demo Progress Bar Logic ---
let demoInterval;
const progressBar = document.getElementById('progress-bar');
const timeCurrent = document.getElementById('time-current');

function startDemoProgress() {
    if (demoInterval) clearInterval(demoInterval);
    demoInterval = setInterval(() => {
        if (!isPlaying) return;
        let val = parseInt(progressBar.value);
        if (val < 100) {
            val += 1;
            progressBar.value = val;
            timeCurrent.innerText = `0:${val < 10 ? '0' + val : val}`; // fake time
        } else {
            progressBar.value = 0;
            controlPlayer('next'); // go to next when finished
        }
    }, 1000);
}

function stopDemoProgress() {
    if (demoInterval) clearInterval(demoInterval);
}

if(progressBar) {
    progressBar.addEventListener('input', (e) => {
        // User is seeking manually
        const val = e.target.value;
        timeCurrent.innerText = `0:${val < 10 ? '0' + val : val}`;
    });
}
