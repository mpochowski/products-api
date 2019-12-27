package com.godaddy.uk.productsapi.services;

import com.godaddy.uk.productsapi.exceptions.ForbiddenException;
import com.godaddy.uk.productsapi.exceptions.InvalidInputException;
import com.godaddy.uk.productsapi.exceptions.NotFoundException;
import com.godaddy.uk.productsapi.models.BasketState;
import com.godaddy.uk.productsapi.models.api.Basket;
import com.godaddy.uk.productsapi.models.database.BasketEntity;
import com.godaddy.uk.productsapi.models.database.BrandEntity;
import com.godaddy.uk.productsapi.models.database.ProductEntity;
import com.godaddy.uk.productsapi.models.database.UserEntity;
import com.godaddy.uk.productsapi.repositories.BasketsRepository;
import com.godaddy.uk.productsapi.services.stub.PaymentsService;
import com.godaddy.uk.productsapi.services.stub.ProductProvisioningService;
import com.godaddy.uk.productsapi.transformers.BasketsTransformer;
import com.godaddy.uk.productsapi.utilities.Loggable;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.godaddy.uk.productsapi.models.BasketState.COMPLETED;
import static com.godaddy.uk.productsapi.models.BasketState.IN_PROCESS;
import static com.godaddy.uk.productsapi.transformers.BasketsTransformer.toApiModel;
import static com.godaddy.uk.productsapi.transformers.ProductsTransformer.toApiModels;
import static com.godaddy.uk.productsapi.utilities.MoneyUtilities.sumMoneyValues;
import static java.util.stream.Collectors.toList;

@Service
public class BasketsService implements Loggable {

    @Autowired
    private PaymentsService paymentsService;

    @Autowired
    private ProductProvisioningService productProvisioningService;

    @Autowired
    private ProductsService productsService;

    @Autowired
    private SubscriptionsService subscriptionsService;

    @Autowired
    private BasketsRepository basketsRepository;

    /**
     * Creates new basket along with initial products.
     * Currently logged in user becomes owner of the basket.
     * All initial products must belong to the same brand as logged in user.
     *
     * @param loggedInUser      currently logged in user, who is going to be owner of the basket
     * @param initialProductIds list of initial product ids, all of them must belong to given brand
     * @return created basket api model
     * @throws NotFoundException if at least one of initial products does not belong to user brand,
     *                           returns product not found message to do not expose existence of brands
     */
    @Transactional
    public Basket createBasket(@NotNull UserEntity loggedInUser, @NotNull List<Integer> initialProductIds) {
        // load initial products
        List<ProductEntity> initialProducts = productsService.getProductEntitiesByIds(initialProductIds);

        // make sure all products belong to given brand
        if (!this.productsBelongToBrand(loggedInUser.getBrand().getId(), initialProducts)) {
            throw new NotFoundException("At least one initial product has not been found.");
        }

        // all good, create the basket
        BasketEntity basket = new BasketEntity();
        basket.setBasketState(IN_PROCESS);
        basket.setOwner(loggedInUser);
        basket.setProducts(initialProducts);
        return toApiModel(basketsRepository.save(basket));
    }

    /**
     * Retrieves basket by basket id and currently logged in user (owner) id.
     *
     * @param loggedInUser currently logged in user
     * @param basketId     id of the basket
     * @return {@link Basket} api model if found
     * @throws NotFoundException if basket was not found or user is not an owner of the basket,
     *                           the error is the same for both scenarios, so that we don't expose
     *                           information about baskets created by different users
     */
    @Transactional
    public Basket getBasketById(@NotNull UserEntity loggedInUser, int basketId) {
        return basketsRepository
                .findByIdAndOwnerId(basketId, loggedInUser.getId())
                .map(BasketsTransformer::toApiModel)
                .orElseThrow(() -> new NotFoundException("Cannot find basket by ID: " + basketId));
    }

    /**
     * Updates basket state.
     *
     * @param loggedInUser currently logged in user
     * @param basketId     id of the basket to be updated
     * @param newState     new desired state
     * @return basket after update as api model
     * @throws NotFoundException  if basket was not found or user is not an owner of the basket,
     *                            the error is the same for both scenarios, so that we don't expose
     *                            information about baskets created by different users
     * @throws ForbiddenException if operation cannot be permitted due to current basket state
     */
    @Transactional
    public Basket updateBasketState(@NotNull UserEntity loggedInUser, int basketId, @NotNull BasketState newState) {
        // load basket
        BasketEntity basket = this.basketsRepository
                .findByIdAndOwnerId(basketId, loggedInUser.getId())
                .orElseThrow(() -> new NotFoundException("Cannot find basket by id: " + basketId));

        // make sure basket can be updated
        if (basket.getBasketState() == COMPLETED) {
            throw new ForbiddenException("The basket purchase has been completed and cannot be modified any more.");
        }

        // calculate basket value (currency less)
        double basketValue = sumMoneyValues(basket.getProducts(), ProductEntity::getPrice);

        // there is extra work to do if basket is to be completed
        if (newState == COMPLETED) {
            // basket can be completed only if it is in checkout
            if (basket.getBasketState() != BasketState.IN_CHECKOUT) {
                throw new ForbiddenException("Basket must be in checkout to finalise purchase.");
            }

            // make sure basket has at least one product
            if (basket.getProducts().size() < 1) {
                throw new ForbiddenException("There must be at least one product in the basket to complete purchase.");
            }

            // do take payment
            this.paymentsService.capturePayment(basket.getId(), basketValue);

            // do provision products
            this.productProvisioningService.provisionProducts(toApiModels(basket.getProducts()));

            // create subscription for each product
            basket.getProducts().forEach(product -> this.subscriptionsService.createSubscription(loggedInUser, product));
        }

        // update state
        basket.setBasketState(newState);

        // save and convert to api model
        return toApiModel(basketsRepository.save(basket));
    }

    /**
     * Adds products to given basket.
     * All new products must belong to the same brand as logged in user.
     *
     * @param loggedInUser  currently logged in user
     * @param basketId      id of the basket
     * @param newProductIds list of new products ids
     * @return basket after update as api model
     * @throws NotFoundException  if basket was not found or user is not an owner of the basket,
     *                            the error is the same for both scenarios, so that we don't expose
     *                            information about baskets created by different users or
     *                            if at least one of new products does not belong to user brand,
     *                            returns product not found message to do not expose existence of brands
     * @throws ForbiddenException if operation cannot be permitted due to current basket state
     */
    @Transactional
    public Basket addProducts(@NotNull UserEntity loggedInUser, int basketId, @NotNull List<Integer> newProductIds) {
        // load basket
        BasketEntity basket = this.basketsRepository
                .findByIdAndOwnerId(basketId, loggedInUser.getId())
                .orElseThrow(() -> new NotFoundException("Cannot find basket by id: " + basketId));

        // make sure basket can be updated
        if (basket.getBasketState() != IN_PROCESS) {
            throw new ForbiddenException("The basket has state: " + basket.getBasketState() + " and therefore cannot be updated.");
        }

        // load new products
        List<ProductEntity> newProducts = productsService.getProductEntitiesByIds(newProductIds);

        // make sure all new products belong to brand
        if (!this.productsBelongToBrand(loggedInUser.getBrand().getId(), newProducts)) {
            throw new NotFoundException("At least one new product has not been found.");
        }

        // do add the products
        basket.getProducts().addAll(newProducts);

        // save and convert to api model
        return toApiModel(basketsRepository.save(basket));
    }

    /**
     * Verifies whether given products belong to given brand.
     *
     * @param brandId  id of the brand to be checked
     * @param products product entities to be verified
     * @return true if all products belong to brand, false otherwise
     */
    @Transactional
    boolean productsBelongToBrand(@NotNull Integer brandId, @NotNull List<ProductEntity> products) {
        return products
                .stream()
                .map(ProductEntity::getBrand)
                .map(BrandEntity::getId)
                .allMatch(brandId::equals);
    }

    /**
     * Removes products from given basket.
     *
     * @param loggedInUser currently logged in user
     * @param basketId     id of the basket
     * @param productIds   list of products ids to be deleted
     * @return basket after update as api model
     * @throws NotFoundException     if basket was not found or user is not an owner of the basket,
     *                               the error is the same for both scenarios, so that we don't expose
     *                               information about baskets created by different users or
     * @throws ForbiddenException    if operation cannot be permitted due to current basket state
     * @throws InvalidInputException if at least one of the product requested to be deleted was not found in the basket
     */
    @Transactional
    public Basket removeProducts(@NotNull UserEntity loggedInUser, int basketId, @NotNull List<Integer> productIds) {
        // load basket
        BasketEntity basket = this.basketsRepository.findByIdAndOwnerId(basketId, loggedInUser.getId())
                .orElseThrow(() -> new NotFoundException("Cannot find basket by id: " + basketId));

        // make sure basket can be updated
        if (basket.getBasketState() != IN_PROCESS) {
            throw new ForbiddenException("The basket has state: " + basket.getBasketState() + " and cannot be updated.");
        }

        // load products to be removed
        List<ProductEntity> productsToRemove = productsService.getProductEntitiesByIds(productIds);

        // make sure all products to remove are in the basket
        if (!this.toProductIdList(basket.getProducts()).containsAll(this.toProductIdList(productsToRemove))) {
            throw new InvalidInputException("At least one product to be deleted was not found in the basket.");
        }

        // for each product to remove, delete first occurrence of this product in basket
        productsToRemove.forEach(product -> this.deleteProductFromList(product.getId(), basket.getProducts()));

        // save and convert to api model
        return toApiModel(basketsRepository.save(basket));
    }

    /**
     * Converts list of products into list of their ids.
     *
     * @param products list of products
     * @return list of product ids
     */
    private List<Integer> toProductIdList(@NotNull List<ProductEntity> products) {
        return products
                .stream()
                .map(ProductEntity::getId)
                .collect(toList());
    }

    /**
     * Utility method to delete first instance of product from list, that may contain duplicates.
     *
     * @param productId id of the product to be deleted
     * @param products  list of products, that may contain duplicates
     * @throws InvalidInputException if product could not be removed
     */
    @Transactional
    void deleteProductFromList(final int productId, @NotNull List<ProductEntity> products) {
        products
                .stream()
                .filter(product -> product.getId() == productId)
                .findFirst()
                .map(products::remove)
                .filter(Boolean.TRUE::equals)
                .orElseThrow(() -> new InvalidInputException("Product could not be removed. Product ID: " + productId));
    }

}