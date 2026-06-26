package com.Ecommerce.demo.address.service;

import com.Ecommerce.demo.address.dto.AddressRequest;
import com.Ecommerce.demo.address.dto.AddressResponse;
import com.Ecommerce.demo.address.entity.Address;
import com.Ecommerce.demo.address.repository.AddressRepository;
import com.Ecommerce.demo.user.entity.User;
import com.Ecommerce.demo.user.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AddressServiceImpl
        implements AddressService {

    private final AddressRepository addressRepository;
    private final UserRepository userRepository;

    public AddressServiceImpl(
            AddressRepository addressRepository,
            UserRepository userRepository
    ) {
        this.addressRepository = addressRepository;
        this.userRepository = userRepository;
    }

    @Override
    public AddressResponse addAddress(
            AddressRequest request,
            String email
    ) {

        User user = userRepository
                .findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        Address address = new Address();

        address.setFullName(request.getFullName());
        address.setPhoneNumber(request.getPhoneNumber());
        address.setAddressLine1(request.getAddressLine1());
        address.setAddressLine2(request.getAddressLine2());
        address.setCity(request.getCity());
        address.setState(request.getState());
        address.setCountry(request.getCountry());
        address.setPostalCode(request.getPostalCode());
        address.setDefaultAddress(request.getDefaultAddress());
        address.setUser(user);

        addressRepository.save(address);

        return mapToResponse(address);
    }

    @Override
    public List<AddressResponse> getMyAddresses(
            String email
    ) {

        User user = userRepository
                .findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        return addressRepository
                .findByUserId(user.getId())
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public AddressResponse getAddressById(
            Long id,
            String email
    ) {

        Address address = addressRepository
                .findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Address not found"));

        return mapToResponse(address);
    }

    @Override
    public AddressResponse updateAddress(
            Long id,
            AddressRequest request,
            String email
    ) {

        Address address = addressRepository
                .findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Address not found"));

        address.setFullName(request.getFullName());
        address.setPhoneNumber(request.getPhoneNumber());
        address.setAddressLine1(request.getAddressLine1());
        address.setAddressLine2(request.getAddressLine2());
        address.setCity(request.getCity());
        address.setState(request.getState());
        address.setCountry(request.getCountry());
        address.setPostalCode(request.getPostalCode());
        address.setDefaultAddress(request.getDefaultAddress());

        addressRepository.save(address);

        return mapToResponse(address);
    }

    @Override
    public void deleteAddress(
            Long id,
            String email
    ) {

        Address address = addressRepository
                .findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Address not found"));

        addressRepository.delete(address);
    }

    private AddressResponse mapToResponse(
            Address address
    ) {

        AddressResponse response =
                new AddressResponse();

        response.setId(address.getId());
        response.setFullName(address.getFullName());
        response.setPhoneNumber(address.getPhoneNumber());
        response.setAddressLine1(address.getAddressLine1());
        response.setAddressLine2(address.getAddressLine2());
        response.setCity(address.getCity());
        response.setState(address.getState());
        response.setCountry(address.getCountry());
        response.setPostalCode(address.getPostalCode());
        response.setDefaultAddress(address.getDefaultAddress());

        return response;
    }
}