const express = require('express');
const bodyparser = require('body-parser');
const app = express();
const cors = require('cors');
const knex = require('knex');

const database = knex({
    client: 'pg',
    connection: {
        host: '127.0.0.1',
        user: 'andreafranco',
        password: '',
        database: 'plant-growing-db'
    }
})

database.select('*').from('datacollection').then(data => {
    console.log(data);
});
//INFO
/*
We could install  npm install body-parser and 
app.use(bodyparser.urlencoded({extended: false}));
app.use(bodyparser.json());
but since express's version is 16+ we can use
app.use(express.urlencoded({extended: false}));
app.use(express.json());
*/
//Middleware, do something with the req or res and go ahead

app.use(bodyparser.urlencoded({extended: false}));
app.use(bodyparser.json());

app.use((req, res, next) => {
    //Do jobs here
    next()
})

app.post('/data', (req, res) => {
    const {id, temperature, humidity, lastWatering } = req.body;
    //Save data on db and return the json after insert
    database('datacollection')
        .returning('*')
        .insert({
            temperature: temperature,
            humidity: humidity,
            last_watering: lastWatering
        })
        .then(data => {
            res.send(data[0])
        })
        .catch(err => {
            res.status(400).send(err)
        })
})

//It accepts the id of a single data
app.get('/data/:id', (req, res) => {
    const { id } = req.params;
    database('datacollection')
        .select('*')
        .where({id})
        .then(data => {
            if (data.length) {
                res.json(data[0])
            } else {
                res.status(400).json('Not found')
            }
        })
        .catch(err => res.status(400).json('Not found'))
})

//Get all data 
app.get('/data/', (req, res) => {
    database('datacollection')
        .select('*')
        .then(data => {
            if (data.length) {
                res.json(data)
            } else {
                res.status(400).json('No data')
            }
        })
        .catch(err => res.status(400).json(err))
})

//BEST METHOD FOF QUERY PARAM
http://localhost:3001/data/?id=1
/*
app.get('/data/', (req, res) => {
    const { id } = req.query;
    console.log(req.query);
    let found = false;
    dataBase.info.forEach(item => {
        console.log(item)
        if (item.id === id) {
            found = true;
            return res.json(item);
        }
    })
    if (!found) {
        res.status(404).json('No such data');
    }
})
*/

/*
//Here arrive the get info (example from mobile app)
app.get('/', (req, res) => {
    const user = {
        name: 'Andrea',
        surname: 'Franco'
    }
    res.status(400).send(user)
})

//Here arrive the post (example data from arduino) and we 
//can save into db
app.post('/profile', (req, res) => {
    console.log(req.body)
    const user = {
        name: 'Ciccio',
        surname: 'Franco'
    }
    res.send(user)
})
*/
app.listen(3000, () => {
    console.log("App is running on port 3000")
});