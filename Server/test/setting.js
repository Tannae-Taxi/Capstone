const mysql = require('mysql');
const fs = require('fs');

// < MySQL Connection >
connection = mysql.createConnection({
    host: 'localhost',
    user: 'capstone',
    database: 'tannae',
    password: 'zoqtmxhs17',
    port: 3306
});

// Create vehicles
function vechileTemp() {
    for (let i = 0; i < 100; i++) {
        let vsn = 'v';
        for (let j = 0; j < 5 - (i + 1).toString().length; j++)
            vsn += '0';
        vsn += (i + 1).toString();
        connection.query(`insert Vehicle values('${vsn}', false, '126.96913490121608 37.55701346877968', 'temp${vsn}', 0, null, null, null, null, null, null, null)`);
    }
}

// Create drivers       100
function createDrivers() {
    for (let i = 0; i < 100; i++) {
        let usn = 'u';
        for (let j = 0; j < 5 - (i + 1).toString().length; j++)
            usn += '0';
        usn += (i + 1).toString();
        usnNum = usn.replace('u', '');
        connection.query(`insert User values('${usn}', 'driverid${usnNum}', 'driverpw${usnNum}', 'driver${usnNum}', '980000-00${usnNum}', true, '010000${usnNum}', 'e${usnNum}@naver.com', true, 100000, 5.0, false)`);
    }
}
// Create Passengers    100
function createUsers() {
    for (let i = 100; i < 200; i++) {
        let usn = 'u';
        for (let j = 0; j < 5 - (i + 1).toString().length; j++)
            usn += '0';
        usn += (i + 1).toString();
        usnNum = usn.replace('u', '');
        connection.query(`insert User values('${usn}', 'passid${usnNum}', 'passpw${usnNum}', 'pass${usnNum}', '980000-00${usnNum}', true, '010000${usnNum}', 'e${usnNum}@naver.com', 0, 100000, 5.0, false)`);
    }
}
// Match Vehicle and Drivers
function matchDV() {
    for (let i = 0; i < 100; i++) {
        let usn = 'u';
        for (let j = 0; j < 5 - (i + 1).toString().length; j++)
            usn += '0';
        usn += (i + 1).toString();
        vsn = usn.replace('u', 'v');
        connection.query(`update Vehicle set usn = '${usn}' where vsn = '${vsn}'`);
    }
}

// Set vehicle coordinate
async function veco()  {
    fs.readFile('coordinate.txt', 'utf8', async (err, data) => {
        try {
            let [result, field] = await connection.query('select vsn from Vehicle');
            let dat = data.split("\r\n");
            for (let i = 0; i < dat.length; i++) {
                let da = dat[i].split(",");
                connection.query(`update Vehicle set pos = '${da[2]} ${da[1]}' where vsn = '${result[i].vsn}'`);
            }
        } catch (errs) {
            console.log(errs);
        }
    });
}