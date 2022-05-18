// <<<< Server >>>>

// <<< Settings >>>
// << Require >>
let mysql = require('mysql2');
let app = require('express')();
let server = require('http').createServer(app);
let io = require('socket.io')(server);
let bodyParser = require('body-parser');
let nav = require('./service.js');

// << Uses >>
app.use(bodyParser.json());
app.use(bodyParser.urlencoded({ extended: true }));

// << MySQL Connection >>
connection = mysql.createConnection({
    host: 'localhost',
    user: 'capstone',
    database: 'tannae',
    password: 'zoqtmxhs17',
    port: 3306
});
connection = connection.promise();

// << Listen >>
server.listen(3000, () => {
    console.log('Listening on port 3000');
});

// <<< Reqeust & Response >>>
// << Account >>
// < Login >
app.get('/account/login', async (req, res) => {
    let data = req.query;
    let resType = { "resType": "OK" };

    try {
        let [result, field] = await connection.query(`select usn, cast(id as char) as id, cast(pw as char) as pw, uname, rrn, gender, phone, email, drive, points, score, state from User where binary id = '${data.id}'`);
        if (result.length === 0) {
            // When entered ID is not registered
            console.log(`/account/login : ${data.id} is not a user`);
            resType.resType = "등록된 사용자가 아닙니다.";
        } else if (req.query.pw !== result[0].pw) {
            // When entered PW doesn't match with ID
            console.log(`/account/login : ${data.pw} is wrong password for ${data.id}`);
            resType.resType = "비밀번호가 잘못되었습니다.";
        } else
            // When entered ID & PW is registered correctly
            console.log(`/account/login : User ${result[0].usn} logged in`);
        result.unshift(resType);
        res.json(JSON.stringify(result));
    } catch (err) {
        // MySQL Error
        console.log(`MySQL error : ${err.code}`);
        resType.resType = "Error";
        res.json(JSON.stringify([resType]));
    }
});

// < Check ID >
app.get('/account/checkID', async (req, res) => {
    let data = req.query;
    let resType = { "resType": "OK" };
    try {
        let [result, field] = await connection.query(`select * from User where binary id = '${data.id}'`);
        if (result.length !== 0) {
            // When entered ID is already registered
            console.log(`/account/checkID : ${data.id} is already used`);
            resType.resType = "이미 등록된 ID입니다.";
        } else
            // When entered ID is not registered
            console.log(`/account/checkID : ID ${data.id} is permitted`);
    } catch (err) {
        // MySQL Error
        console.log(`MySQL error : ${err.code}`);
        resType.resType = "Error";
    }
    res.json(JSON.stringify([resType]));
});

// < Sign Up >
app.post('/account/signup', async (req, res) => {
    let data = req.body.nameValuePairs;
    let resType = { "resType": "OK" };

    try {
        // User Serial Number Generator
        let [result, field] = await connection.query('select usn from User where usn like "u%" order by usn asc');
        let usnNew = 'u';
        for (let i = 0; i < result.length; i++) {
            let usn = result[i].usn;
            usn = usn.replace('u', '');
            usn = Number(usn);
            if (i + 1 !== usn) {
                for (let j = 0; j < 5 - (i + 1).toString().length; j++)
                    usnNew += '0';
                usnNew += (i + 1);
            }
        }
        if (usnNew === 'u') {
            let usnNum = result.length + 1;
            for (let j = 0; j < 5 - usnNum.toString().length; j++)
                usnNew += '0';
            usnNew += usnNum;
        }


        // Insert new user to User database
        await connection.query(`insert User values('${usnNew}', '${data.id}', '${data.pw}', '${data.uname}', '${data.rrn}', ${data.gender}, '${data.phone}', '${data.email}', false, 0, 4.5, false)`);
        console.log(`/account/signup : User ${data.name} sign up successfully`);
    } catch (err) {
        // MySQL Error
        console.log(`MySQL error : ${err.code}`);
        resType.resType = "Error";
    }
    res.json(JSON.stringify([resType]));
});

// < Find Account >
app.get('/account/findAccount', async (req, res) => {
    let data = req.query;
    let resType = { "resType": "OK" };

    try {
        let [result, fields] = await connection.query(`select * from User where uname = '${data.uname}'`);
        if (result.length === 0) {
            // When entered name is not registered
            console.log(`/account/findAccount : ${data.uname} is not a user`);
            resType.resType = "등록된 사용자가 아닙니다.";
        } else if (result[0].rrn !== data.rrn || result[0].phone !== data.phone || result[0].email !== data.email) {
            // When entered private infos doesn't match with user name
            console.log(`/account/findAccount : ${data.uname} entered wrong private info`);
            resType.resType = "잘못된 사용자 정보입니다."
        } else
            // Found account
            console.log(`/account/findAccount : Found ${data.uname}'s account`);
        result[0].id = String(result[0].id)
        result[0].pw = String(result[0].pw);
        result.unshift(resType);
        res.json(JSON.stringify(result));
    } catch (err) {
        // MySQL Error
        console.log(`MySQL error : ${err.code}`);
        resType.resType = "Error";
        res.json(JSON.stringify([resType]));
    }
});

// < Edit Account >
app.post('/account/editAccount', async (req, res) => {
    let data = req.body.nameValuePairs;
    let resType = { "resType": "OK" };

    try {
        // Update user infos to new infos
        await connection.query(`update User set id = '${data.id}', pw = '${data.pw}', email = '${data.email}', phone = '${data.phone}' where usn = '${data.usn}'`);
        console.log('/account/editAccount : Account is updated');
    } catch (err) {
        // MySQL Error
        console.log(`MySQL error : ${err.code}`);
        resType.resType = "Error";
    }
    res.json(JSON.stringify([resType]));
});

// < Sign Out >
app.post('/account/signout', async (req, res) => {
    let data = req.body.nameValuePairs;
    let resType = { "resType": "OK" };

    try {
        // Delete user info from database
        await connection.query(`delete from User where usn = '${data.usn}'`);
        console.log('/account/signout : Account is deleted');
    } catch (err) {
        // MySQL Error
        console.log(`MySQL error : ${err.code}`);
        resType.resType = "Error";
    }
    res.json(JSON.stringify([resType]));
});
/////////////////////////////////////////////////////////////////////////// Checked complete line
// << User >>
// < Charge Point >
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
    res.json(JSON.stringify([resType]));
});

// < Get History >
app.get('/user/getHistory', async (req, res) => {
    let data = req.query;
    let resType = { "resType": "OK" };

    try {
        let [result, field] = await connection.query(`select * from History where usn = '${data.usn}'`);
        if (result.length === 0) {
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

// < Get Lost >
app.get('/user/getLost', async (req, res) => {
    let resType = { "resType": "OK" };

    try {
        let [result, field] = await connection.query('select * from Lost');
        if (result.length === 0) {
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

// < Post Lost >
app.post('/user/postLost', async (req, res) => {
    let data = req.body.nameValuePairs;
    let resType = { "resType": "OK" };

    try {
        // Lost Serial Number Generator
        let [result, field] = await connection.query('select lsn from Lost where lsn like "l%" order by lsn asc');
        let lsnNew = 'l';
        for (let i = 0; i < result.length; i++) {
            let lsn = result[i].lsn;
            lsn = lsn.replace('l', '');
            lsn = Number(lsn);
            if (i + 1 !== lsn) {
                for (let j = 0; j < 5 - (i + 1).toString().length; j++)
                    lsnNew += '0';
                lsnNew += (i + 1);
            }
        }
        if (lsnNew === 'l') {
            let lsnNum = result.length + 1;
            for (let j = 0; j < 5 - lsnNum.toString().length; j++)
                lsnNew += '0';
            lsnNew += lsnNum;
        }

        // Get VSN from driver(USN) who requested
        [result, field] = await connection.query(`select vsn from Vehicle where usn = '${data.usn}'`);

        // Post new Lost data
        await connection.query(`insert Lost value('${lsnNew}', ${data.date}, '${data.type}', '${result.vsn}')`);
        console.log(`/user/postLost : Driver ${data.usn} posted new Lost data`);
    } catch (err) {
        // MySQL Error
        console.log(err.code);
        resType.resType = "Error";
    }
    res.json(JSON.stringify([resType]));
})

// < Get Content >
app.get('/user/getContent', async (req, res) => {
    let resType = { "resType": "OK" };
    try {
        let [result, field] = await connection.query('select * from Content');
        if (result.length === 0) {
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

// < Edit Content >
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
    res.json(JSON.stringify([resType]));
});

// < Post Content >
app.post('/user/postContent', async (req, res) => {
    let data = req.body.nameValuePairs;
    let resType = { "resType": "OK" };

    try {
        let [result, field] = await connection.query('select csn from Content where csn like "c%" order by csn asc');
        let csnNew = 'c';
        for (let i = 0; i < result.length; i++) {
            let csn = result[i].lsn;
            csn = csn.replace('c', '');
            csn = Number(csn);
            if (i + 1 !== csn) {
                for (let j = 0; j < 5 - (i + 1).toString().length; j++)
                    csnNew += '0';
                csnNew += (i + 1);
            }
        }
        if (csnNew === 'c') {
            let csnNum = result.length + 1;
            for (let j = 0; j < 5 - csnNum.toString().length; j++)
                csnNew += '0';
            csnNew += csnNum;
        }

        await connection.query(`insert Content values('${csnNew}', '${data.title}', '${data.cont}', Null, '${data.usn}')`);
        console.log('/user/postContent : Content inserted');
    } catch (err) {
        console.log(err.code);
        resType.resType = "Error";
    }
    res.json(JSON.stringify([resType]));
});

// < Post Evaluate >
app.post('/user/evaluate', async (req, res) => {
    let data = req.body.nameValuePairs;
    let resType = { "resType": "OK"};

    try {
        let [result, field] = await connection.query(`select usn from Vehicle where license = '${data.license}'`);
        let usn = result[0].usn;
        await connection.query(`updata User set score = (score + ${data.score}) / 2 where license = '${data.license}'`);
    } catch (err) {
        ////////////////////////////////////
    }
});

// <<< Socket.io >>>
io.on('connection', (socket) => {
    // << Connection >>
    // < Connected >
    console.log(`Socket connected : ${socket.id}`);

    // < Disconnected >
    socket.on('disconnect', () => {
        console.log(`Socket disconnected : ${socket.id}`);
    });

    // << Driver >>
    // < Service On >
    socket.on('serviceOn', async (driver) => {
        await connection.query(`update Vehicle set state = true where usn = '${driver.usn}'`);                  // Update state of vehicle to true
        let [vehicles, field] = await connection.query(`select * from Vehicle where usn = '${driver.usn}'`);    // Select vehicle which driver started service
        socket.join(vehicles[0].vsn);                                                                           // Join vsn room
        console.log(`Driver ${driver.usn} started service on vehicle ${vehicles[0].vsn}`);                      // LOG
    });

    // < Service Off >
    socket.on('serviceOff', async (driver) => {
        await connection.query(`update Vehicle set state = false where usn = '${driver.usn}'`);                 // Update state of vehicle to false
        let [vehicles, field] = await connection.query(`select * from Vehicle where usn = '${driver.usn}'`);    // Select vehicle which driver ended service
        console.log(`Driver ${driver.usn} stopped servicing on vehicle ${vehicles[0].vsn}`);                    // LOG
    });

    // < Pass Waypoint >
    socket.on('passWaypoint', async (driver) => {
        // Setting
        let [vehicles, field] = await connection.query(`select * from Vehicle where usn = '${driver.usn}'`);    // Select vehicle driver is driving
        let vehicle = vehicles[0];                                                                              // Set vehicle
        let pass = JSON.parse(vehicle.pass);                                                                    // Get passed points of vehilce
        let unpass = JSON.parse(vehicle.unpass);                                                                // Get unpassed points of vehicle
        let passPoint = unpass.waypoints.length !== 0 ? unpass.waypoints[0] : unpass.destination;               // Get point just passed

        // If passed point is init point (vehicle) than init set 
        if (unpass.origin.name === 'Vehicle') {
            pass = {}
            pass.waypoints = [];
            pass.sections = [];
        }

        // Set pass & unpass
        if (unpass.waypoints.length !== 0) {                            // If no waypoints are left == vehicle arrived at destination
            pass.waypoints.push(unpass.origin);                         // Push unpass origin to pass waypoints
            pass.sections.push(unpass.sections.shift());                // Push unpass sections[0] to pass sections
            unpass.origin = unpass.waypoints.shift();                   // Set unpass origin to unpass waypoints[0]
            console.log(`Vehicle ${vehicle.vsn} has passed waypoint`);  // LOG
        } else {                                                        // If waypoints are left == vehicle arrived at waypoint
            pass.waypoints.push(unpass.origin);                         // Push unpass origin to pass wapoints
            pass.waypoints.push(unpass.destination);                    // Push unpass destination to pass waypoint
            pass.sections.push(unpass.sections.shift());                // Push unpass sections[0] to pass sections
            pass.distance = unpass.distance;                            // Update total distance
            pass.duration = unpass.duration;                            // Update total duration
            unpass = null;                                              // Set unpass as null
            console.log(`Vehicle ${vehicle.vsn} arrived at end point`); // LOG
        }

        // Get passenger of current waypoint
        let point = JSON.parse(vehicle.names)[`${passPoint.x}_${passPoint.y}_${passPoint.name}`];   // Get point json names data just passed
        let usn = point.usn;                                                                        // Get usn of point
        let type = point.type                                                                       // Get type of point

        // Update DB
        let pos = `${passPoint.x} ${passPoint.y}`;                  // New position
        let num = type === 'end' ? vehicle.num - 1 : vehicle.num;   // New number of passengers
        await connection.query(`update Vehicle set pos = '${pos}', num = ${num}, pass = '${JSON.stringify(pass)}', unpass = ${unpass !== null ? `'${JSON.stringify(unpass)}'` : null}${unpass === null ? ', state = false' : ''} where vsn = '${vehicle.vsn}'`);
        await connection.query(`update User set state = ${type == 'start' ? true : false} where usn = '${usn}'`);

        // Send response
        io.to(vehicle.vsn).emit('responseService', type == 'start' ? 4 : 5, unpass, usn);
    });

    // < End service >
    socket.on('serviceEnd', async (driver) => {
        // Select vehicle driver ended service
        let [vehicles, field] = await connection.query(`select * from Vehicle where usn = '${driver.usn}'`);
        let vehicle = vehicles[0];

        // Get pass, cost, names
        let pass = JSON.parse(vehicle.pass);
        let cost = vehicle.cost;
        let names = JSON.parse(vehicle.names);

        // Except info's of vehicle to first waypoint
        cost -= cost * pass.sections[0].distance / pass.distance;
        pass.distance -= pass.sections[0].distance;
        pass.duration -= pass.sections[0].duration;
        pass.waypoints.shift();
        pass.sections.shift();

        // Initial setting
        let result = {};    // Result for receipt { 'usn' : {name:String, start:String, end:String, cost:int}, ... , license:String }
        let count = 0;      // Number of users in the vehicle
        let current = [];   // User's usn who is riding
        result.license = vehicle.license;

        // Calculate cost
        for (let i = 0; i < pass.waypoints.length - 1; i++) {
            let point = pass.waypoints[i];
            let user = names[`${point.x}_${point.y}_${point.name}`];
            let usn = user.usn;
            let type = user.type;

            if (type === 'start') {
                current.push(usn);
                result[usn] = { start: point.name, end: null, cost: 0 };
                count++;
            } else {
                current.splice(users.indexOf(usn), 1);
                result[usn].end = point.name;
                count--;
            }

            if (count !== 0) {
                let pathCost = parseInt(cost * pass.sections[i].distance / pass.distance);
                for (let j = 0; j < current.length; j++)
                    result[current[i]].cost += pathCost / count;
            }
        }

        // Update User DB
        let usns = Object.keys(result);
        for (let i = 0; i < usns.length; i++) {
            let usn = usns[i];
            if (usn === 'license') continue;
            await connection.query(`update User set points = points - ${result[usn].cost} where usn = '${usn}'`);
        }

        // Update Vechile DB
        await connection.query(`update Vehicle set pass = null, unpass = null, share = null, gender = null, cost = null, names = null where vsn = '${vehicle.vsn}'`);

        // Update History DB
        for (let i = 0; i < usns.length; i++) {
            let usn = usns[i];
            if (usn === 'license') continue;

            let [history, field] = await connection.query('select hsn from History where hsn like "h%" order by hsn asc');
            let hsnNew = 'h';
            for (let i = 0; i < history.length; i++) {
                let hsn = history[i].hsn;
                hsn = hsn.replace('h', '');
                hsn = Number(hsn);
                if (i + 1 !== hsn) {
                    for (let j = 0; j < 5 - (i + 1).toString().length; j++)
                        hsnNew += '0';
                    hsnNew += (i + 1);
                }
            }
            if (hsnNew === 'h') {
                let hsnNum = history.length + 1;
                for (let j = 0; j < 5 - hsnNum.toString().length; j++)
                    hsnNew += '0';
                hsnNew += hsnNum;
            }

            await connection.query(`insert History values('${hsnNew}', '${vehicle.license}', '${new Date().toLocaleString()}', '${result[usn].start}', '${result[usn].end}', ${result[usn].cost}, '${usn}')`);
        }

        // Emit result
        io.to(vehicle.vsn).emit('serviceEnd', result);

        // Exit room
        let users = io.sockets.adapter.rooms.get(vehicle.vsn);
        for (let i = 0; i < users.length; i++)
            users[i].leave(vehicle.vsn);

        // LOG
        console.log(`Service ended on vehicle ${vehicle.vsn}`);
    });

    // << Passenger >>
    // < Request Service >
    socket.on('requestService', async (data) => {
        // Create new Service
        let service = new nav.Service(connection, socket, data);

        // Service
        try {
            // Set vechile
            let flag = await service.setVehicle();
            // Check if vehicle is allowed
            if (service.vehicle != null) {
                service.setPath();                                                                              // Set request path
                let destinationName = service.path.destination.name;                                            // Save destination name before request
                service.path = await service.reqPath();                                                         // Request path to kakao navigation api and return path data
                service.path.summary.destination.name = destinationName;                                        // Reset destination name after request
                await service.updateDB();                                                                       // Update Database
                socket.join(service.vehicle.vsn);                                                               // Join vsn room
                io.to(service.vehicle.vsn).emit('responseService', service.flag, service.path, data.user.usn);  // Send response to vsn room users
                console.log(`User ${data.user.usn} is matched with vehicle ${service.vehicle.vsn}`);
            } else {
                // When there is no vehicle available
                console.log(`No vehicle is available for user ${data.user.usn}(Start : ${data.start.name} / End : ${data.end.name})`);
                socket.emit('responseService', 0, null, null, null);
            }
        } catch (err) {
            // MySQL Error
            console.log(err);
            socket.emit('responseService', -1, null, null, null);
        }
    });
});