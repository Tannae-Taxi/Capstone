i = '{"HELLO_3_ww aa" : {"j": 3}}'
let k = 'HELLO'
let h = 3
let p = 'ww aa'
console.log(JSON.parse(i)[`${k}_${h}_${p}`]);