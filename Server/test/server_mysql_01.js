// <<< Server >>>

// << Settings >>
// < Import >
let mysql = require('mysql');
let express = require('express');
let app = express();
let server = require('http').createServer(app);
let io = require('socket.io')(server);
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
server.listen(3000, () => {
    console.log('Listening on port 3000');
});

// << Reqeust & Response >>
// < Account >
// Login
app.get('/account/login', (req, res) => {
    connection.query('select * from User where binary id = ?', req.query.id, (err, result) => {
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
    connection.query('select * from User where binary id = ?', req.query.id, (err, result) => {
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

    connection.query('select usn from User where usn like "u%" order by usn asc', (err, result) => {
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

            connection.query('insert User values(?, ?, ?, ?, ?, ?, ?, ?, false, 0, 4.5)', [usnNew, jsonReq.id, jsonReq.pw, jsonReq.uname, jsonReq.rrn, jsonReq.sex, jsonReq.phone, jsonReq.email, false, 0, 4.5], (err, result) => {
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
app.get('/account/findAccount', async (req, res) => {
    let jsonQue = req.query;

    connection.query('select * from User where uname = ?', jsonQue.uname, (err, result) => {
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

    connection.query('update User set id = ?, pw = ?, email = ?, phone = ? where usn = ?', [jsonReq.id, jsonReq.pw, jsonReq.email, jsonReq.phone, jsonReq.usn], (err, result) => {
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

    connection.query('delete from User where usn = ?', jsonReq.usn, (err, result) => {
        let resType = { "resType": "OK" };
        if (err) {
            console.log(err.code);
            resType.resType = "Error";
        } else
            console.log('/account/signout : Account is deleted');
        res.json(JSON.stringify(resType));
    });
});

// < User >
// Charge Point
app.post('/user/charge', (req, res) => {
    let jsonReq = req.body.nameValuePairs;

    connection.query('update User set point = ? where usn = ?', [jsonReq.point, jsonReq.usn], (err, result) => {
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

    connection.query('select * from History where usn = ?', jsonQue.usn, (err, result) => {
        let resType = { "resType": "OK" };
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
    connection.query('select * from Lost', (err, result) => {
        let resType = { "resType": "OK" };
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

    connection.query('select lsn from Lost where usn like "l%" order by usn asc', (err, result) => {
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

            connection.query('select vsn from Vehicle where usn = ?', jsonReq.usn, (err, result) => {
                if (err) {
                    console.log(err.code);
                    resType.resType = "Error";
                    res.json(JSON.stringify(resType));
                } else {
                    connection.query('insert Lost value(?, ?, ?, ?)', [lsnNew, jsonReq.date, jsonReq.type, result.vsn], (err, result) => {
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
    connection.query('select * from Content', (err, result) => {
        let resType = { "resType": "OK" };
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

    connection.query('update Content set title = ?, cont = ? where usn = ?', [jsonReq.title, jsonReq.cont, jsonReq.usn], (err, result) => {
        let resType = { "resType": "OK" };
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

    connection.query('select csn from Content where usn like "c%" order by usn asc', (err, result) => {
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

            connection.query('insert Content values(?, ?, ?, ?, ?)', [csnNew, jsonReq.title, jsonReq.cont, null, jsonReq.usn], (err, result) => {
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

// < Passenger >
// Get available vehicle
app.get('/passenger/reqVehicles', (req, res) => {
    let jsonQue = req.query;

    if (jsonQue.share) {
        connection.query('select * from Path where', (err, result) => {
            let resType = {"resType": "OK"};
            if (err) {
                console.log(err.code);
                resType.resType = "Error";
                res.json(JSON.stringify([resType]));
            } else {
                if (result.length == 0) {
                    console.log('/passenger/reqVehicles : No available vehicle exists');
                    resType.resType = "이용 가능한 차량이 없습니다.";
                } else {
                    for (let i = 0; i < result.length; i++) {
                        /////////////////////// 각 경로DB tuple들을 계산하여 불가능한 차량 delete
                    }
                }
            }
        });
    } else {
        connection.query('select * from Vehicle where state = true and num = 0 and usn is not null', (err, result) => {
                let resType = {"resType": "OK"};
                if (err) {
                    console.log(err.code);
                    resType.resType = "Error";
                    res.json(JSON.stringify([resType]))
                } else {
                    for (let i = 0; i < result.length; i++) {
                        let pos = result[i].split(' ');
                        let distance = Math.sqrt(Math.pow(jsonQue.originX - pos[0], 2) + Math.pow(jsonQue.originY - pos[1]));
                        if (distance < 30)  //////////////////////////////////////////////////최대 수치 정하기
                            delete result[i];
                    }
                    if (result.length == 0) {
                        console.log('/passenger/reqVehicles : No available vehicle exists');
                        resType.resType = "이용 가능한 차량이 없습니다.";
                    }
                    result.unshift(resType);
                    res.json(JSON.stringify(result));
                }
        });
    }
});

// < Driver >

// << Socket.io >>
io.on('connection', (socket) => {
    // < Connection >
    // Connected
    console.log(`Socket connected : ${socket.id}`);

    // Disconnected
    socket.on('disconnect', () => {
        console.log(`Socket disconnected : ${socket.id}`);
    });

    // < Driver >
    // Start service
    socket.on('serviceOn', (usn, vsn) => {
        console.log(`Driver ${usn} started service on vehicle ${vsn}`);
        socket.join(vsn);
    });

    // Stop service
    socket.on('serviceOff', (usn, vsn) => {
        console.log(`Drive ${usn} stopped service on vehicle ${vsn}`);
        socket.leave(vsn);
    });

    // < Passenger >
    // Request Service
    socket.on('Request Service', (usn, vsn) => {
        console.log(`Passenger ${usn} requested service to vehicle ${vsn}`);
        socket.join(vsn);
    });
});