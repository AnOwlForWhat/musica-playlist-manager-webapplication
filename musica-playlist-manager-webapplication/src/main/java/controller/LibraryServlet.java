package controller;

import model.Song;
import service.PlaybackController;

import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "LibraryServlet", urlPatterns = {"/library"})
public class LibraryServlet extends HttpServlet {
    private final PlaybackController playbackController = PlaybackController.getInstance();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String searchQuery = request.getParameter("search");
        List<Song> songList;
        if (searchQuery != null && !searchQuery.trim().isEmpty()) {
            songList = playbackController.searchSongsByTitle(searchQuery);
        } else {
            songList = playbackController.getSortedLibrary();
        }
        if (playbackController.playlistManager.isEmpty() && songList != null) {
            for (Song s : songList) {
                playbackController.addSongToPlaylist(s);
            }
        }
        request.setAttribute("songs", songList);
        request.getRequestDispatcher("index.jsp").forward(request, response);
    }
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}
