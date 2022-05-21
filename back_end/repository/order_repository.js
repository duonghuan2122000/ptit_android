const db = require('../data/db');

/**
 * hàm tạo đơn hàng
 * createdBy: dbhuan 19/05/2022
 */
const createOrder = async (order) => {
    await db.from('order').insert(order);
    return order;
}

/**
 * hàm cập nhật đơn hàng nếu đã tạo trong api đặt hàng
 * createdBy: dbhuan 20/05/2022
 */
const updateOrder = async (order) => {
    await db.from('order').where('id', order.id).update({
        totalPrice: order.totalPrice,
        totalDiscount: order.totalDiscount,
        updatedDate: order.updatedDate
    });

    return await db.from('order').where('id', order.id).first();
}

/**
 * hàm xóa orderItem trong đơn hàng
 * createdBy: dbhuan 20/05/2022
 */
const deleteOrderItem = async (orderId) => {
    await db.from('orderItem').where('orderId', orderId).del();
}

const insertOrderItems = async (orderItems) => {
    for (let orderItem of orderItems) {
        await db.from('orderItem').insert(orderItem);
    }
}

/**
 * lấy thông tin đơn hàng
 * createdBy: dbhuan 20/05/2022
 */
const getOrder = async (orderId) => {
    let order = await db.from('order').where('id', orderId).first();

    if (order) {
        let orderItems = await db.from('orderItem').join('product', 'orderItem.productId', '=', 'product.id').select('orderItem.id as orderItemId', 'orderItem.quantity as quantity', 'product.id as productId', 'product.name as productName', 'product.price as price', 'product.discount as discount', 'product.thumbnail as productThumbnail').where('orderId', orderId);

        orderItems = orderItems.map(item => ({
            ...item,
            price: parseInt(item.price),
            discount: parseInt(item.discount)
        }));

        order.orderItems = orderItems;
    }

    return order;
}

/**
 * Cập nhật thông tin khách hàng và phương thức thanh toán trong đơn hàng
 * createdBy: dbhuan 20/05/2022
 */
const updateInfoCustomerAndPaymenttInOrder = async (order) => {
    console.log(order);
    await db.from('order').where('id', order.id).update({
        firstName: order.firstName,
        lastName: order.lastName,
        province: order.Province,
        district: order.district,
        address: order.address,
        status: order.status,
        updatedDate: new Date()
    });
    return await getOrder(order.id);
}

const updateOrderStatusAfterPayment = async (orderCode, status) => {
    let order = await db.from('order').where('code', orderCode).first();
    order.status = status;
    await db.from('order').where('id', order.id).update({
        status
    });
}


module.exports = {
    createOrder,
    updateOrder,
    deleteOrderItem,
    insertOrderItems,
    getOrder,
    updateInfoCustomerAndPaymenttInOrder,
    updateOrderStatusAfterPayment
}