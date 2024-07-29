package com.project.backend.controller;

import com.project.backend.model.TotalRequest;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import java.util.HashMap;
import java.util.Map;

@RestController
@CrossOrigin("http://localhost:3001")
public class PaymentController {

    @PostMapping("/create-payment")
    public ResponseEntity<?> createPayment(@RequestBody TotalRequest totalRequest) {
    	Long amount = totalRequest.getTotal();
        try {
            Stripe.apiKey = "sk_test_51P5IjLP1PtyodjX4QVY760xOBAmkJBWxzOWLQr4dujuFVMxWbMZkHGMZUkDwg3k4F5hiR7NCVt1SdBRiK6ix7ZB300PZx6CwkS"; // Thay YOUR_STRIPE_SECRET_KEY bằng secret key của bạn

            PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                    .setAmount(amount)
                    .setCurrency("usd")
                    .addPaymentMethodType("card")
                    .build();
            PaymentIntent paymentIntent = PaymentIntent.create(params);

            // Trả về mã xác nhận của khách hàng từ Stripe
//            Map<String, Object> responseData = new HashMap<>();
//            responseData.put("clientSecret", paymentIntent.getClientSecret());
            
            Map<String, Object> responseData = new HashMap<>();
            responseData.put("clientSecret", paymentIntent.getClientSecret());
            
            return ResponseEntity.ok(responseData);
        } catch (StripeException e) {
            System.out.println(e.getMessage()); // In ra lỗi để debug, bạn có thể xóa dòng này sau
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (NumberFormatException e) {
            // Xử lý lỗi nếu dữ liệu không phải là một số
            System.out.println("Dữ liệu gửi từ frontend không phải là một số.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Dữ liệu gửi từ frontend không phải là một số.");
        }
    }
}

