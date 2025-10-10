package com.es.core.dao;

import com.es.core.enums.SortCriteria;
import com.es.core.enums.SortOrder;
import com.es.core.model.Phone;
import com.es.core.model.PhoneListItem;

import java.util.List;
import java.util.Optional;

public interface PhoneDao {
    Optional<Phone> get(Long key);
    Long save(Phone phone);
    List<PhoneListItem> findAll(SortCriteria sortCriteria, SortOrder sortOrder);
    void savePhoneColorRelations(List<Long> colors, Long phoneId);
}
