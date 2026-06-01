# Musica - Playlist Manager (Week 2 Skeleton)

## Current Progress (30%)
*   **MVC Architecture**: Setup simplified packages (`model`, `dsa`, `service`, `controller`).
*   **Custom DSA (in-memory)**:
    *   `CircularLinkedList`: Single circular list for repeating tracks.
    *   `MyBSTree`: Binary Search Tree for alphabetical sorting/searching.
    *   `HistoryStack`: LIFO Stack for managing song playback history.
*   **Web Interface**: Minimalist HTML/JSP view linked with `LibraryServlet` and `PlayerServlet`.
*   **Data Source**: Switched to in-memory hardcoded demo songs (database connection is disabled for now).

## Teammate Notes
*   **Run Project**: Open in NetBeans -> Right-click -> Clean and Build -> Run (using Tomcat).
*   **Next Tasks**:
    1. Setup SQL Server database.
    2. Implement `DBContext.java` and `SongDAO.java` to load songs from database.
    3. Add physical audio playback using HTML5 `<audio>` tag linked to actual `.mp3` files.
    4. Implement dynamic random shuffle in `PlaylistManager` using `shuffleList`.
