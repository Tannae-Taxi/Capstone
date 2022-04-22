module.exports = class ServerF {
    constructor(connection) {
        this.connection = connection;
    }

    usnGenerator() {
        let sql = 'select usn from User where usn like "u%" order by usn asc';
        connection.query(sql, (err, result) => {
            for (let i = 0; i < result.length; i++) {
                let usn = result[i].usn;
                usn = usn.replace('u', '');
                usn = usn.replace(/0/g, '');
                if (i + 1 != Number(usn)) {
                    let usnNew = 'u';
                    for (let j = 0; j < 5 - (i + 1).toString().length; j++)
                        usnNew += '0';
                    usnNew += (i + 1);
                    console.log('Generated ' + usnNew);
                    return usnNew;
                }
            }
            let usnNew = 'u';
            let usnNum = result.length + 1;
            for (let j = 0; j < 5 - usnNum.toString.length; j++)
                usnNew += '0';
            usnNew += usnNum;
            console.log('Generated ' + usnNew);
            return usnNew;
        });
    }
}