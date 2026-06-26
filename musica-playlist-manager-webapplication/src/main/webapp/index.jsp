<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Musica - Project Skeleton</title>
    <style>
        /* Primitive Black and White Styling */
        body {
            font-family: monospace;
            background-color: #ffffff;
            color: #000000;
            margin: 0;
            padding: 0;
            display: flex;
            flex-direction: column;
            height: 100vh;
        }

        header {
            padding: 20px;
            border-bottom: 1px solid #000;
        }

        h1, h2, h3 {
            text-transform: uppercase;
            margin-top: 0;
        }

        .container {
            display: flex;
            flex: 1;
            overflow: hidden; 
        }

        .sidebar {
            width: 200px;
            border-right: 1px solid #000000;
            padding: 15px;
            overflow-y: auto;
        }

        .sidebar p a {
            text-decoration: none;
            color: #000;
        }
        
        .sidebar p a:hover {
            text-decoration: underline;
        }

        /* The Main Content Area for Fragments */
        .content-section {
            flex-grow: 1;
            padding: 15px;
            overflow-y: auto;
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

        /* Fixed Bottom Player Strip */
        .player-bar-fixed {
            border-top: 1px solid #000000;
            padding: 15px;
            display: flex;
            justify-content: space-between;
            align-items: center;
            background-color: #fff;
            position: fixed;
            bottom: 0;
            left: 0;
            right: 0;
            height: 60px; /* Fixed height for player */
        }
        
        /* Add margin to bottom of container so content isn't hidden behind player */
        .container {
            margin-bottom: 90px; 
        }

        .progress-container {
            display: flex;
            align-items: center;
            flex: 1;
            margin: 0 20px;
        }
        
        .progress-container input[type="range"] {
            flex: 1;
            margin: 0 10px;
        }
        
        .player-info {
            min-width: 200px;
        }

    </style>
</head>
<body>

    <header>
        <h1>Musica - System Console (Week 3 AJAX)</h1>
    </header>

    <div class="container">
        <!-- sidebar nav -->
        <nav class="sidebar">
            <h3>Menu</h3>
            <p><a href="#" id="nav-library" onclick="loadSection('library'); return false;">[01] Library</a></p>
            <p><a href="#" id="nav-playlist" onclick="loadSection('playlist'); return false;">[02] Playlists</a></p>
            <p><a href="#" id="nav-history" onclick="loadSection('history'); return false;">[03] History</a></p>
        </nav>

        <!-- Main Directory Area (Dynamic Fragment Loading) -->
        <main id="content-section" class="content-section">
            <div style="text-align: center; padding: 20px;">Đang tải thư viện...</div>
            <!-- Fragments will be injected here via AJAX -->
        </main>
    </div>

    <!-- Fixed Bottom Player Panel -->
    <footer class="player-bar-fixed">
        <div class="player-info">
            <strong>Status: </strong> <span id="player-status">Idle</span><br>
            <strong>Track: </strong> <span id="current-track">None</span> - <span id="current-artist">None</span>
        </div>
        
        <div class="progress-container">
            <span id="time-current">0:00</span>
            <input type="range" id="progress-bar" min="0" max="100" value="0">
            <span id="time-total">0:00</span>
        </div>

        <div>
            <button id="btn-shuffle" onclick="controlPlayer('shuffle')">[Shuffle]</button>
            <button onclick="controlPlayer('prev')">[Prev]</button>
            <button onclick="togglePlay()">[Play/Pause]</button>
            <button onclick="controlPlayer('next')">[Next]</button>
        </div>
    </footer>

    <!-- Define context path for JS -->
    <script>
        window.contextPath = "${pageContext.request.contextPath}";
    </script>
    <!-- Load JS scripts -->
    <script src="${pageContext.request.contextPath}/assets/js/navigation.js?v=<%= System.currentTimeMillis() %>"></script>
    <script src="${pageContext.request.contextPath}/assets/js/player.js?v=<%= System.currentTimeMillis() %>"></script>
</body>
</html>
