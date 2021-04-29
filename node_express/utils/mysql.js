//匯入promisfy中的promisfy
const { promisfy } = require("promisfy");

/**
 * @param {import('express').Request} req
 * @param {import('sql-template-strings').SQLStatement} statement
 */
async function query(statement, req) {
    //將request的getConnevtion綁定到req上，並用promisfy包裝
    const connection = await promisfy(req.getConnection.bind(req))();
    //將connection的query綁定到connection上，並用promisfy包裝
    const [rows, fields] = await promisfy(connection.query.bind(connection))(statement);

    return [rows, fields];
}

module.exports.query = query;
