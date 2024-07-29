import Container from "react-bootstrap/Container";
import Nav from "react-bootstrap/Nav";
import Navbar from "react-bootstrap/Navbar";
import NavDropdown from "react-bootstrap/NavDropdown";
import { LinkContainer } from "react-router-bootstrap";
import { UserContext } from "../context/userContext";
import { useContext, useEffect, useRef, useState } from "react";
import { Button } from "react-bootstrap";
import "./navigation.css";
import axios from "axios";
import Loading from "./loading"
import { useNavigate } from "react-router-dom";
const Navigation = () => {
  const { user, dispatchUser } = useContext(UserContext);
  const [notifications,setNotifications] = useState(null)
  const bellRef = useRef(null);
  const notificationRef = useRef(null);
  const [bellPos, setBellPos] = useState({});
  const [filteredNotifications, setFilteredNotifications] = useState(null);
  const [activeFilter, setActiveFilter] = useState("all");
  const navigate = useNavigate()


  // console.log(user);
  // const handleLogout = () => {
  //   setLoading(true)
  //   setTimeout(()=>{
  //     dispatchUser({ type: "LOGOUT" });
  //   },1500)
  //   setLoading(false)
  // };

  useEffect(() => {
    const fetchNotifications = async () => {
      try {
        const res = await axios.get(`http://localhost:8080/users/notifications/${user.userId}`);
        setNotifications(res.data);
        setFilteredNotifications(res.data)
        
        console.log(res.data);
      } catch (error) {
        console.log(error);
      }
    };
  
    if (user) {
      fetchNotifications();
    }
  }, [user]);

  const handleLogout = async () => {
    try {
        await new Promise(resolve => setTimeout(resolve, 1000));
        
        dispatchUser({ type: "LOGOUT" });
        navigate("/login");
    } catch (error) {
        console.error("Error during logout:", error);
    } finally {
    }
};
const filterNotifications = (type) => {
  if (type === "all") {
    // Hiển thị tất cả thông báo
    setFilteredNotifications(notifications);
    setActiveFilter("all");
  } else if (type === "unread") {
    // Lọc và hiển thị các thông báo chưa đọc
    const unread = notifications.filter(notification => notification.status === "unread");
    setFilteredNotifications(unread);
    setActiveFilter("unread"); 
  }
};
  // Đếm tin chưa đọc
  const unreadNotifications = Array.isArray(notifications) ?
  notifications.reduce((acc, curr) => {
    if (curr.status === "unread") return acc + 1;
    return acc;
  }, 0) : 0;


  const handleToggleNotifications = async () => {
    const position = bellRef.current.getBoundingClientRect();
    setBellPos(position);
    
    notificationRef.current.style.display =
      notificationRef.current.style.display === "block" ? "none" : "block";
      
    if(notificationRef.current.style.display === "none"){
      if (unreadNotifications > 0) {
        try {
          const res = await axios.post(`/users/notifications/${user.userId}`);
          setNotifications(res.data);
        } catch (error) {
          console.error("Error updating notifications:", error);
        }
      }
    }
    // Nếu đã thay đổi trạng thái và có thông báo chưa đọc, thực hiện cập nhật thông báo
    
  };
  

  // const handleClickOutside = (event) => {
  //   if (notificationRef.current && !notificationRef.current.contains(event.target)) {
  //     notificationRef.current.style.display = "none";
  //   }
  // };

  // // Add event listener when component mounts
  // document.addEventListener("click", handleClickOutside);
  return (
    <Navbar expand="lg" className="bg-body-tertiary">
      <Container >
        <LinkContainer to="/">
          <Navbar.Brand ><i className="fas fa-home"></i> Ecommerce</Navbar.Brand>
        </LinkContainer>

        <Navbar.Toggle aria-controls="basic-navbar-nav" />
        <Navbar.Collapse id="basic-navbar-nav">
          <Nav className="ms-auto">
            {!user && (
              <LinkContainer to="/login">
                <Nav.Link>Login</Nav.Link>
              </LinkContainer>
            )}

            {user && !user.isAdmin && (
              <LinkContainer to="/cart">
                <Nav.Link style={{alignItems:"center"}}>
                  <i className="fas fa-shopping-cart"></i>
                  <span>Cart</span>
                  {user.count > 0 && (
                    <span className="badge badge-warning" id="cartcount">
                      {user.count}
                    </span>
                  )}
                </Nav.Link>
              </LinkContainer>
            )}
            {user && (
              <>
                <Nav.Link
                  style={{ position: "relative" }}
                  onClick={handleToggleNotifications}
                >
                  <i
                    className="fas fa-bell"
                    ref={bellRef}
                    data-count={unreadNotifications > 0 ? unreadNotifications : null}
                  ></i>
                  <span>Notification</span>
                </Nav.Link>
                <NavDropdown
                  title={<><i className="fas fa-user fa-lg"></i> {user.username || "Dropdown"}</>}
                  id="basic-nav-dropdown"
                >
                  {user.isAdmin && (
                    <>
                      <LinkContainer to="/admin">
                        <NavDropdown.Item>Dashboard</NavDropdown.Item>
                      </LinkContainer>
                      <LinkContainer to="/new-product">
                        <NavDropdown.Item>Create product</NavDropdown.Item>
                      </LinkContainer>
                    </>
                  )}
                  {!user.isAdmin && (
                    <>
                      <LinkContainer to="/cart">
                        <NavDropdown.Item>Cart</NavDropdown.Item>
                      </LinkContainer>
                      <LinkContainer to="/orders">
                        <NavDropdown.Item>My orders</NavDropdown.Item>
                      </LinkContainer>
                    </>
                  )}
                  <NavDropdown.Divider />
                  <Button
                    variant="danger"
                    onClick={handleLogout}
                    className="logout-btn"
                  >
                    Logout
                  </Button>
                </NavDropdown>
              </>
            )}
          </Nav>
        </Navbar.Collapse>
      </Container>
      <div
        className="notifications-container"
        ref={notificationRef}
        style={{
          position: "absolute",
          top: bellPos.top + 30,
          left: bellPos.left-100,
          display: "none",
        }}
      >
        {/* {notifications && notifications.length > 0 ? (
          notifications.map((notification) => (
            <p className={`notification-${notification.status}`}>
              {notification.message}
              <br />
              <span>
                {notification.currentDateTime.split("T")[0] +
                  " " +
                  notification.currentDateTime.split("T")[1]}
              </span>
            </p>
          ))
        ) : (
          <p>No notifcations yet</p>
        )} */}
        
        <span onClick={() => filterNotifications("all")} className={activeFilter === "all" ? "active" : ""}>All</span>
        <span onClick={() => filterNotifications("unread") }  className={activeFilter === "unread" ? "active" : ""}>Unread</span>
        {filteredNotifications ? (
        filteredNotifications.map((notification) => (
        <p className={`notification-${notification.status}`}>
        {notification.message}
        <br />
        <span>
          {notification.currentDateTime.split("T")[0] +
            " " +
            notification.currentDateTime.split("T")[1]}
        </span>
      </p>
    ))
  ) : (
    <p>No notifications yet</p>
  )}
      </div>
    </Navbar>
  );
};

export default Navigation;
