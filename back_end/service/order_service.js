/**
 * hàm đặt hàng
 * createdBy: dbhuan 19/05/2022
 */
const cartRepository = require('../repository/cart_repository');
const cartService = require('./cart_service');
const orderRepository = require('../repository/order_repository');
const { v4: uuidv4 } = require('uuid');
const dbCache = require('../data/db_cache');
const axios = require('axios');
const queryString = require('query-string');
const crypto = require('crypto');
const orderStatus = require('../data/order_status');
const dayjs = require('dayjs');

const placeOrder = async (cartId) => {
    let cart = await cartService.getCart(cartId);

    if (!cart || !cart.cartItems) {
        return;
    }

    // let orderId = await orderRepository.getOrder(cartId);
    let orderId = dbCache.get(cartId).value();

    let order = null;

    if (!orderId) {
        order = {
            id: uuidv4(),
            code: "DH" + dayjs(new Date()).format("YYMMDDHHmmss"),
            totalPrice: cart.totalPrice,
            totalDiscount: cart.totalDiscount,
            status: orderStatus.create,
            createdDate: new Date()
        };
        dbCache.set(cartId, order.id).write();
        order = await orderRepository.createOrder(order);
    } else {
        order = {
            id: orderId,
            totalPrice: cart.totalPrice,
            totalDiscount: cart.totalDiscount,
            updatedDate: new Date()
        };
        order = await orderRepository.updateOrder(order);

        await orderRepository.deleteOrderItem(order.id);
    }

    let orderItems = [];
    for (let cartItem of cart.cartItems) {
        orderItems.push({
            id: uuidv4(),
            quantity: cartItem.quantity,
            productId: cartItem.productId,
            price: cartItem.price,
            discount: cart.discount,
            orderId: order.id
        });
    }

    await orderRepository.insertOrderItems(orderItems);


    return order;
}

function genStr(length) {
    var result = '';
    var characters = 'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789';
    var charactersLength = characters.length;
    for (var i = 0; i < length; i++) {
        result += characters.charAt(Math.floor(Math.random() *
            charactersLength));
    }
    return result;
}

/**
 * hàm lấy thông tin đơn hàng
 * createdBy: dbhuan 20/05/2022
 */
const getOrder = async (orderId) => {
    let order = await orderRepository.getOrder(orderId);
    return order;
}

/**
 * tạo giao dịch thanh toán
 * createdBy: dbhuan 20/05/2022
 */
const createCheckout = async (reqJson) => {
    let order = await orderRepository.getOrder(reqJson.id);
    let orderInfoCustomer = {
        id: order.id,
        firstName: reqJson.firstName,
        lastName: reqJson.lastName,
        province: reqJson.province,
        district: reqJson.district,
        address: reqJson.address,
        phoneNumber: reqJson.phoneNumber,
        email: reqJson.email,
        status: orderStatus.processPayment
    };

    order = await orderRepository.updateInfoCustomerAndPaymenttInOrder(orderInfoCustomer);

    console.log(reqJson);

    // remove giỏ hàng
    await cartRepository.removeCart(reqJson.cartId);
    dbCache.unset(reqJson.cartId).write();

    // gọi api tạo giao dịch thanh toán
    let getTokenRes = await axios({
        url: 'https://testdcauthapi.jetpay.vn/connect/token',
        method: 'post',
        data: queryString.stringify({
            client_id: 'jp2-0317156937-client',
            client_secret: '5955887c-8ac9-43d4-9bbf-a72696981d0f',
            grant_type: 'client_credentials'
        })
    });

    if (!getTokenRes.data) {
        return;
    }

    let token = getTokenRes.data['access_token'];

    let createTransactionRes = await axios({
        url: 'https://testdcpaymentapi.jetpay.vn/transactions',
        method: 'post',
        data: {
            "Order": {
                "Id": order.code,
                "Amount": order.totalDiscount ? parseInt(order.totalPrice) - parseInt(order.totalDiscount) : parseInt(order.totalPrice),
                "FeeAmount": 0,
                "Ccy": "VND"
            },
            "PaymentMethod": {
                "PaymentType": 0,
                "IsCanChangePaymentMethod": true
            },
            "Customer": {
                "FirstName": order.firstName,
                "LastName": order.lastName,
            },
            "Merchant": {
                "Code": "0000707",
                "ReturnUrl": reqJson.successUrl,
                "CancelUrl": reqJson.failureUrl
            }
        },
        headers: {
            'Authorization': `Bearer ${token}`
        }
    });

    if (!createTransactionRes.data) {
        return;
    }

    let transactionId = createTransactionRes.data['Id'];
    let checkoutUrl = createTransactionRes.data['UrlCheckout'];

    let checksumKey = '627093e9-f9e5-4e6b-a055-95695dba7a23';

    let signature = crypto.createHash('sha256').update(`${transactionId}${checksumKey}`).digest('hex');

    return `${checkoutUrl}&signature=${signature}`;
}

/**
 * gạch nợ đơn hàng (ipn)
 * createdBy: dbhuan 21/05/2022
 */
const ipnPayment = async (reqJson) => {
    await orderRepository.updateOrderStatusAfterPayment(reqJson['OrderId'], mapStatus(reqJson['Status']));
}

const mapStatus = (statusPg) => {
    switch (statusPg) {
        case 3:
            return orderStatus.successPayment;
        case 5:
            return orderStatus.cancelPayment;
        default:
            return orderStatus.failurePayment;
    }
}

module.exports = {
    placeOrder,
    getOrder,
    createCheckout,
    ipnPayment
};