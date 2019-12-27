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
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.Duration;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.godaddy.uk.productsapi.models.BasketState.*;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class BasketsServiceTest {

    @Mock
    private PaymentsService paymentsService;

    @Mock
    private ProductProvisioningService productProvisioningService;

    @Mock
    private ProductsService productsService;

    @Mock
    private SubscriptionsService subscriptionsService;

    @Mock
    private BasketsRepository basketsRepository;

    @InjectMocks
    private BasketsService basketsService;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testCreateBasket() {
        // given
        BrandEntity brand = mockBrand(1);
        UserEntity loggedInUser = mockUser(10, brand);
        List<Integer> initialProductIds = List.of(1);
        // given: mock products response
        List<ProductEntity> products = List.of(mockProduct(1, 9.99, brand));
        when(productsService.getProductEntitiesByIds(initialProductIds)).thenReturn(products);
        // given: mock save response
        when(basketsRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        // when
        Basket result = this.basketsService.createBasket(loggedInUser, initialProductIds);

        // then: verify basket was saved
        verify(basketsRepository).save(any(BasketEntity.class));
        // then: verify basket details
        assertNotNull(result);
        assertEquals(IN_PROCESS, result.getState());
        assertEquals(1, result.getProducts().size());
        assertEquals(9.99D, result.getBasketValue(), 0);
    }

    @Test(expected = NotFoundException.class)
    public void testCreateBasket_productDoesNotBelongToUserBrand() {
        // given
        BrandEntity brand = mockBrand(1);
        BrandEntity brand2 = mockBrand(2);
        UserEntity loggedInUser = mockUser(10, brand);
        List<Integer> initialProductIds = List.of(1);
        // given: mock products response
        List<ProductEntity> products = List.of(mockProduct(1, 9.99, brand2));
        when(productsService.getProductEntitiesByIds(initialProductIds)).thenReturn(products);

        // when
        this.basketsService.createBasket(loggedInUser, initialProductIds);

        // then
        fail("The test failed, because expected exception to be thrown.");
    }

    @Test
    public void testGetBasketById() {
        // given
        int userId = 10;
        int basketId = 2;
        BrandEntity brand = mockBrand(1);
        UserEntity loggedInUser = mockUser(userId, brand);
        // given: mock response from database
        BasketEntity basket = new BasketEntity();
        basket.setOwner(loggedInUser);
        basket.setBasketState(IN_PROCESS);
        basket.setProducts(List.of(mockProduct(1, 9.99D, brand), mockProduct(2, 14.99D, brand)));
        when(basketsRepository.findByIdAndOwnerId(basketId, userId)).thenReturn(Optional.of(basket));

        // when
        Basket result = this.basketsService.getBasketById(loggedInUser, basketId);

        // then
        assertNotNull(result);
        assertEquals(24.98, result.getBasketValue());
        assertEquals(2, result.getProducts().size());
        assertEquals(IN_PROCESS, result.getState());
    }

    @Test(expected = NotFoundException.class)
    public void testGetBasketById_basketNotFound() {
        // given
        int userId = 10;
        int basketId = 2;
        BrandEntity brand = mockBrand(1);
        UserEntity loggedInUser = mockUser(userId, brand);
        // given: mock response from database
        when(basketsRepository.findByIdAndOwnerId(basketId, userId)).thenReturn(Optional.empty());

        // when
        Basket result = this.basketsService.getBasketById(loggedInUser, basketId);

        // then
        fail("The test failed, because expected exception to be thrown.");
    }

    @Test
    public void testUpdateBasketState_fromInProcessToInCheckout() {
        // given
        BrandEntity brand = mockBrand(1);
        UserEntity loggedInUser = mockUser(1, brand);
        int basketId = 100;
        // given: mock current basket
        BasketEntity basket = new BasketEntity();
        basket.setBasketState(IN_PROCESS);
        basket.setProducts(List.of(mockProduct(1, 9.99, brand)));
        when(this.basketsRepository.findByIdAndOwnerId(100, 1)).thenReturn(Optional.of(basket));
        // given: mock save
        when(this.basketsRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        // when
        Basket result = this.basketsService.updateBasketState(loggedInUser, basketId, IN_CHECKOUT);

        // then: verify basket was updated
        verify(this.basketsRepository).save(any(BasketEntity.class));
        assertNotNull(result);
        assertEquals(IN_CHECKOUT, result.getState());
    }

    @Test(expected = ForbiddenException.class)
    public void testUpdateBasketState_fromInProcessToCompleted() {
        // given
        BrandEntity brand = mockBrand(1);
        UserEntity loggedInUser = mockUser(1, brand);
        int basketId = 100;
        // given: mock current basket
        BasketEntity basket = new BasketEntity();
        basket.setBasketState(IN_PROCESS);
        basket.setProducts(List.of(mockProduct(1, 9.99, brand)));
        when(this.basketsRepository.findByIdAndOwnerId(100, 1)).thenReturn(Optional.of(basket));

        // when
        this.basketsService.updateBasketState(loggedInUser, basketId, COMPLETED);

        // then: verify basket was updated
        fail("The test failed, because expected exception to be thrown.");
    }

    @Test
    public void testUpdateBasketState_fromInCheckoutToInProcess() {
        // given
        BrandEntity brand = mockBrand(1);
        UserEntity loggedInUser = mockUser(1, brand);
        int basketId = 100;
        // given: mock current basket
        BasketEntity basket = new BasketEntity();
        basket.setBasketState(IN_CHECKOUT);
        basket.setProducts(List.of(mockProduct(1, 9.99, brand)));
        when(this.basketsRepository.findByIdAndOwnerId(100, 1)).thenReturn(Optional.of(basket));
        // given: mock save
        when(this.basketsRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        // when
        Basket result = this.basketsService.updateBasketState(loggedInUser, basketId, IN_PROCESS);

        // then: verify basket was updated
        verify(this.basketsRepository).save(any(BasketEntity.class));
        assertNotNull(result);
        assertEquals(IN_PROCESS, result.getState());
    }

    @Test
    public void testUpdateBasketState_fromInCheckoutToCompleted() {
        // given
        BrandEntity brand = mockBrand(1);
        UserEntity loggedInUser = mockUser(1, brand);
        int basketId = 100;
        // given: mock current basket
        BasketEntity basket = new BasketEntity();
        basket.setBasketState(IN_CHECKOUT);
        basket.setProducts(List.of(mockProduct(1, 9.99, brand)));
        when(this.basketsRepository.findByIdAndOwnerId(100, 1)).thenReturn(Optional.of(basket));
        // given: mock save
        when(this.basketsRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        // when
        Basket result = this.basketsService.updateBasketState(loggedInUser, basketId, COMPLETED);

        // then: verify basket was updated
        verify(this.basketsRepository).save(any(BasketEntity.class));
        assertNotNull(result);
        assertEquals(COMPLETED, result.getState());
        // then: verify transaction has been finalised
        verify(this.paymentsService).capturePayment(basket.getId(), 9.99D);
        verify(this.productProvisioningService).provisionProducts(anyList());
        verify(this.subscriptionsService).createSubscription(eq(loggedInUser), any());
    }

    @Test(expected = ForbiddenException.class)
    public void testUpdateBasketState_fromCompletedToInProcess() {
        // given
        BrandEntity brand = mockBrand(1);
        UserEntity loggedInUser = mockUser(1, brand);
        int basketId = 100;
        // given: mock current basket
        BasketEntity basket = new BasketEntity();
        basket.setBasketState(COMPLETED);
        basket.setProducts(List.of(mockProduct(1, 9.99, brand)));
        when(this.basketsRepository.findByIdAndOwnerId(100, 1)).thenReturn(Optional.of(basket));

        // when
        this.basketsService.updateBasketState(loggedInUser, basketId, IN_PROCESS);

        // then: verify basket was updated
        fail("The test failed, because expected exception to be thrown.");
    }

    @Test(expected = ForbiddenException.class)
    public void testUpdateBasketState_fromCompletedToInCheckout() {
        // given
        BrandEntity brand = mockBrand(1);
        UserEntity loggedInUser = mockUser(1, brand);
        int basketId = 100;
        // given: mock current basket
        BasketEntity basket = new BasketEntity();
        basket.setBasketState(COMPLETED);
        basket.setProducts(List.of(mockProduct(1, 9.99, brand)));
        when(this.basketsRepository.findByIdAndOwnerId(100, 1)).thenReturn(Optional.of(basket));

        // when
        this.basketsService.updateBasketState(loggedInUser, basketId, IN_CHECKOUT);

        // then: verify basket was updated
        fail("The test failed, because expected exception to be thrown.");
    }

    @Test(expected = NotFoundException.class)
    public void testUpdateBasketState_basketNotFound() {
        // given
        BrandEntity brand = mockBrand(1);
        UserEntity loggedInUser = mockUser(1, brand);
        int basketId = 100;
        // given: mock current basket
        when(this.basketsRepository.findByIdAndOwnerId(100, 1)).thenReturn(Optional.empty());

        // when
        this.basketsService.updateBasketState(loggedInUser, basketId, IN_CHECKOUT);

        // then: verify basket was updated
        fail("The test failed, because expected exception to be thrown.");
    }

    @Test(expected = ForbiddenException.class)
    public void testUpdateBasketState_emptyBasketCannotBeCompleted() {
        // given
        BrandEntity brand = mockBrand(1);
        UserEntity loggedInUser = mockUser(1, brand);
        int basketId = 100;
        // given: mock current basket
        BasketEntity basket = new BasketEntity();
        basket.setBasketState(IN_CHECKOUT);
        basket.setProducts(emptyList());
        when(this.basketsRepository.findByIdAndOwnerId(100, 1)).thenReturn(Optional.of(basket));

        // when
        this.basketsService.updateBasketState(loggedInUser, basketId, COMPLETED);

        // then
        fail("The test failed, because expected exception to be thrown.");
    }

    @Test
    public void testAddProducts() {
        // given
        BrandEntity brand = mockBrand(1);
        UserEntity loggedInUser = mockUser(1, brand);
        int basketId = 100;
        List<Integer> newProductIds = List.of(2);

        // given: mock current basket
        BasketEntity basket = mockBasket(IN_PROCESS, mockProduct(1, 9.99, brand));
        when(this.basketsRepository.findByIdAndOwnerId(100, 1)).thenReturn(Optional.of(basket));

        // given: mock new products
        ProductEntity newProduct = mockProduct(2, 14.99, brand);
        when(productsService.getProductEntitiesByIds(newProductIds)).thenReturn(List.of(newProduct));

        // given: mock save
        when(this.basketsRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        // when
        Basket result = this.basketsService.addProducts(loggedInUser, basketId, newProductIds);

        // then
        assertNotNull(result);
        assertEquals(24.98, result.getBasketValue());
        assertEquals(2, result.getProducts().size());
    }

    @Test(expected = NotFoundException.class)
    public void testAddProducts_basketNotFound() {
        // given
        BrandEntity brand = mockBrand(1);
        UserEntity loggedInUser = mockUser(1, brand);
        int basketId = 100;
        List<Integer> newProductIds = List.of(2);

        // given: mock current basket
        when(this.basketsRepository.findByIdAndOwnerId(100, 1)).thenReturn(Optional.empty());

        // when
        this.basketsService.addProducts(loggedInUser, basketId, newProductIds);

        // then
        fail("The test failed, because expected exception to be thrown.");
    }

    @Test(expected = ForbiddenException.class)
    public void testAddProducts_basketInCheckout() {
        // given
        BrandEntity brand = mockBrand(1);
        UserEntity loggedInUser = mockUser(1, brand);
        int basketId = 100;
        List<Integer> newProductIds = List.of(2);

        // given: mock current basket
        BasketEntity basket = new BasketEntity();
        basket.setBasketState(IN_CHECKOUT);
        when(this.basketsRepository.findByIdAndOwnerId(100, 1)).thenReturn(Optional.of(basket));

        // when
        this.basketsService.addProducts(loggedInUser, basketId, newProductIds);

        // then
        fail("The test failed, because expected exception to be thrown.");
    }

    @Test(expected = ForbiddenException.class)
    public void testAddProducts_basketCompleted() {
        // given
        BrandEntity brand = mockBrand(1);
        UserEntity loggedInUser = mockUser(1, brand);
        int basketId = 100;
        List<Integer> newProductIds = List.of(2);

        // given: mock current basket
        BasketEntity basket = new BasketEntity();
        basket.setBasketState(COMPLETED);
        when(this.basketsRepository.findByIdAndOwnerId(100, 1)).thenReturn(Optional.of(basket));

        // when
        this.basketsService.addProducts(loggedInUser, basketId, newProductIds);

        // then
        fail("The test failed, because expected exception to be thrown.");
    }

    @Test(expected = NotFoundException.class)
    public void testAddProducts_newProductDoesNotBelongToUserBrand() {
        // given
        BrandEntity brand = mockBrand(1);
        BrandEntity brand2 = mockBrand(2);
        UserEntity loggedInUser = mockUser(1, brand);
        int basketId = 100;
        List<Integer> newProductIds = List.of(2);

        // given: mock current basket
        BasketEntity basket = mockBasket(IN_PROCESS, mockProduct(1, 9.99, brand));
        when(this.basketsRepository.findByIdAndOwnerId(100, 1)).thenReturn(Optional.of(basket));

        // given: mock new products
        ProductEntity newProduct = mockProduct(2, 14.99, brand2);
        when(productsService.getProductEntitiesByIds(newProductIds)).thenReturn(List.of(newProduct));

        // when
        this.basketsService.addProducts(loggedInUser, basketId, newProductIds);

        // then
        fail("The test failed, because expected exception to be thrown.");
    }

    @Test
    public void testRemoveProducts() {
        // given
        BrandEntity brand = mockBrand(1);
        UserEntity loggedInUser = mockUser(1, brand);
        int basketId = 100;
        List<Integer> toRemoveProductIds = List.of(1);

        // given: mock current basket
        BasketEntity basket = mockBasket(IN_PROCESS, mockProduct(1, 9.99, brand), mockProduct(2, 14.99, brand));
        when(this.basketsRepository.findByIdAndOwnerId(100, 1)).thenReturn(Optional.of(basket));

        // given: mock products to remove
        ProductEntity productToRemove = mockProduct(1, 9.99, brand);
        when(productsService.getProductEntitiesByIds(toRemoveProductIds)).thenReturn(List.of(productToRemove));

        // given: mock save
        when(this.basketsRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        // when
        Basket result = this.basketsService.removeProducts(loggedInUser, basketId, toRemoveProductIds);

        // then
        assertNotNull(result);
        assertEquals(14.99, result.getBasketValue());
        assertEquals(1, result.getProducts().size());
    }

    @Test
    public void testRemoveProducts_duplicatedProducts() {
        // given
        BrandEntity brand = mockBrand(1);
        UserEntity loggedInUser = mockUser(1, brand);
        int basketId = 100;
        List<Integer> toRemoveProductIds = List.of(1);

        // given: mock current basket
        BasketEntity basket = mockBasket(
                IN_PROCESS,
                mockProduct(1, 9.99, brand),
                mockProduct(1, 9.99, brand),
                mockProduct(2, 14.99, brand),
                mockProduct(2, 14.99, brand)
        );
        when(this.basketsRepository.findByIdAndOwnerId(100, 1)).thenReturn(Optional.of(basket));

        // given: mock products to remove
        ProductEntity firstProductToRemove = mockProduct(1, 9.99, brand);
        ProductEntity secondProductToRemove = mockProduct(2, 14.99, brand);
        ProductEntity thirdProductToRemove = mockProduct(2, 14.99, brand);
        when(productsService.getProductEntitiesByIds(toRemoveProductIds)).thenReturn(List.of(firstProductToRemove, secondProductToRemove, thirdProductToRemove));

        // given: mock save
        when(this.basketsRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        // when
        Basket result = this.basketsService.removeProducts(loggedInUser, basketId, toRemoveProductIds);

        // then
        assertNotNull(result);
        assertEquals(9.99, result.getBasketValue());
        assertEquals(1, result.getProducts().size());
    }

    @Test(expected = NotFoundException.class)
    public void testRemoveProducts_basketNotFound() {
        // given
        BrandEntity brand = mockBrand(1);
        UserEntity loggedInUser = mockUser(1, brand);
        int basketId = 100;
        List<Integer> toRemoveProductIds = List.of(1);

        // given: mock current basket
        when(this.basketsRepository.findByIdAndOwnerId(100, 1)).thenReturn(Optional.empty());

        // when
        this.basketsService.removeProducts(loggedInUser, basketId, toRemoveProductIds);

        // then
        fail("The test failed, because expected exception to be thrown.");
    }

    @Test(expected = ForbiddenException.class)
    public void testRemoveProducts_basketInCheckout() {
        // given
        BrandEntity brand = mockBrand(1);
        UserEntity loggedInUser = mockUser(1, brand);
        int basketId = 100;
        List<Integer> toRemoveProductIds = List.of(1);

        // given: mock current basket
        BasketEntity basket = mockBasket(IN_CHECKOUT, mockProduct(1, 9.99, brand));
        when(this.basketsRepository.findByIdAndOwnerId(100, 1)).thenReturn(Optional.of(basket));

        // when
        this.basketsService.removeProducts(loggedInUser, basketId, toRemoveProductIds);

        // then
        fail("The test failed, because expected exception to be thrown.");
    }

    @Test(expected = ForbiddenException.class)
    public void testRemoveProducts_basketCompleted() {
        // given
        BrandEntity brand = mockBrand(1);
        UserEntity loggedInUser = mockUser(1, brand);
        int basketId = 100;
        List<Integer> toRemoveProductIds = List.of(1);

        // given: mock current basket
        BasketEntity basket = mockBasket(COMPLETED, mockProduct(1, 9.99, brand));
        when(this.basketsRepository.findByIdAndOwnerId(100, 1)).thenReturn(Optional.of(basket));

        // when
        this.basketsService.removeProducts(loggedInUser, basketId, toRemoveProductIds);

        // then
        fail("The test failed, because expected exception to be thrown.");
    }

    @Test(expected = InvalidInputException.class)
    public void testRemoveProducts_productToRemoveIsNotInBasket() {
        // given
        BrandEntity brand = mockBrand(1);
        UserEntity loggedInUser = mockUser(1, brand);
        int basketId = 100;
        List<Integer> toRemoveProductIds = List.of(2);

        // given: mock current basket
        BasketEntity basket = mockBasket(IN_PROCESS, mockProduct(1, 9.99, brand));
        when(this.basketsRepository.findByIdAndOwnerId(100, 1)).thenReturn(Optional.of(basket));

        // given: mock products to remove
        ProductEntity firstProductToRemove = mockProduct(2, 14.99, brand);
        when(productsService.getProductEntitiesByIds(toRemoveProductIds)).thenReturn(List.of(firstProductToRemove));

        // when
        this.basketsService.removeProducts(loggedInUser, basketId, toRemoveProductIds);

        // then
        fail("The test failed, because expected exception to be thrown.");
    }

    @Test(expected = InvalidInputException.class)
    public void testRemoveProducts_productRequestedToRemoveMoreTimesThanInBasket() {
        // given
        BrandEntity brand = mockBrand(1);
        UserEntity loggedInUser = mockUser(1, brand);
        int basketId = 100;
        List<Integer> toRemoveProductIds = List.of(1);

        // given: mock current basket
        BasketEntity basket = mockBasket(IN_PROCESS, mockProduct(1, 9.99, brand));
        when(this.basketsRepository.findByIdAndOwnerId(100, 1)).thenReturn(Optional.of(basket));

        // given: mock products to remove
        ProductEntity firstProductToRemove = mockProduct(1, 9.99, brand);
        ProductEntity secondProductToRemove = mockProduct(1, 9.99, brand);
        when(productsService.getProductEntitiesByIds(toRemoveProductIds)).thenReturn(List.of(firstProductToRemove, secondProductToRemove));

        // when
        this.basketsService.removeProducts(loggedInUser, basketId, toRemoveProductIds);

        // then
        fail("The test failed, because expected exception to be thrown.");
    }

    private BasketEntity mockBasket(BasketState basketState, ProductEntity... currentBasketProducts) {
        BasketEntity basket = new BasketEntity();
        basket.setBasketState(basketState);
        basket.setProducts(new ArrayList<>(asList(currentBasketProducts)));
        return basket;
    }

    private UserEntity mockUser(int userId, BrandEntity brandEntity) {
        UserEntity userEntity = mock(UserEntity.class);
        when(userEntity.getId()).thenReturn(userId);
        when(userEntity.getBrand()).thenReturn(brandEntity);
        return userEntity;
    }

    private ProductEntity mockProduct(int productId, double price, BrandEntity brandEntity) {
        ProductEntity productEntity = mock(ProductEntity.class);
        when(productEntity.getId()).thenReturn(productId);
        when(productEntity.getPrice()).thenReturn(price);
        when(productEntity.getBrand()).thenReturn(brandEntity);
        when(productEntity.getTermLength()).thenReturn(Period.ofMonths(24));
        when(productEntity.getPaymentInterval()).thenReturn(Duration.ofDays(30));
        return productEntity;
    }

    private BrandEntity mockBrand(int brandId) {
        BrandEntity brandEntity = mock(BrandEntity.class);
        when(brandEntity.getId()).thenReturn(brandId);
        return brandEntity;
    }

}
