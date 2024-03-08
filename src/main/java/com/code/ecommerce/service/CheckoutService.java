package com.code.ecommerce.service;

import com.code.ecommerce.dto.PaymentInfo;
import com.code.ecommerce.dto.Purchase;
import com.code.ecommerce.dto.PurchaseResponse;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;

public interface CheckoutService {

    PurchaseResponse placeOrder(Purchase purchase);

    PaymentIntent createPaymentIntent(PaymentInfo paymentInfo) throws StripeException;
}
