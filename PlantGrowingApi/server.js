const express = require('express');
const bodyparser = require('body-parser');
const app = express();
const cors = require('cors');
const knex = require('knex');
const bcrypt = require('bcrypt-nodejs');//Password crypting
const moment = require('moment');

const database = knex({
    client: 'pg',
    connection: {
        host: '127.0.0.1',
        user: 'andreafranco',
        password: '',
        database: 'plant-growing-db'
    }
})

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
    var {plantId: plant_id, temperature, humidity, execTime: exec_time} = req.body;
    //Save data on db and return the json after insert
    //TODO arduino doesn't have RTC module, so the solutions are
    //1 - call the web and get the time
    //2 - Check the value here, in case we set the current time (actual solution)
    console.log("Post data")
    console.log(req.body)
    if (exec_time == "") {
        console.log(exec_time)
        exec_time = moment().format() 
        console.log(exec_time)
    }
    database('datacollection')
    .returning('*')
    .insert({
        plant_id: plant_id,
        temperature: temperature,
        humidity: humidity,
        exec_time: exec_time
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
    console.log("Post plant")
    const {id, userId: user_id, name, type, last_watering } = req.body;
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
 * Save a new Command
 */
app.post('/command', (req, res) => {
    const {plantId: plant_id, commandType: command_type } = req.body
    console.log(plant_id)
    database('command')
        .returning('*')
        .insert({
            plant_id: plant_id,
            command_type: command_type,
            executed: false
        })
        .then(data => {
            console.log(data[0])
            res.send(data[0])
        })
        .catch(err => {
            res.status(400).send(err)
        })
})

/**
 * Update command status
 */
app.put('/command', (req, res) => {
    const {commandId: id } = req.body
    console.log(id)
    database('command')
        .returning('*')
        .where({id: id})
        .update({
            executed: true
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

/**
 * Get command list by plant, limit 1. Next step I'll remove limit
 */
app.get('/command', (req, res) => {
    const { plantId: plant_id, commandType: command_type} = req.query;
    const executed = false;

    database('command')
        .select('*')
        .where({plant_id,command_type, executed})
        .limit(1)
        .then(data => {
            if (data.length) {
                console.log(data)
                res.json({data})
            } else {
                res.status(400).json('Not found')
            }
        })
        .catch(err => res.status(400).json('Not found'))
})

/**
 * Get command for watering, only first fase of project. It returns the id
 */
app.get('/command-water', (req, res) => {
    const { plantId: plant_id, commandType: command_type} = req.query;
    const executed = false;

    database('command')
        .select('*')
        .where({plant_id,command_type, executed})
        .limit(1)
        .then(data => {
            if (data.length) {
                console.log(data)
                res.json(data[0].id)
            } else {
                res.status(400).json('Not found')
            }
        })
        .catch(err => res.status(400).json('Not found'))
})

app.listen(3000, () => {
    console.log("App is running on port 3000")
});