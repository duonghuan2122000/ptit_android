const knex = require('knex');

const db = knex({
    client: 'mysql2',
    // debug: true,
    connection: {
      host : '127.0.0.1',
      port : 3306,
      user : 'root',
      password : '123456',
      database : 'android.shopping'
    }
});

module.exports = db;