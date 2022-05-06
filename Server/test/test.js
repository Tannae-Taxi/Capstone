let i = false;

function test() {
    console.log('Hello');
    return true;
}

if (i && test()) {
    console.log('world');
}