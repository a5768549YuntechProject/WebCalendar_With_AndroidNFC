//匯入express中的Router
const { Router } = require("express");
//匯入../services/schedules中的addSchedule、removeSchedule、lastSchedule、listSchedule
const {
    addSchedule,
    removeSchedule,
    lastSchedule,
    listSchedule,
} = require("../services/schedules");
//匯入../validators/schedules/validateScheduleAdd的validateScheduleAdd
/** Add api資料驗證器 */
const validateScheduleAdd = require("../validators/schedules/validateScheduleAdd");

/** 排程路由 */
const ScheduleRouter = Router();

//根目錄get api
ScheduleRouter.get("/", listSchedule);
//根目錄get last api
ScheduleRouter.get("/last", lastSchedule);
//根目錄post api，並套上Add api資料驗證器
ScheduleRouter.post("/", validateScheduleAdd, addSchedule);
//根目錄delete api
ScheduleRouter.delete("/", removeSchedule);

module.exports = ScheduleRouter;
