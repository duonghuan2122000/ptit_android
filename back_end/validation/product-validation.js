/**
 * product validation
 * createdBy: dbhuan 15/05/2022
 */
const { body } = require('express-validator');

const validateAddProduct = () => [
    body('name').notEmpty(),
    body('price').notEmpty().isNumeric(),
    body('discount').isNumeric(),
    body('available').notEmpty().isBoolean(),
    body('stock').notEmpty().isNumeric()
];

module.exports = {
    validateAddProduct
}