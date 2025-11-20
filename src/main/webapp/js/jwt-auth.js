/**
 * JWT 認證工具函式庫
 * 用於處理 JWT token 的儲存、讀取和自動在 AJAX 請求中加入 Authorization header
 */

// ========== Token 管理函數 ==========

/**
 * 儲存 JWT token 到 localStorage
 * @param {string} token - JWT token 字串
 */
function setToken(token) {
    localStorage.setItem('jwtToken', token);
}

/**
 * 從 localStorage 取得 JWT token
 * @returns {string|null} JWT token 或 null（如果不存在）
 */
function getToken() {
    return localStorage.getItem('jwtToken');
}

/**
 * 從 localStorage 移除 JWT token
 */
function removeToken() {
    localStorage.removeItem('jwtToken');
    localStorage.removeItem('username');
    localStorage.removeItem('userId');
}

/**
 * 檢查是否已登入（是否有 token）
 * @returns {boolean} 是否已登入
 */
function isLoggedIn() {
    return getToken() !== null;
}

/**
 * 儲存使用者資訊到 localStorage
 * @param {object} userInfo - 使用者資訊物件
 */
function setUserInfo(userInfo) {
    if (userInfo.username) {
        localStorage.setItem('username', userInfo.username);
    }
    if (userInfo.userId) {
        localStorage.setItem('userId', userInfo.userId);
    }
}

/**
 * 取得使用者資訊
 * @returns {object} 使用者資訊物件
 */
function getUserInfo() {
    return {
        username: localStorage.getItem('username'),
        userId: localStorage.getItem('userId')
    };
}

// ========== jQuery AJAX 攔截器 ==========

/**
 * 設定 jQuery AJAX 全域攔截器
 * 自動在所有 AJAX 請求中加入 Authorization header
 */
$(document).ready(function() {
    // 在發送請求前自動加入 Authorization header
    $.ajaxSetup({
        beforeSend: function(xhr, settings) {
            var token = getToken();

            // ✅ 只排除 login 和 register，logout 需要 token
            if (token && !settings.url.includes('/api/auth/login') &&
                !settings.url.includes('/api/auth/register')) {
                xhr.setRequestHeader('Authorization', 'Bearer ' + token);
            }
        },
        error: function(xhr, status, error) {
            if (xhr.status === 401) {
                console.log('Token 無效或已過期，重定向到登入頁面');
                removeToken();
                var contextPath = window.contextPath || '';
                window.location.href = contextPath + '/login';
            }
        }
    });
});

// ========== 認證檢查函數 ==========

/**
 * 檢查使用者是否已登入並驗證 token 有效性
 * 如果未登入或 token 無效，重定向到登入頁面
 * @param {string} contextPath - 應用程式根路徑
 * @param {function} callback - token 有效時的回調函數
 */
function requireAuth(contextPath, callback) {
    var token = getToken();

    if (!token) {
        // 沒有 token，重定向到登入頁面
        console.log('未登入，重定向到登入頁面');
        window.location.href = contextPath + '/login';
        return;
    }

    // 驗證 token 是否有效
    $.ajax({
        url: contextPath + '/api/auth/verify',
        type: 'POST',
        headers: {
            'Authorization': 'Bearer ' + token
        },
        dataType: 'json',
        success: function(response) {
            if (response && response.success) {
                // Token 有效，執行回調函數
                if (callback) {
                    callback(response.data);
                }
            } else {
                // Token 無效，清除並重定向
                console.log('Token 無效，重定向到登入頁面');
                removeToken();
                window.location.href = contextPath + '/login';
            }
        },
        error: function(xhr, status, error) {
            // Token 驗證失敗，清除並重定向
            console.log('Token 驗證失敗，重定向到登入頁面');
            removeToken();
            window.location.href = contextPath + '/login';
        }
    });
}