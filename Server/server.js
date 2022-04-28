// <<< Server >>>

// << Settings >>
// < Import >
let mysql = require('mysql');
let express = require('express');
let app = express();
app.io = require('socket.io')();
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
        let resType = { "resType": "OK" };
        if (err) {
            console.log(err.code);
            resType.resType = "Error";
        } else {
            if (result.length !== 0) {
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
        let resType = { "resType": "OK" };
        if (err) {
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
    let jsonQue = req.query;

    let sql = 'select * from User where uname = ?';
    connection.query(sql, jsonQue.uname, (err, result) => {
        let resType = { "resType": "OK" };
        if (err) {
            console.log(err.code);
            resType.resType = "Error";
            res.json(JSON.stringify([resType]));
        } else {
            if (result.length === 0) {
                console.log('/account/findAccount : Not a user');
                resType.resType = "등록된 사용자가 아닙니다.";
            } else if (result[0].rrn !== jsonQue.rrn || result[0].phone !== jsonQue.phone || result[0].email !== jsonQue.email) {
                console.log('/account/findAccount : Wrong private info');
                resType.resType = "잘못된 사용자 정보입니다."
            } else
                console.log('/account/findAccount : Found user');
            result[0].id = String(result[0].id)
            result[0].pw = String(result[0].pw);
            result.unshift(resType);
            res.json(JSON.stringify(result));
        }
    });
});

// Edit Account
app.post('/account/editAccount', (req, res) => {
    let jsonReq = req.body.nameValuePairs;

    let sql = 'update User set id = ?, pw = ?, email = ?, phone = ? where usn = ?';
    connection.query(sql, [jsonReq.id, jsonReq.pw, jsonReq.email, jsonReq.phone, jsonReq.usn], (err, result) => {
        let resType = { "resType": "OK" };
        if (err) {
            console.log(err.code);
            resType.resType = "Error";
        } else
            console.log('/account/editAccount : Account is updated');
        res.json(JSON.stringify(resType));
    });
});

// Sign Out
app.post('/account/signout', (req, res) => {
    let jsonReq = req.body.nameValuePairs;

    let sql = 'delete from User where usn = ?';
    connection.query(sql, jsonReq.usn, (err, result) => {
        let resType = { "resType": "OK" };
        if (err) {
            console.log(err.code);
            resType.resType = "Error";
        } else
            console.log('/account/signout : Account is deleted');
        res.json(JSON.stringify(resType));
    });
});

// < Passenger >
// Get available vehicle
app.get('/passenger/getVehicle', (req, res) => {

});

// < User >
// Charge Point
app.post('/user/charge', (req, res) => {
    let jsonReq = req.body.nameValuePairs;

    let sql = 'update User set point = ? where usn = ?';
    connection.query(sql, [jsonReq.point, jsonReq.usn], (err, result) => {
        let resType = { "resType": "OK" };
        if (err) {
            console.log(err.code);
            resType.resType = "Error";
        } else
            console.log('/user/charge : Point us updated');
        res.json(JSON.stringify(resType));
    });
});

// Get History
app.get('/user/getHistory', (req, res) => {
    let jsonQue = req.query;

    let sql = 'select * from History where usn = ?';
    connection.query(sql, jsonQue.usn, (err, result) => {
        let resType = {"resType": "OK"};
        if (err) {
            console.log(err.code);
            resType.resType = "Error";
            res.json(JSON.stringify([resType]));
        } else {
            if (result.length == 0) {
                console.log('/user/getHistory : No history');
                resType.resType = "이용 현황이 없습니다.";
            } else
                console.log('/user/getHistory : History found');
            result.unshift(resType);
            res.json(JSON.stringify(result));
        }
    });
});

// Get Lost
app.get('/user/getLost', (req, res) => {
    let sql = 'select * from Lost';
    connection.query(sql, (err, result) => {
        let resType = {"resType": "OK"};
        if (err) {
            console.log(err.code);
            resType.resType = "Error";
            res.json(JSON.stringify([resType]));
        } else {
            if (result.length == 0) {
                console.log('/user/getLost : No Lost');
                resType.resType = "등록된 분실물이 없습니다.";
            } else
                console.log('/user/getLost : Lost list returned');
            result.unshift(resType);
            res.json(JSON.stringify(result));
        }
    });
});

// Post Lost
app.post('/user/postLost', (req, res) => {
    let jsonReq = req.body.nameValuePairs;

    let sql = 'select lsn from Lost where usn like "l%" order by usn asc';
    connection.query(sql, (err, result) => {
        let resType = { "resType": "OK" };
        if (err) {
            console.log(err.code);
            resType.resType = "Error";
            res.json(JSON.stringify(resType));
        } else {
            let lsnNew = 'l';
            for (let i = 0; i < result.length; i++) {
                let lsn = result[i].usn;
                lsn = lsn.replace('l', '');
                lsn = lsn.replace(/0/g, '');
                if (i + 1 !== Number(usn)) {
                    for (let j = 0; j < 5 - (i + 1).toString().length; j++)
                        lsnNew += '0';
                    lsnNew += (i + 1);
                }
            }
            if (lsnNew === 'l') {
                let lsnNum = result.length + 1;
                for (let j = 0; j < 5 - lsnNum.toString.length; j++)
                    lsnNew += '0';
                lsnNew += lsnNum;
            }
            sql = 'select vsn from Vehicle where usn = ?';
            connection.query(sql, jsonReq.usn, (err, result) => {
                if (err) {
                    console.log(err.code);
                    resType.resType = "Error";
                    res.json(JSON.stringify(resType));
                } else {
                    sql = 'insert Lost value(?, ?, ?, ?)';
                    connection.query(sql, [lsnNew, jsonReq.date, jsonReq.type, result.vsn], (err, result) => {
                        if (err) {
                            console.log(err.code);
                            resType.resType = "Error";
                            res.json(JSON.stringify([resType]));
                        } else 
                            console.log('/user/postLost : Lost inserted');
                        res.json(JSON.stringify(resType));
                    });
                }
            });
        }
    });
})

// Get Content
app.get('/user/getContent', (req, res) => {
    let sql = 'select * from Content';
    connection.query(sql, (err, result) => {
        let resType = {"resType": "OK"};
        if (err) {
            console.log(err.code);
            resType.resType = "Error";
            res.json(JSON.stringify([resType]));
        } else {
            if (result.length == 0) {
                console.log('/user/getContent : No Content');
                resType.resType = "등록된 컨텐츠가 없습니다.";
            } else 
                console.log('/user/getContent : Content list returned');
            result.unshift(resType);
            res.json(JSON.stringify(result));
        }
    });
});

// Edit Content
app.post('/user/editContent', (req, res) => {
    let jsonReq = req.body.nameValuePairs;

    let sql = 'update Content set title = ?, cont = ? where usn = ?';
    connection.query(sql, [jsonReq.title, jsonReq.cont, jsonReq.usn], (err, result) => {
        let resType = {"resType": "OK"};
        if (err) {
            console.log(err.code);
            resType.resType = "Error";
        } else
            console.log('/user/editContent : Content updated');
        res.json(JSON.stringify(resType));
    });
});

// Post Content
app.post('/user/postContent', (req, res) => {
    let jsonReq = req.body.nameValuePairs;

    let sql = 'select csn from Content where usn like "c%" order by usn asc';
    connection.query(sql, (err, result) => {
        let resType = { "resType": "OK" };
        if (err) {
            console.log(err.code);
            resType.resType = "Error";
            res.json(JSON.stringify(resType));
        } else {
            let csnNew = 'l';
            for (let i = 0; i < result.length; i++) {
                let csn = result[i].usn;
                csn = csn.replace('l', '');
                csn = csn.replace(/0/g, '');
                if (i + 1 !== Number(usn)) {
                    for (let j = 0; j < 5 - (i + 1).toString().length; j++)
                        csnNew += '0';
                    csnNew += (i + 1);
                }
            }
            if (csnNew === 'c') {
                let csnNum = result.length + 1;
                for (let j = 0; j < 5 - csnNum.toString.length; j++)
                    lsnNew += '0';
                csnNew += csnNum;
            }

            sql = 'insert Content values(?, ?, ?, ?, ?)';
            connection(sql, [csnNew, jsonReq.title, jsonReq.cont, null, jsonReq.usn], (err, result) => {
                if (err) {
                    console.log(err.code);
                    resType.resType = "Error";
                } else
                    console.log('/user/postContent : Content inserted');
                res.json(JSON.stringify(resType));
            });
        }
    });
});

// Socket.io
app.io.on('connection', (socket) => {
    // Driver
    socket.on('serviceOn', (vsn) => {
        socket.join(vsn);
    });
    socket.on('serviceOff', (vsn) => {
        socket.leave(vsn);
    });
    socket.on('serviceEnd', (vsn) => {
        
    });

    // Passenger
    socket.on('serviceReq', (vsn) => {
        socket.join(vsn);
    });
    
});