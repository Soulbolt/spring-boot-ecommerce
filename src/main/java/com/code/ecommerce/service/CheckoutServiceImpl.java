package com.code.ecommerce.service;

import com.code.ecommerce.dao.CustomerRepository;
import com.code.ecommerce.dto.Purchase;
import com.code.ecommerce.dto.PurchaseResponse;
import com.code.ecommerce.entity.Customer;
import com.code.ecommerce.entity.Order;
import com.code.ecommerce.entity.OrderItem;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.UUID;

@Service
public class CheckoutServiceImpl implements CheckoutService {

    private CustomerRepository customerRepository;

     public CheckoutServiceImpl(CustomerRepository customerRepository){
         this.customerRepository = customerRepository;
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
        customer.add(order);
        // Save the order in the database
        customerRepository.save(customer);
        // Return a response
        return new PurchaseResponse(orderTrackingNumber);
    }

    private String generateOrderTrackingNumber() {

         // Generate a unique tracking number using UUID
         return UUID.randomUUID().toString();
    }
}
