const sql = /** @type {any} */ (require("sql-template-strings"));
const { query } = require("../../utils/mysql");

const serachStatement = (body) => sql `
SELECT COUNT(*) FROM my_tag
WHERE cardID = ${body.cardID}
`;

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
    var serachTotal

    try {
        const [rows, fields] = await query(serachStatement(req.body), req);
        serachTotal = rows[0]['COUNT(*)'];
    } catch (err) {
        req.flash("error", err);
        res.status(409).json({ errors: [err] });
    }

    try {
        req.body.times = serachTotal + 1;
        const [rows, fields] = await query(insertStatement(req.body), req);
        res.status(201).json({ message: "新增成功" });
    } catch (err) {
        req.flash("error", err);
        res.status(409).json({ errors: [err] });
    }
}

module.exports = addSchedule;