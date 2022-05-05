// <<< Server >>>

// << Settings >>
// < Require >
let mysql = require('mysql2');
let express = require('express');
let app = express();
let server = require('http').createServer(app);
let io = require('socket.io')(server);
let bodyParser = require('body-parser');
let nav = require('./navigation.js');

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

connection = connection.promise();

// < Listen >
server.listen(3000, () => {
    console.log('Listening on port 3000');
});

// << Reqeust & Response >>
// < Account >
// Login
app.get('/account/login', async (req, res) => {
    let data = req.query;
    let resType = { "resType": "OK" };
    console.log(data.id);
    try {
        let [result, field] = await connection.query(`select * from User where binary id = '${data.id}'`);
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
    } catch (err) {
        console.log(err.code);
        resType.resType = "Error";
        res.json(JSON.stringify([resType]));
    }
});

// Check ID
app.get('/account/checkID', async (req, res) => {
    let data = req.query;
    let resType = { "resType": "OK" };
    try {
        let [result, field] = await connection.query(`select * from User where binary id = '${data.id}'`);
        if (result.length !== 0) {
            console.log('/account/checkID : Used ID');
            resType.resType = "이미 등록된 ID입니다.";
        } else
            console.log('/account/checkID : ID permitted');
    } catch (err) {
        console.log(err.code);
        resType.resType = "Error";
    }
    res.json(JSON.stringify(resType));
});

// Sign Up
app.post('/account/signup', async (req, res) => {
    let data = req.body.nameValuePairs;
    let resType = { "resType": "OK" };

    try {
        let [result, field] = await connection.query('select usn from User where usn like "u%" order by usn asc');
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
        await connection.query(`insert User values('${usnNew}', '${data.id}', '${data.pw}', '${data.uname}', '${data.rrn}', ${data.gender}, '${data.phone}', '${data.email}', false, 0, 4.5)`);
        console.log('/account/signup : Sign Up complete');
    } catch (err) {
        console.log(err.code);
        resType.resType = "Error";
    }
    res.json(JSON.stringify(resType));
});

// Find Account
app.get('/account/findAccount', async (req, res) => {
    let data = req.query;
    let resType = { "resType": "OK" };
    try {
        let [result, fields] = await connection.query(`select * from User where uname = '${data.uname}'`);
        if (result.length === 0) {
            console.log('/account/findAccount : Not a user');
            resType.resType = "등록된 사용자가 아닙니다.";
        } else if (result[0].rrn !== data.rrn || result[0].phone !== data.phone || result[0].email !== data.email) {
            console.log('/account/findAccount : Wrong private info');
            resType.resType = "잘못된 사용자 정보입니다."
        } else
            console.log('/account/findAccount : Found user');
        result[0].id = String(result[0].id)
        result[0].pw = String(result[0].pw);
        result.unshift(resType);
        res.json(JSON.stringify(result));
    } catch (err) {
        console.log(err.code);
        resType.resType = "Error";
        res.json(JSON.stringify([resType]));
    }
});

// Edit Account
app.post('/account/editAccount', async (req, res) => {
    let data = req.body.nameValuePairs;
    let resType = { "resType": "OK" };

    try {
        await connection.query(`update User set id = '${data.id}', pw = '${data.pw}', email = '${data.email}', phone = '${data.phone}' where usn = '${data.usn}'`);
        console.log('/account/editAccount : Account is updated');
    } catch (err) {
        console.log(err.code);
        resType.resType = "Error";
    }
    res.json(JSON.stringify(resType));
});

// Sign Out
app.post('/account/signout', async (req, res) => {
    let data = req.body.nameValuePairs;
    let resType = { "resType": "OK" };

    try {
        await connection.query(`delete from User where usn = '${data.usn}'`);
        console.log('/account/signout : Account is deleted');
    } catch (err) {
        console.log(err.code);
        resType.resType = "Error";
    }
    res.json(JSON.stringify(resType));
});

// < User >
// Charge Point
app.post('/user/charge', async (req, res) => {
    let data = req.body.nameValuePairs;
    let resType = { "resType": "OK" };

    try {
        await connection.query(`update User set point = ${data.point} where usn = '${data.usn}'`);
        console.log('/user/charge : Point us updated');
    } catch (err) {
        console.log(err.code);
        resType.resType = "Error";
    }
    res.json(JSON.stringify(resType));
});

// Get History
app.get('/user/getHistory', async (req, res) => {
    let data = req.query;
    let resType = { "resType": "OK" };

    try {
        let [result, field] = await connection.query(`select * from History where usn = '${data.usn}'`);
        if (result.length == 0) {
            console.log('/user/getHistory : No history');
            resType.resType = "이용 현황이 없습니다.";
        } else
            console.log('/user/getHistory : History found');
        result.unshift(resType);
        res.json(JSON.stringify(result));
    } catch (err) {
        console.log(err.code);
        resType.resType = "Error";
        res.json(JSON.stringify([resType]));
    }
});

// Get Lost
app.get('/user/getLost', async (req, res) => {
    let resType = { "resType": "OK" };

    try {
        let [result, field] = await connection.query('select * from Lost');
        if (result.length == 0) {
            console.log('/user/getLost : No Lost');
            resType.resType = "등록된 분실물이 없습니다.";
        } else
            console.log('/user/getLost : Lost list returned');
        result.unshift(resType);
        res.json(JSON.stringify(result));
    } catch (err) {
        console.log(err.code);
        resType.resType = "Error";
        res.json(JSON.stringify([resType]));
    }
});

// Post Lost
app.post('/user/postLost', async (req, res) => {
    let data = req.body.nameValuePairs;
    let resType = { "resType": "OK" };

    try {
        let [result, field] = await connection.query('select lsn from Lost where lsn like "l%" order by lsn asc');
        let lsnNew = 'l';
        for (let i = 0; i < result.length; i++) {
            let lsn = result[i].lsn;
            lsn = lsn.replace('l', '');
            lsn = lsn.replace(/0/g, '');
            if (i + 1 !== Number(lsn)) {
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

        [result, field] = await connection.query(`select vsn from Vehicle where usn = '${data.usn}'`);
        await connection.query(`insert Lost value('${lsnNew}', ${data.date}, '${data.type}', '${result.vsn}')`);
        console.log('/user/postLost : Lost inserted');
    } catch (err) {
        console.log(err.code);
        resType.resType = "Error";
    }
    res.json(JSON.stringify(resType));
})

// Get Content
app.get('/user/getContent', async (req, res) => {
    let resType = { "resType": "OK" };
    try {
        let [result, field] = await connection.query('select * from Content');
        if (result.length == 0) {
            console.log('/user/getContent : No Content');
            resType.resType = "등록된 컨텐츠가 없습니다.";
        } else
            console.log('/user/getContent : Content list returned');
        result.unshift(resType);
        res.json(JSON.stringify(result));
    } catch (err) {
        console.log(err.code);
        resType.resType = "Error";
        res.json(JSON.stringify([resType]));
    }
});

// Edit Content
app.post('/user/editContent', async (req, res) => {
    let data = req.body.nameValuePairs;
    let resType = { "resType": "OK" };

    try {
        await connection.query(`update Content set title = '${data.title}', cont = '${data.cont}' where usn = '${data.usn}'`);
        console.log('/user/editContent : Content updated');
    } catch (err) {
        console.log(err.code);
        resType.resType = "Error";
    }
    res.json(JSON.stringify(resType));
});

// Post Content
app.post('/user/postContent', async (req, res) => {
    let data = req.body.nameValuePairs;
    let resType = { "resType": "OK" };

    try {
        let [result, field] = await connection.query('select csn from Content where csn like "c%" order by csn asc');
        let csnNew = 'l';
        for (let i = 0; i < result.length; i++) {
            let csn = result[i].csn;
            csn = csn.replace('l', '');
            csn = csn.replace(/0/g, '');
            if (i + 1 !== Number(csn)) {
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
        await connection.query(`insert Content values('${csnNew}', '${data.title}', '${data.cont}', Null, '${data.usn}')`);
        console.log('/user/postContent : Content inserted');
    } catch (err) {
        console.log(err.code);
        resType.resType = "Error";
    }
    res.json(JSON.stringify(resType));
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
    socket.on('requestVehicle', async (data) => {
        let resType = { "resType": "OK" };
        let service = new nav.Service(connection, socket, data);

        try {
            await service.setVehicle();
            await service.setPath();
            service.path = await service.reqPath();
            //await service.updateDB();
            //socket.join(service.vehicle.vsn);
            //io.to(vsn).emit('newService', service.path);
        } catch (err) {
            console.log(err);
            resType.resType = "Error";
        }
    });
});