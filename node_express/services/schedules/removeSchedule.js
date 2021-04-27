const sql = /** @type {any} */ (require("sql-template-strings"));
const { query } = require("../../utils/mysql");

const lastStatement = (body) => sql`
SELECT id 
FROM my_tag 
ORDER BY id 
DESC LIMIT 0 , 1
`;

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
    var serachLast

    try {
        const [rows, fields] = await query(lastStatement(req.body), req);
        serachLast = rows[0]['id'];
    } catch (err) {
        req.flash("error", err);
        res.status(409).json({ errors: [err] });
    }


    try {
        req.body.id = serachLast;
        const [rows, fields] = await query(insertStatement(req.body), req);
        res.status(200).json({ message: "刪除成功" });
    } catch (err) {
        req.flash("error", err);
        res.status(409).json({ errors: [err] });
    }
}

module.exports = removeSchedule;
