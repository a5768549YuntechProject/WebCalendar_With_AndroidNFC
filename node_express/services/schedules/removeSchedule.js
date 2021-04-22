const sql = /** @type {any} */ (require("sql-template-strings"));
const { query } = require("../../utils/mysql");

const insertStatement = (body) => sql`
DELETE FROM my_schedule
WHERE id=${body.id}`;

/**
 * 刪除排程
 * @param {import('express').Request} req
 * @param {import('express').Response} res
 */
async function removeSchedule(req, res) {
    try {
        const [rows, fields] = await query(insertStatement(req.body), req);
        res.status(200).json({ message: "刪除成功" });
    } catch (err) {
        req.flash("error", err);
        res.status(409).json({ errors: [err] });
    }
}

module.exports = removeSchedule;
