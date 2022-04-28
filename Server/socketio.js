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

app.io.on('connection', (socket) => {
    socket.io('disconnect', () => {
        console.log('User out');
    });

    socket.on('endService', (vsn) => {
    });
});

let sql = 'select * from Path where vpsn = ?';
connection.query(sql, vsn, (err, result) => {
    if (err)
        console.log('Socket(endService) : Error');
    else {
        let paths = result[0].paths;
        
    }
})