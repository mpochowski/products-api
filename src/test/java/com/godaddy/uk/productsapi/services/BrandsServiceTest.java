package com.godaddy.uk.productsapi.services;

import com.godaddy.uk.productsapi.exceptions.NotFoundException;
import com.godaddy.uk.productsapi.models.database.BrandEntity;
import com.godaddy.uk.productsapi.repositories.BrandsRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

public class BrandsServiceTest {

    @Mock
    private BrandsRepository brandsRepository;

    @InjectMocks
    private BrandsService brandsService;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testFindById() {
        // given
        int brandId = 1;
        // given mock response
        BrandEntity brandEntity = new BrandEntity();
        brandEntity.setName("test brand");
        when(brandsRepository.findById(brandId)).thenReturn(Optional.of(brandEntity));

        // when
        BrandEntity result = brandsService.findById(brandId);

        // then
        assertNotNull(result);
        assertEquals("test brand", result.getName());
    }

    @Test(expected = NotFoundException.class)
    public void testFindById_brandNotFound() {
        // given
        int brandId = 1;
        // given mock response
        when(brandsRepository.findById(brandId)).thenReturn(Optional.empty());

        // when
        brandsService.findById(brandId);

        // then
        fail("The test failed, because expected exception to be thrown.");
    }

}