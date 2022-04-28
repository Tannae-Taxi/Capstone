let mysql = require('mysql');

// < MySQL Connection >
connection = mysql.createConnection({
    host: 'localhost',
    user: 'capstone',
    database: 'tannae',
    password: 'zoqtmxhs17',
    port: 3306
});

// Vehicle tempory setting
let sql = 'insert Vehicle values(?, false, 0, "0,0", ?, null)';
for (let i = 0; i < 100; i++) {
    let vsn = 'v';
    for (let j = 0; j < 5 - (i + 1).toString().length; j++)
        vsn += '0';
    vsn += (i + 1).toString();
    
    connection.query(sql, [vsn, 'temp' + vsn], (err, result) => {
        if (err) {
            console.log(err.code)
        } else {
            console.log(vsn);
        }
    });
}