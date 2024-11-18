package appointmenthospital.paymentservice.payment;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/payment")
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentService paymentService;
    @GetMapping("/vn-pay")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public PaymentDTO.VNPayResponse pay(HttpServletRequest request) {
        return paymentService.createVnPayPayment(request);
    }
    @GetMapping("/vn-pay-callback")
    @ResponseBody
    public ResponseEntity<?> payCallbackHandler(HttpServletRequest request) {
        String status = request.getParameter("vnp_ResponseCode");
        if (status.equals("00")) {
            return ResponseEntity.status(HttpStatus.OK).body(new PaymentDTO.VNPayResponse("00","Success",""));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed");
        }
    }

}