const express = require('express');
const bodyparser = require('body-parser');
const app = express();
const cors = require('cors');
const knex = require('knex');
const bcrypt = require('bcrypt-nodejs');//Password crypting

const database = knex({
    client: 'pg',
    connection: {
        host: '127.0.0.1',
        user: 'andreafranco',
        password: '',
        database: 'plant-growing-db'
    }
})

// database.select('*').from('datacollection').then(data => {
//     console.log(data);
// });

//INFO TODO DELETE ME
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

/**
 * Save a new data info by plant
 */
app.post('/data', (req, res) => {
    const {id, plantId, temperature, humidity } = req.body;
    //Save data on db and return the json after insert
    database('datacollection')
        .returning('*')
        .insert({
            plantId: plantId,
            temperature: temperature,
            humidity: humidity,
        })
        .then(data => {
            res.send(data[0])
        })
        .catch(err => {
            res.status(400).send(err)
        })
})

/**
 * Save a new plant
 */
app.post('/plant', (req, res) => {
    const {id, user_id, name, type, last_watering } = req.body;
    //Save data on db and return the json after insert
    database('plant')
        .returning('*')
        .insert({
            user_id: user_id,
            name: name,
            type: type,
            last_watering: last_watering
        })
        .then(data => {
            res.send(data[0])
        })
        .catch(err => {
            res.status(400).send(err)
        })
})

 
/**
 * Save a new user
 */
app.post('/user', (req, res) => {
    const {id, name, surname, email, password } = req.body
    const hash = bcrypt.hashSync(password)
    database('enduser')
        .returning('*')
        .insert({
            name: name,
            surname: surname,
            email: email,
            password: hash,
            joined: new Date()
        })
        .then(data => {
            res.send(data[0])
        })
        .catch(err => {
            res.status(400).send(err)
        })
})

/**
 * It accepts email and pass and retrieve the user
 * Select uses onlye email, db schema doesn't allow email duplicates
 * */
app.get('/user/', (req, res) => {
    const { email, password } = req.query;
    database('enduser')
        .select('*')
        .where({email})
        .then(data => {
            if (data.length) {
                console.log(data)
                let isValid = bcrypt.compareSync(password, data[0].password);
                console.log(isValid)
                if (isValid) {
                    res.json(data[0])
                } else {
                    res.status(400).json('User Not found with psw')
                }
            } else {
                res.status(400).json('User Not found')
            }
        })
        .catch(err => res.status(400).json('Exception Not found'))
})

/**
 * It accepts the id of a single data requested. :plantId is better for path variable
 * */
app.get('/data/:plantId', (req, res) => {
    const { plantId: plant_id } = req.params;//change the name with destructuring
    console.log(plant_id)
    database('datacollection')
        .select('*')
        .where({plant_id})
        .then(data => {
            if (data.length) {
                console.log(data)
                res.json({infodata: data})
            } else {
                res.status(400).json('Not found')
            }
        })
        .catch(err => res.status(400).json('Not found'))
})

//Get data by plant
app.get('/data', (req, res) => {
    const {plantId: plant_id} = req.query
    database('datacollection')
        .select('*')
        .where({plant_id})
        .then(data => {
            if (data.length) {
                console.log(data)
                res.json({infodata: data})
            } else {
                res.status(400).json('No data')
            }
        })
        .catch(err => res.status(400).json(err))
})

/**
 * Get plant list by user id
 */
app.get('/plant', (req, res) => {
    const { userId: user_id } = req.query;
    database('plant')
        .select('*')
        .where({user_id})
        .then(data => {
            if (data.length) {
                console.log(data)
                res.json({plants: data})
            } else {
                res.status(400).json('Not found')
            }
        })
        .catch(err => res.status(400).json('Not found'))
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