package com.AllInSmall.demo.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.AllInSmall.demo.dto.AccessTokenResponse;
import com.AllInSmall.demo.dto.PaypalOrderResponse;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class PayPalService {

    @Value("${paypal.client.id}")
    private String clientId;

    @Value("${paypal.client.secret}")
    private String clientSecret;
    
    private final RestTemplate restTemplate = new RestTemplate();
    
    private static final String PAYPAL_BASE_URL = "https://api-m.sandbox.paypal.com";
    
 
    
    public PaypalOrderResponse captureOrderByToken(String token) {
        final String PAYPAL_CAPTURE_URL = PAYPAL_BASE_URL + "/v2/checkout/orders/" + token + "/capture";
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(getAccessToken());
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> request = new HttpEntity<>(headers);
        ResponseEntity<PaypalOrderResponse> response = restTemplate.exchange(
                PAYPAL_CAPTURE_URL,
                HttpMethod.POST,
                request,
                PaypalOrderResponse.class
        );

        if (response.getStatusCode() == HttpStatus.CREATED) {
            return response.getBody();
        } else {
            throw new RuntimeException("Failed to capture PayPal order");
        }
    }
    
    
    public byte[] createOrderWithQRCode(String value) throws WriterException, IOException{
    	log.info("I am at createOrderWithQRCode method");
    	PaypalOrderResponse orderResponse = createOrder(value);
    	if(orderResponse == null) {
    		throw new RuntimeException("Fail to create PayPal order");
    	}
    	String approvalUrl = getApprovalLink(orderResponse.getLinks());
    	log.info("approval link: " + approvalUrl);
    	
    	return generateQRCode(approvalUrl,200,200);
    }
    
    public PaypalOrderResponse createOrder(String totalValue) {
    	final String PAYPAL_CHECKOUT_URL = PAYPAL_BASE_URL + "/v2/checkout/orders";
    	log.info("I am at createOrder method");
    	//headers of the request
    	HttpHeaders headers = new HttpHeaders();
    	headers.setBearerAuth(getAccessToken());
    	log.info(getAccessToken());
    	headers.setContentType(MediaType.APPLICATION_JSON);
    	
    	Map<String,Object> orderRequest = buildOrderRequest(totalValue);
    	
    	HttpEntity<Map<String,Object>> request = new HttpEntity<>(orderRequest,headers);
    	 //capture the response after exchange request:
    	ResponseEntity<PaypalOrderResponse> response = restTemplate.exchange(
    			PAYPAL_CHECKOUT_URL,
    			HttpMethod.POST,
    			request,
    			PaypalOrderResponse.class
    			);
    	return response.getBody();		
    	
    }
    
 public String getAccessToken() {
        log.info("I am at getAccessToken method");
        final String PAYPAL_OAUTH_URL = PAYPAL_BASE_URL + "/v1/oauth2/token";
        log.info(PAYPAL_OAUTH_URL);
        //headers of the request
        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth(clientId, clientSecret);
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        
        // new request with the defined-headers
        HttpEntity<String> request = new HttpEntity<String>("grant_type=client_credentials", headers);
        //send the post request to PAYPAL_OAUTH_URL with expected response format defined by AccessTokenResponse class
        //capture the response after exchange request:
    	
		try {
			ResponseEntity<AccessTokenResponse> response = restTemplate.exchange(PAYPAL_OAUTH_URL, HttpMethod.POST, request, AccessTokenResponse.class);

//			ResponseEntity<AccessTokenResponse> response = restTemplate.exchange(PAYPAL_OAUTH_URL, HttpMethod.POST, request, AccessTokenResponse.class);

			log.info("Response Status: {}", response.getStatusCode());
			log.info("Response Headers: {}", response.getHeaders());
			// check the response body
			log.info("Response Body: {}", response.getBody());
			if (response.getBody() != null) {
				if (response.getBody().getAccessToken() != null) {
					log.info("Access Token: {}", response.getBody().getAccessToken());
					return response.getBody().getAccessToken();
//        if (response.getBody() == null) {
//            throw new RuntimeException("Failed to obtain PayPal access token");
//        }

				} else {
					log.error("Access token is null");
				}
			} else {
				log.error("Response body is null");
			}
		} catch (Exception e) {
			log.error("Error during PayPal request: ", e);
		}
		return null;
	
    }
    
    public byte[] generateQRCode(String text, int width, int height) throws WriterException, IOException{
    	QRCodeWriter qrCodeWriter = new QRCodeWriter();
    	BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE,width,height); //writerException can happen here
    	
    	ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
    	MatrixToImageWriter.writeToStream(bitMatrix, "PNG", pngOutputStream);
    	return pngOutputStream.toByteArray();

    }
    
    private String getApprovalLink(List<PaypalOrderResponse.Link>links) {
    	for(PaypalOrderResponse.Link link : links) {
    		if("approve".equalsIgnoreCase(link.getRel())) {
    			return link.getHref();
    		}
    	}
    	throw new RuntimeException("Approval link not found");
    }

	private Map<String, Object> buildOrderRequest(String value) {
		Map<String,Object> orderRequest = new HashMap<>();
		orderRequest.put("intent", "CAPTURE");
		
		Map<String,Object> purchaseUnit = new HashMap<>();
		Map<String,Object> amount = new HashMap<>();
		amount.put("currency_code", "AUD");
		amount.put("value", value);
		purchaseUnit.put("amount", amount);
		orderRequest.put("purchase_units", Arrays.asList(purchaseUnit));
		
		//add return_url and cancel_url for PayPal redirection
		orderRequest.put("application_context", Map.of(
				"return_url","http://localhost:8080/order/placeOrder?action=paypal-confirm", // where paypal redirects users after they approve the payment
				"cancel_url","http://localhost:8080/order/placeOrder?action=paypal-cancel" // where Paypal redirects users if they cancel the payment process
				));
		
		return orderRequest;
	}
	
	
	
	
}