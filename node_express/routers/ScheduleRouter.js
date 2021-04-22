const { Router } = require("express");
const {
    addSchedule,
    removeSchedule,
    lastSchedule,
    listSchedule,
} = require("../services/schedules");
const validateScheduleAdd = require("../validators/schedules/validateScheduleAdd");

/** 排程路由 */
const ScheduleRouter = Router();

ScheduleRouter.get("/", listSchedule);
ScheduleRouter.get("/last", lastSchedule);
ScheduleRouter.post("/", validateScheduleAdd, addSchedule);
ScheduleRouter.delete("/", removeSchedule);

module.exports = ScheduleRouter;
