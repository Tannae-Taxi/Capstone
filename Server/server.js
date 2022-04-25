// <<< Server >>>

// << Settings >>
// < Import >
let mysql = require('mysql');
let express = require('express');
let app = express();
let bodyParser = require('body-parser');

// < Uses >
app.use(bodyParser.json());
app.use(bodyParser.urlencoded({ extended: true }));

// < MySQL Connection >
connection = mysql.createConnection({
    host: 'localhost',
    user: 'capstone',
    database: 'tannae',
    password: 'zoqtmxhs17',
    port: 3306
});

// < Listen >
app.listen(3000, () => {
    console.log('Listening on port 3000');
});

// << Reqeust & Response >>
// < Account >
// Login
app.get('/account/login', (req, res) => {
    let sql = 'select * from User where binary id = ?';
    connection.query(sql, req.query.id, (err, result) => {
        let resType = { "resType": "OK" };
        if (err) {
            console.log(err.code);
            resType.resType = "Error";
            res.json(JSON.stringify([resType]));
        } else {
            if (result.length === 0) {
                console.log('/account/login : Not a user');
                resType.resType = "등록된 사용자가 아닙니다.";
            } else if (req.query.pw !== result[0].pw.toString('utf-8')) {
                console.log('/account/login : Password mismatch');
                resType.resType = "비밀번호가 잘못되었습니다.";
            } else
                console.log('/account/login : Login success');
            result.unshift(resType);
            res.json(JSON.stringify(result));
        }
    });
});

// Check ID
app.get('/account/checkID', (req, res) => {
    let sql = 'select * from User where binary id = ?';
    connection.query(sql, req.query.id, (err, result) => {
        let resType = {"resType": "OK"};
        if(err) {
            console.log(err.code);
            resType.resType = "Error";
        } else {
            if(result.length !== 0) {
                console.log('/account/checkID : Used ID');
                resType.resType = "이미 등록된 ID입니다.";
            } else
                console.log('/account/checkID : ID permitted');
            res.json(JSON.stringify(resType));
        }
    });
});

    
// Sign Up
app.post('/account/signup', (req, res) => {
    let jsonReq = req.body.nameValuePairs;

    let sql = 'select usn from User where usn like "u%" order by usn asc';
    connection.query(sql, (err, result) => {
        let resType = {"resType": "OK"};
        if(err) {
            console.log(err.code);
            resType.resType = "Error";
            res.json(JSON.stringify([resType]));
        } else {
            let usnNew = 'u';
            for (let i = 0; i < result.length; i++) {
                let usn = result[i].usn;
                usn = usn.replace('u', '');
                usn = usn.replace(/0/g, '');
                if (i + 1 !== Number(usn)) {
                    for (let j = 0; j < 5 - (i + 1).toString().length; j++)
                        usnNew += '0';
                    usnNew += (i + 1);
                }
            }
            if (usnNew === 'u') {
                let usnNum = result.length + 1;
                for (let j = 0; j < 5 - usnNum.toString.length; j++)
                    usnNew += '0';
                usnNew += usnNum;
            }
    
            sql = 'insert User values(?, ?, ?, ?, ?, ?, ?, ?, false, 0, 4.5)'
            connection.query(sql, [usnNew, jsonReq.id, jsonReq.pw, jsonReq.uname, jsonReq.rrn, jsonReq.sex, jsonReq.phone, jsonReq.email, false, 0, 4.5], (err, result) => {
                if (err) {
                    console.log(err.code);
                    resType.resType = "Error";
                } else
                    console.log('/account/signup : Sign Up complete');
                res.json(JSON.stringify(resType));
            });
        }
    });
});

// Find Account
app.get('/account/findAccount', (req, res) => {
    let uname = req.query.uname;
    let rrn = req.query.rrn;
    let phone = req.query.phone;
    let email = req.query.email;
    let sql = 'select * from User where uname = ?';

    connection.query(sql, uname, (err, result) => {
        let resType = { "resType": "OK" };
        if (err) {
            console.log(err.code);
            resType.resType = "Error";
            res.json(JSON.stringify([resType]));
        } else {
            if (result.length === 0) {
                console.log('/account/findAccount : Not a user');
                resType.resType = "등록된 사용자가 아닙니다.";
            } else if (result[0].rrn !== rrn || result[0].phone !== phone || result[0].email !== email) {
                console.log('/account/findAccount : Wrong rrn');
                resType.resType = "잘못된 사용자 정보입니다."
            } else
                console.log('/account/findAccount : Found user');
            result.unshift(resType);
            res.json(JSON.stringify(result));  
        }
    });
});