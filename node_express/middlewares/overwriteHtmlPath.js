//匯入fs
const fs = require('fs');
//宣告publicdir常數為./public
const publicdir = './public';

/**
 * 覆寫HTML路徑
 * @param {string} rootPath 根路徑
 */
function overwriteHtmlPath(rootPath) {
    /**
     * @param {import('express').Request} req
     * @param {import('express').Response} res
     * @param {import('express').NextFunction} next
     */
    const overwriteHtmlPath = (req, res, next) => {
        //若request中的path沒有.，並且path中也沒有/
        if (req.path.indexOf('.') === -1 && req.path !== '/') {
            //宣告常數filePath，值為根目錄加上request的path與.html（完整檔案路徑）
            const filePath = rootPath + req.path + '.html';

            //將request的url加上檔名(html)
            fs.stat(filePath, function(err, stat) {
                if (stat) {
                    req.url += '.html';
                }

                next();
            });
        } else {
            next();
        }
    }

    return overwriteHtmlPath;
}

module.exports = overwriteHtmlPath;