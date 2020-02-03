const http = require('http');

const server = http.createServer((request, response) => {
    // response.setHeader('Content-Type', 'text/html')
    // response.end('<h1>Helloposdasdasdasdasdasdoo</h1>')
    const user = {
        name: 'Andrea',
        surname: 'Franco'
    }
    response.setHeader('Content-Type', 'application/json');
    response.end(JSON.stringify(user));
})

server.listen(3000);