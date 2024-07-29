import axios from 'axios'
import { useContext, useEffect, useState } from 'react'
import { Button, Table } from 'react-bootstrap'
import { UserContext } from '../context/userContext'
import { ProductContext } from '../context/productContext'
import { Link } from 'react-router-dom'
import "./DashBoardProduct.css"
const DashBoardProduct = ()=>{
    const {user} = useContext(UserContext)
    const {product,dispatch} = useContext(ProductContext)
    const [products,setProducts] = useState([])
    useEffect(() => {
        fetchData();
    }, []);

    const fetchData = async () => {
        try {
            const res = await axios.get("http://localhost:8080/products");
            setProducts(res.data); // Cập nhật trạng thái sản phẩm
            console.log(res.data)
        } catch (error) {
            console.error(error);
        }
    };

    const handleDeleteProduct = async (id, userId) => {
        console.log(id);
        if (window.confirm("Are you sure?")) {
            try {
                const config = {
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    data: {
                        userId
                    }
                };

                await axios.delete(`/products/${id}`, config);
                // Xóa thành công, làm mới danh sách sản phẩm
                fetchData();
            } catch (error) {
                console.log(error.message);
            }
        }
    };
    console.log(product)
    // console.log(user)
    return(
        <Table striped bordered hover responsive>
            <thead>
                <tr>
                    <th></th>
                    <th>Product Id</th>
                    <th>Product Pame</th>
                    <th>Product Price</th>

                </tr>

            </thead>
            <tbody>
    {products && products.length > 0 ? (
        products.map(p => (
            <tr key={p.productId}>
                <td>
                    <img src={p.pictureUrls[0]} className='dashboard-product-preview'/>
                </td>
                <td>{p.productId}</td>
                <td>{p.name}</td>
                <td>{p.price}</td>
                <td>
                    <Button onClick={() => handleDeleteProduct(p.productId, user.userId)}>Delete</Button>
                    <Link to={`/product/${p.productId}/edit`} className='btn btn-warning'>Edit</Link>
                </td>
            </tr>
        ))
    ) : (
        <tr>
            <td colSpan="5">No products available</td>
        </tr>
    )}
</tbody>

        </Table>
    )
}
export default DashBoardProduct