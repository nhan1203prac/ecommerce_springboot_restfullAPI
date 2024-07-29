import axios from 'axios'
import { useContext, useEffect, useState } from 'react'
import { ProductContext } from '../context/productContext'
import Loading from './loading'
import { Badge, Button, Table,Modal } from 'react-bootstrap'

const DashBoardOrder = ()=>{
    const [orders, setOrders] = useState([])
    const [loading,setLoading] = useState(false)
    const [orderToShow,setOrderToShow] = useState([])
    const [show,setShow] = useState(false)
    const [product,setProduct] = useState([])

    const handleClose = () => setShow(false);
    const markShipped = async (orderId, userId) => {
        try {
            const { data } = await axios.patch(`/orders/${orderId}/mark-shipped`, { userId });
            if (Array.isArray(data)) {
                setOrders(data);
            } else {
                console.error("Unexpected data format from API response.");
            }
        } catch (e) {
            console.error(e);
        }
    };
    
    function showOrder(productsObj) {
        // let productsToShow = product.filter(product => productsObj.hasOwnProperty(product.productId));
        const productsToShow = productsObj.map((product)=>{
            return {
                ...product,
                
            };
        })
        // productsToShow = productsToShow.map(product => {
        //     return {
        //         ...product,
        //         count: productsObj[product.productId].count
        //     };
        // });
        
        console.log(productsToShow);
        setShow(true);
        setOrderToShow(productsToShow);
    }
    
    
    useEffect(()=>{
        setLoading(true)
        axios.get("/orders").then(({data})=>{
            setLoading(false)
            setOrders(data)
            console.log(data);
            
        }).catch(err=>{
            setLoading(false)
        })
    },[])

    useEffect(()=>{
        const fetchData = async () => {
            try {
                const res = await axios.get("http://localhost:8080/products");
                setProduct(res.data); 
                
            } catch (error) {
                console.error(error);
            }
        };
        fetchData()
    },[])
    if(loading) return <Loading/>
    if(orders.length===0) return <h1 className='text-center pt-4'>No orders yet</h1>
 
    return(
        <>
        <Table responsive striped bordered hover>
                <thead>
                    <tr>
                        <th>#</th>
                        <th>Client name</th>
                        <th>Items</th>
                        <th>Order total</th>
                        <th>Address</th>
                        <th>Status</th>
                        <th>Detail</th>

                    </tr>
                </thead>
                <tbody>
                    {orders.map((order) => (
                        <tr>
                            <td>{order.orderId}</td>
                            <td>{order.username}</td>
                            <td>{order.count}</td>
                            <td>{order.total}</td>
                            <td>{order.address}</td>
                            <td>
                            {order.status === "processing" ? (
                                <Button size="sm" onClick={() => markShipped(order.orderId,order.userId)}>
                                    Mark as shipped
                                </Button>
                            ) : (
                                <Badge bg="success">Shipped</Badge>
                            )}
                            </td>

                            <td>
                                <span style={{cursor:"pointer"}} onClick={()=>showOrder(order.product)}>
                                    View order <i className='fa fa-eye'></i>
                                </span>
                            </td>

                        </tr>
                    ))}
                </tbody>
            </Table>

            <Modal show={show} onHide={handleClose}>
                <Modal.Header closeButton>
                    <Modal.Title>Order details</Modal.Title>
                </Modal.Header>
                {orderToShow.map((order) => (
                    <div className="order-details__container d-flex justify-content-around py-2">
                        <img src={order.pictureUrls[0]} style={{ maxWidth: 100, height: 100, objectFit: "cover" }} />
                        <p>
                            <span>{order.name} x {order.count}</span> 
                        </p>
                        <p>Price: ${Number(order.price) * order.count}</p>
                    </div>
                ))}
                <Modal.Footer>
                    <Button variant="secondary" onClick={handleClose}>
                        Close
                    </Button>
                </Modal.Footer>
            </Modal>
            </>
    )
}
export default DashBoardOrder