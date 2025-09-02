package com.example.inspection.mapper;

import org.springframework.stereotype.Component;

import com.example.inspection.dto.request.CustomerRequest;
import com.example.inspection.dto.response.CustomerResponse;
import com.example.inspection.entity.Customer;

@Component
public class CustomerMapper {

    public Customer toEntity(CustomerRequest request) {
        Customer customer = new Customer();
        customer.setName(request.getName());
        customer.setAddress(request.getAddress());
        customer.setEmail(request.getEmail());
        customer.setDob(request.getDob());
        customer.setPhone(request.getPhone());
        customer.setNote(request.getNote());
        customer.setCustomerType(request.getCustomerType());
        return customer;
    }

    public void updateEntity(Customer customer, CustomerRequest request) {
        customer.setName(request.getName());
        customer.setAddress(request.getAddress());
        customer.setEmail(request.getEmail());
        customer.setDob(request.getDob());
        customer.setPhone(request.getPhone());
        customer.setNote(request.getNote());
        customer.setCustomerType(request.getCustomerType());
    }

    public CustomerResponse toResponse(Customer customer) {
        CustomerResponse response = new CustomerResponse();
        response.setCustomerId(customer.getCustomerId());
        response.setName(customer.getName());
        response.setAddress(customer.getAddress());
        response.setEmail(customer.getEmail());
        response.setDob(customer.getDob());
        response.setPhone(customer.getPhone());
        response.setNote(customer.getNote());
        response.setCustomerType(customer.getCustomerType());
        response.setCreatedAt(customer.getCreatedAt());
        response.setUpdatedAt(customer.getUpdatedAt());
        return response;
    }
}
