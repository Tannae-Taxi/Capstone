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
app.post('/user/login', function (req, res) {
    console.log('Login Request');

    let id = req.body.id;
    let pw = req.body.pw;
    let sql = 'select * from User where id = ?';

    connection.query(sql, [id, pw], function (err, result) {
        let code = 404;
        let message = 'Error';

        if (err) {
            console.log(err);
        } else {
            if(result.length === 0) {
                code = 204;
                message = '존재하지 않는 계정입니다.';
                console.log('/user/login : Not a user');
            } else if(pw !== result[0].pw) {
                code = 204;
                message = '잘못된 비밀번호입니다.';
                console.log('/user/login : Wrong password');
            } else {
                code = 200;
                message = '로그인하였습니다.';
                console.log("/user/login : Login Success");
            }
        }

        res.json({
            'code': code,
            'message': message
        })
    });
});

app.listen(3000, () => {
    console.log('Listening on port 3000');
});