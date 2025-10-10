package com.es.core.service;

import com.es.core.model.Phone;
import com.es.core.model.PhoneListResponse;

public interface PhoneService {
    Phone get(Long key);
    Long save(Phone phone);
    PhoneListResponse findAll(String query, String sortCriteria, String sortOrderString, int page, int phonesPerPage);
}
