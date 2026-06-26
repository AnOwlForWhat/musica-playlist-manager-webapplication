// Lắng nghe sự kiện popstate (back/forward trên trình duyệt)
window.addEventListener('popstate', (event) => {
    if (event.state && event.state.view) {
        loadSection(event.state.view, false);
    } else {
        loadSection('library', false);
    }
});

// Tự động load library khi vào trang chính (nếu không có hash hay tham số, để đơn giản ta load luôn library)
document.addEventListener("DOMContentLoaded", () => {
    // Kiểm tra URL xem đang ở view nào, nếu không có thì mặc định là library
    const urlParams = new URLSearchParams(window.location.search);
    const view = urlParams.get('view') || 'library';
    loadSection(view, false);
});

async function loadSection(viewName, pushHistory = true) {
    const contentSec = document.getElementById('content-section');
    contentSec.innerHTML = '<div style="text-align: center; padding: 20px;">Đang tải...</div>';

    try {
        // Thêm timestamp để tránh bị trình duyệt lưu cache AJAX (Nguyên nhân 1)
        var url = 'page?view=' + viewName + '&t=' + Date.now();
        if (window.contextPath) {
            url = window.contextPath + '/' + url;
        }
        const response = await fetch(url);
        if (response.ok) {
            const htmlContent = await response.text();
            contentSec.innerHTML = htmlContent;

            if (pushHistory) {
                // Thay đổi URL mà không reload trang
                history.pushState({view: viewName}, '', '?view=' + viewName);
            }
            
            // Cập nhật lại UI menu đang chọn (nếu có)
            document.querySelectorAll('.sidebar p a').forEach(a => a.style.fontWeight = 'normal');
            const activeLink = document.getElementById('nav-' + viewName);
            if (activeLink) activeLink.style.fontWeight = 'bold';
            
        } else {
            contentSec.innerHTML = '<p>Lỗi tải dữ liệu.</p>';
        }
    } catch (error) {
        console.error("Lỗi mạng:", error);
        contentSec.innerHTML = '<p>Lỗi kết nối mạng.</p>';
    }
}

async function searchLibrary(event) {
    event.preventDefault();
    const query = document.getElementById('search-input').value;
    const contentSec = document.getElementById('content-section');
    contentSec.innerHTML = '<div style="text-align: center; padding: 20px;">Đang tìm kiếm...</div>';

    try {
        var url = 'page?view=library&search=' + encodeURIComponent(query);
        if (window.contextPath) {
            url = window.contextPath + '/' + url;
        }
        const response = await fetch(url);
        if (response.ok) {
            const htmlContent = await response.text();
            contentSec.innerHTML = htmlContent;
        }
    } catch (error) {
        console.error("Lỗi mạng:", error);
    }
}

async function addToPlaylist(songId) {
    try {
        var url = 'player?action=add_playlist&songId=' + encodeURIComponent(songId);
        if (window.contextPath) {
            url = window.contextPath + '/' + url;
        }
        const response = await fetch(url);
        if (response.ok) {
            const data = await response.json();
            if (data.status === 'success') {
                alert('✅ Đã thêm bài hát vào playlist!');
            } else if (data.status === 'already_exists') {
                alert('⚠️ Bài hát này đã có trong playlist rồi!');
            } else {
                alert('❌ Không thể thêm bài hát.');
            }
        } else {
            alert('❌ Có lỗi mạng khi thêm bài hát.');
        }
    } catch (error) {
        console.error("Lỗi mạng:", error);
    }
}
