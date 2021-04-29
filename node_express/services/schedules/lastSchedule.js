//TODO:處理type問題
//匯入sql-template-strings的sql
const sql = /** @type {any} */ (require("sql-template-strings"));
//匯入../../utils/mysql的query
const { query } = require("../../utils/mysql");

//mysql語法查詢，查詢最後一筆資料
const insertStatement = (body) =>
    sql`SELECT * FROM my_tag
    ORDER BY id DESC LIMIT 0 , 1
    `;

/**
 * 取得清單
 * @param {import('express').Request} req
 * @param {import('express').Response} res
 */
async function lastSchedule(req, res) {
    try {
        //查詢insertStatement語句
        const [rows, fields] = await query(insertStatement(req.body), req);
        //response的狀態設為200(200 OK)
        res.status(200).json(rows);
    } catch (err) {
        //釋放request
        req.flash("error", err);
        //response的狀態設為409(409 Conflict)
        res.status(409).json({ errors: [err] });
    }
}

module.exports = lastSchedule;
