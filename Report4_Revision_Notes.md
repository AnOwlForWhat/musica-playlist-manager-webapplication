# Báo cáo: Các điểm cần cập nhật cho Report 4 (Mini Project Final Submission)

Bản nháp Report 4 hiện tại được viết cực kỳ xuất sắc, bám sát chuẩn IEEE và có phần phân tích "Human Delta" rất thuyết phục. 

Tuy nhiên, bản nháp này đang sử dụng thông tin từ phiên bản code **CŨ**. Chúng ta vừa thực hiện một bản update code (tối ưu hóa `nextTrack()` từ O(N) xuống O(1)). Do đó, cần phải cập nhật lại nội dung báo cáo để ghi nhận thành quả này, tránh việc code một đằng báo cáo một nẻo.

Vui lòng nhờ người viết Report điều chỉnh lại 5 điểm sau:

### 1. Mục 2.3 Playback Logic (Phần Bảng)
- **Nội dung cũ:** Normal | both false | do-while traversal of CircularLinkedList to find current song, returns p.next.info — **O(N) worst case**
- **Sửa thành:** Normal | both false | Uses `currentPlaylistNode.next` directly to jump to the next node — **O(1)**

### 2. Mục 4.1 Feature completion (Phần Bảng)
- **Nội dung cũ:** Play next — normal mode | Complete | do-while CLL traversal
- **Sửa thành:** Play next — normal mode | Complete | **O(1) direct node traversal**

### 3. Mục 4.2 Complexity summary (Phần Bảng)
Ở dòng `nextTrack() normal`:
- **Cột Best/Worst:** Đổi từ `O(1) / O(N)` thành **`O(1) / O(1)`**.
- **Cột Reason:** Đổi thành **`Direct node reference via currentPlaylistNode.next`**.

### 4. Mục 4.3 Research Question answer (Sửa đoạn văn)
- **Đoạn cần xóa (đang nhận là chưa làm được):** *"However, for the nextTrack() operation in normal mode, the traversal is still O(N) because the implementation looks up the current song by value in the list on every call. If the current Node reference were stored directly (instead of just the current Song), nextTrack() could be reduced to O(1). This is the main optimization opportunity for a future version."*
- **Thay bằng đoạn mới (khẳng định thành công):** *"Furthermore, the `nextTrack()` operation in normal mode has been successfully optimized to O(1) by storing a direct `Node` reference (`currentPlaylistNode`) instead of just tracking the `Song` object. This eliminates the need for any O(N) traversal, proving that CircularLinkedList is not only structurally superior for this problem but also delivers optimal constant-time performance."*

### 5. Mục 5. Conclusion (Sửa đoạn văn)
- **Đoạn cần xóa (hạn chế O(N) đã được fix):** *"The main limitation is that nextTrack() in normal mode is O(N) because it traverses the CircularLinkedList on every call. This would become a problem at very large playlist sizes. The fix would be to store a Node pointer as the current position instead of a Song object, which would make the operation O(1)."*
- **Thay bằng đoạn mới:** *"The final implementation successfully eliminates previous O(N) bottlenecks in both history tracking and normal playback, achieving O(1) time complexity for core navigation features by strategically tracking Node pointers."*
