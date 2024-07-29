import "./Footer.css"
const Footer = ()=>{
    return(
        <div id="footer">
            <div class="container2">
                {/* <div class="box-logo"> */}
                    {/* <a href=""><img style={{height:"35px"}} src="https://hc.com.vn/ords/r/website/888/files/static/v1520/logo_yellow_long.png" alt=""/></a> */}
                    {/* <span style={{fontSize:"25px",marginTop:"60px",fontWeight:"bold"}}>Ecommerce</span> */}
                {/* </div> */}
                <div class="contact">
                    <h4>Connect</h4>
                    <ul class="footer-menu">
                        <li><a href="">FACEBOOK</a></li>
                        <li><a href="">WIKIPEDIA</a></li>
                        <li><a href="">YOUTUBE</a></li>
                        <li><a href="">CONTACT</a></li>
                    </ul>
                </div>
                <div class="general-rules">
                    <h4>Information</h4>
                    <ul class="footer-menu">
                        <li><a href="">Company name</a></li>
                        <li><a href="">Address</a></li>
                        <li><a href="">Customer care</a></li>
                        <li><a href="">Business code</a></li>

                    </ul>
                </div>
                <div class="resource">
                    <h4>Policy</h4>
                    <ul class="footer-menu">
                        <li><a href="">shipping policy</a></li>
                        <li><a href="">Warranty Policy</a></li>
                        <li><a href="">return policy</a></li>
                    </ul>
                </div>
            </div>
            <div class="container3">
                <p class>Copyright 2022 Website University of Technology and Education - The University of Danang. All
                    Rights Reserved.</p>
                
            </div>
        </div>
    )
}
export default Footer