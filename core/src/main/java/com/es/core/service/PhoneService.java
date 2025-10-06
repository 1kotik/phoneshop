package com.es.core.service;

import com.es.core.model.Phone;

import java.util.List;

public interface PhoneService {
    Phone get(Long key);
    Long save(Phone phone);
    List<Phone> findAll(int offset, int limit);
}
