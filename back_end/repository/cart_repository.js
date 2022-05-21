const db = require('../data/db');
const { v4: uuidv4 } = require('uuid');

/**
 * hàm lấy thông tin giỏ hàng
 * createdBy: dbhuan 18/05/2022
 */
const getCart = async (id) => {
    let cart = await db.from('cart').where('id', id).first();
    if (cart == null) {
        cart = {
            id,
            totalPrice: 0,
            createdDate: new Date()
        };
        await createNewCart(cart);
    }

    let cartItems = await db.from('cartItem').join('product', 'cartItem.productId', '=', 'product.id').select('cartItem.id as cartItemId', 'cartItem.quantity as quantity', 'product.id as productId', 'product.name as productName', 'product.price as price', 'product.discount as discount', 'product.thumbnail as productThumbnail').where('cartId', cart.id);

    cart.cartItems = cartItems.map(item => ({ ...item, price: parseInt(item.price), discount: parseInt(item.discount) }));

    return cart;
}

const createNewCart = async (cart) => {
    await db.from('cart').insert(cart);
}

const createOrUpdateCartItem = async (cartId, productId, quantity_add) => {
    let product = await db.from('product').where('id', productId).first();
    let cartItem = await db.from('cartItem').where({
        cartId, productId
    }).first();

    if (cartItem) {
        await db.from('cartItem').where('id', cartItem.id).update({
            quantity: cartItem.quantity + quantity_add, price: product.price, discount: product.discount
        });
    } else {
        await db.from('cartItem').insert({
            id: uuidv4(),
            productId,
            cartId,
            quantity: quantity_add,
            price: product.price,
            discount: product.discount
        });
    }
}

const updateCart = async (cartId, productId, quantity_add) => {
    let product = await db.from('product').where('id', productId).first();
    let cart = await db.from('cart').where('id', cartId).first();

    if (!cart) {
        // tạo mới cart
        cart = {
            id: cartId,
            totalPrice: 0,
            createdDate: new Date()
        };
        await createNewCart(cart);
    }

    let productDiscount = product.discount ? parseInt(product.discount) : 0;

    let totalPrice = parseInt(cart.totalPrice) + parseInt(product.price) * quantity_add;
    let totalDiscount = cart.totalDiscount ? parseInt(cart.totalDiscount) + parseInt(productDiscount) * quantity_add : parseInt(productDiscount) * quantity_add;
    cart = {
        ...cart,
        totalPrice,
        totalDiscount,
        updatedDate: new Date()
    };
    await db.from('cart').update(cart);
}

const updateCartItemQuantity = async (cartItemId, quantity) => {
    let cartItem = await db.from('cartItem').where('id', cartItemId).first();
    let cart = await db.from('cart').where('id', cartItem.cartId).first();

    if (quantity <= 0) {
        await db.from('cartItem').where('id', cartItemId).del();
    } else {
        await db.from('cartItem').where('id', cartItemId).update({
            quantity
        });

    }

    let cartItems = await db.from('cartItem').join('product', 'cartItem.productId', '=', 'product.id').select('cartItem.id as cartItemId', 'cartItem.quantity as quantity', 'product.id as productId', 'product.name as productName', 'product.price as price', 'product.discount as discount', 'product.thumbnail as productThumbnail').where('cartId', cart.id);

    if(!cartItems){
        cart.cartItems = null;
        cart.totalPrice = 0;
        cart.totalDiscount = null;
    } else {
        cart.cartItems = cartItems.map(item => ({ ...item, price: parseInt(item.price), discount: parseInt(item.discount) }));
        let totalPrice = 0;
        let totalDiscount = 0;
        for(let cartItem of cart.cartItems){
            totalPrice += cartItem.price * cartItem.quantity;
            totalDiscount += cartItem.discount ? cartItem.discount * quantity : 0;
        }
        cart.totalPrice = totalPrice;
        cart.totalDiscount = totalDiscount;
    }

    await db.from('cart').where('id', cart.id).update({
        totalPrice: cart.totalPrice,
        totalDiscount: cart.totalDiscount
    });

    return await getCart(cart.id);
}

const removeCart = async (cartId) => {
    await db.from('cartItem').where('cartId', cartId).del();
    await db.from('cart').where('id', cartId).del();
}

module.exports = {
    getCart,
    createOrUpdateCartItem,
    updateCart,
    updateCartItemQuantity,
    removeCart
}