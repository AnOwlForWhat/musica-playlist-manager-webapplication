<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="model.Song" %>
<%@ page import="java.util.List" %>

<h2>Listening History</h2>

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
            <td colspan="5" style="text-align: center;">History is empty. Play some songs from the Library!</td>
        </tr>
        <% 
            } 
        %>
    </tbody>
</table>
