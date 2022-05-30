let mysql = require('mysql2');
connection = mysql.createConnection({
    host: 'localhost',
    user: 'capstone',
    database: 'tannae',
    password: 'zoqtmxhs17',
    port: 3306
});
connection = connection.promise();

async function calc() {
    let [result, field] = await connection.query('select * from Data');

    for (let i = 0; i < result.length; i++) {
        let data = result[i];
        let costo = JSON.parse(data.costo);
        let costn = JSON.parse(data.costn);
         
    }
}