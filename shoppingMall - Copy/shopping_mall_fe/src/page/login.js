import { useState } from "react"
import { Button, Form, Col, Row,Alert } from "react-bootstrap"
import Container from "react-bootstrap/esm/Container"
import { Link } from "react-router-dom"
import './login.css'
import { useNavigate } from "react-router-dom"
import axios from 'axios'
import { UserContext } from "../context/userContext"
import { useContext } from "react"
import Navigation from "../component/navigation"

const Login = () => {
    const [creadential, setCreadential] = useState({
        username: '',
        password: ''
    });
    const [err,setErr] = useState("")
    const handleChange = (e) => {
        setCreadential((pre) => ({ ...pre, [e.target.id]: e.target.value }));
    };
    const {loading, dispatchUser } = useContext(UserContext)
    // const [err,setErr] = useState("")
    const navigate = useNavigate()
    const handleLogin = async (e) => {
        e.preventDefault();
        dispatchUser({ type: "LOGIN_START" })
        try {
            const res = await axios.post('http://localhost:8080/users/login', creadential,{
                headers: {
                    'Content-Type': 'application/json'
         } })
            console.log(res.data)
            dispatchUser({ type: "LOGIN_SUCCESS", payload: res.data })
            navigate('/')
        } catch (error) {
            console.log(error)
            setErr(error.response.data.message)
            dispatchUser({ type: "LOGIN_FAILURE", payload: error.response ? error.response.data : "Something went wrong", })
            
        }
    }
    return (
        // <Container className="login_container">
        //     {/* <Row> */}
        //         {/* <Col md={12} > */}
        //             <Form   className="login_form">
        //                 <h1>Login to your account</h1>
        //                 {err && <Alert variant="danger">{err}</Alert>}
        //                 <Form.Group>
        //                     <Form.Label>Email address</Form.Label>
        //                     <Form.Control type='text' placeholder="Enter username" value={creadential.username} id='username' onChange={handleChange} />
        //                 </Form.Group>

        //                 <Form.Group>
        //                     <Form.Label>Password</Form.Label>
        //                     <Form.Control type='password' placeholder="Enter password" value={creadential.password} id='password' onChange={handleChange} />
        //                 </Form.Group>
        //                 <Form.Group>
        //                     <Button disabled={loading} onClick={handleLogin}>Login</Button>
                            
        //                 </Form.Group>
                       

        //                 <p>Don't have an account?
        //                     <Link to="/signup">Create account</Link>
                            
        //                     <>
        //                 {/* <span className="error-message" style={{display:"block"}}>{err}</span> */}
        //                     </>
        //                 </p>
        //             </Form>
                    
                        
        //         {/* </Col> */}
        //         {/* <Col md={6} className="login_image">
        //         </Col> */}
        //     {/* </Row> */}
        // </Container>
        <>
        <Navigation/>
        <div id="wrapper">
        <div id="login">
            <h3>Login to your account</h3>
            {err && <Alert variant="danger">{err}</Alert>}
            <div id="usernames">
                <input type="text"  placeholder="Enter username" value={creadential.username} id='username' onChange={handleChange}  class="username"/>
                <i class="fa-solid fa-user"></i>
            </div>
            <div id="passwords">
                <input type="password" placeholder="Enter password" value={creadential.password} id='password' onChange={handleChange} class="password"/>
                <i class="fa-solid fa-lock"></i>
            </div>
            <div id="help">
                {/* <div class="forget-password">
                    <span>Forgot Password?</span>
                    <a href="">Click here</a>
                </div> */}
                <div class="to-register">
                    {/* <span>New user</span>
                    <a href="">Register here</a> */}
                    <p>Don't have an account?
                     <Link to="/signup">Create account</Link>   </p>
                </div>
            </div>
            <Button disabled={loading} onClick={handleLogin}>Login</Button>
        </div>
    </div>
    </>
    )
}
export default Login
