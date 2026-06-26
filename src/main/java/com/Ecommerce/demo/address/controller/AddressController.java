package com.Ecommerce.demo.address.controller;

import com.Ecommerce.demo.address.dto.AddressRequest;
import com.Ecommerce.demo.address.dto.AddressResponse;
import com.Ecommerce.demo.address.service.AddressService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/addresses")
public class AddressController {

    private final AddressService addressService;

    public AddressController(
            AddressService addressService
    ) {
        this.addressService = addressService;
    }

    @PostMapping
    public AddressResponse addAddress(
            @RequestBody AddressRequest request,
            Authentication authentication
    ) {

        return addressService.addAddress(
                request,
                authentication.getName()
        );
    }

    @GetMapping
    public List<AddressResponse> getMyAddresses(
            Authentication authentication
    ) {

        return addressService.getMyAddresses(
                authentication.getName()
        );
    }

    @GetMapping("/{id}")
    public AddressResponse getAddressById(
            @PathVariable Long id,
            Authentication authentication
    ) {

        return addressService.getAddressById(
                id,
                authentication.getName()
        );
    }

    @PutMapping("/{id}")
    public AddressResponse updateAddress(
            @PathVariable Long id,
            @RequestBody AddressRequest request,
            Authentication authentication
    ) {

        return addressService.updateAddress(
                id,
                request,
                authentication.getName()
        );
    }

    @DeleteMapping("/{id}")
    public void deleteAddress(
            @PathVariable Long id,
            Authentication authentication
    ) {

        addressService.deleteAddress(
                id,
                authentication.getName()
        );
    }
}