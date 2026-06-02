package controller;

import model.Song;
import service.PlaybackController;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "PlayerServlet", urlPatterns = {"/player"})
public class PlayerServlet extends HttpServlet {

    private final PlaybackController playbackController = PlaybackController.getInstance();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String action = request.getParameter("action");
        String songId = request.getParameter("songId");
        
        Song currentSong = null;

        if (action != null) {
            if (action.equals("play")) {
                Song songToPlay = playbackController.searchSongInLibrary(songId); 
                playbackController.playSong(songToPlay);
                currentSong = songToPlay;
            } else if (action.equals("next")) {
                currentSong = playbackController.nextTrack();
            } else if (action.equals("prev")) {
                currentSong = playbackController.prevTrack();
            }
        }

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        
        if (currentSong != null) {
            String json = String.format(
                "{\"status\":\"success\", \"title\":\"%s\", \"artist\":\"%s\", \"filePath\":\"%s\"}",
                currentSong.getTitle(), currentSong.getArtist(), currentSong.getFilePath()
            );
            response.getWriter().write(json);
        } else {
            response.getWriter().write("{\"status\":\"idle\"}");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}
