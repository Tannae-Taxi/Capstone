let mysql = require('mysql');

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
// Create Passengers    100
// Match Vehicle and Drivers
