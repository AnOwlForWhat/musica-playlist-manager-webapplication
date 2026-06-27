<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="model.Song" %>
<%@ page import="java.util.List" %>

<h2>Track Library</h2>

<div class="search-box">
    <!-- Using javascript to search without full reload -->
    <form action="#" onsubmit="searchLibrary(event)">
        <label>Search Song Title: </label>
        <input type="text" id="search-input" name="search" style="font-family: monospace;">
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
                <button onclick="playSong('<%= song.getId() %>', '<%= song.getTitle().replace("'", "\\'") %>', '<%= song.getArtist().replace("'", "\\'") %>')">[Play]</button>
                <button onclick="addToPlaylist('<%= song.getId() %>')">[+] Playlist</button>
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
            <td colspan="5" style="text-align: center;">No tracks found. <a href="#" onclick="loadSection('library'); return false;">Reload</a></td>
        </tr>
        <% 
            } 
        %>
    </tbody>
</table>
