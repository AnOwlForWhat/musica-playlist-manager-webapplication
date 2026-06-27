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

@WebServlet(name = "PageLoaderServlet", urlPatterns = {"/page"})
public class PageLoaderServlet extends HttpServlet {
    private final PlaybackController playbackController = PlaybackController.getInstance();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String view = request.getParameter("view");
        String fragmentPath = "";

        if ("playlist".equals(view)) {
            List<Song> playlistSongs = playbackController.playlistManager.playlist.toList();
            request.setAttribute("songs", playlistSongs);
            fragmentPath = "/WEB-INF/fragments/playlist-content.jsp";
        } else if ("history".equals(view)) {
            List<Song> historySongs = playbackController.historyStack.toList();
            request.setAttribute("songs", historySongs);
            fragmentPath = "/WEB-INF/fragments/history-content.jsp";
        } else {
            // Default to library
            String searchQuery = request.getParameter("search");
            List<Song> songList;
            if (searchQuery != null && !searchQuery.trim().isEmpty()) {
                songList = playbackController.searchSongsByTitle(searchQuery);
            } else {
                songList = playbackController.getSortedLibrary();
            }
            request.setAttribute("songs", songList);
            fragmentPath = "/WEB-INF/fragments/library-content.jsp";
        }

        request.getRequestDispatcher(fragmentPath).include(request, response);
    }
}
