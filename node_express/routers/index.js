//匯入express中的Router
const { Router } = require('express')
//匯入ScheduleRouter
const ScheduleRouter = require('./ScheduleRouter');
/** 根路由 */
const RouterRoot = Router();
//Router使用ScheduleRouter，並建立/schedules路由
RouterRoot.use('/schedules', ScheduleRouter);
module.exports = RouterRoot;