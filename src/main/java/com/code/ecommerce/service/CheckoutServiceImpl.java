package com.code.ecommerce.service;

import com.code.ecommerce.dao.CustomerRepository;
import com.code.ecommerce.dto.PaymentInfo;
import com.code.ecommerce.dto.Purchase;
import com.code.ecommerce.dto.PurchaseResponse;
import com.code.ecommerce.entity.Customer;
import com.code.ecommerce.entity.Order;
import com.code.ecommerce.entity.OrderItem;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CheckoutServiceImpl implements CheckoutService {

    private final CustomerRepository customerRepository;

     public CheckoutServiceImpl(CustomerRepository customerRepository, @Value("${stripe.key.secret}") String secretKey) {
         this.customerRepository = customerRepository;
         // Initialize Stripe API key
         Stripe.apiKey = secretKey;
     }
    @Override
    @Transactional
    public PurchaseResponse placeOrder(Purchase purchase) {
        // Retrieve the order info from the DTO
        Order order = purchase.getOrder();

        // Generate a tracking number
        String orderTrackingNumber = generateOrderTrackingNumber();
        order.setOrderTrackingNumber(orderTrackingNumber);

        // Populate order with orderItems
        Set<OrderItem> orderItems = purchase.getOrderItems();
        orderItems.forEach(item -> order.add(item));

        // Populate order with billingAddress and shippingAddress
        order.setBillingAddress(purchase.getBillingAddress());
        order.setShippingAddress(purchase.getShippingAddress());

        // Populate customer with order
        Customer customer = purchase.getCustomer();

        // Check if this is an existing customer
        String email = customer.getEmail();

        Customer customerFromDB = customerRepository.findByEmail(email);

        if (customerFromDB != null) {
            // This is an existing customer
            customer = customerFromDB;
        }

        // Assign to the order
        customer.add(order);

        // Save the order in the database
        customerRepository.save(customer);

        // Return a response
        return new PurchaseResponse(orderTrackingNumber);
    }

    @Override
    public PaymentIntent createPaymentIntent(PaymentInfo paymentInfo) throws StripeException {
        List<String> paymentMethodTypes = new ArrayList<>();
        paymentMethodTypes.add("card");

        Map<String, Object> params = new HashMap<>();
        params.put("amount", paymentInfo.getAmount());
        params.put("currency", paymentInfo.getCurrency());
        params.put("payment_method_types", paymentMethodTypes);
        params.put("description", "Ecommerce Order Purchase");

        return PaymentIntent.create(params);
    }

    private String generateOrderTrackingNumber() {

         // Generate a unique tracking number using UUID
         return UUID.randomUUID().toString();
    }
}
