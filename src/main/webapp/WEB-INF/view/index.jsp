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
    <title>個人記帳系統</title>
    <!-- 設定瀏覽器標籤頁標題 -->
    <script src="https://code.jquery.com/jquery-3.7.1.js"></script>
    <!-- 引入 jQuery 3.7.1 函式庫，用於簡化 DOM 操作和 AJAX 請求 -->
    <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
    <!-- 引入 Chart.js 圖表函式庫，用於繪製圓餅圖 -->

    <script src="${pageContext.request.contextPath}/js/jwt-auth.js"></script>

    <style>
        /* CSS 樣式區域開始 */
        * {
            /* 全局樣式：重置所有元素的預設樣式 */
            margin: 0;
            /* 清除所有外邊距 */
            padding: 0;
            /* 清除所有內邊距 */
            list-style: none;
            /* 移除列表項目符號 */
        }

        .container {
            /* 主容器樣式 */
            width: 100vw;
            /* 寬度設為視窗寬度的 100% */
            margin: 50px auto;
            /* 上下外邊距 50px，左右自動置中 */
        }

        .container #pieChart {
            /* 圓餅圖容器樣式 */
            width: 50vw;
            /* 寬度設為視窗寬度的 50% */
            height: 50vh;
            /* 高度設為視窗高度的 50% */
            margin: 0 auto;
            /* 左右自動置中 */
        }

        .container h1 {
            /* 主標題樣式 */
            text-align: center;
            /* 文字置中對齊 */
            margin: 20px 0;
            /* 上下外邊距 20px */
        }

        .filter-toolbar {
            /* 篩選工具列樣式 */
            text-align: center;
            /* 文字置中對齊 */
            margin: 20px 0;
            /* 上下外邊距 20px */
            padding: 15px;
            /* 內邊距 15px */
            background-color: #f5f5f5;
            /* 背景顏色為淺灰色 */
            border-radius: 5px;
            /* 圓角半徑 5px */
        }

        .filter-toolbar select,
        .filter-toolbar input,
        .filter-toolbar button {
            /* 篩選工具列中的下拉選單、輸入框和按鈕樣式 */
            padding: 8px 12px;
            /* 內邊距：上下 8px，左右 12px */
            font-size: 14px;
            /* 字體大小 14px */
        }

        .filter-toolbar button {
            /* 篩選工具列按鈕樣式 */
            background-color: #007bff;
            /* 背景顏色為藍色 */
            color: white;
            /* 文字顏色為白色 */
            border: none;
            /* 移除邊框 */
            border-radius: 4px;
            /* 圓角半徑 4px */
            cursor: pointer;
            /* 滑鼠游標變為手型 */
        }

        .filter-toolbar button:hover {
            /* 篩選工具列按鈕滑鼠懸停時的樣式 */
            background-color: #0056b3;
            /* 背景顏色變為深藍色 */
        }

        .container .table-group {
            /* 表格群組容器樣式 */
            display: flex;
            /* 使用 Flexbox 布局 */
            flex-direction: row;
            /* 子元素橫向排列 */
            height: 90%;
            /* 高度為父容器的 90% */
            box-shadow: 10px 10px 5px grey;
            /* 陰影效果：右 10px 下 10px 模糊 5px 灰色 */
            position: relative;
            /* 相對定位，作為絕對定位子元素的參考 */
        }

        .container .btn-add-income,
        .container .btn-add-expense {
            /* 新增收入和支出按鈕的共同樣式 */
            position: absolute;
            /* 絕對定位 */
        }

        .container .btn-add-income {
            /* 新增收入按鈕樣式 */
            left: 0%;
            /* 定位在左側 */
            top: -5px;
            /* 定位在表格上方 5px */
        }

        .container .btn-add-expense {
            /* 新增支出按鈕樣式 */
            left: 50%;
            /* 定位在中間（50% 位置）*/
            top: -5px;
            /* 定位在表格上方 5px */
        }

        .container .table-group .btn-group {
            /* 表格內按鈕群組樣式 */
            display: flex;
            /* 使用 Flexbox 布局 */
            flex-direction: row;
            /* 子元素橫向排列 */
            justify-content: center;
            /* 水平置中對齊 */
            align-items: center;
            /* 垂直置中對齊 */
            gap: 10px;
            /* 子元素之間的間距 10px */
        }

        .container .balance {
            /* 結餘資訊區域樣式 */
            text-align: center;
            /* 文字置中對齊 */
            font-size: 20px;
            /* 字體大小 20px */
            margin: 20px 0;
            /* 上下外邊距 20px */
        }

        .popup {
            /* 彈出視窗樣式 */
            display: none;
            /* 預設隱藏 */
            position: fixed;
            /* 固定定位（相對於瀏覽器視窗）*/
            left: 50%;
            /* 定位在水平中間 */
            top: 50%;
            /* 定位在垂直中間 */
            transform: translate(-50%, -50%);
            /* 平移自身寬高的 50%，實現完全置中 */
            width: 350px;
            /* 寬度 350px */
            background-color: white;
            /* 背景顏色為白色 */
            border: 2px solid #333;
            /* 邊框：2px 實線 深灰色 */
            padding: 20px;
            /* 內邊距 20px */
            z-index: 1000;
            /* 層級設為 1000，確保在最上層 */
            box-shadow: 0 4px 6px rgba(0, 0, 0, 0.3);
            /* 陰影效果：下方 4px 模糊 6px 半透明黑色 */
            border-radius: 8px;
            /* 圓角半徑 8px */
        }

        .popup h2 {
            /* 彈出視窗標題樣式 */
            margin-bottom: 15px;
            /* 下方外邊距 15px */
        }

        .popup label {
            /* 彈出視窗標籤樣式 */
            display: inline-block;
            /* 行內區塊元素 */
            width: 80px;
            /* 寬度 80px */
            margin-bottom: 10px;
            /* 下方外邊距 10px */
        }

        .popup input[type="date"],
        .popup input[type="text"],
        .popup input[type="number"] {
            /* 彈出視窗中的日期、文字和數字輸入框樣式 */
            width: calc(100% - 90px);
            /* 寬度為 100% 減去 90px（扣除標籤寬度）*/
            padding: 5px;
            /* 內邊距 5px */
            margin-bottom: 10px;
            /* 下方外邊距 10px */
        }

        .popup button {
            /* 彈出視窗按鈕樣式 */
            margin-top: 10px;
            /* 上方外邊距 10px */
            padding: 8px 15px;
            /* 內邊距：上下 8px，左右 15px */
            margin-right: 10px;
            /* 右方外邊距 10px */
            cursor: pointer;
            /* 滑鼠游標變為手型 */
        }
    </style>
    <!-- CSS 樣式區域結束 -->
</head>
<!-- 頁面頭部區域結束 -->
<body>
<%-- 在 body 頂部加入使用者資訊和登出按鈕 --%>
<div style="position: fixed; top: 10px; right: 10px; background: white; padding: 10px 15px; border-radius: 5px; box-shadow: 0 2px 5px rgba(0,0,0,0.1); z-index: 1001; display: flex; align-items: center; gap: 15px;">
    <span style="color: #333;">歡迎，<strong id="currentUsername" style="color: #667eea;"></strong></span>
    <a href="#" id="logoutBtn" style="color: #667eea; text-decoration: none; padding: 5px 10px; border: 1px solid #667eea; border-radius: 4px; transition: all 0.3s; cursor: pointer;">登出</a>
</div>

<!-- 頁面主體區域開始 -->
<div class="container">
    <!-- 主容器開始 -->
    <canvas id="pieChart"></canvas>
    <!-- 圓餅圖的 Canvas 畫布元素 -->
    <p class="balance">目前結餘: 0 元</p>
    <!-- 結餘資訊顯示區域，初始值為 0 元 -->
    <h1>個人記帳系統</h1>
    <!-- 頁面主標題 -->

    <div class="filter-toolbar">
        <!-- 篩選工具列區域開始 -->
        <label for="filterMode">篩選模式：</label>
        <!-- 篩選模式標籤 -->
        <select id="filterMode">
            <!-- 篩選模式下拉選單 -->
            <option value="all">全部顯示</option>
            <!-- 選項：顯示所有資料 -->
            <option value="today">今天</option>
            <!-- 選項：顯示今天的資料 -->
            <option value="week">本週</option>
            <!-- 選項：顯示本週的資料 -->
            <option value="month" selected>本月</option>
            <!-- 選項：顯示本月的資料（預設選中）-->
            <option value="year">本年</option>
            <!-- 選項：顯示本年的資料 -->
            <option value="custom">自定義區間</option>
            <!-- 選項：自定義日期區間 -->
        </select>

        <span id="customDateRange" style="display: none; margin-left: 20px;">
            <!-- 自定義日期區間輸入區域，預設隱藏，左邊距 20px -->
            <label for="startDate">開始日期：</label>
            <!-- 開始日期標籤 -->
            <input type="date" id="startDate">
            <!-- 開始日期輸入框 -->
            <label for="endDate" style="margin-left: 10px;">結束日期：</label>
            <!-- 結束日期標籤，左邊距 10px -->
            <input type="date" id="endDate">
            <!-- 結束日期輸入框 -->
        </span>

        <button onclick="applyFilter()" style="margin-left: 20px;">套用篩選</button>
        <!-- 套用篩選按鈕，左邊距 20px，點擊時執行 applyFilter() 函數 -->
        <button onclick="resetFilter()" style="margin-left: 10px;">清除篩選</button>
        <!-- 清除篩選按鈕，左邊距 10px，點擊時執行 resetFilter() 函數 -->
    </div>
    <!-- 篩選工具列區域結束 -->

    <div class="table-group">
        <!-- 表格群組區域開始 -->
        <button class="btn-add-income" onclick="showPopup('income', 'add', null)">新增收入項目</button>
        <!-- 新增收入項目按鈕，點擊時執行 showPopup() 函數並傳入收入類型和新增模式 -->
        <table id="table-income" border="1" width="100%" height="100%">
            <!-- 收入明細表格，設定邊框、寬度和高度為 100% -->
            <caption>收入明細表</caption>
            <!-- 表格標題 -->
            <tr>
                <!-- 表頭列 -->
                <th>序號</th>
                <!-- 表頭：序號欄位 -->
                <th>進帳日期</th>
                <!-- 表頭：進帳日期欄位 -->
                <th>收入項目</th>
                <!-- 表頭：收入項目名稱欄位 -->
                <th>進帳金額</th>
                <!-- 表頭：進帳金額欄位 -->
                <th>操作按鈕</th>
                <!-- 表頭：操作按鈕（修改/刪除）欄位 -->
            </tr>
        </table>
        <!-- 收入明細表格結束 -->

        <button class="btn-add-expense" onclick="showPopup('expense', 'add', null)">新增支出項目</button>
        <!-- 新增支出項目按鈕，點擊時執行 showPopup() 函數並傳入支出類型和新增模式 -->
        <table id="table-expense" border="1" width="100%" height="100%">
            <!-- 支出明細表格，設定邊框、寬度和高度為 100% -->
            <caption>支出明細表</caption>
            <!-- 表格標題 -->
            <tr>
                <!-- 表頭列 -->
                <th>序號</th>
                <!-- 表頭：序號欄位 -->
                <th>出帳日期</th>
                <!-- 表頭：出帳日期欄位 -->
                <th>支出項目</th>
                <!-- 表頭：支出項目名稱欄位 -->
                <th>出帳金額</th>
                <!-- 表頭：出帳金額欄位 -->
                <th>操作按鈕</th>
                <!-- 表頭：操作按鈕（修改/刪除）欄位 -->
            </tr>
        </table>
        <!-- 支出明細表格結束 -->
    </div>
    <!-- 表格群組區域結束 -->

    <div id="popup" class="popup">
        <!-- 彈出視窗容器，預設隱藏 -->
        <form id="accountForm">
            <!-- 帳目表單 -->
            <h2>新增/修改項目</h2>
            <!-- 表單標題 -->
            <p id="itemTypeLabel">項目類型:</p>
            <!-- 項目類型標籤（動態顯示收入或支出）-->
            <input type="hidden" id="itemId">
            <!-- 隱藏欄位：儲存項目 ID（用於修改時識別項目）-->

            <label for="accountDate">日期:</label>
            <!-- 日期標籤 -->
            <input type="date" id="accountDate" required>
            <!-- 日期輸入框，設為必填欄位 -->
            <br>
            <!-- 換行 -->

            <label for="accountItem">項目:</label>
            <!-- 項目名稱標籤 -->
            <input type="text" id="accountItem" required>
            <!-- 項目名稱輸入框，設為必填欄位 -->
            <br>
            <!-- 換行 -->

            <label for="accountAmount">金額:</label>
            <!-- 金額標籤 -->
            <input type="number" id="accountAmount" min="1" required>
            <!-- 金額輸入框，最小值為 1，設為必填欄位 -->
            <br>
            <!-- 換行 -->

            <button type="button" onclick="hidePopup()">關閉</button>
            <!-- 關閉按鈕，點擊時執行 hidePopup() 函數關閉彈出視窗 -->
            <button type="button" onclick="save()">儲存</button>
            <!-- 儲存按鈕，點擊時執行 save() 函數儲存資料 -->
        </form>
        <!-- 帳目表單結束 -->
    </div>
    <!-- 彈出視窗容器結束 -->
</div>
<!-- 主容器結束 -->

<script>
    // 全域變數：取得應用程式的根路徑（使用 JSP 表達式）
    var contextPath = '<%= request.getContextPath() %>';
    var myPieChart = null;

    // 頁面載入時初始化
    $(document).ready(function() {
        console.log('========== 頁面初始化 ==========');

        //登出按鈕
        $('#logoutBtn').click(function(e) {
            e.preventDefault();
            localStorage.removeItem('jwtToken');
            localStorage.removeItem('username');
            localStorage.removeItem('userId');
            window.location.href = contextPath + '/logout';
        });

        // ========== JWT 認證檢查 ==========
        // 檢查使用者是否已登入，如果未登入則重定向到登入頁面
        requireAuth(contextPath, function(userData) {
            // Token 驗證成功的回調函數
            console.log('使用者已登入:', userData);

            // 顯示使用者名稱
            var username = userData.username || getUserInfo().username || '使用者';
            console.log('username:'+username);
            $('#currentUsername').text(username);

            // 初始化圓餅圖
            initializeChart();

            // 監聽篩選模式變化
            $('#filterMode').change(function() {
                var mode = $(this).val();
                if (mode === 'custom') {
                    $('#customDateRange').show();
                } else {
                    $('#customDateRange').hide();
                }
            });

            // 刪除收入項目
            $(document).on('click', '.btn-delete-income', function() {
                var id = $(this).data('id');
                deleteItem('income', id);
            });

            // 刪除支出項目
            $(document).on('click', '.btn-delete-expense', function() {
                var id = $(this).data('id');
                deleteItem('expense', id);
            });

            // 默認加載本月數據
            applyFilter();

            // ========== 啟動自動刷新 Token 機制（可選）==========
            // 每 5 分鐘檢查一次，如果 token 在 10 分鐘內過期則自動刷新
            // startAutoRefresh(contextPath, 5, 10);

            // 注意：所有 AJAX 請求都會自動加上 Authorization header
            // 因為 jwt-auth.js 中已經設定了全域攔截器
        });
    });

    // ========== 全局函數（可被 onclick 調用）==========

    // 初始化圖表
    function initializeChart() {
        // 初始化圓餅圖的函數
        var pieChart = document.getElementById('pieChart');
        // 取得圓餅圖的 Canvas 元素
        var data = {
            // 圖表資料物件
            labels: ['總收入', '總支出'],
            // 資料標籤：總收入和總支出
            datasets: [{
                // 資料集陣列
                data: [0, 0],
                // 初始資料：收入和支出都為 0
                backgroundColor: ['rgba(40, 167, 69, 0.8)', 'rgba(220, 53, 69, 0.8)'],
                // 背景顏色：綠色（收入）和紅色（支出），透明度 0.8
                borderColor: ['rgb(40, 167, 69)', 'rgb(220, 53, 69)'],
                // 邊框顏色：綠色和紅色
                borderWidth: 1,
                // 邊框寬度 1px
                hoverOffset: 4
                // 滑鼠懸停時的偏移量 4px
            }]
        };

        var config = {
            // 圖表配置物件
            type: 'pie',
            // 圖表類型：圓餅圖
            data: data,
            // 使用上面定義的資料物件
            options: {
                // 圖表選項
                responsive: true,
                // 啟用響應式設計
                plugins: {
                    // 插件配置
                    legend: { position: 'bottom' },
                    // 圖例位置設在底部
                    tooltip: {
                        // 提示框配置
                        callbacks: {
                            // 回調函數
                            label: function(context) {
                                // 自定義標籤顯示格式
                                return context.label + ': ' + context.raw.toLocaleString() + ' 元';
                                // 回傳格式：標籤名稱: 數值（千分位格式） 元
                            }
                        }
                    }
                }
            }
        };

        if (myPieChart) myPieChart.destroy();
        // 如果圖表已存在，先銷毀舊圖表（避免重複建立）
        myPieChart = new Chart(pieChart, config);
        // 建立新的圓餅圖實例並儲存到全域變數
    }

    // 載入所有數據
    function loadAllData() {
        // 載入所有收入和支出資料的函數
        console.log('載入所有數據...');
        // 在控制台輸出載入訊息

        $.ajax({
            // 發送 AJAX 請求
            url: contextPath + '/api/account/list',
            // 請求 URL：應用程式根路徑 + API 路徑
            type: 'GET',
            // HTTP 請求方法：GET
            dataType: 'json',
            // 預期伺服器回傳的資料類型：JSON
            success: function(response) {
                // 請求成功時的回調函數
                console.log('數據加載成功:', response);
                // 在控制台輸出回應資料
                if (response && response.success) {
                    // 如果回應存在且操作成功
                    renderIncomeTable(response.data.incomeList);
                    // 渲染收入表格
                    renderExpenseTable(response.data.expenseList);
                    // 渲染支出表格
                    calculateBalance();
                    // 計算並顯示結餘
                } else {
                    // 如果操作失敗
                    alert('載入數據失敗');
                    // 顯示錯誤提示
                }
            },
            error: function(xhr, status, error) {
                // 請求失敗時的回調函數
                console.error('載入失敗:', error);
                // 在控制台輸出錯誤訊息
                alert('載入數據時發生錯誤');
                // 顯示錯誤提示
            }
        });
    }

    // 加載篩選後的數據
    function loadFilteredData(startDate, endDate) {
        // 根據日期區間載入篩選後資料的函數
        console.log('加載篩選數據：', startDate, '到', endDate);
        // 在控制台輸出篩選日期範圍

        $.ajax({
            // 發送 AJAX 請求
            url: contextPath + '/api/account/filter',
            // 請求 URL：篩選 API 端點
            type: 'GET',
            // HTTP 請求方法：GET
            data: {
                // 請求參數
                startDate: startDate,
                // 開始日期
                endDate: endDate
                // 結束日期
            },
            dataType: 'json',
            // 預期伺服器回傳的資料類型：JSON
            success: function(response) {
                // 請求成功時的回調函數
                console.log('篩選數據成功:', response);
                // 在控制台輸出回應資料
                if (response && response.success) {
                    // 如果回應存在且操作成功
                    renderIncomeTable(response.data.incomeList);
                    // 渲染收入表格
                    renderExpenseTable(response.data.expenseList);
                    // 渲染支出表格
                    calculateBalance();
                    // 計算並顯示結餘
                } else {
                    // 如果操作失敗
                    alert('篩選數據失敗');
                    // 顯示錯誤提示
                }
            },
            error: function(xhr, status, error) {
                // 請求失敗時的回調函數
                console.error('篩選失敗:', error);
                // 在控制台輸出錯誤訊息
                alert('篩選數據時發生錯誤');
                // 顯示錯誤提示
            }
        });
    }

    // 渲染收入表格
    function renderIncomeTable(incomeList) {
        // 將收入資料渲染到表格的函數
        console.log('渲染收入表格，共 ' + incomeList.length + ' 筆');
        // 在控制台輸出收入資料筆數
        $('#table-income tr:gt(0)').remove();
        // 移除表格中除表頭外的所有列（索引大於 0 的列）

        for (var i = 0; i < incomeList.length; i++) {
            // 迴圈遍歷收入資料陣列
            var income = incomeList[i];
            // 取得當前收入項目
            var row = '<tr>' +
                // 建立表格列的 HTML 字串
                '<td>' + (i + 1) + '</td>' +
                // 第一欄：序號（從 1 開始）
                '<td>' + income.accountDate + '</td>' +
                // 第二欄：進帳日期
                '<td>' + income.accountItem + '</td>' +
                // 第三欄：收入項目名稱
                '<td>' + income.accountAmount.toLocaleString() + '</td>' +
                // 第四欄：進帳金額（使用千分位格式）
                '<td><div class="btn-group">' +
                // 第五欄：操作按鈕群組
                '<button onclick="editIncome(' + income.incomeItemId + ', this)">修改</button>' +
                // 修改按鈕，點擊時呼叫 editIncome() 並傳入 ID 和按鈕元素
                '<button class="btn-delete-income" data-id="' + income.incomeItemId + '">刪除</button>' +
                // 刪除按鈕，將 ID 儲存在 data-id 屬性中
                '</div></td>' +
                // 結束按鈕群組和表格儲存格
                '</tr>';
            // 結束表格列
            $('#table-income').append(row);
            // 將建立的列附加到收入表格中
        }
    }

    // 渲染支出表格
    function renderExpenseTable(expenseList) {
        // 將支出資料渲染到表格的函數
        console.log('渲染支出表格，共 ' + expenseList.length + ' 筆');
        // 在控制台輸出支出資料筆數
        $('#table-expense tr:gt(0)').remove();
        // 移除表格中除表頭外的所有列（索引大於 0 的列）

        for (var i = 0; i < expenseList.length; i++) {
            // 迴圈遍歷支出資料陣列
            var expense = expenseList[i];
            // 取得當前支出項目
            var row = '<tr>' +
                // 建立表格列的 HTML 字串
                '<td>' + (i + 1) + '</td>' +
                // 第一欄：序號（從 1 開始）
                '<td>' + expense.accountDate + '</td>' +
                // 第二欄：出帳日期
                '<td>' + expense.accountItem + '</td>' +
                // 第三欄：支出項目名稱
                '<td>' + expense.accountAmount.toLocaleString() + '</td>' +
                // 第四欄：出帳金額（使用千分位格式）
                '<td><div class="btn-group">' +
                // 第五欄：操作按鈕群組
                '<button onclick="editExpense(' + expense.expenseItemId + ', this)">修改</button>' +
                // 修改按鈕，點擊時呼叫 editExpense() 並傳入 ID 和按鈕元素
                '<button class="btn-delete-expense" data-id="' + expense.expenseItemId + '">刪除</button>' +
                // 刪除按鈕，將 ID 儲存在 data-id 屬性中
                '</div></td>' +
                // 結束按鈕群組和表格儲存格
                '</tr>';
            // 結束表格列
            $('#table-expense').append(row);
            // 將建立的列附加到支出表格中
        }
    }

    // 編輯收入
    function editIncome(id, button) {
        // 編輯收入項目的函數
        var row = $(button).closest('tr')[0];
        // 取得按鈕所在的表格列元素
        showPopup('income', 'edit', row, id);
        // 呼叫顯示彈出視窗函數，傳入收入類型、編輯模式、列元素和 ID
    }

    // 編輯支出
    function editExpense(id, button) {
        // 編輯支出項目的函數
        var row = $(button).closest('tr')[0];
        // 取得按鈕所在的表格列元素
        showPopup('expense', 'edit', row, id);
        // 呼叫顯示彈出視窗函數，傳入支出類型、編輯模式、列元素和 ID
    }

    // 新增或修改項目
    function save() {
        // 儲存收入或支出項目的函數（新增或修改）
        var popup = document.getElementById('popup');
        // 取得彈出視窗元素
        var type = popup.getAttribute('data-type');
        // 從彈出視窗取得項目類型（income 或 expense）
        var mode = popup.getAttribute('data-mode');
        // 從彈出視窗取得操作模式（add 或 edit）
        var itemId = document.getElementById('itemId').value;
        // 取得項目 ID（修改時使用）
        var date = document.getElementById('accountDate').value;
        // 取得日期輸入值
        var item = document.getElementById('accountItem').value;
        // 取得項目名稱輸入值
        var amount = document.getElementById('accountAmount').value;
        // 取得金額輸入值

        if (!date || !item.trim() || !amount) {
            // 驗證：如果日期、項目名稱或金額為空
            alert('請填寫所有欄位');
            // 顯示提示訊息
            return;
            // 中止函數執行
        }

        var amountNum = parseInt(amount);
        // 將金額轉換為整數
        if (isNaN(amountNum) || amountNum <= 0) {
            // 驗證：如果金額不是數字或小於等於 0
            alert('金額必須為大於零的整數');
            // 顯示提示訊息
            return;
            // 中止函數執行
        }

        var userInfo = getUserInfo();

        // 建立要傳送的資料物件
        var data = {
            // 日期
            accountDate: date,
            // 項目名稱
            accountItem: item,

            accountAmount: amountNum,
            // 金額

            userId:userInfo.userId
        };

        var url, method;
        // 宣告 URL 和 HTTP 方法變數
        if (mode === 'edit') {
            // 如果是編輯模式
            url = contextPath + '/api/' + type + '/update';
            // 設定更新 API 的 URL
            data[type + 'ItemId'] = itemId;
            // 在資料物件中加入項目 ID（incomeItemId 或 expenseItemId）
            method = 'POST';
            // 設定 HTTP 方法為 POST
        } else {
            // 如果是新增模式
            url = contextPath + '/api/' + type + '/add';
            // 設定新增 API 的 URL
            method = 'POST';
            // 設定 HTTP 方法為 POST
        }

        $.ajax({
            // 發送 AJAX 請求
            url: url,
            // 請求 URL（新增或更新）
            type: method,
            // HTTP 請求方法（POST）
            contentType: 'application/json',
            // 請求內容類型：JSON
            data: JSON.stringify(data),
            // 將資料物件轉換為 JSON 字串
            dataType: 'json',
            // 預期伺服器回傳的資料類型：JSON
            success: function(response) {
                // 請求成功時的回調函數
                if (response.success) {
                    // 如果操作成功
                    alert(response.message);
                    // 顯示成功訊息
                    hidePopup();
                    // 關閉彈出視窗
                    applyFilter();  // 重新套用當前篩選
                    // 重新載入資料以反映變更
                } else {
                    // 如果操作失敗
                    alert('操作失敗: ' + response.message);
                    // 顯示錯誤訊息
                }
            },
            error: function(xhr, status, error) {
                // 請求失敗時的回調函數
                console.error('儲存錯誤:', error);
                // 在控制台輸出錯誤訊息
                alert('儲存時發生錯誤');
                // 顯示錯誤提示
            }
        });
    }

    // 刪除項目
    function deleteItem(type, id) {
        // 刪除收入或支出項目的函數
        if (!confirm('確定要刪除這筆記錄嗎?')) {
            // 顯示確認對話框
            return;
            // 如果使用者取消，中止函數執行
        }

        $.ajax({
            // 發送 AJAX 請求
            url: contextPath + '/api/' + type + '/delete/' + id,
            // 請求 URL：刪除 API 端點 + 項目 ID
            type: 'POST',
            // HTTP 請求方法：POST
            dataType: 'json',
            // 預期伺服器回傳的資料類型：JSON
            success: function(response) {
                // 請求成功時的回調函數
                if (response.success) {
                    // 如果操作成功
                    alert(response.message);
                    // 顯示成功訊息
                    applyFilter();  // 重新套用當前篩選
                    // 重新載入資料以反映變更
                } else {
                    // 如果操作失敗
                    alert('刪除失敗: ' + response.message);
                    // 顯示錯誤訊息
                }
            },
            error: function(xhr, status, error) {
                // 請求失敗時的回調函數
                console.error('刪除錯誤:', error);
                // 在控制台輸出錯誤訊息
                alert('刪除時發生錯誤');
                // 顯示錯誤提示
            }
        });
    }

    // 彈窗函數
    function showPopup(type, mode, row, itemId) {
        // 顯示彈出視窗的函數
        var popup = document.getElementById('popup');
        // 取得彈出視窗元素
        popup.setAttribute('data-type', type);
        // 設定項目類型屬性（income 或 expense）
        popup.setAttribute('data-mode', mode);
        // 設定操作模式屬性（add 或 edit）

        document.querySelector('#popup h2').textContent = (mode === 'add' ? '新增' : '修改') + '項目';
        // 根據模式設定彈出視窗標題（新增或修改）
        document.querySelector('#itemTypeLabel').textContent = '項目類型: ' + (type === 'income' ? '收入項目' : '支出項目');
        // 根據類型設定項目類型標籤（收入或支出）

        if (mode === 'edit' && row) {
            // 如果是編輯模式且有表格列資料
            var cells = row.cells;
            // 取得表格列的所有儲存格
            document.getElementById('itemId').value = itemId || '';
            // 設定項目 ID
            document.getElementById('accountDate').value = cells[1].textContent.trim();
            // 設定日期（第二個儲存格）
            document.getElementById('accountItem').value = cells[2].textContent.trim();
            // 設定項目名稱（第三個儲存格）
            document.getElementById('accountAmount').value = cells[3].textContent.replace(/,/g, '').trim();
            // 設定金額（第四個儲存格，移除千分位逗號）
        } else {
            // 如果是新增模式
            document.getElementById('itemId').value = '';
            // 清空項目 ID
            document.getElementById('accountDate').value = '';
            // 清空日期
            document.getElementById('accountItem').value = '';
            // 清空項目名稱
            document.getElementById('accountAmount').value = '';
            // 清空金額
        }

        popup.style.display = 'block';
        // 顯示彈出視窗
    }

    function hidePopup() {
        // 隱藏彈出視窗的函數
        document.getElementById('popup').style.display = 'none';
        // 將彈出視窗設為隱藏
    }

    // 計算結餘
    function calculateBalance() {
        // 計算總收入、總支出和結餘的函數
        var totalIncome = 0;
        // 初始化總收入為 0
        var totalExpense = 0;
        // 初始化總支出為 0

        $('#table-income tr:gt(0)').each(function() {
            // 遍歷收入表格中除表頭外的所有列
            var amount = parseInt($(this).find('td').eq(3).text().replace(/,/g, '')) || 0;
            // 取得金額欄位的值（第四欄），移除逗號並轉為整數，若失敗則為 0
            totalIncome += amount;
            // 累加到總收入
        });

        $('#table-expense tr:gt(0)').each(function() {
            // 遍歷支出表格中除表頭外的所有列
            var amount = parseInt($(this).find('td').eq(3).text().replace(/,/g, '')) || 0;
            // 取得金額欄位的值（第四欄），移除逗號並轉為整數，若失敗則為 0
            totalExpense += amount;
            // 累加到總支出
        });

        var balance = totalIncome - totalExpense;
        // 計算結餘：總收入減去總支出
        var balanceColor = balance >= 0 ? 'green' : 'red';
        // 根據結餘決定顯示顏色：正數為綠色，負數為紅色

        $('.balance').html(
            // 更新結餘區域的 HTML 內容
            '<div style="color: green;">總收入: ' + totalIncome.toLocaleString() + ' 元</div>' +
            // 顯示總收入（綠色，千分位格式）
            '<div style="color: red;">總支出: ' + totalExpense.toLocaleString() + ' 元</div>' +
            // 顯示總支出（紅色，千分位格式）
            '<div style="color: ' + balanceColor + '; font-weight: bold;">結餘: ' + balance.toLocaleString() + ' 元</div>'
            // 顯示結餘（動態顏色，粗體，千分位格式）
        );

        if (myPieChart) {
            // 如果圓餅圖存在
            myPieChart.data.datasets[0].data = [totalIncome, totalExpense];
            // 更新圓餅圖的資料（總收入和總支出）
            myPieChart.update();
            // 重新渲染圓餅圖
        }
    }

    // 套用篩選
    function applyFilter() {
        // 套用篩選條件載入資料的函數
        var mode = $('#filterMode').val();
        // 取得當前選中的篩選模式
        var startDate, endDate;
        // 宣告開始和結束日期變數
        var today = new Date();
        // 取得今天的日期

        switch(mode) {
            // 根據篩選模式執行不同的邏輯
            case 'all':
                // 如果選擇全部顯示
                loadAllData();
                // 載入所有資料
                return;
            // 結束函數執行

            case 'today':
                // 如果選擇今天
                startDate = formatDate(today);
                // 開始日期設為今天
                endDate = formatDate(today);
                // 結束日期設為今天
                break;
            // 跳出 switch

            case 'week':
                // 如果選擇本週
                var monday = new Date(today);
                // 建立日期物件
                monday.setDate(today.getDate() - today.getDay() + 1);
                // 計算本週一的日期（getDay() 回傳 0-6，0 為週日）
                var sunday = new Date(monday);
                // 建立日期物件
                sunday.setDate(monday.getDate() + 6);
                // 計算本週日的日期（週一加 6 天）
                startDate = formatDate(monday);
                // 開始日期設為本週一
                endDate = formatDate(sunday);
                // 結束日期設為本週日
                break;
            // 跳出 switch

            case 'month':
                // 如果選擇本月
                startDate = formatDate(new Date(today.getFullYear(), today.getMonth(), 1));
                // 開始日期設為本月第一天
                endDate = formatDate(new Date(today.getFullYear(), today.getMonth() + 1, 0));
                // 結束日期設為本月最後一天（下個月的第 0 天）
                break;
            // 跳出 switch

            case 'year':
                // 如果選擇本年
                startDate = formatDate(new Date(today.getFullYear(), 0, 1));
                // 開始日期設為本年 1 月 1 日
                endDate = formatDate(new Date(today.getFullYear(), 11, 31));
                // 結束日期設為本年 12 月 31 日
                break;
            // 跳出 switch

            case 'custom':
                // 如果選擇自定義區間
                startDate = $('#startDate').val();
                // 取得使用者輸入的開始日期
                endDate = $('#endDate').val();
                // 取得使用者輸入的結束日期
                if (!startDate || !endDate) {
                    // 如果開始或結束日期為空
                    alert('請選擇開始和結束日期');
                    // 顯示提示訊息
                    return;
                    // 中止函數執行
                }
                break;
            // 跳出 switch
        }

        console.log('套用篩選：', startDate, '到', endDate);
        // 在控制台輸出篩選日期範圍
        loadFilteredData(startDate, endDate);
        // 呼叫載入篩選資料的函數
    }

    // 清除篩選
    function resetFilter() {
        // 重置篩選條件的函數
        $('#filterMode').val('all');
        // 將篩選模式設為全部顯示
        $('#customDateRange').hide();
        // 隱藏自定義日期區間輸入欄位
        $('#startDate').val('');
        // 清空開始日期
        $('#endDate').val('');
        // 清空結束日期
        loadAllData();
        // 載入所有資料
    }

    // 日期格式化工具函數
    function formatDate(date) {
        // 將日期物件格式化為 YYYY-MM-DD 字串的函數
        var year = date.getFullYear();
        // 取得年份
        var month = String(date.getMonth() + 1).padStart(2, '0');
        // 取得月份（加 1，因為月份從 0 開始），並補齊為兩位數
        var day = String(date.getDate()).padStart(2, '0');
        // 取得日期，並補齊為兩位數
        return year + '-' + month + '-' + day;
        // 回傳格式化後的日期字串
    }
</script>
<!-- JavaScript 程式碼區域結束 -->
</body>
<!-- 頁面主體區域結束 -->
</html>
<!-- HTML 文檔結束 -->