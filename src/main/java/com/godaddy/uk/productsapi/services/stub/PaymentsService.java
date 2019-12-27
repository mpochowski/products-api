package com.godaddy.uk.productsapi.services.stub;

import org.springframework.stereotype.Service;

@Service
public class PaymentsService {

    /**
     * Dummy method for payment service.
     * Normally we would either:
     * - delegate payment processing for another micro-service or;
     * - integrate with payment provider like Stripe and require more data here (e.g. payment method, card details, currency, etc);
     *
     * @param basketId      id of the basket
     * @param paymentAmount amount to be paid
     */
    public void capturePayment(int basketId, double paymentAmount) {
    }

}