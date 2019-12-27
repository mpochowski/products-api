package com.godaddy.uk.productsapi.services.stub;

import com.godaddy.uk.productsapi.models.api.Product;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductProvisioningService {

    /**
     * Dummy method to provision products.
     *
     * @param products not null list of products to be provisioned.
     */
    public void provisionProducts(List<Product> products) {
    }

}