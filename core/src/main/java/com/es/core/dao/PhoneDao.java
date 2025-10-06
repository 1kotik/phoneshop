package com.es.core.dao;

import com.es.core.model.Phone;

import java.util.List;
import java.util.Optional;

public interface PhoneDao {
    Optional<Phone> get(Long key);
    Long save(Phone phone);
    List<Phone> findAll(int offset, int limit);
    void savePhoneColorRelations(List<Long> colors, Long phoneId);
}
