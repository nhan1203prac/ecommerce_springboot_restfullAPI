
import { Button, Form, Col, Row,Alert } from "react-bootstrap"
import Container from "react-bootstrap/esm/Container"
import { Link, useParams } from "react-router-dom"
import "./NewProduct.css"
import { useNavigate } from "react-router-dom"
import { useState,useContext, useEffect } from "react"
import { ProductContext } from "../context/productContext"
import axios from 'axios'
import Navigation from "../component/navigation"

function EditProductPage(){
    // const [creadential, setCreadential] = useState({
    //     name:'',
    //     description:"",
    //     price:"",
    //     category:"",
    //     images:[],
    //     imgToRemove:null,
    // })
    const navigate = useNavigate()
    const [name,setName] = useState("")
    const [description,setDescription] = useState("")
    const [price,setPrice] = useState("")
    const [category,setCategory] = useState("")
    const [pictures,setPicturse] = useState([])
    const [imgToRemove,setImgToRemove] = useState(null)
    const [productWithUrl,setProductWithUrl] = useState("")
    const {product,success,error,loading,dispatch} = useContext(ProductContext)
    const {id} = useParams()
    useEffect(()=>{
        const fetchData = async () => {
            try {
                const res = await axios.get("/products/"+id);
                console.log(res.data)
                const product = res.data.productWithId
                setName(product.name);
                setDescription(product.description);
                setCategory(product.category);
                setPicturse(product.pictureUrls);
                setPrice(product.price);
               
            } catch (error) {
                console.error(error);
            }
        };
        fetchData()

    },[])
    const handleRemoveImg = async(imgObj)=>{
        // setImgToRemove(imgObj.public_id)
        console.log(imgObj.public_id)
        try {
            await axios.delete(`/images/${imgObj.public_id}`)
            setPicturse(prev=>prev.filter(img=>img.public_id !== imgObj.public_id))
        } catch (error) {
            console.log(error)
        }
        
    }
    const handleSubmit = async (e) => {
        e.preventDefault();
        if (!name || !description || !price || !category) {
            alert("Please fill out all the fields");
            return;
        }
        
        try {
            const productRequest = {
                name,
                description,
                price,
                category,
                pictureUrls:pictures // Thêm ảnh vào đối tượng productRequest
            };
            console.log(productRequest)
            const res = await axios.patch("/products/" + id, productRequest);
            // console.log(res.data);
            navigate('/');
        } catch (error) {
            console.error(error);
        }
    }
    
    const showWidget = ()=>{
        const widget = window.cloudinary.createUploadWidget(
            {
            cloudName:"dqmlemao7",
            uploadPreset:"imgload",
        },(error,result)=>{
            if(!error && result.event ==="success"){
                setPicturse(prev=>[...prev,result.info.url])
            }
        })
        widget.open()
    }
    return(
        <>
        <Navigation/>
        <Container>
            <Row>
                <Col md={6} className = "new-product-container">
                <Form style={{ width: "100%" }}>
                        <h1 className="mt-4">Edit a product</h1>
                        {success&& <Alert>Product updated</Alert>}
                        {error&& <Alert>{error}</Alert>}
                        <Form.Group className="mb-3">
                            <Form.Label>Product name</Form.Label>
                            <Form.Control type='text' placeholder="Enter product name" value={name}  onChange={e=>setName(e.target.value)} />
                        </Form.Group>

                        <Form.Group className="mb-3">
                            <Form.Label>Description</Form.Label>
                            <Form.Control as="textarea" placeholder="Enter product description" value={description}  onChange={e=>setDescription(e.target.value)} />
                        </Form.Group>
                        <Form.Group className="mb-3">
                            <Form.Label>Price($)</Form.Label>
                            <Form.Control type='number' placeholder="Enter price" value={price}  onChange={e=>setPrice(e.target.value)} />
                        </Form.Group>
                        <Form.Group className="mb-3" >
                            <Form.Label>Category</Form.Label>
                            <Form.Select name="category" value={category} onChange={e => setCategory(e.target.value)}>

                                <option disabled selected>--Select One--</option>
                                <option value="technology">technology</option>
                                <option value = "tables">tables</option>
                                <option value="phones">phones</option>
                                <option value="laptops">laptops</option>
                            </Form.Select>
                        </Form.Group>
                        <Form.Group className="mb-3">
                            <Button type="button" onClick={showWidget}>Upload Images</Button>
                            <div className="images-preview-container">
                            {pictures.map(image=>(
                                    <div className="image-preview">
                                        <img src={image}/>
                                        <i className="fa fa-times-circle" onClick={()=>handleRemoveImg(image)}/>
                                    </div>
                                ))}
                            </div>
                        </Form.Group>
                        <Form.Group>
                            <Button disabled={loading} onClick={handleSubmit} >Update product</Button>
                            
                        </Form.Group>
                       

                
                    </Form>
                </Col>
                <Col md={6} className="img-product-container">
                </Col>
            </Row>
        </Container>
        </>
    )
}
export default EditProductPage