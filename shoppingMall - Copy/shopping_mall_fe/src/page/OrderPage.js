import { useContext, useEffect, useState } from "react";
import { UserContext } from "../context/userContext";
import { ProductContext } from "../context/productContext";
import Loading from "../component/loading";
import { Badge, Container, Table } from "react-bootstrap";
import axios from 'axios'
import Navigation from "../component/navigation";
function OrderPage(){
    const {user,dispatchUser} = useContext(UserContext)
    const {product} = useContext(ProductContext)
    const [orders, setOrders] = useState([])
    const [loading,setLoading] = useState(false)
    const [orderToShow,setOrderToShow] = useState([])
    const [show,setShow] = useState(false)

    useEffect(()=>{
        setLoading(true)
        // axios.get(`/users/${user.userId}/orders`)
        // .then(({data})=>{
        //     setLoading(false)
        //     setOrders(data)
        // })
        // .catch(e=>{
        //     setLoading(false)
        //     console.log(e)
        // })
        const fetchData = async()=>{
            const res = await axios.get(`/users/${user.userId}/orders`)
            console.log(res.data)
            setLoading(false)
            setOrders(res.data)
        }
        fetchData()
    },[])
    if(loading)
        return <Loading/>
    
    if (orders.length === 0) {
        return <h1 className="text-center pt-3">No orders yet</h1>;
    }

    return(
        <>
        <Navigation/>
        <Container>
             <h1 className="text-center">Your orders</h1>
             <Table responsive striped bordered hover>
                <thead>
                    <tr>
                        <th>#</th>
                        <th>Status</th>
                        <th>Date</th>
                        <th>Total</th>
                    </tr>
                </thead>
                <tbody>
                    {orders.map((order) => (
                        <tr>
                            <td>{order.orderId}</td>
                            <td>
                                <Badge bg={`${order.status == "processing" ? "warning" : "success"}`} text="white">
                                    {order.status}
                                </Badge>
                            </td>
                            <td>{order.date}</td>

                            <td>${order.total}</td>
                        </tr>
                    ))}
                </tbody>
            </Table>
        </Container>
        </>
    )
}
export default OrderPage