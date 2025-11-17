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
            // 取得 JWT token
            var token = getToken();

            // 如果 token 存在且不是登入或註冊 API，則加入 Authorization header
            if (token && !settings.url.includes('/api/auth/login') && !settings.url.includes('/api/auth/register')) {
                xhr.setRequestHeader('Authorization', 'Bearer ' + token);
            }
        },
        error: function(xhr, status, error) {
            // 如果回應狀態碼為 401（未授權），表示 token 無效或過期
            if (xhr.status === 401) {
                console.log('Token 無效或已過期，重定向到登入頁面');
                // 清除 token
                removeToken();
                // 取得當前的 contextPath
                var contextPath = window.contextPath || '';
                // 重定向到登入頁面
                window.location.href = contextPath + '/login.jsp';
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
        window.location.href = contextPath + '/login.jsp';
        return;
    }

    // 驗證 token 是否有效
    $.ajax({
        url: contextPath + '/api/auth/verify',
        type: 'GET',
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
                window.location.href = contextPath + '/login.jsp';
            }
        },
        error: function(xhr, status, error) {
            // Token 驗證失敗，清除並重定向
            console.log('Token 驗證失敗，重定向到登入頁面');
            removeToken();
            window.location.href = contextPath + '/login.jsp';
        }
    });
}

// ========== Token 解析函數 ==========

/**
 * 解析 JWT token 的 payload 部分（不驗證簽章）
 * 僅用於前端顯示資訊，不能用於安全驗證
 * @param {string} token - JWT token
 * @returns {object|null} 解析後的 payload 物件或 null
 */
function parseJwt(token) {
    try {
        // JWT 格式：header.payload.signature
        var base64Url = token.split('.')[1];
        var base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/');
        var jsonPayload = decodeURIComponent(atob(base64).split('').map(function(c) {
            return '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2);
        }).join(''));

        return JSON.parse(jsonPayload);
    } catch (e) {
        console.error('解析 JWT token 失敗:', e);
        return null;
    }
}

/**
 * 檢查 token 是否即將過期（在指定分鐘數內）
 * @param {number} minutesBeforeExpiry - 提前多少分鐘視為即將過期（預設 5 分鐘）
 * @returns {boolean} 是否即將過期
 */
function isTokenExpiringSoon(minutesBeforeExpiry) {
    minutesBeforeExpiry = minutesBeforeExpiry || 5;

    var token = getToken();
    if (!token) {
        return false;
    }

    var payload = parseJwt(token);
    if (!payload || !payload.exp) {
        return false;
    }

    // JWT 的 exp 是以秒為單位的 Unix 時間戳
    var expiryTime = payload.exp * 1000; // 轉換為毫秒
    var currentTime = new Date().getTime();
    var timeUntilExpiry = expiryTime - currentTime;
    var warningTime = minutesBeforeExpiry * 60 * 1000; // 轉換為毫秒

    return timeUntilExpiry > 0 && timeUntilExpiry <= warningTime;
}

/**
 * 檢查 token 是否已過期
 * @returns {boolean} 是否已過期
 */
function isTokenExpired() {
    var token = getToken();
    if (!token) {
        return true;
    }

    var payload = parseJwt(token);
    if (!payload || !payload.exp) {
        return true;
    }

    // JWT 的 exp 是以秒為單位的 Unix 時間戳
    var expiryTime = payload.exp * 1000; // 轉換為毫秒
    var currentTime = new Date().getTime();

    return currentTime >= expiryTime;
}

// ========== Token 刷新函數 ==========

/**
 * 刷新 JWT token（如果後端支援）
 * @param {string} contextPath - 應用程式根路徑
 * @param {function} successCallback - 刷新成功的回調函數
 * @param {function} errorCallback - 刷新失敗的回調函數
 */
function refreshToken(contextPath, successCallback, errorCallback) {
    var token = getToken();

    if (!token) {
        if (errorCallback) {
            errorCallback('沒有 token');
        }
        return;
    }

    $.ajax({
        url: contextPath + '/api/auth/refresh',
        type: 'POST',
        headers: {
            'Authorization': 'Bearer ' + token
        },
        dataType: 'json',
        success: function(response) {
            if (response && response.success && response.data && response.data.token) {
                // 儲存新的 token
                setToken(response.data.token);
                console.log('Token 刷新成功');

                if (successCallback) {
                    successCallback(response.data.token);
                }
            } else {
                console.error('Token 刷新失敗:', response.message);
                if (errorCallback) {
                    errorCallback(response.message);
                }
            }
        },
        error: function(xhr, status, error) {
            console.error('Token 刷新錯誤:', error);
            if (errorCallback) {
                errorCallback(error);
            }
        }
    });
}

// ========== 自動刷新 Token 機制 ==========

/**
 * 啟動自動刷新 token 的機制
 * 當 token 即將過期時自動刷新
 * @param {string} contextPath - 應用程式根路徑
 * @param {number} checkIntervalMinutes - 檢查間隔（分鐘，預設 5 分鐘）
 * @param {number} refreshBeforeMinutes - 提前多少分鐘刷新（預設 10 分鐘）
 */
function startAutoRefresh(contextPath, checkIntervalMinutes, refreshBeforeMinutes) {
    checkIntervalMinutes = checkIntervalMinutes || 5;
    refreshBeforeMinutes = refreshBeforeMinutes || 10;

    setInterval(function() {
        if (isLoggedIn() && isTokenExpiringSoon(refreshBeforeMinutes)) {
            console.log('Token 即將過期，自動刷新...');
            refreshToken(
                contextPath,
                function(newToken) {
                    console.log('Token 自動刷新成功');
                },
                function(error) {
                    console.error('Token 自動刷新失敗，重定向到登入頁面');
                    removeToken();
                    window.location.href = contextPath + '/login.jsp';
                }
            );
        }
    }, checkIntervalMinutes * 60 * 1000); // 轉換為毫秒
}
