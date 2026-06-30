	package com.payment.payment_service.controller;
	
	import java.util.Map;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentConfirmParams;
import com.stripe.param.PaymentIntentCreateParams;

import lombok.extern.slf4j.Slf4j;
	
	@RestController
	@RequestMapping("/payment")
	@Slf4j
	public class PaymentController {
	
		@PostMapping("/create")	
		public static String createPayment(@RequestParam("amount") Long paymentAmount, @RequestParam("currency") String paymentCurrency) throws StripeException {
	
			PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
					.setAmount(paymentAmount)
					.setCurrency(paymentCurrency)
					.addPaymentMethodType("upi")
//					.setPaymentMethod("pm_card_visa")
					.putAllMetadata(Map.of("orderId","Order"))
//					.setConfirm(true)
					.build();
			
			PaymentIntent paymentIntent = PaymentIntent.create(params);
			
			return """
					Payment Created
	
					ID : %s
	
					Status : %s
	
					Amount : %d
	
					Currency : %s
					"""
					.formatted(
					        paymentIntent.getId(),
					        paymentIntent.getStatus(),
					        paymentIntent.getAmount(),
					        paymentIntent.getCurrency()
					);
	
		}

		

		@PostMapping("/confirm/{id}")
		public static String confirmPayment(@PathVariable String id) throws StripeException {
			
			PaymentIntent paymentIntent = PaymentIntent.retrieve(id);
			
			PaymentIntentConfirmParams params = PaymentIntentConfirmParams.builder().build();
			
			paymentIntent.confirm(params);
			
			return paymentIntent.getStatus();
			
			
			
		}
	}
