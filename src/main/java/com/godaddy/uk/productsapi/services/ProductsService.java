package com.godaddy.uk.productsapi.services;

import com.godaddy.uk.productsapi.exceptions.NotFoundException;
import com.godaddy.uk.productsapi.models.api.Product;
import com.godaddy.uk.productsapi.models.database.ProductEntity;
import com.godaddy.uk.productsapi.repositories.ProductsRepository;
import com.godaddy.uk.productsapi.utilities.Loggable;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.Period;
import java.util.List;

import static com.godaddy.uk.productsapi.transformers.ProductsTransformer.toApiModel;
import static java.util.stream.Collectors.toList;

@Service
public class ProductsService implements Loggable {

    @Autowired
    private BrandsService brandsService;

    @Autowired
    private ProductsRepository productsRepository;

    /**
     * Creates new product.
     * This method requires currently logged in user to own {@link com.godaddy.uk.productsapi.models.UserRole#ROLE_ADMIN} role.
     *
     * @param brandId               id of the brand to create product for
     * @param name                  name of the new product
     * @param price                 price of the new product
     * @param termLengthInMonths    the length of the term in months
     * @param paymentIntervalInDays payment interval in days
     * @return created {@link Product} as api model
     */
    @Secured("ROLE_ADMIN")
    @Transactional
    public Product createNewProduct(int brandId, String name, double price, int termLengthInMonths, long paymentIntervalInDays) {
        ProductEntity product = new ProductEntity();
        product.setBrand(brandsService.findById(brandId));
        product.setName(name);
        product.setPrice(price);
        product.setTermLength(Period.ofMonths(termLengthInMonths));
        product.setPaymentInterval(Duration.ofDays(paymentIntervalInDays));
        return toApiModel(productsRepository.save(product));
    }

    /**
     * Searches for list of {@link ProductEntity} by given ids.
     * This method returns entity and is intended for internal use only.
     *
     * @param productIds list of product ids to search for
     * @return list of all requested products
     * @throws NotFoundException if at least one required product was not found
     */
    @Transactional
    List<ProductEntity> getProductEntitiesByIds(@NotNull List<Integer> productIds) {
        // load all required product entities
        List<ProductEntity> databaseProducts = productsRepository.findAllById(productIds);

        // for each product id find product entity (throw exception if not found)
        return productIds
                .stream()
                .map(requiredProductId -> this.requireProductInList(requiredProductId, databaseProducts))
                .collect(toList());
    }

    /**
     * Searches for product with given id in a list of products.
     *
     * @param requiredProductId id of the product to be found
     * @param products          list of products to search in
     * @return product entity if found
     * @throws NotFoundException if required product is not found in given list
     */
    private ProductEntity requireProductInList(final int requiredProductId, @NotNull List<ProductEntity> products) {
        return products
                .stream()
                .filter(product -> product.getId() == requiredProductId)
                .findAny()
                .orElseThrow(() -> new NotFoundException("Cannot find product by id: " + requiredProductId));
    }

}