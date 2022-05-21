const cartRepository = require('../repository/cart_repository');

/**
 * hàm lấy thông tin giỏ hàng
 * createdBy: dbhuan 18/05/2022
 */
const getCart = async (id) => {
    let cart = await cartRepository.getCart(id);
    return cart;
}

const addToCart = async (cartId, productId, quantity_add) => {
    await cartRepository.updateCart(cartId, productId, quantity_add);
    await cartRepository.createOrUpdateCartItem(cartId, productId, quantity_add);
}

const updateCartItemQuantity = async (cartItemId, quantity) => {
    let cart = await cartRepository.updateCartItemQuantity(cartItemId, quantity);
    console.log(cart);
    return cart;
}

module.exports = {
    getCart,
    addToCart,
    updateCartItemQuantity
}