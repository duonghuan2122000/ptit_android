/**
 * Product router
 * createdBy: dbhuan 13/05/2022
 */

const express = require('express');
const { validateAddProduct } = require('../validation/product-validation');

const productService = require('../service/product_service');

const router = express.Router();

router.get('/', async (req, res) => {
    let { start, limit, query } = req.query;
    console.log(query);
    if (!query) query = null;
    let products = await productService.getList({ start, limit, query });
    return res.status(200).json(products);
});

router.get('/:id', async (req, res) => {
    let product = await productService.getById(req.params.id);
    return res.status(200).json(product);
});

router.post('/', validateAddProduct(), async (req, res) => {
    let product = await productService.addProduct(req.body);
    return res.status(200).json(product);
});

router.put('/:id', async (req, res) => {
    let product = await productService.editProduct(req.body);
    return product;
});

module.exports = router;