let [result, field] = await connection.query('select hsn from History where hsn like "h%" order by hsn asc');
let hsnNew = 'h';
for (let i = 0; i < result.length; i++) {
    let hsn = result[i].hsn;
    hsn = hsn.replace('h', '');
    hsn = Number(hsn);
    if (i + 1 !== hsn) {
        for (let j = 0; j < 5 - (i + 1).toString().length; j++)
            hsnNew += '0';
        hsnNew += (i + 1);
    }
}
if (hsnNew === 'h') {
    let hsnNum = result.length + 1;
    for (let j = 0; j < 5 - hsnNum.toString().length; j++)
        hsnNew += '0';
    hsnNew += hsnNum;
}

// MySQL Error
console.log(`MySQL error : ${err.code}`);