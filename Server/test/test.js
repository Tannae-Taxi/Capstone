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


result = (connection.query('select * from User'))[0];
console.log(result);

