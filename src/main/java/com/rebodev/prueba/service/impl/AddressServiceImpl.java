package com.rebodev.prueba.service.impl;

import com.rebodev.prueba.model.entity.copomex.Address;
import com.rebodev.prueba.model.entity.copomex.CopoMexResponse;
import com.rebodev.prueba.service.IAddressService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class AddressServiceImpl implements IAddressService {


    @Value("${url.copomex}")
    private String urlCopomex;

    @Value("${api.key.copomex}")
    private String apiKeyCopomex;

    private String urlInfoByCp = "query/info_cp";

    @Override
    public Address getAddressByCp(String cp) {
        Address address = Address.builder()
                .cp(cp).build();

        List<CopoMexResponse> listAddress;
        try {
            String uri = urlCopomex + "/" + urlInfoByCp + "/" + cp + "?token=" + apiKeyCopomex;
            RestTemplate template = new RestTemplate();
            listAddress = template.exchange(
                    uri,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<CopoMexResponse>>() {
                    }
            ).getBody();

            assert listAddress != null;
            if (!listAddress.isEmpty()) {
                Address firstAddress = listAddress.get(0).getResponse();
                address.setCiudad(firstAddress.getCiudad());
                address.setEstado(firstAddress.getEstado());
                address.setMunicipio(firstAddress.getMunicipio());
                address.setTipo_asentamiento(firstAddress.getTipo_asentamiento());
            }
            return address;
        } catch (Exception e) {
            return address;
        }
    }
}
