/**
 * product service
 * createdBy: dbhuan 13/05/2022
 */
const productRepository = require('../repository/product_repository');
const { v4: uuidv4 } = require('uuid');

/**
 * hàm lấy danh sách sản phẩm
 * createdBy: dbhuan 13/05/2022
 */
const getList = async ({
    // vị trị bắt đầu
    start = 0,

    // số sản phẩm sẽ lấy
    limit = 10,

    query = null
}) => {
    let products = await productRepository.getList({ start, limit, query });
    return products;
}

/**
 * hàm thêm mới sản phẩm
 * createdBy: dbhuan 15/05/2022
 */
const addProduct = async (reqJson) => {
    let product = {
        id: uuidv4(),
        name: reqJson.name,
        sku: genStr(8),
        stock: reqJson.stock,
        available: reqJson.available,
        price: reqJson.price,
        discount: reqJson.discount,
        description: reqJson.description,
        thumbnail: reqJson.thumbnail,
        createdDate: new Date()
    };
    let newProduct = await productRepository.addProduct(product);
    return newProduct;
}

/**
 * cập nhật product
 * createdBy: dbhuan 15/05/2022
 */
const editProduct = async (id, reqJson) => {
    let product = await productRepository.getById(id);

    product = {
        ...product,

    };

    await productRepository.editProduct(product);
    return product;
}

const getById = async (id) => {
    let product = await productRepository.getById(id);
    return product;
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

module.exports = {
    getList,
    addProduct,
    editProduct,
    getById
}