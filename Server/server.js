// <<< Server >>>

// << Settings >>
// < Import >
let mysql = require('mysql');
let express = require('express');
let app = express();
let bodyParser = require('body-parser');

// < Uses >
app.use(bodyParser.json());
app.use(bodyParser.urlencoded({extended: true}));

// < MySQL Connection >
let connection = mysql.createConnection({
    host: 'localhost',
    user: 'capstone',
    database: 'tannae',
    password: 'zoqtmxhs17',
    port: 3306
});

// << Reqeust & Response >>
// < Account >
// Login
app.get('/account/login', (req, res) => {
    let id = req.query.id;
    let pw = req.query.pw;
    let ip = req.query.ip;
    console.log(ip);
    let sql = 'select * from User where binary id = ?';

    connection.query(sql, id, (err, result) => {
        let jsErr = {"error": "false"};

        if(err) {
            console.log(err)
        } else {
            if(result.length === 0) {
                jsErr.error = "등록된 사용자가 아닙니다.";
                console.log('/user/login : Not a user');
            } else if(pw !== result[0].pw.toString('utf-8')) {
                jsErr.error = "비밀번호가 잘못되었습니다.";
                console.log('/user/login : Password mismatch');
            } else {
                let sqlIP = "update User set ip = ? where id = cast(? as binary);";
                connection.query(sqlIP, [ip, id]);
                console.log('/user/login : Login success');
            }
        }
        result.unshift(jsErr);
        res.json(JSON.stringify(result));
    });
});

app.listen(3000, () => {
    console.log('Listening on port 3000');
});
