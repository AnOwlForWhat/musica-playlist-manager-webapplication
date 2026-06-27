# Luồng Hoạt Động Của Hệ Thống Phát Nhạc (Playback Workflow)

Dưới đây là sơ đồ luồng hoạt động chi tiết (Flowchart) mô tả cách hệ thống xử lý khi người dùng tương tác với các chức năng **Phát nhạc (Play)**, **Chuyển bài tiếp theo (Next)**, và **Quay lại bài trước (Previous)** trong cả 2 trường hợp có và không có Shuffle.

## 1. Sơ Đồ Luồng Hoạt Động (Mermaid Flowchart)

```mermaid
flowchart TD
    %% Định nghĩa các node
    StartUser((Người Dùng Tương Tác))
    
    %% Các hành động
    ActionPlay[Phát 1 bài hát bất kỳ\nPlay(Song)]
    ActionNext[Chuyển bài tiếp theo\nnextTrack()]
    ActionPrev[Quay lại bài trước\nprevTrack()]
    
    %% Phân nhánh
    StartUser --> ActionPlay
    StartUser --> ActionNext
    StartUser --> ActionPrev

    %% === LUỒNG PLAY ===
    subgraph Play_Flow [Luồng Play Nhạc]
        PlayCheck{Đang có bài\nhát phát?}
        PlayPushStack[Đẩy bài hát hiện tại\nvào HistoryStack]
        PlayUpdate[Cập nhật currentPlayingSong\nbằng bài hát mới]
        
        ActionPlay --> PlayCheck
        PlayCheck -- Có --> PlayPushStack --> PlayUpdate
        PlayCheck -- Không --> PlayUpdate
    end

    %% === LUỒNG NEXT ===
    subgraph Next_Flow [Luồng Next Track]
        NextCheckEmpty{Playlist rỗng?}
        NextShuffleCheck{isShuffle = True?}
        
        NextCLL[Lấy bài hát từ danh sách liên kết:\nnextSong = current.getNext()]
        
        NextArr[Lấy bài hát từ mảng xáo trộn:\nnextSong = shuffleList.get(index)]
        NextArrIndexUpdate[Tăng currentShuffleIndex++]
        NextArrEndCheck{Index >= Kích thước\nShuffleList?}
        NextArrShuffle[Trộn lại mảng (Fisher-Yates)\nReset Index = 0]
        
        ActionNext --> NextCheckEmpty
        NextCheckEmpty -- Có --> ReturnNull1((Return Null))
        NextCheckEmpty -- Không --> NextShuffleCheck
        
        %% Nhánh Không Shuffle
        NextShuffleCheck -- False --> NextCLL
        
        %% Nhánh Có Shuffle
        NextShuffleCheck -- True --> NextArr --> NextArrIndexUpdate --> NextArrEndCheck
        NextArrEndCheck -- Có --> NextArrShuffle --> TriggerPlayedSong
        NextArrEndCheck -- Không --> TriggerPlayedSong
        
        NextCLL --> TriggerPlayedSong
        
        TriggerPlayedSong(Gọi lại hàm Play nhạc)
        TriggerPlayedSong --> PlayCheck
    end

    %% === LUỒNG PREVIOUS ===
    subgraph Prev_Flow [Luồng Previous Track]
        PrevCheckStack{HistoryStack rỗng?}
        PrevPopStack[Lấy bài trên cùng ra:\nprevSong = historyStack.pop()]
        PrevUpdate[Cập nhật currentPlayingSong\nbằng prevSong]
        
        ActionPrev --> PrevCheckStack
        PrevCheckStack -- Có --> ReturnNull2((Return Null))
        PrevCheckStack -- Không --> PrevPopStack --> PrevUpdate
    end

```

## 2. Diễn Giải Chi Tiết Các Luồng (Trace)

### A. Luồng Phát Nhạc (Play Song)
Luồng này được gọi mỗi khi người dùng chủ động chọn 1 bài hát, hoặc khi hệ thống tự động chuyển bài thành công.
*   **Kiểm tra:** Có bài hát nào đang phát hay không?
*   **Lưu lịch sử:** Nếu đang có một bài hát phát, nó sẽ bị đẩy (Push) vào **HistoryStack** trước khi chuyển sang bài mới.
*   **Cập nhật:** Đặt `currentPlayingSong` thành bài hát mới.

### B. Luồng Bài Tiếp Theo (Next Track)
Đây là luồng phức tạp nhất vì nó phải rẻ nhánh tùy theo chế độ Shuffle (Trộn bài).
*   **Bước 1:** Kiểm tra xem Playlist có rỗng không. Nếu rỗng thì dừng (Return null).
*   **Bước 2:** Hệ thống kiểm tra biến `isShuffle`.
    *   👉 **Trường hợp Không Shuffle (isShuffle = false)**: Hệ thống sử dụng **Circular Linked List**. Nó đơn giản gọi `.getNext()` từ bài hát hiện tại để lấy bài tiếp theo. (Do là vòng tròn nên nếu là bài cuối thì nó tự động trỏ lại đầu - tức là tự động lặp lại playlist).
    *   👉 **Trường hợp Có Shuffle (isShuffle = true)**: Hệ thống bỏ qua Linked List và truy xuất vào **Mảng động (ArrayList)** đã được trộn sẵn ngẫu nhiên (shuffleList). Dùng biến `currentShuffleIndex` để lấy bài hát ở vị trí tương ứng. Tăng Index lên 1. Nếu Index chạy hết mảng, hệ thống tự động trộn bài lại từ đầu bằng thuật toán Fisher-Yates và reset Index về 0.
*   **Bước 3:** Lấy được bài hát mới (nextSong), hệ thống chuyển hướng gọi lại **Luồng Phát Nhạc (Play Song)** để cập nhật giao diện và lưu bài hát cũ vào Stack.

### C. Luồng Bài Trước Đó (Previous Track)
Nhờ có **HistoryStack**, luồng này hoạt động cực kỳ đơn giản và tính chính xác đạt 100%, hoàn toàn không bị ảnh hưởng bởi việc Shuffle đang bật hay tắt.
*   **Bước 1:** Kiểm tra **HistoryStack** có rỗng không (nghĩa là trước đó người dùng có phát bài nào chưa). Nếu chưa có thì không làm gì (Return null).
*   **Bước 2:** Lấy (Pop) bài hát nằm trên cùng của Stack ra ngoài.
*   **Bước 3:** Cập nhật `currentPlayingSong` bằng bài hát vừa lấy ra. Lưu ý: Thao tác này không gọi lại Luồng Phát Nhạc (Play Song) vì ta không muốn lưu chính cái hành động lùi bài vào lịch sử.

> [!TIP]
> **Điểm mạnh của kiến trúc này:** 
> Dù tính năng Shuffle có "nhảy cóc" hỗn loạn ra sao trên danh sách, thì bài hát trước đó luôn được lưu lại chính xác vào Stack. Khi nhấn Previous, hệ thống chỉ việc bốc trong Stack ra mà không cần quan tâm đến thứ tự hiện tại của mảng hay danh sách liên kết.
