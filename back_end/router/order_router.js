const express = require('express');
const orderService = require('../service/order_service');

const router = express.Router();

router.post('/place_order', async (req, res) => {
    let { cartId } = req.query;
    let order = await orderService.placeOrder(cartId);
    return res.status(200).json(order);
});

router.get('/:id', async (req, res) => {
    let orderId = req.params.id;
    let order = await orderService.getOrder(orderId);
    return res.status(200).json(order);
});

router.post('/checkout', async (req, res) => {
    let reqJson = req.body;
    let checkoutUrl = await orderService.createCheckout(reqJson);
    return res.status(200).json({
        checkoutUrl
    });
});

router.post('/payment/ipn', async (req, res) => {
    let reqJson = req.body;
    await orderService.ipnPayment(reqJson);
    return res.status(200).json({
        success: true
    });
});

module.exports = router;