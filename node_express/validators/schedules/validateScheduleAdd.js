//資料驗證器
const { body } = require('express-validator');
const rejectOnInvalid = require('../../middlewares/rejectOnInvalid');

//TODO:express-validator沒有驗datetime的功能，之後記得用regex做
const validateScheduleAdd = [
    body('cardID').escape().trim().not().isEmpty(),
    rejectOnInvalid,
];

module.exports = validateScheduleAdd;