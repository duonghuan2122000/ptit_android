/**
 * product repository
 * createdBy: dbhuan 13/05/2022
 */
const db = require('../data/db');

/**
 * lấy danh sách sản phẩm có phân trang
 * createdBy: dbhuan 13/05/2022
 */
const getList = async ({
    // trang hiện tại
    start = 0,

    // số bản ghi sẽ lấy
    limit = 10,
     
    query = null
}) => {
    let totalProducts = 0;

    console.log(query);

    if(query){
        totalProducts = await db.from('product').count({ total: '*' }).whereILike('name', `%${query}%`).first();
    } else {
        totalProducts = await db.from('product').count({ total: '*' }).first();
    }

    totalProducts = totalProducts['total'];

    let items = [];

    if(query){
        items = await db.from('product').whereILike('name', `%${query}%`).limit(limit).offset(start);
    } else {
        items = await db.from('product').limit(limit).offset(start);
    }

    let itemsFormat = [];

    for(let item of items){
        itemsFormat.push({...item, price: parseInt(item.price), discount: parseInt(item.discount)});
    }

    return {
        totalRecord: totalProducts,
        items: itemsFormat
    }
}

/**
 * thêm mới sản phẩm
 * createdBy: dbhuan 15/05/2022
 */
const addProduct = async (product) => {
    await db.from('product').insert(product);
    return product;
}

/**
 * cập nhật sản phẩm
 * createdBy: dbhuan 15/05/2022
 */
const editProduct = async (product) => {
    await db.from('product').update(product);
    return product;
}

/**
 * lấy thông tin sản phẩm
 * createdBy: dbhuan 15/05/2022
 */
const getById = async (id) => {
    let product = await db.from('product').where('id', id).first();
    return product;
}

module.exports = {
    getList,
    addProduct,
    editProduct,
    getById
}