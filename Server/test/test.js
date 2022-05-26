let now = new Date();
console.log(new Date(now.getTime() + now.getTimezoneOffset() * 60000 + 32400000).toLocaleString());