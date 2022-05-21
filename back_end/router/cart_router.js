const express = require('express');
const cartService = require('../service/cart_service');

const router = express.Router();

router.get('/', async (req, res) => {
    let { id } = req.query;
    if (!/^[0-9a-f]{8}-[0-9a-f]{4}-[0-5][0-9a-f]{3}-[089ab][0-9a-f]{3}-[0-9a-f]{12}$/i.test(id)) {
        return res.status(400).json({
            errorCode: "E1000",
            errorMessage: "Id không hợp lệ"
        });
    }
    let cart = await cartService.getCart(id);
    return res.status(200).json(cart);
});

router.post('/add_to_cart', async (req, res) => {
    let reqJson = req.body;
    await cartService.addToCart(reqJson.cartId, reqJson.productId, reqJson.quantity);
    return res.status(200).json({
        success: true
    });
});

router.post('/update_cart_item_quantity', async (req, res) => {
    let reqJson = req.body;
    console.log(reqJson);
    let cart = await cartService.updateCartItemQuantity(reqJson.cartItemId, reqJson.quantity);
    console.log(cart);
    return res.status(200).json(cart);
});

module.exports = router;