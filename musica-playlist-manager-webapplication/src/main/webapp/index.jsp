<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="model.Song" %>
<%@ page import="java.util.List" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Musica - Project Skeleton (30% Complete)</title>
    <style>
        /* Primitive Black and White Styling */
        body {
            font-family: monospace;
            background-color: #ffffff;
            color: #000000;
            margin: 20px;
        }

        h1, h2, h3 {
            text-transform: uppercase;
        }

        .container {
            display: flex;
            gap: 20px;
        }

        .sidebar {
            width: 200px;
            border: 1px solid #000000;
            padding: 15px;
        }

        .main-content {
            flex-grow: 1;
            border: 1px solid #000000;
            padding: 15px;
        }

        .search-box {
            margin-bottom: 20px;
            padding: 5px;
        }

        table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 15px;
        }

        table, th, td {
            border: 1px solid #000000;
        }

        th, td {
            padding: 8px;
            text-align: left;
        }

        button {
            font-family: monospace;
            background-color: #ffffff;
            border: 1px solid #000000;
            padding: 5px 10px;
            cursor: pointer;
        }

        button:hover {
            background-color: #000000;
            color: #ffffff;
        }

        /* Basic Bottom Player Strip */
        .player-panel {
            margin-top: 20px;
            border: 1px solid #000000;
            padding: 15px;
            display: flex;
            justify-content: space-between;
            align-items: center;
        }
    </style>
</head>
<body>

    <h1>Musica - System Console (Week 2 Draft)</h1>
    <hr style="border: 1px solid #000000; margin-bottom: 20px;">

    <div class="container">
        <!-- sidebar nav -->
        <div class="sidebar">
            <h3>Menu</h3>
            <p><a href="library" style="color: #000;">[01] Library</a></p>
            <p><a href="#" style="color: #000;">[02] Playlists</a></p>
            <p><a href="#" style="color: #000;">[03] History</a></p>
        </div>

        <!-- Main Directory Area -->
        <div class="main-content">
            <h2>Track Library</h2>
            
            <div class="search-box">
                <form action="library" method="get">
                    <label>Search Song Title: </label>
                    <input type="text" name="search" style="font-family: monospace;">
                    <button type="submit">Query</button>
                </form>
            </div>

            <table>
                <thead>
                    <tr>
                        <th>Action</th>
                        <th>ID</th>
                        <th>Title</th>
                        <th>Artist</th>
                        <th>Duration (Sec)</th>
                    </tr>
                </thead>
                <tbody>
                    <% 
                        List<Song> songs = (List<Song>) request.getAttribute("songs");
                        if (songs != null && !songs.isEmpty()) {
                            for (Song song : songs) {
                    %>
                    <tr>
                        <td>
                            <button onclick="playSong('<%= song.getTitle() %>')">[Play]</button>
                        </td>
                        <td><%= song.getId() %></td>
                        <td><%= song.getTitle() %></td>
                        <td><%= song.getArtist() %></td>
                        <td><%= song.getDuration() %>s</td>
                    </tr>
                    <% 
                            }
                        } else {
                    %>
                    <tr>
                        <td colspan="5" style="text-align: center;">No tracks found. <a href="library">Reload</a></td>
                    </tr>
                    <% 
                        } 
                    %>
                </tbody>
            </table>
        </div>
    </div>

    <!-- Basic Player Panel -->
    <div class="player-panel">
        <div>
            <strong>Status: </strong> <span id="player-status">Idle</span><br>
            <strong>Track: </strong> <span id="current-track">None</span> - <span id="current-artist">None</span>
        </div>
        <div>
            <button onclick="controlPlayer('prev')">[Prev]</button>
            <button onclick="togglePlay()">[Play/Pause]</button>
            <button onclick="controlPlayer('next')">[Next]</button>
        </div>
    </div>


    <script>
        let isPlaying = false;

        function playSong(songTitle) {
            fetch('player?action=play&songId=' + encodeURIComponent(songTitle))
                .then(response => response.json())
                .then(data => {
                    if (data.status === 'success') {
                        document.getElementById('player-status').innerText = 'Playing';
                        document.getElementById('current-track').innerText = data.title;
                        document.getElementById('current-artist').innerText = data.artist;
                        isPlaying = true;
                    }
                });
        }

        function controlPlayer(action) {
            fetch('player?action=' + action)
                .then(response => response.json())
                .then(data => {
                    if (data.status === 'success') {
                        document.getElementById('player-status').innerText = 'Playing';
                        document.getElementById('current-track').innerText = data.title;
                        document.getElementById('current-artist').innerText = data.artist;
                        isPlaying = true;
                    } else {
                        document.getElementById('player-status').innerText = 'Idle';
                        document.getElementById('current-track').innerText = 'None';
                        document.getElementById('current-artist').innerText = 'None';
                        isPlaying = false;
                    }
                });
        }

        function togglePlay() {
            if (isPlaying) {
                document.getElementById('player-status').innerText = 'Paused';
                isPlaying = false;
            } else {
                document.getElementById('player-status').innerText = 'Playing';
                isPlaying = true;
            }
        }
    </script>
</body>
</html>
