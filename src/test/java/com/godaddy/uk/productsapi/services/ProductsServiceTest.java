package com.godaddy.uk.productsapi.services;

import com.godaddy.uk.productsapi.exceptions.NotFoundException;
import com.godaddy.uk.productsapi.models.api.Product;
import com.godaddy.uk.productsapi.models.database.BrandEntity;
import com.godaddy.uk.productsapi.models.database.ProductEntity;
import com.godaddy.uk.productsapi.repositories.ProductsRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ProductsServiceTest {

    @Mock
    private BrandsService brandsService;

    @Mock
    private ProductsRepository productsRepository;

    @InjectMocks
    private ProductsService productsService;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testCreateNewProduct() {
        // given
        int brandId = 1;
        String name = "hosting package";
        double price = 12.0;
        int termLengthInMonths = 12;
        long paymentIntervalInDays = 30L;
        // given: mock brand
        when(brandsService.findById(brandId)).thenReturn(new BrandEntity());
        // given: mock save
        when(productsRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        // when
        Product product = productsService.createNewProduct(brandId, name, price, termLengthInMonths, paymentIntervalInDays);

        // then
        assertNotNull(product);
        assertEquals("hosting package", product.getName());
        assertEquals(12.0D, product.getPrice(), 0);
        assertEquals(12, product.getTermLengthInMonths());
        assertEquals(30L, product.getPaymentIntervalInDays());
    }

    @Test
    public void testGetProductEntitiesByIds() {
        // given
        List<Integer> productIds = List.of(1, 2);
        // given: mock database response
        ProductEntity product = mockProduct(1);
        ProductEntity product2 = mockProduct(2);
        when(productsRepository.findAllById(productIds)).thenReturn(List.of(product, product2));

        // when
        List<ProductEntity> result = this.productsService.getProductEntitiesByIds(productIds);

        // then
        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    public void testGetProductEntitiesByIds_duplicatedProducts() {
        // given
        List<Integer> productIds = List.of(1, 2, 1);
        // given: mock database response (only 2 unique products are returned)
        ProductEntity product = mockProduct(1);
        ProductEntity product2 = mockProduct(2);
        when(productsRepository.findAllById(productIds)).thenReturn(List.of(product, product2));

        // when
        List<ProductEntity> result = this.productsService.getProductEntitiesByIds(productIds);

        // then
        assertNotNull(result);
        assertEquals(3, result.size());
    }

    @Test(expected = NotFoundException.class)
    public void testGetProductEntitiesByIds_someProductWasNotFound() {
        // given
        List<Integer> productIds = List.of(1, 2);
        // given: mock database response (only 1  product is returned)
        ProductEntity product = mockProduct(1);
        when(productsRepository.findAllById(productIds)).thenReturn(List.of(product));

        // when
        this.productsService.getProductEntitiesByIds(productIds);

        // then
        fail("The test failed, because expected exception to be thrown.");
    }

    private ProductEntity mockProduct(int id) {
        ProductEntity productEntity = mock(ProductEntity.class);
        when(productEntity.getId()).thenReturn(id);
        return productEntity;
    }

}