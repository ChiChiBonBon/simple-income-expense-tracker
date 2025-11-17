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
    <title>登出中 - 個人記帳系統</title>
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

        .logout-container {
            /* 登出容器樣式 */
            background-color: white;
            /* 背景顏色為白色 */
            padding: 60px 40px;
            /* 內邊距：上下 60px，左右 40px */
            border-radius: 10px;
            /* 圓角半徑 10px */
            box-shadow: 0 10px 25px rgba(0, 0, 0, 0.2);
            /* 陰影效果：下方 10px 模糊 25px 半透明黑色 */
            width: 400px;
            /* 寬度 400px */
            max-width: 90%;
            /* 最大寬度為視窗寬度的 90% */
            text-align: center;
            /* 文字置中對齊 */
        }

        .logout-container .icon {
            /* 圖示區域樣式 */
            font-size: 60px;
            /* 字體大小 60px */
            margin-bottom: 20px;
            /* 下方外邊距 20px */
        }

        .logout-container h1 {
            /* 標題樣式 */
            color: #333;
            /* 文字顏色為深灰色 */
            margin-bottom: 15px;
            /* 下方外邊距 15px */
            font-size: 24px;
            /* 字體大小 24px */
        }

        .logout-container p {
            /* 段落文字樣式 */
            color: #666;
            /* 文字顏色為灰色 */
            margin-bottom: 30px;
            /* 下方外邊距 30px */
            font-size: 14px;
            /* 字體大小 14px */
        }

        .spinner {
            /* 載入動畫容器樣式 */
            border: 4px solid #f3f3f3;
            /* 邊框：4px 實線 淺灰色 */
            border-top: 4px solid #667eea;
            /* 上邊框：4px 實線 紫藍色 */
            border-radius: 50%;
            /* 圓角半徑 50%（形成圓形）*/
            width: 50px;
            /* 寬度 50px */
            height: 50px;
            /* 高度 50px */
            animation: spin 1s linear infinite;
            /* 動畫：旋轉 1 秒 線性 無限循環 */
            margin: 0 auto 20px;
            /* 外邊距：上下為 0 和 20px，左右自動置中 */
        }

        @keyframes spin {
            /* 定義旋轉動畫 */
            0% { transform: rotate(0deg); }
            /* 起始角度 0 度 */
            100% { transform: rotate(360deg); }
            /* 結束角度 360 度 */
        }

        .btn-login {
            /* 返回登入按鈕樣式 */
            display: inline-block;
            /* 行內區塊元素 */
            padding: 10px 30px;
            /* 內邊距：上下 10px，左右 30px */
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            /* 漸層背景：從左上到右下，紫藍色到紫色 */
            color: white;
            /* 文字顏色為白色 */
            border: none;
            /* 移除邊框 */
            border-radius: 5px;
            /* 圓角半徑 5px */
            font-size: 14px;
            /* 字體大小 14px */
            font-weight: 600;
            /* 字體粗細為粗體 */
            cursor: pointer;
            /* 滑鼠游標變為手型 */
            text-decoration: none;
            /* 移除底線 */
            transition: opacity 0.3s;
            /* 透明度變化時有 0.3 秒的過渡效果 */
            display: none;
            /* 預設隱藏 */
        }

        .btn-login:hover {
            /* 返回登入按鈕滑鼠懸停時的樣式 */
            opacity: 0.9;
            /* 透明度降至 90% */
        }

        .success-icon {
            /* 成功圖示樣式 */
            color: #27ae60;
            /* 文字顏色為綠色 */
            display: none;
            /* 預設隱藏 */
        }

        .error-icon {
            /* 錯誤圖示樣式 */
            color: #e74c3c;
            /* 文字顏色為紅色 */
            display: none;
            /* 預設隱藏 */
        }
    </style>
    <!-- CSS 樣式區域結束 -->
</head>
<!-- 頁面頭部區域結束 -->
<body>
<!-- 頁面主體區域開始 -->
<div class="logout-container">
    <!-- 登出容器開始 -->
    <div class="spinner" id="spinner"></div>
    <!-- 載入動畫，預設顯示 -->

    <div class="icon success-icon" id="successIcon">✓</div>
    <!-- 成功圖示，預設隱藏 -->

    <div class="icon error-icon" id="errorIcon">✕</div>
    <!-- 錯誤圖示，預設隱藏 -->

    <h1 id="statusTitle">正在登出...</h1>
    <!-- 狀態標題，預設顯示「正在登出」-->

    <p id="statusMessage">請稍候，系統正在處理您的登出請求</p>
    <!-- 狀態訊息，預設顯示處理中訊息 -->

    <a href="#" class="btn-login" id="loginBtn">返回登入頁面</a>
    <!-- 返回登入按鈕，預設隱藏 -->
</div>
<!-- 登出容器結束 -->

<script>
    // JavaScript 程式碼區域開始
    var contextPath = '<%= request.getContextPath() %>';
    // 全域變數：取得應用程式的根路徑（使用 JSP 表達式）

    // 頁面載入時自動執行登出
    $(document).ready(function() {
        // jQuery 文檔就緒事件：當 DOM 完全載入後執行
        console.log('========== 登出頁面初始化 ==========');
        // 在控制台輸出初始化訊息

        // 延遲 500 毫秒後執行登出，讓使用者看到載入動畫
        setTimeout(function() {
            // 設定延遲執行
            logout();
            // 呼叫登出函數
        }, 500);
        // 延遲時間為 500 毫秒

        // 監聽返回登入按鈕點擊事件
        $('#loginBtn').click(function(e) {
            // 當返回登入按鈕被點擊時觸發
            e.preventDefault();
            // 阻止預設行為
            window.location.href = contextPath + '/login.jsp';
            // 重定向到登入頁面
        });
    });

    // 登出函數
    function logout() {
        // 執行登出操作的函數
        console.log('開始執行登出...');
        // 在控制台輸出訊息

        var token = localStorage.getItem('jwtToken');
        // 從 localStorage 取得 JWT token

        $.ajax({
            // 發送 AJAX 請求
            url: contextPath + '/api/auth/logout',
            // 請求 URL：登出 API 端點
            type: 'POST',
            // HTTP 請求方法：POST
            headers: {
                // 設定請求標頭
                'Authorization': 'Bearer ' + token
                // 在 Authorization 標頭中帶上 JWT token
            },
            dataType: 'json',
            // 預期伺服器回傳的資料類型：JSON
            success: function(response) {
                // 請求成功時的回調函數
                console.log('登出回應:', response);
                // 在控制台輸出回應資料

                // 清除 localStorage 中的所有認證資訊
                localStorage.removeItem('jwtToken');
                // 清除 JWT token
                localStorage.removeItem('username');
                // 清除使用者名稱
                localStorage.removeItem('userId');
                // 清除使用者 ID

                showSuccess();
                // 顯示成功訊息
            },
            error: function(xhr, status, error) {
                // 請求失敗時的回調函數
                console.error('登出錯誤:', error);
                // 在控制台輸出錯誤訊息
                console.error('狀態碼:', xhr.status);
                // 在控制台輸出 HTTP 狀態碼
                console.error('回應內容:', xhr.responseText);
                // 在控制台輸出回應內容

                // 即使發生錯誤，也清除本地的 token
                localStorage.removeItem('jwtToken');
                // 清除 JWT token
                localStorage.removeItem('username');
                // 清除使用者名稱
                localStorage.removeItem('userId');
                // 清除使用者 ID

                // 顯示成功訊息並重定向
                showSuccess();
                // 顯示成功訊息
            }
        });
    }

    // 顯示成功訊息
    function showSuccess() {
        // 顯示登出成功訊息的函數
        console.log('登出成功');
        // 在控制台輸出訊息

        // 隱藏載入動畫
        $('#spinner').hide();
        // 隱藏旋轉動畫

        // 顯示成功圖示
        $('#successIcon').fadeIn();
        // 以淡入效果顯示成功圖示

        // 更新標題和訊息
        $('#statusTitle').text('登出成功');
        // 更新標題為「登出成功」
        $('#statusMessage').text('您已成功登出系統，2 秒後將自動跳轉到登入頁面');
        // 更新訊息為成功訊息

        // 顯示返回登入按鈕
        $('#loginBtn').fadeIn();
        // 以淡入效果顯示返回登入按鈕

        // 2 秒後自動跳轉到登入頁面
        setTimeout(function() {
            // 設定延遲執行
            window.location.href = contextPath + '/login.jsp';
            // 重定向到登入頁面
        }, 2000);
        // 延遲時間為 2000 毫秒（2 秒）
    }

    // 顯示錯誤訊息
    function showError(message) {
        // 顯示登出錯誤訊息的函數
        console.error('登出失敗:', message);
        // 在控制台輸出錯誤訊息

        // 隱藏載入動畫
        $('#spinner').hide();
        // 隱藏旋轉動畫

        // 顯示錯誤圖示
        $('#errorIcon').fadeIn();
        // 以淡入效果顯示錯誤圖示

        // 更新標題和訊息
        $('#statusTitle').text('登出失敗');
        // 更新標題為「登出失敗」
        $('#statusMessage').text(message || '登出時發生錯誤，請稍後再試');
        // 更新訊息為錯誤訊息

        // 顯示返回登入按鈕
        $('#loginBtn').fadeIn();
        // 以淡入效果顯示返回登入按鈕
    }
</script>
<!-- JavaScript 程式碼區域結束 -->
</body>
<!-- 頁面主體區域結束 -->
</html>
<!-- HTML 文檔結束 -->
