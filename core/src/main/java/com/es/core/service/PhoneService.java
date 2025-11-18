package com.es.core.service;

import com.es.core.model.Phone;
import com.es.core.model.PhoneIdAndModelDto;
import com.es.core.model.PhoneListItem;
import com.es.core.model.PhoneListResponse;

import java.util.Collection;
import java.util.List;

public interface PhoneService {
    Phone get(Long key);
    Long save(Phone phone);
    PhoneListResponse findAll(String query, String sortCriteria, String sortOrderString, int page, int phonesPerPage);
    PhoneListItem getBriefInfoById(Long id);
    List<PhoneIdAndModelDto> findPhonesByModelList(Collection<String> models);
}
