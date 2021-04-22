/**
 * 將SQL DateTime格式轉為顯示格式並更動時區
 * @param {String} str YYYY-MM-DDThh:mm:ss.000Z
 * @returns YYYY-MM-DD hh:mm:ss
 */
function formatSQLDateTimeAndTimeZone(str) {
    let value1 = str.split("T");
    let value2 = value1[1].split(".");
    var d1 = new Date(value1[0] + " " + value2[0]);
    console.log("offset" + d1.getTimezoneOffset());
    d1.setTime(d1.getTime() + 480 * 60 * 1000);
    return d1.toLocaleString();
    //return value1[0] + ' ' + value2[0]
}

/**
 * 畫出資料表格
 * @param {String[]} data 資料庫資料
 */
function renderTable(data) {
    let eventTable = document.getElementById("eventTable");
    let mainTr = document.createElement("tr");
    let idTd = document.createElement("td");
    let cardTd = document.createElement("td");
    let dateTd = document.createElement("td");
    let timesTd = document.createElement("td");
    let totalTd = document.createElement("td");

    idTd.append(data["id"]);
    cardTd.append(data["cardID"]);
    dateTd.append(formatSQLDateTimeAndTimeZone(data["Date"]));
    timesTd.append(data["times"]);
    totalTd.append(data["TotallyCount"]);

    mainTr.append(idTd);
    mainTr.append(cardTd);
    mainTr.append(dateTd);
    mainTr.append(timesTd);
    mainTr.append(totalTd);

    eventTable.append(mainTr);
}

try {
    fetch(apiUrl + "api/schedules")
        .then((res) => {
            return res.json();
        })
        .then((result) => {
            result.forEach((element) => {
                renderTable(element);
            });
        });
} catch (error) {
    console.log(error);
}
