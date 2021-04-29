//TODO:處理type問題
//匯入sql-template-strings的sql
const sql = /** @type {any} */ (require("sql-template-strings"));
//匯入../../utils/mysql的query
const { query } = require("../../utils/mysql");

//mysql語法查詢，查詢卡號出現次數
const serachStatement = (body) => sql `
SELECT COUNT(*) FROM my_tag
WHERE cardID = ${body.cardID}
`;

//mysql語法查詢，插入資料
const insertStatement = (body) => sql `
INSERT INTO my_tag (cardID,times)
VALUES (
    ${body.cardID},
    ${body.times}
)`;

/**
 * 新增排程
 * @param {import('express').Request} req
 * @param {import('express').Response} res
 */
async function addSchedule(req, res) {
    //宣告變數serachTotal
    var serachTotal

    try {
        //查詢serachStatement語句
        const [rows, fields] = await query(serachStatement(req.body), req);
        //將查詢到的次數塞入serachTotal
        serachTotal = rows[0]['COUNT(*)'];
    } catch (err) {
        //釋放request
        req.flash("error", err);
        //response的狀態設為409(409 Conflict)
        res.status(409).json({ errors: [err] });
    }

    try {
        //將次數加一
        req.body.times = serachTotal + 1;
        //查詢insertStatement語句
        const [rows, fields] = await query(insertStatement(req.body), req);
        //response的狀態設為201(201 Created)
        res.status(201).json({ message: "新增成功" });
    } catch (err) {
        //釋放request
        req.flash("error", err);
        //response的狀態設為409(409 Conflict)
        res.status(409).json({ errors: [err] });
    }
}

module.exports = addSchedule;