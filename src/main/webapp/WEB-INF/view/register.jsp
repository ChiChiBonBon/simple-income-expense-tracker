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
    <title>註冊 - 個人記帳系統</title>
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
            padding: 20px 0;
            /* 上下內邊距 20px，左右為 0 */
        }

        .register-container {
            /* 註冊容器樣式 */
            background-color: white;
            /* 背景顏色為白色 */
            padding: 40px;
            /* 內邊距 40px */
            border-radius: 10px;
            /* 圓角半徑 10px */
            box-shadow: 0 10px 25px rgba(0, 0, 0, 0.2);
            /* 陰影效果：下方 10px 模糊 25px 半透明黑色 */
            width: 450px;
            /* 寬度 450px */
            max-width: 90%;
            /* 最大寬度為視窗寬度的 90% */
        }

        .register-container h1 {
            /* 註冊標題樣式 */
            text-align: center;
            /* 文字置中對齊 */
            color: #333;
            /* 文字顏色為深灰色 */
            margin-bottom: 10px;
            /* 下方外邊距 10px */
            font-size: 28px;
            /* 字體大小 28px */
        }

        .register-container .subtitle {
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

        .form-group label .required {
            /* 必填欄位標記樣式 */
            color: #e74c3c;
            /* 文字顏色為紅色 */
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

        .form-group input.error {
            /* 輸入框錯誤狀態樣式 */
            border-color: #e74c3c;
            /* 邊框顏色變為紅色 */
        }

        .form-group .help-text {
            /* 輔助說明文字樣式 */
            font-size: 12px;
            /* 字體大小 12px */
            color: #999;
            /* 文字顏色為淺灰色 */
            margin-top: 5px;
            /* 上方外邊距 5px */
        }

        .form-group .error-text {
            /* 錯誤提示文字樣式 */
            font-size: 12px;
            /* 字體大小 12px */
            color: #e74c3c;
            /* 文字顏色為紅色 */
            margin-top: 5px;
            /* 上方外邊距 5px */
            display: none;
            /* 預設隱藏 */
        }

        .password-strength {
            /* 密碼強度指示器樣式 */
            height: 4px;
            /* 高度 4px */
            margin-top: 5px;
            /* 上方外邊距 5px */
            border-radius: 2px;
            /* 圓角半徑 2px */
            transition: all 0.3s;
            /* 所有屬性變化時有 0.3 秒的過渡效果 */
        }

        .password-strength.weak {
            /* 弱密碼樣式 */
            background-color: #e74c3c;
            /* 背景顏色為紅色 */
            width: 33%;
            /* 寬度為 33% */
        }

        .password-strength.medium {
            /* 中等密碼樣式 */
            background-color: #f39c12;
            /* 背景顏色為橙色 */
            width: 66%;
            /* 寬度為 66% */
        }

        .password-strength.strong {
            /* 強密碼樣式 */
            background-color: #27ae60;
            /* 背景顏色為綠色 */
            width: 100%;
            /* 寬度為 100% */
        }

        .btn-register {
            /* 註冊按鈕樣式 */
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
            margin-top: 10px;
            /* 上方外邊距 10px */
        }

        .btn-register:hover {
            /* 註冊按鈕滑鼠懸停時的樣式 */
            opacity: 0.9;
            /* 透明度降至 90% */
        }

        .btn-register:disabled {
            /* 註冊按鈕禁用時的樣式 */
            opacity: 0.6;
            /* 透明度降至 60% */
            cursor: not-allowed;
            /* 滑鼠游標變為禁止符號 */
        }

        .login-link {
            /* 登入連結區域樣式 */
            text-align: center;
            /* 文字置中對齊 */
            margin-top: 20px;
            /* 上方外邊距 20px */
            font-size: 14px;
            /* 字體大小 14px */
            color: #666;
            /* 文字顏色為灰色 */
        }

        .login-link a {
            /* 登入連結樣式 */
            color: #667eea;
            /* 文字顏色為紫藍色 */
            text-decoration: none;
            /* 移除底線 */
            font-weight: 600;
            /* 字體粗細為粗體 */
        }

        .login-link a:hover {
            /* 登入連結滑鼠懸停時的樣式 */
            text-decoration: underline;
            /* 添加底線 */
        }

        .success-message {
            /* 成功訊息樣式 */
            background-color: #d4edda;
            /* 背景顏色為淺綠色 */
            color: #155724;
            /* 文字顏色為深綠色 */
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

        .terms {
            /* 服務條款區域樣式 */
            margin: 20px 0;
            /* 上下外邊距 20px，左右為 0 */
            font-size: 12px;
            /* 字體大小 12px */
            color: #666;
            /* 文字顏色為灰色 */
            display: flex;
            /* 使用 Flexbox 布局 */
            align-items: flex-start;
            /* 垂直對齊到頂部 */
        }

        .terms input[type="checkbox"] {
            /* 服務條款勾選框樣式 */
            width: auto;
            /* 寬度自動 */
            margin-right: 8px;
            /* 右方外邊距 8px */
            margin-top: 2px;
            /* 上方外邊距 2px */
        }

        .terms a {
            /* 服務條款連結樣式 */
            color: #667eea;
            /* 文字顏色為紫藍色 */
            text-decoration: none;
            /* 移除底線 */
        }

        .terms a:hover {
            /* 服務條款連結滑鼠懸停時的樣式 */
            text-decoration: underline;
            /* 添加底線 */
        }
    </style>
    <!-- CSS 樣式區域結束 -->
</head>
<!-- 頁面頭部區域結束 -->
<body>
<!-- 頁面主體區域開始 -->
<div class="register-container">
    <!-- 註冊容器開始 -->
    <h1>建立新帳號</h1>
    <!-- 系統標題 -->
    <p class="subtitle">開始使用個人記帳系統</p>
    <!-- 副標題 -->

    <div class="success-message" id="successMessage">
        <!-- 成功訊息顯示區域，預設隱藏 -->
    </div>

    <div class="error-message" id="errorMessage">
        <!-- 錯誤訊息顯示區域，預設隱藏 -->
    </div>

    <form id="registerForm">
        <!-- 註冊表單 -->
        <div class="form-group">
            <!-- 表單群組：帳號輸入 -->
            <label for="username">帳號 <span class="required">*</span></label>
            <!-- 帳號標籤，標示為必填欄位 -->
            <input type="text" id="username" name="username" placeholder="請輸入帳號（4-20個字元）" required>
            <!-- 帳號輸入框，設為必填欄位 -->
            <div class="help-text">帳號長度需為 4-20 個字元，可使用英文字母、數字和底線</div>
            <!-- 輔助說明文字 -->
            <div class="error-text" id="usernameError"></div>
            <!-- 錯誤提示文字區域 -->
        </div>

        <div class="form-group">
            <!-- 表單群組：電子郵件輸入 -->
            <label for="email">電子郵件</label>
            <!-- 電子郵件標籤 -->
            <input type="email" id="email" name="email" placeholder="example@email.com">
            <!-- 電子郵件輸入框 -->
            <div class="help-text">選填，可用於找回密碼</div>
            <!-- 輔助說明文字 -->
            <div class="error-text" id="emailError"></div>
            <!-- 錯誤提示文字區域 -->
        </div>

        <div class="form-group">
            <!-- 表單群組：密碼輸入 -->
            <label for="password">密碼 <span class="required">*</span></label>
            <!-- 密碼標籤，標示為必填欄位 -->
            <input type="password" id="password" name="password" placeholder="請輸入密碼（至少6個字元）" required>
            <!-- 密碼輸入框，設為必填欄位 -->
            <div class="password-strength" id="passwordStrength"></div>
            <!-- 密碼強度指示器 -->
            <div class="help-text">密碼長度至少 6 個字元，建議包含英文大小寫、數字和特殊符號</div>
            <!-- 輔助說明文字 -->
            <div class="error-text" id="passwordError"></div>
            <!-- 錯誤提示文字區域 -->
        </div>

        <div class="form-group">
            <!-- 表單群組：確認密碼輸入 -->
            <label for="confirmPassword">確認密碼 <span class="required">*</span></label>
            <!-- 確認密碼標籤，標示為必填欄位 -->
            <input type="password" id="confirmPassword" name="confirmPassword" placeholder="請再次輸入密碼" required>
            <!-- 確認密碼輸入框，設為必填欄位 -->
            <div class="error-text" id="confirmPasswordError"></div>
            <!-- 錯誤提示文字區域 -->
        </div>

        <div class="terms">
            <!-- 服務條款區域 -->
            <input type="checkbox" id="agreeTerms" name="agreeTerms" required>
            <!-- 同意服務條款勾選框，設為必填欄位 -->
            <label for="agreeTerms">我已閱讀並同意<a href="#" onclick="return false;">服務條款</a>和<a href="#" onclick="return false;">隱私政策</a></label>
            <!-- 服務條款標籤和連結 -->
        </div>

        <button type="submit" class="btn-register" id="registerBtn">註冊</button>
        <!-- 註冊按鈕 -->
    </form>
    <!-- 註冊表單結束 -->

    <div class="login-link">
        <!-- 登入連結區域 -->
        已經有帳號了？<a href="login.jsp">立即登入</a>
        <!-- 登入連結 -->
    </div>
</div>
<!-- 註冊容器結束 -->

<script>
    // JavaScript 程式碼區域開始
    var contextPath = '<%= request.getContextPath() %>';
    // 全域變數：取得應用程式的根路徑（使用 JSP 表達式）

    // 頁面載入時初始化
    $(document).ready(function() {
        // jQuery 文檔就緒事件：當 DOM 完全載入後執行
        console.log('========== 註冊頁面初始化 ==========');
        // 在控制台輸出初始化訊息

        // 監聽表單提交事件
        $('#registerForm').submit(function(e) {
            // 當註冊表單提交時觸發
            e.preventDefault();
            // 阻止表單的預設提交行為（避免頁面重新載入）
            register();
            // 呼叫註冊函數
        });

        // 監聽帳號輸入，即時驗證
        $('#username').blur(function() {
            // 當帳號輸入框失去焦點時觸發
            validateUsername();
            // 呼叫帳號驗證函數
        });

        // 監聽電子郵件輸入，即時驗證
        $('#email').blur(function() {
            // 當電子郵件輸入框失去焦點時觸發
            var email = $(this).val().trim();
            // 取得電子郵件輸入值並移除前後空白
            if (email) {
                // 如果電子郵件不為空
                validateEmail();
                // 呼叫電子郵件驗證函數
            }
        });

        // 監聽密碼輸入，顯示強度指示器
        $('#password').on('input', function() {
            // 當密碼輸入框內容改變時觸發
            checkPasswordStrength();
            // 呼叫密碼強度檢查函數
        });

        // 監聽密碼輸入，即時驗證
        $('#password').blur(function() {
            // 當密碼輸入框失去焦點時觸發
            validatePassword();
            // 呼叫密碼驗證函數
        });

        // 監聽確認密碼輸入，即時驗證
        $('#confirmPassword').on('input blur', function() {
            // 當確認密碼輸入框內容改變或失去焦點時觸發
            validateConfirmPassword();
            // 呼叫確認密碼驗證函數
        });
    });

    // 驗證帳號
    function validateUsername() {
        // 驗證帳號格式的函數
        var username = $('#username').val().trim();
        // 取得帳號輸入值並移除前後空白
        var usernameRegex = /^[a-zA-Z0-9_]{4,20}$/;
        // 定義帳號正規表達式：4-20 個字元，僅允許英文字母、數字和底線

        if (!username) {
            // 如果帳號為空
            showFieldError('username', '請輸入帳號');
            // 顯示欄位錯誤訊息
            return false;
            // 回傳驗證失敗
        }

        if (!usernameRegex.test(username)) {
            // 如果帳號格式不符合正規表達式
            showFieldError('username', '帳號格式不正確，請使用 4-20 個英文字母、數字或底線');
            // 顯示欄位錯誤訊息
            return false;
            // 回傳驗證失敗
        }

        clearFieldError('username');
        // 清除欄位錯誤訊息
        return true;
        // 回傳驗證成功
    }

    // 驗證電子郵件
    function validateEmail() {
        // 驗證電子郵件格式的函數
        var email = $('#email').val().trim();
        // 取得電子郵件輸入值並移除前後空白

        if (!email) {
            // 如果電子郵件為空（選填欄位）
            clearFieldError('email');
            // 清除欄位錯誤訊息
            return true;
            // 回傳驗證成功
        }

        var emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        // 定義電子郵件正規表達式：基本的電子郵件格式驗證

        if (!emailRegex.test(email)) {
            // 如果電子郵件格式不正確
            showFieldError('email', '電子郵件格式不正確');
            // 顯示欄位錯誤訊息
            return false;
            // 回傳驗證失敗
        }

        clearFieldError('email');
        // 清除欄位錯誤訊息
        return true;
        // 回傳驗證成功
    }

    // 驗證密碼
    function validatePassword() {
        // 驗證密碼強度的函數
        var password = $('#password').val();
        // 取得密碼輸入值

        if (!password) {
            // 如果密碼為空
            showFieldError('password', '請輸入密碼');
            // 顯示欄位錯誤訊息
            return false;
            // 回傳驗證失敗
        }

        if (password.length < 6) {
            // 如果密碼長度小於 6 個字元
            showFieldError('password', '密碼長度至少需要 6 個字元');
            // 顯示欄位錯誤訊息
            return false;
            // 回傳驗證失敗
        }

        clearFieldError('password');
        // 清除欄位錯誤訊息
        return true;
        // 回傳驗證成功
    }

    // 驗證確認密碼
    function validateConfirmPassword() {
        // 驗證確認密碼是否與密碼一致的函數
        var password = $('#password').val();
        // 取得密碼輸入值
        var confirmPassword = $('#confirmPassword').val();
        // 取得確認密碼輸入值

        if (!confirmPassword) {
            // 如果確認密碼為空
            showFieldError('confirmPassword', '請再次輸入密碼');
            // 顯示欄位錯誤訊息
            return false;
            // 回傳驗證失敗
        }

        if (password !== confirmPassword) {
            // 如果密碼和確認密碼不一致
            showFieldError('confirmPassword', '兩次輸入的密碼不一致');
            // 顯示欄位錯誤訊息
            return false;
            // 回傳驗證失敗
        }

        clearFieldError('confirmPassword');
        // 清除欄位錯誤訊息
        return true;
        // 回傳驗證成功
    }

    // 檢查密碼強度
    function checkPasswordStrength() {
        // 檢查密碼強度並顯示指示器的函數
        var password = $('#password').val();
        // 取得密碼輸入值
        var strength = 0;
        // 初始化密碼強度分數為 0

        if (password.length >= 6) strength++;
        // 如果密碼長度大於等於 6，強度加 1
        if (password.length >= 10) strength++;
        // 如果密碼長度大於等於 10，強度再加 1
        if (/[a-z]/.test(password) && /[A-Z]/.test(password)) strength++;
        // 如果密碼同時包含小寫和大寫字母，強度加 1
        if (/[0-9]/.test(password)) strength++;
        // 如果密碼包含數字，強度加 1
        if (/[^a-zA-Z0-9]/.test(password)) strength++;
        // 如果密碼包含特殊符號，強度加 1

        var $strengthBar = $('#passwordStrength');
        // 取得密碼強度指示器元素
        $strengthBar.removeClass('weak medium strong');
        // 移除所有強度樣式類別

        if (password.length === 0) {
            // 如果密碼為空
            $strengthBar.hide();
            // 隱藏強度指示器
        } else if (strength <= 2) {
            // 如果強度分數小於等於 2（弱密碼）
            $strengthBar.addClass('weak').show();
            // 添加弱密碼樣式並顯示
        } else if (strength <= 4) {
            // 如果強度分數小於等於 4（中等密碼）
            $strengthBar.addClass('medium').show();
            // 添加中等密碼樣式並顯示
        } else {
            // 如果強度分數大於 4（強密碼）
            $strengthBar.addClass('strong').show();
            // 添加強密碼樣式並顯示
        }
    }

    // 顯示欄位錯誤訊息
    function showFieldError(fieldName, message) {
        // 顯示特定欄位錯誤訊息的函數
        $('#' + fieldName).addClass('error');
        // 為輸入框添加錯誤樣式
        $('#' + fieldName + 'Error').text(message).show();
        // 設定錯誤訊息文字並顯示
    }

    // 清除欄位錯誤訊息
    function clearFieldError(fieldName) {
        // 清除特定欄位錯誤訊息的函數
        $('#' + fieldName).removeClass('error');
        // 移除輸入框的錯誤樣式
        $('#' + fieldName + 'Error').hide();
        // 隱藏錯誤訊息
    }

    // 註冊函數
    function register() {
        // 執行註冊操作的函數
        console.log('開始註冊...');
        // 在控制台輸出訊息

        // 驗證所有欄位
        var isUsernameValid = validateUsername();
        // 驗證帳號
        var isEmailValid = validateEmail();
        // 驗證電子郵件
        var isPasswordValid = validatePassword();
        // 驗證密碼
        var isConfirmPasswordValid = validateConfirmPassword();
        // 驗證確認密碼

        // 檢查是否同意服務條款
        if (!$('#agreeTerms').is(':checked')) {
            // 如果未勾選同意服務條款
            showError('請閱讀並同意服務條款和隱私政策');
            // 顯示錯誤訊息
            return;
            // 中止函數執行
        }

        // 如果有任何欄位驗證失敗
        if (!isUsernameValid || !isEmailValid || !isPasswordValid || !isConfirmPasswordValid) {
            // 如果任何一個驗證失敗
            showError('請修正表單中的錯誤');
            // 顯示錯誤訊息
            return;
            // 中止函數執行
        }

        // 取得表單資料
        var username = $('#username').val().trim();
        // 取得帳號
        var email = $('#email').val().trim();
        // 取得電子郵件
        var password = $('#password').val();
        // 取得密碼

        // 禁用註冊按鈕，防止重複提交
        $('#registerBtn').prop('disabled', true).text('註冊中...');
        // 禁用按鈕並改變按鈕文字

        // 準備註冊資料
        var registerData = {
            // 建立註冊資料物件
            username: username,
            // 帳號
            email: email || null,
            // 電子郵件（如果為空則傳 null）
            password: password
            // 密碼
        };

        console.log('發送註冊請求:', registerData);
        // 在控制台輸出註冊資料（密碼會被遮蔽）

        // 發送註冊請求
        $.ajax({
            // 發送 AJAX 請求
            url: contextPath + '/api/auth/register',
            // 請求 URL：註冊 API 端點
            type: 'POST',
            // HTTP 請求方法：POST
            contentType: 'application/json',
            // 請求內容類型：JSON
            data: JSON.stringify(registerData),
            // 將註冊資料物件轉換為 JSON 字串
            dataType: 'json',
            // 預期伺服器回傳的資料類型：JSON
            success: function(response) {
                // 請求成功時的回調函數
                console.log('註冊回應:', response);
                // 在控制台輸出回應資料

                if (response && response.success) {
                    // 如果註冊成功
                    showSuccess('註冊成功！3 秒後將跳轉到登入頁面...');
                    // 顯示成功訊息
                    setTimeout(function() {
                        // 設定延遲執行
                        window.location.href = contextPath + '/login.jsp';
                        // 3 秒後重定向到登入頁面
                    }, 3000);
                    // 延遲時間為 3000 毫秒（3 秒）
                } else {
                    // 如果註冊失敗
                    showError(response.message || '註冊失敗，請稍後再試');
                    // 顯示錯誤訊息
                    $('#registerBtn').prop('disabled', false).text('註冊');
                    // 恢復註冊按鈕
                }
            },
            error: function(xhr, status, error) {
                // 請求失敗時的回調函數
                console.error('註冊錯誤:', error);
                // 在控制台輸出錯誤訊息
                console.error('狀態碼:', xhr.status);
                // 在控制台輸出 HTTP 狀態碼
                console.error('回應內容:', xhr.responseText);
                // 在控制台輸出回應內容

                var errorMsg = '註冊時發生錯誤';
                // 預設錯誤訊息
                if (xhr.status === 409) {
                    // 如果狀態碼是 409（衝突，帳號已存在）
                    errorMsg = '該帳號已被註冊，請使用其他帳號';
                    // 設定錯誤訊息為帳號已存在
                } else if (xhr.responseJSON && xhr.responseJSON.message) {
                    // 如果回應包含錯誤訊息
                    errorMsg = xhr.responseJSON.message;
                    // 使用伺服器回傳的錯誤訊息
                }

                showError(errorMsg);
                // 顯示錯誤訊息
                $('#registerBtn').prop('disabled', false).text('註冊');
                // 恢復註冊按鈕
            }
        });
    }

    // 顯示成功訊息
    function showSuccess(message) {
        // 顯示成功訊息的函數
        $('#errorMessage').hide();
        // 隱藏錯誤訊息
        $('#successMessage').text(message).fadeIn();
        // 設定成功訊息文字並以淡入效果顯示
    }

    // 顯示錯誤訊息
    function showError(message) {
        // 顯示錯誤訊息的函數
        $('#successMessage').hide();
        // 隱藏成功訊息
        $('#errorMessage').text(message).fadeIn();
        // 設定錯誤訊息文字並以淡入效果顯示
        setTimeout(function() {
            // 設定延遲執行
            $('#errorMessage').fadeOut();
            // 5 秒後以淡出效果隱藏錯誤訊息
        }, 5000);
        // 延遲時間為 5000 毫秒（5 秒）
    }
</script>
<!-- JavaScript 程式碼區域結束 -->
</body>
<!-- 頁面主體區域結束 -->
</html>
<!-- HTML 文檔結束 -->
