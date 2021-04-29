//定義today變數為Date格式
let today = new Date();
//定義currentMonth變數為today的getMonth(取得月份)
let currentMonth = today.getMonth();
//定義currentYear變數為today的getFullYear(取得年分)
let currentYear = today.getFullYear();
/** 月份字串 */
let months = [
    "January",
    "February",
    "March",
    "April",
    "May",
    "June",
    "July",
    "August",
    "September",
    "October",
    "November",
    "December",
];

//定義table變數為id為tabapay_calendar的DOM
let table = document.getElementById("tabapay_calendar");
//定義header變數為id為calendar_header的DOM
let header = document.getElementById("calendar_header");
//定義modal變數為id為calendar_modal的DOM
let modal = document.getElementById("calendar_modal");
//定義span變數為class為close的第一個DOM
let span = document.getElementsByClassName("close")[0];
//定義modal_body變數為class為body的第一個DOM
let modal_body = document.getElementsByClassName("modal-body")[0];

/**
 * render上個月份的資料
 */
function previous() {
    //若currentMonth等於0，就把currentYear減一，反之不更動
    currentYear = currentMonth === 0 ? currentYear - 1 : currentYear;
    //若currentMonth等於0，就把currentMonth設為11，反之將currentMonth減一
    currentMonth = currentMonth === 0 ? 11 : currentMonth - 1;

    //將id為calendar_header的DOM中的innerHTML(內容)設為當月英文名及日期
    document.getElementById("calendar_header").innerHTML = months[currentMonth] + " " + currentYear;
    //在console印出月及年
    console.log([currentMonth, currentYear]);
    //fetch後端並且render出日曆
    showCalendar(currentMonth, currentYear);
}

/**
 * render下個月份的資料
 */
function next() {
    //若currentMonth等於11，就把currentYear加一，反之不更動
    currentYear = currentMonth === 11 ? currentYear + 1 : currentYear;
    //若currentMonth等於11，就把currentMonth設為0，反之將currentMonth加一
    currentMonth = currentMonth === 11 ? 0 : currentMonth + 1;

    //將id為calendar_header的DOM中的innerHTML(內容)設為當月英文名及日期
    document.getElementById("calendar_header").innerHTML = months[currentMonth] + " " + currentYear;
    //在console印出月及年
    console.log([currentMonth, currentYear]);
    //fetch後端並且render出日曆
    showCalendar(currentMonth, currentYear);
}

/**
 * 檢查傳入日期是否為今日
 * @param {number} year 年
 * @param {number} month 月
 * @param {number} date 日
 * @returns 傳入日期是否為今日
 */
function valiNowDate(year, month, date) {
    return date === today.getDate() && month === today.getMonth() && year === today.getFullYear();
}

/**
 * 列出卡號清單並儲存於全域變數內
 * @param {String[]} data
 */
function listEvent(data) {
    //將卡號及日期綁定在同一字串
    data.forEach((element) => {
        list.push(element["Date"] + "#" + element["cardID"]);
    });
    //在consloe印出list
    console.log(list);
}

/**
 * 將資料庫內的event格式化成YYYY-MM-DDThh:mm:ss.000Z#{event}
 * @param {string | number} year
 * @param {string | number} month
 * @param {string | number} date
 * @param {string} eventString
 * @returns YYYY-MM-DDThh:mm:ss.000Z#{event}
 */
function putEvent(year, month, date, eventString) {
    //將month加一(month從0開始)
    /** @type {any} */ (month) += 1;
    //將eventString以#分割，並取得第二份(取得事件)
    let event = eventString.split("#")[1];
    //將eventString以T分割，並取得第一份(取得年月日)
    let dateString = eventString.split("T")[0];
    //將dateString以-分割，並取得第一份(取得年)
    let _year = dateString.split("-")[0];
    //將dateString以-分割，並取得第二份(取得月)
    let _month = dateString.split("-")[1];
    //將dateString以-分割，並取得第三份(取得日)
    let _date = dateString.split("-")[2];
    //將年轉為字串格式
    year = year.toString();
    //將月轉為字串格式
    month = month.toString();
    //將日轉為字串格式
    date = date.toString();

    //若month只有個位數，前面就補0
    if (month.length === 1) {
        month = "0" + month;
    }
    //若date只有個位數，前面就補0
    if (date.length === 1) {
        date = "0" + date;
    }
    //若傳入日期與現在處理一樣，就傳回事件，反之傳回空字串
    if (year === _year && month === _month && date === _date) {
        return event;
    } else {
        return "";
    }
}

/**
 * fetch後端並且render出日曆
 * @param {number} month
 * @param {number} year
 */
function showCalendar(month, year) {
    //取得當月的總日期
    let daysInMonth = 32 - new Date(year, month, 32).getDate();
    //取得一個月第一個星期的日期
    let firstDayOfWeek = new Date(year, month).getDay();
    //定義table_body變數為id為calendar_body的DOM
    let table_body = /**@type {any} */ (document.getElementById("calendar_body"));
    //設定table_body的innerHTML(內容)為空字串
    table_body.innerHTML = "";

    //若globalThis.data為空(全域變數內沒有之前取到值)
    if (globalThis.data === undefined || globalThis.data === null) {
        //呼叫fetch做request，method為get，對象為API位置+api/schedule
        fetch(apiUrl + "api/schedules")
            .then((res) => {
                return res.json();
            })
            .then((result) => {
                //將資料儲存至globalThis.data(全域變數)
                globalThis.data = result;

                //呼叫listEvent
                listEvent(globalThis.data);

                //控制id為calendar_header的DOM的innerHTML(內容)為當月名稱及年份
                document.getElementById("calendar_header").innerHTML = months[month] + " " + year;
                //宣告變數date，初始值為1
                let date = 1;
                //宣告變數week，初始值為空陣列
                let week = [];
                //宣告變數weekEvent，初始值為空陣列
                let weekEvent = [];
                //雙重迴圈，處理印出日曆
                for (let i = 0; i < 6; i++) {
                    //宣告變數row，初始值為table_body的DOM的insertRow(處理列)
                    let row = table_body.insertRow(i);
                    for (let j = 0; j < 7; j++) {
                        //宣告變數cell，初始值為row的DOM的insertCell(處理行)
                        let cell = row.insertCell(j);
                        //若當行當列沒有日期，cell的innerHTML(內容)為空字串
                        if (i === 0 && j < firstDayOfWeek) {
                            cell.innerHTML = "";
                            //若日期大於總天數則跳過迴圈
                        } else if (date > daysInMonth) {
                            break;
                            //正常日期塞入字串
                        } else {
                            //判斷日期是否為今天，是的話加上css current_date
                            if (valiNowDate(year, month, date)) {
                                cell.classList.add("current_date");
                            }

                            //定義變數event，初始值為空字串
                            let event = "";

                            //將list逐一交給putEvent檢查，檢查通過的寫入event
                            list.forEach((element) => {
                                if (putEvent(year, month, date, element) !== "") {
                                    event += putEvent(year, month, date, element) + "<br/>";
                                }
                            });

                            //將date放入week陣列
                            week.push(date);
                            //將event放入weekEvent陣列
                            weekEvent.push(event);
                            //在console印出event
                            console.log(event);
                            //將處理日的innerHTML(內容)設為日期加event
                            cell.innerHTML = "" + date + "<br/><br/>" + event;
                            //將處理日的id設為date
                            cell.id = date;
                            //date加一
                            date++;
                        }
                    }
                }
            });
    } else {
        document.getElementById("calendar_header").innerHTML = months[month] + " " + year;
        let date = 1;
        for (let i = 0; i < 6; i++) {
            let row = table_body.insertRow(i);
            for (let j = 0; j < 7; j++) {
                let cell = row.insertCell(j);
                if (i === 0 && j < firstDayOfWeek) {
                    cell.innerHTML = "";
                } else if (date > daysInMonth) {
                    break;
                } else {
                    if (valiNowDate(year, month, date)) {
                        cell.classList.add("current_date");
                    }

                    let event = "";

                    list.forEach((element) => {
                        if (putEvent(year, month, date, element) !== "") {
                            event += putEvent(year, month, date, element) + "<br/>";
                        }
                    });

                    console.log(event);
                    cell.innerHTML = "" + date + "<br/><br/>" + event;
                    cell.id = date;
                    date++;
                }
            }
        }
    }
}

//初始化畫面
showCalendar(currentMonth, currentYear);
