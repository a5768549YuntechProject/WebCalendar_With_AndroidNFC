//TODO:處理type問題
//匯入sql-template-strings的sql
const sql = /** @type {any} */ (require("sql-template-strings"));
//匯入../../utils/mysql的query
const { query } = require("../../utils/mysql");

//mysql語法查詢，查詢最後一筆id
const lastStatement = (body) => sql`
SELECT id 
FROM my_tag 
ORDER BY id 
DESC LIMIT 0 , 1
`;

//mysql語法查詢，刪除指定id資料
const insertStatement = (body) => sql`
DELETE FROM my_tag
WHERE id = ${body.id}
`;

/**
 * 刪除排程
 * @param {import('express').Request} req
 * @param {import('express').Response} res
 */
async function removeSchedule(req, res) {
    //宣告變數serachLast
    var serachLast;

    try {
        //查詢lastStatement語句
        const [rows, fields] = await query(lastStatement(req.body), req);
        //將查詢到的id塞入serachLast
        serachLast = rows[0]["id"];
    } catch (err) {
        //釋放request
        req.flash("error", err);
        //response的狀態設為409(409 Conflict)
        res.status(409).json({ errors: [err] });
    }

    try {
        //將查詢到的id帶入request的body中
        req.body.id = serachLast;
        //查詢insertStatement語句
        const [rows, fields] = await query(insertStatement(req.body), req);
        //response的狀態設為200(200 OK)
        res.status(200).json({ message: "刪除成功" });
    } catch (err) {
        //釋放request
        req.flash("error", err);
        //response的狀態設為409(409 Conflict)
        res.status(409).json({ errors: [err] });
    }
}

module.exports = removeSchedule;
