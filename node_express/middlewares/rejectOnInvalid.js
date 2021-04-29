//匯入express-validator的validationResult
const { validationResult } = require('express-validator');

//宣告function rejectOnInvalid
function rejectOnInvalid(req, res, next) {
    //將req交給validationResult處理，結果儲存至result
    const result = validationResult(req);
    //result轉為array格式，結果儲存至errors
    const errors = result.array();

    //若errors的長度大於1，則把request的status設為422(422 Unprocessable Entity)
    //反之將繼續進行request
    if (errors.length) {
        res.status(422).json({ errors });
    } else {
        next();
    }
}

module.exports = rejectOnInvalid;