package com.rebodev.prueba.service;

import com.rebodev.prueba.model.entity.copomex.Address;

public interface IAddressService {
    Address getAddressByCp(String cp);
}
