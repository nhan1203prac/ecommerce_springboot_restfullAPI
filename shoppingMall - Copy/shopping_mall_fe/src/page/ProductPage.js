import { useContext, useEffect, useState } from 'react'
import {useParams} from 'react-router-dom'
import { UserContext } from '../context/userContext'
import {Container} from "react-bootstrap"
import axios from 'axios'
import Loading from '../component/loading'
import SimilarProduct from '../component/SimilarProduct'
import AliceCarousel from 'react-alice-carousel';
import 'react-alice-carousel/lib/alice-carousel.css';
import { Row ,Col,Button,ButtonGroup,Form} from "react-bootstrap"
import {LinkContainer} from "react-router-bootstrap"
import {Card,Badge} from 'react-bootstrap'
import "./ProductPage.css"
import { ProductContext } from '../context/productContext'
import ToastMessage from '../component/ToastMessage'
import Navigation from '../component/navigation'

const ProductPage = ()=>{
    const {productId} = useParams()
    const {user,dispatchUser} = useContext(UserContext)
    const [product,setProduct] = useState(null)
    const [similar,setSimilar] = useState(null)
    const [success,setSuccess] = useState(false)

    const handleDragStart = (e)=>e.preventDefault()
    useEffect(()=>{
        axios.get(`/products/${productId}`).then((data)=>{

            setProduct(data.data.productWithId)
            
            setSimilar(data.data.productsWithSameCategory)
            console.log(data)
            // console.log(data.data.product)

        })
    },[productId])

    const handleAddCart = async(e)=>{
        e.preventDefault();
        try {
            const res = await axios.post("http://localhost:8080/products/add-to-cart",{userId:user.userId,productId:product.productId,price:product.price})
            console.log(res.data)
            // dispatch({type:"ADD_TO_CART",payload:res.data})
            dispatchUser({type:"UPDATE_USER",payload:res.data})
            setSuccess(true)
        } catch (error) {
            
        }
    }
    // console.log(similar)
    if (!product || !similar) {
        return <Loading />;
    }
    const responsive = {
        0: { items: 1 },
        568: { items: 2 },
        1024: { items: 3 },
    };
    const images =product.pictureUrls.map(picture=><img className='product-carousel-image' src={picture} onDragStart={handleDragStart}/>)
    let similarProducts = [];
    if (similar) {
        similarProducts = similar.map((product, idx) => (
            <div className="item" data-value={idx}>
                <SimilarProduct {...product} />
            </div>
        ));
    }

    console.log(success)

    return(
        <>
        <Navigation/>
        <Container className='pt-4' style={{position:"relative"}}>
            <Row>
                <Col lg={6}>
                    <AliceCarousel mouseTrackin items={images} controlsStrategy='alternate'/>
                </Col> 
                <Col lg={6} className="pt-4">
                <h1>{product.name}</h1>
                    <p>
                        <Badge bg="primary">{product.category}</Badge>
                    </p>
                    <p className="product__price">${product.price}</p>
                    <p style={{ textAlign: "justify" }} className="py-3">
                        <strong>Description:</strong> {product.description}
                    </p>
                    {user && !user.isAdmin && (
                        <ButtonGroup style={{ width: "90%" }}>
                            <Form.Select size="lg" style={{ width: "40%", borderRadius: "0" }}>
                                <option value="1">1</option>
                                <option value="2">2</option>
                                <option value="3">3</option>
                                <option value="4">4</option>
                                <option value="5">5</option>
                            </Form.Select>
                            <Button size="lg" onClick={handleAddCart}>
                                Add to cart
                            </Button>
                        </ButtonGroup>
                    )}
                     {user && user.isAdmin && (
                        <LinkContainer to={`/product/${product.productId}/edit`}>
                            <Button size="lg">Edit Product</Button>
                        </LinkContainer>
                    )}
                     {success && <ToastMessage bg="info" title="Added to cart" body={`${product.name} is in your cart`} />}
                </Col>
            </Row>
            <div className="my-4">
                <h2>Similar Products</h2>
                <div className="d-flex justify-content-center align-items-center flex-wrap">
                    <AliceCarousel mouseTracking items={similarProducts} responsive={responsive} controlsStrategy="alternate" />
                </div>
            </div>
        </Container>
        </>
    )
}
export default ProductPage