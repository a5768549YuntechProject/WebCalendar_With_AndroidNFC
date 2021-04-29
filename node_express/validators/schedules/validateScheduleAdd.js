//匯入express-validator的body
const { body } = require('express-validator');
//匯入../../middlewares/rejectOnInvalid的rejectOnInvalid
const rejectOnInvalid = require('../../middlewares/rejectOnInvalid');

//驗證器
const validateScheduleAdd = [
    body('cardID').escape().trim().not().isEmpty(),
    rejectOnInvalid,
];

module.exports = validateScheduleAdd;