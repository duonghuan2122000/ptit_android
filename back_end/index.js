/**
 * App shopping
 * createdBy: dbhuan 13/05/2022
 */

const express = require("express");
const swaggerUi = require('swagger-ui-express');
const bodyParser = require('body-parser');
const cors = require('cors');

const swaggerDocument = require('./swagger.json');

// router
const productRouter = require('./router/product-router');
const cartRouter = require('./router/cart_router');
const orderRouter = require('./router/order_router');

const app = express();
const port = process.env.PORT || 3000;

app.use(cors());
app.use(bodyParser.urlencoded({ extended: false }));
app.use(bodyParser.json());
app.use('/api-docs', swaggerUi.serve, swaggerUi.setup(swaggerDocument));

app.get("/", (req, res) => {
    res.send('ok');
});

app.use('/products', productRouter);
app.use('/carts', cartRouter);
app.use('/orders', orderRouter);

app.listen(port);