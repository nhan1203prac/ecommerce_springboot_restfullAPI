import {Button,Form,Col,Row, Alert} from "react-bootstrap"
import Container from "react-bootstrap/esm/Container"
import { Link } from "react-router-dom"
import { useContext, useState } from "react"
import './signup.css'
import axios from "axios"
import { UserContext } from "../context/userContext"
import { useNavigate } from "react-router-dom"
import Navigation from "../component/navigation"

const Signup = ()=>{
    const [err,setErr] = useState("")
    // const [creadential, setCreadential] = useState({
    //     username: '',
    //     email: '',
    //     password: ''
    // }); 
    const [username,setUsername] = useState("")
    const [email,setEmail] = useState("")
    const [password,setPassword] = useState("")

    const { loading, dispatchUser, error } = useContext(UserContext)
    const navigate = useNavigate()
    // const handleChange = (e)=>{
    //     setCreadential(pre=>({...pre,[e.target.id]:e.target.value}))
    // }
    // console.log(creadential)

    const handleRegister = async (e) => {
        e.preventDefault();
        dispatchUser({ type: "REGISTER_START" });
        // const creadential = {username,email,password}
        // console.log(creadential)
        try {
            const res = await axios.post("http://localhost:8080/users/register", { username, email, password });
        
            console.log(res.data);
            dispatchUser({ type: "REGISTER_SUCCESS", payload: res.data });
            navigate("/");
        } catch (error) {
            setErr(error.response.data);
            dispatchUser({
                type: "REGISTER_FAILURE",
                payload: error.response
                    ? error.response.data
                    : "Something went wrong",
            });
        }
    };
    
    return(
        <>
        <Navigation/>
        {/* <Container>
        <Row>
            <Col md={6} className="signup_form">
                <Form style={{width:"100%"}}>
                    <h1>Create an account</h1>
                    {error && <Alert variant="danger">{err}</Alert>}
                    <Form.Group>
                        <Form.Label>Name</Form.Label>
                        <Form.Control type='text' placeholder="Username" value={username} id="username" onChange={e=>setUsername(e.target.value)} />

                    </Form.Group>
                    <Form.Group>
                        <Form.Label>Email address</Form.Label>
                        <Form.Control type='email' placeholder="Enter email" value={email} id="email" onChange={e=>setEmail(e.target.value)} />
                    </Form.Group>

                    <Form.Group>
                        <Form.Label>Password</Form.Label>
                        <Form.Control type='password' placeholder="Enter password" value={password} id="password"  onChange={e=>setPassword(e.target.value)}/>
                    </Form.Group>
                    <Form.Group>
                        <Button disabled={loading} onClick={handleRegister}>Register</Button>
                    </Form.Group>
                    <p>Do you have a account?
                        <Link to="/login">Let login</Link>
                    </p>
                </Form>
            </Col>
            <Col md={6} className="signup_image">
            </Col>
        </Row>
    </Container> */}

<div id="wrapper">
        <div id="login">
            <h3>Create an account</h3>
            {err && <Alert variant="danger">{err}</Alert>}
            <div id="usernames">
                <input type="text"  placeholder="Username" value={username} id="username" onChange={e=>setUsername(e.target.value)} className="username"/>
                <i class="fa-solid fa-user"></i>
            </div>
            <div id="emails">
                <input type="text"  placeholder="Enter email" value={email} id="email" onChange={e=>setEmail(e.target.value)} className="email"/>
                <i class="fa-solid fa-envelope"></i>
            </div>
            <div id="passwords">
                <input type="password" placeholder="Enter password" value={password} id="password"  onChange={e=>setPassword(e.target.value)} className="password"/>
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
                     <Link to="/login">Let login</Link>   </p>
                </div>
            </div>
            <Button disabled={loading} onClick={handleRegister}>Register</Button>
        </div>
    </div>
    </>
    )
}
export default Signup