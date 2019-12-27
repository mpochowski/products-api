package com.godaddy.uk.productsapi.services;

import com.godaddy.uk.productsapi.exceptions.NotFoundException;
import com.godaddy.uk.productsapi.models.database.BrandEntity;
import com.godaddy.uk.productsapi.repositories.BrandsRepository;
import com.godaddy.uk.productsapi.utilities.Loggable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BrandsService implements Loggable {

    @Autowired
    private BrandsRepository brandsRepository;

    /**
     * Retrieves brand entity {@link BrandEntity} by id.
     * This method returns entity and is intended for internal use only.
     *
     * @param brandId id of the brand
     * @return brand entity if found
     * @throws NotFoundException if brand was not found by given id
     */
    @Transactional
    BrandEntity findById(int brandId) {
        return brandsRepository
                .findById(brandId)
                .orElseThrow(() -> new NotFoundException("Cannot find brand by ID: " + brandId));
    }

}