//匯入express
const express = require("express");
//匯入mysql
const mysql = require("mysql");
//匯入morgan
const logger = require("morgan");
//匯入express-flash
const flash = require("express-flash");
//匯入express-session
const session = require('express-session');
//在此專案尚未需要cookie，故註解
// const cookieParser = require('cookie-parser');
//匯入express-myconnection
const myConnection = require("express-myconnection");
//./middlewares/overwriteHtmlPath"
const overwriteHtmlPath = require("./middlewares/overwriteHtmlPath");
//匯入./routers
const RootRouter = require("./routers");

//匯入./config.json
const config = require("./config.json");
//宣告常數serverConfig，資料為config中的server
const serverConfig = config.server;
//宣告常數databaseConfig，資料為config.databases中的default
const databaseConfig = config.databases.default;

//宣告常數app為express物件
const app = express();

//設定資料庫選項
const databaseOptions = {
    host: databaseConfig.hostname,
    user: databaseConfig.username,
    password: databaseConfig.password,
    port: databaseConfig.port,
    database: databaseConfig.database,
};

//設定session資料
const sessionOptions = {
    secret: 'keyboard cat',
    resave: false,
    saveUninitialized: true,
    cookie: { maxAge: 60000 },
}

//log等級設為dev等級(開發等級)
app.use(logger("dev"));
//連結資料庫
app.use(myConnection(mysql, databaseOptions, "pool"));
//支援urlencoded
app.use(express.urlencoded({ extended: true }));
//格式為json
app.use(express.json());
//在此專案尚未需要cookie，故註解
// app.use(cookieParser('keyboard cat'));
//設定session資料
app.use(session(sessionOptions));
//釋放
app.use(flash());
//使用publicDir，並使用overwriteHtmlPath中間件處理路徑
app.use(overwriteHtmlPath("." + serverConfig.publicDir));
//使用static前端畫面(CSR)
app.use(express.static("." + serverConfig.publicDir));
//設定api主route為/api
app.use("/api", RootRouter);
//專案監聽位置為serverConfig的port(預設3000)
app.listen(serverConfig.port, () =>
    console.log("listen on", serverConfig.port)
);