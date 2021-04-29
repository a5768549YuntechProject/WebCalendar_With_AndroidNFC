/**
 * 將SQL DateTime格式轉為顯示格式並更動時區
 * @param {String} str YYYY-MM-DDThh:mm:ss.000Z
 * @returns YYYY-MM-DD hh:mm:ss
 */
function formatSQLDateTimeAndTimeZone(str) {
    //將輸入的字串以T作為分割
    let value1 = str.split("T");
    //將value1的第二部分以.作為分割
    let value2 = value1[1].split(".");
    //宣告變數d1為Date格式，並將value1的第一部分與value2的第一部分作為初始參數
    var d1 = new Date(value1[0] + " " + value2[0]);
    //在console印出offset值
    console.log("offset" + d1.getTimezoneOffset());
    //將時區更動為UTC+8(在不更動資料庫全域環境下，只能使用此方式)
    d1.setTime(d1.getTime() + 480 * 60 * 1000);
    //回傳更動完時區的時間格式字串
    return d1.toLocaleString();
    //return value1[0] + ' ' + value2[0]
}

/**
 * 畫出資料表格
 * @param {String[]} data 資料庫資料
 */
function renderTable(data) {
    //定義eventTable變數為id為eventTable的DOM
    let eventTable = document.getElementById("eventTable");
    //定義mainTr變數為叫做tr的新DOM
    let mainTr = document.createElement("tr");
    //定義idTd變數為叫做td的新DOM
    let idTd = document.createElement("td");
    //定義cardTd變數為叫做td的新DOM
    let cardTd = document.createElement("td");
    //定義dateTd變數為叫做td的新DOM
    let dateTd = document.createElement("td");
    //定義timesTd變數為叫做td的新DOM
    let timesTd = document.createElement("td");

    //將資料庫資料id附加到idTd上
    idTd.append(data["id"]);
    //將資料庫資料cardID附加到cardTd上
    cardTd.append(data["cardID"]);
    //將格式化過的資料庫資料Date附加到dateTd上
    dateTd.append(formatSQLDateTimeAndTimeZone(data["Date"]));
    //將資料庫資料times附加到timesTd上
    timesTd.append(data["times"]);

    //idTd附加到mainTr上
    mainTr.append(idTd);
    //idTd附加到cardTd上
    mainTr.append(cardTd);
    //idTd附加到dateTd上
    mainTr.append(dateTd);
    //idTd附加到timesTd上
    mainTr.append(timesTd);

    //將mainTr附加到eventTable上
    eventTable.append(mainTr);
}

try {
    //使用fetch進行request，method為get，對象為api網址加上api/schedules
    fetch(apiUrl + "api/schedules")
        .then((res) => {
            return res.json();
        })
        .then((result) => {
            //將result逐行處理，並將資料render到畫面上
            result.forEach((element) => {
                renderTable(element);
            });
        });
} catch (error) {
    console.log(error);
}
