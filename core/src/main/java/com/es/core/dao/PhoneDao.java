package com.es.core.dao;

import com.es.core.enums.SortCriteria;
import com.es.core.enums.SortOrder;
import com.es.core.model.Phone;
import com.es.core.model.PhoneListResponse;

import java.util.List;
import java.util.Optional;

public interface PhoneDao {
    Optional<Phone> get(Long key);
    Long save(Phone phone);
    PhoneListResponse findAll(String query, SortCriteria sortCriteria, SortOrder sortOrder, int offset, int limit);
    void savePhoneColorRelations(List<Long> colors, Long phoneId);
}
