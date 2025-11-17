<%-- pageEncoding這個是表示這一頁jsp的內容的編碼 --%>
<%-- contentType設定頁面內容形態和編碼 - 這個是最後告知瀏覽器顯示畫面所要使用的編碼 --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!-- JSP 頁面指令：設定語言為 Java，內容類型為 HTML，字符編碼為 UTF-8 -->
<!DOCTYPE html>
<!-- 文檔類型聲明為 HTML5 -->
<html lang="zh-TW">
<!-- HTML 根元素，語言設定為繁體中文 -->
<head>
    <!-- 頁面頭部區域開始 -->
    <meta charset="UTF-8">
    <!-- 設定字符集為 UTF-8 -->
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <!-- 設定視口，讓頁面在不同裝置上自適應顯示 -->
    <title>登入 - 個人記帳系統</title>
    <!-- 設定瀏覽器標籤頁標題 -->
    <script src="https://code.jquery.com/jquery-3.7.1.js"></script>
    <!-- 引入 jQuery 3.7.1 函式庫，用於簡化 DOM 操作和 AJAX 請求 -->
    <style>
        /* CSS 樣式區域開始 */
        * {
            /* 全局樣式：重置所有元素的預設樣式 */
            margin: 0;
            /* 清除所有外邊距 */
            padding: 0;
            /* 清除所有內邊距 */
            box-sizing: border-box;
            /* 設定盒模型為邊框盒模型 */
        }

        body {
            /* 頁面主體樣式 */
            font-family: "Microsoft JhengHei", "Arial", sans-serif;
            /* 設定字體：微軟正黑體、Arial、無襯線字體 */
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            /* 設定漸層背景：從左上到右下，紫藍色到紫色 */
            min-height: 100vh;
            /* 最小高度為視窗高度的 100% */
            display: flex;
            /* 使用 Flexbox 布局 */
            justify-content: center;
            /* 水平置中對齊 */
            align-items: center;
            /* 垂直置中對齊 */
        }

        .login-container {
            /* 登入容器樣式 */
            background-color: white;
            /* 背景顏色為白色 */
            padding: 40px;
            /* 內邊距 40px */
            border-radius: 10px;
            /* 圓角半徑 10px */
            box-shadow: 0 10px 25px rgba(0, 0, 0, 0.2);
            /* 陰影效果：下方 10px 模糊 25px 半透明黑色 */
            width: 400px;
            /* 寬度 400px */
            max-width: 90%;
            /* 最大寬度為視窗寬度的 90% */
        }

        .login-container h1 {
            /* 登入標題樣式 */
            text-align: center;
            /* 文字置中對齊 */
            color: #333;
            /* 文字顏色為深灰色 */
            margin-bottom: 10px;
            /* 下方外邊距 10px */
            font-size: 28px;
            /* 字體大小 28px */
        }

        .login-container .subtitle {
            /* 副標題樣式 */
            text-align: center;
            /* 文字置中對齊 */
            color: #666;
            /* 文字顏色為灰色 */
            margin-bottom: 30px;
            /* 下方外邊距 30px */
            font-size: 14px;
            /* 字體大小 14px */
        }

        .form-group {
            /* 表單群組樣式 */
            margin-bottom: 20px;
            /* 下方外邊距 20px */
        }

        .form-group label {
            /* 表單標籤樣式 */
            display: block;
            /* 區塊元素，獨佔一行 */
            margin-bottom: 8px;
            /* 下方外邊距 8px */
            color: #333;
            /* 文字顏色為深灰色 */
            font-weight: 500;
            /* 字體粗細為中等 */
            font-size: 14px;
            /* 字體大小 14px */
        }

        .form-group input {
            /* 表單輸入框樣式 */
            width: 100%;
            /* 寬度為 100% */
            padding: 12px 15px;
            /* 內邊距：上下 12px，左右 15px */
            border: 1px solid #ddd;
            /* 邊框：1px 實線 淺灰色 */
            border-radius: 5px;
            /* 圓角半徑 5px */
            font-size: 14px;
            /* 字體大小 14px */
            transition: border-color 0.3s;
            /* 邊框顏色變化時有 0.3 秒的過渡效果 */
        }

        .form-group input:focus {
            /* 輸入框獲得焦點時的樣式 */
            outline: none;
            /* 移除預設的外框 */
            border-color: #667eea;
            /* 邊框顏色變為紫藍色 */
        }

        .btn-login {
            /* 登入按鈕樣式 */
            width: 100%;
            /* 寬度為 100% */
            padding: 12px;
            /* 內邊距 12px */
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            /* 漸層背景：從左上到右下，紫藍色到紫色 */
            color: white;
            /* 文字顏色為白色 */
            border: none;
            /* 移除邊框 */
            border-radius: 5px;
            /* 圓角半徑 5px */
            font-size: 16px;
            /* 字體大小 16px */
            font-weight: 600;
            /* 字體粗細為粗體 */
            cursor: pointer;
            /* 滑鼠游標變為手型 */
            transition: opacity 0.3s;
            /* 透明度變化時有 0.3 秒的過渡效果 */
        }

        .btn-login:hover {
            /* 登入按鈕滑鼠懸停時的樣式 */
            opacity: 0.9;
            /* 透明度降至 90% */
        }

        .btn-login:disabled {
            /* 登入按鈕禁用時的樣式 */
            opacity: 0.6;
            /* 透明度降至 60% */
            cursor: not-allowed;
            /* 滑鼠游標變為禁止符號 */
        }

        .register-link {
            /* 註冊連結區域樣式 */
            text-align: center;
            /* 文字置中對齊 */
            margin-top: 20px;
            /* 上方外邊距 20px */
            font-size: 14px;
            /* 字體大小 14px */
            color: #666;
            /* 文字顏色為灰色 */
        }

        .register-link a {
            /* 註冊連結樣式 */
            color: #667eea;
            /* 文字顏色為紫藍色 */
            text-decoration: none;
            /* 移除底線 */
            font-weight: 600;
            /* 字體粗細為粗體 */
        }

        .register-link a:hover {
            /* 註冊連結滑鼠懸停時的樣式 */
            text-decoration: underline;
            /* 添加底線 */
        }

        .error-message {
            /* 錯誤訊息樣式 */
            background-color: #fee;
            /* 背景顏色為淺紅色 */
            color: #c33;
            /* 文字顏色為紅色 */
            padding: 10px;
            /* 內邊距 10px */
            border-radius: 5px;
            /* 圓角半徑 5px */
            margin-bottom: 20px;
            /* 下方外邊距 20px */
            font-size: 14px;
            /* 字體大小 14px */
            display: none;
            /* 預設隱藏 */
        }

        .remember-me {
            /* 記住我區域樣式 */
            display: flex;
            /* 使用 Flexbox 布局 */
            align-items: center;
            /* 垂直置中對齊 */
            margin-bottom: 20px;
            /* 下方外邊距 20px */
        }

        .remember-me input[type="checkbox"] {
            /* 記住我勾選框樣式 */
            width: auto;
            /* 寬度自動 */
            margin-right: 8px;
            /* 右方外邊距 8px */
        }

        .remember-me label {
            /* 記住我標籤樣式 */
            margin: 0;
            /* 清除外邊距 */
            font-size: 14px;
            /* 字體大小 14px */
            color: #666;
            /* 文字顏色為灰色 */
        }
    </style>
    <!-- CSS 樣式區域結束 -->
</head>
<!-- 頁面頭部區域結束 -->
<body>
<!-- 頁面主體區域開始 -->
<div class="login-container">
    <!-- 登入容器開始 -->
    <h1>個人記帳系統</h1>
    <!-- 系統標題 -->
    <p class="subtitle">歡迎回來，請登入您的帳號</p>
    <!-- 副標題 -->

    <div class="error-message" id="errorMessage">
        <!-- 錯誤訊息顯示區域，預設隱藏 -->
    </div>

    <form id="loginForm">
        <!-- 登入表單 -->
        <div class="form-group">
            <!-- 表單群組：帳號輸入 -->
            <label for="username">帳號</label>
            <!-- 帳號標籤 -->
            <input type="text" id="username" name="username" placeholder="請輸入帳號" required>
            <!-- 帳號輸入框，設為必填欄位 -->
        </div>

        <div class="form-group">
            <!-- 表單群組：密碼輸入 -->
            <label for="password">密碼</label>
            <!-- 密碼標籤 -->
            <input type="password" id="password" name="password" placeholder="請輸入密碼" required>
            <!-- 密碼輸入框，設為必填欄位 -->
        </div>

        <div class="remember-me">
            <!-- 記住我選項區域 -->
            <input type="checkbox" id="rememberMe" name="rememberMe">
            <!-- 記住我勾選框 -->
            <label for="rememberMe">記住我</label>
            <!-- 記住我標籤 -->
        </div>

        <button type="submit" class="btn-login" id="loginBtn">登入</button>
        <!-- 登入按鈕 -->
    </form>
    <!-- 登入表單結束 -->

    <div class="register-link">
        <!-- 註冊連結區域 -->
        還沒有帳號？<a href="register.jsp">立即註冊</a>
        <!-- 註冊連結 -->
    </div>
</div>
<!-- 登入容器結束 -->

<script>
    // JavaScript 程式碼區域開始
    var contextPath = '<%= request.getContextPath() %>';
    // 全域變數：取得應用程式的根路徑（使用 JSP 表達式）

    // 頁面載入時初始化
    $(document).ready(function() {
        // jQuery 文檔就緒事件：當 DOM 完全載入後執行
        console.log('========== 登入頁面初始化 ==========');
        // 在控制台輸出初始化訊息

        // 檢查是否已登入
        checkLoginStatus();
        // 呼叫函數檢查登入狀態

        // 監聽表單提交事件
        $('#loginForm').submit(function(e) {
            // 當登入表單提交時觸發
            e.preventDefault();
            // 阻止表單的預設提交行為（避免頁面重新載入）
            login();
            // 呼叫登入函數
        });

        // 監聽輸入框的 Enter 鍵
        $('#username, #password').keypress(function(e) {
            // 當帳號或密碼輸入框按下鍵盤時觸發
            if (e.which === 13) {
                // 如果按下的是 Enter 鍵（鍵碼 13）
                e.preventDefault();
                // 阻止預設行為
                login();
                // 呼叫登入函數
            }
        });
    });

    // 檢查登入狀態
    function checkLoginStatus() {
        // 檢查使用者是否已登入的函數（檢查 JWT token 是否存在）
        var token = localStorage.getItem('jwtToken');
        // 從 localStorage 取得 JWT token

        if (token) {
            // 如果 token 存在，驗證 token 是否有效
            $.ajax({
                // 發送 AJAX 請求
                url: contextPath + '/api/auth/verify',
                // 請求 URL：驗證 token 的 API 端點
                type: 'GET',
                // HTTP 請求方法：GET
                headers: {
                    // 設定請求標頭
                    'Authorization': 'Bearer ' + token
                    // 在 Authorization 標頭中帶上 JWT token
                },
                dataType: 'json',
                // 預期伺服器回傳的資料類型：JSON
                success: function(response) {
                    // 請求成功時的回調函數
                    if (response && response.success) {
                        // 如果 token 有效
                        console.log('使用者已登入，重定向到主頁');
                        // 在控制台輸出訊息
                        window.location.href = contextPath + '/index.jsp';
                        // 重定向到記帳系統主頁
                    } else {
                        // token 無效，清除並停留在登入頁
                        localStorage.removeItem('jwtToken');
                        // 清除無效的 token
                        localStorage.removeItem('username');
                        // 清除使用者名稱
                    }
                },
                error: function(xhr, status, error) {
                    // 請求失敗時的回調函數（token 可能過期或無效）
                    console.log('Token 驗證失敗，清除 token');
                    // 在控制台輸出訊息
                    localStorage.removeItem('jwtToken');
                    // 清除無效的 token
                    localStorage.removeItem('username');
                    // 清除使用者名稱
                }
            });
        }
    }

    // 登入函數
    function login() {
        // 執行登入操作的函數
        var username = $('#username').val().trim();
        // 取得帳號輸入值並移除前後空白
        var password = $('#password').val();
        // 取得密碼輸入值
        var rememberMe = $('#rememberMe').is(':checked');
        // 取得記住我勾選狀態

        // 驗證輸入
        if (!username) {
            // 如果帳號為空
            showError('請輸入帳號');
            // 顯示錯誤訊息
            $('#username').focus();
            // 將焦點移到帳號輸入框
            return;
            // 中止函數執行
        }

        if (!password) {
            // 如果密碼為空
            showError('請輸入密碼');
            // 顯示錯誤訊息
            $('#password').focus();
            // 將焦點移到密碼輸入框
            return;
            // 中止函數執行
        }

        // 禁用登入按鈕，防止重複提交
        $('#loginBtn').prop('disabled', true).text('登入中...');
        // 禁用按鈕並改變按鈕文字

        // 準備登入資料
        var loginData = {
            // 建立登入資料物件
            username: username,
            // 帳號
            password: password,
            // 密碼
            rememberMe: rememberMe
            // 記住我選項
        };

        // 發送登入請求
        $.ajax({
            // 發送 AJAX 請求
            url: contextPath + '/api/auth/login',
            // 請求 URL：登入 API 端點
            type: 'POST',
            // HTTP 請求方法：POST
            contentType: 'application/json',
            // 請求內容類型：JSON
            data: JSON.stringify(loginData),
            // 將登入資料物件轉換為 JSON 字串
            dataType: 'json',
            // 預期伺服器回傳的資料類型：JSON
            success: function(response) {
                // 請求成功時的回調函數
                console.log('登入回應:', response);
                // 在控制台輸出回應資料

                if (response && response.success && response.data && response.data.token) {
                    // 如果登入成功並收到 JWT token
                    console.log('登入成功，儲存 JWT token');
                    // 在控制台輸出訊息

                    // 將 JWT token 儲存到 localStorage
                    localStorage.setItem('jwtToken', response.data.token);
                    // 儲存 JWT token

                    // 儲存使用者名稱（可選）
                    if (response.data.username) {
                        localStorage.setItem('username', response.data.username);
                        // 儲存使用者名稱
                    }

                    // 儲存使用者 ID（可選）
                    if (response.data.userId) {
                        localStorage.setItem('userId', response.data.userId);
                        // 儲存使用者 ID
                    }

                    console.log('重定向到主頁');
                    // 在控制台輸出訊息
                    window.location.href = contextPath + '/index.jsp';
                    // 重定向到記帳系統主頁
                } else {
                    // 如果登入失敗
                    showError(response.message || '登入失敗，請檢查帳號密碼');
                    // 顯示錯誤訊息
                    $('#loginBtn').prop('disabled', false).text('登入');
                    // 恢復登入按鈕
                }
            },
            error: function(xhr, status, error) {
                // 請求失敗時的回調函數
                console.error('登入錯誤:', error);
                // 在控制台輸出錯誤訊息
                console.error('狀態碼:', xhr.status);
                // 在控制台輸出 HTTP 狀態碼
                console.error('回應內容:', xhr.responseText);
                // 在控制台輸出回應內容

                var errorMsg = '登入時發生錯誤';
                // 預設錯誤訊息
                if (xhr.status === 401) {
                    // 如果狀態碼是 401（未授權）
                    errorMsg = '帳號或密碼錯誤';
                    // 設定錯誤訊息為帳號密碼錯誤
                } else if (xhr.responseJSON && xhr.responseJSON.message) {
                    // 如果回應包含錯誤訊息
                    errorMsg = xhr.responseJSON.message;
                    // 使用伺服器回傳的錯誤訊息
                }

                showError(errorMsg);
                // 顯示錯誤訊息
                $('#loginBtn').prop('disabled', false).text('登入');
                // 恢復登入按鈕
            }
        });
    }

    // 顯示錯誤訊息
    function showError(message) {
        // 顯示錯誤訊息的函數
        $('#errorMessage').text(message).fadeIn();
        // 設定錯誤訊息文字並以淡入效果顯示
        setTimeout(function() {
            // 設定延遲執行
            $('#errorMessage').fadeOut();
            // 3 秒後以淡出效果隱藏錯誤訊息
        }, 3000);
        // 延遲時間為 3000 毫秒（3 秒）
    }
</script>
<!-- JavaScript 程式碼區域結束 -->
</body>
<!-- 頁面主體區域結束 -->
</html>
<!-- HTML 文檔結束 -->
