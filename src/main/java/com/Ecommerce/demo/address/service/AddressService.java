package com.Ecommerce.demo.address.service;

import com.Ecommerce.demo.address.dto.AddressRequest;
import com.Ecommerce.demo.address.dto.AddressResponse;

import java.util.List;

public interface AddressService {

    AddressResponse addAddress(
            AddressRequest request,
            String email
    );

    List<AddressResponse> getMyAddresses(
            String email
    );

    AddressResponse getAddressById(
            Long id,
            String email
    );

    AddressResponse updateAddress(
            Long id,
            AddressRequest request,
            String email
    );

    void deleteAddress(
            Long id,
            String email
    );
}