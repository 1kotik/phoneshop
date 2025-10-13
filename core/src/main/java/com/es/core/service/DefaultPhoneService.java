package com.es.core.service;

import com.es.core.dao.ColorDao;
import com.es.core.dao.PhoneDao;
import com.es.core.enums.SortCriteria;
import com.es.core.enums.SortOrder;
import com.es.core.exception.PhoneNotFoundException;
import com.es.core.model.Color;
import com.es.core.model.Phone;
import com.es.core.model.PhoneListItem;
import com.es.core.model.PhoneListResponse;
import com.es.core.util.PhoneQueryMatchNumberComparator;
import com.es.core.util.PhoneQueryUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class DefaultPhoneService implements PhoneService {
    private final PhoneDao phoneDao;
    private final ColorDao colorDao;

    public DefaultPhoneService(PhoneDao phoneDao, ColorDao colorDao) {
        this.phoneDao = phoneDao;
        this.colorDao = colorDao;
    }

    @Override
    public Phone get(Long key) {
        return phoneDao.get(key)
                .orElseThrow(() -> new PhoneNotFoundException(key));
    }

    @Override
    @Transactional
    public Long save(Phone phone) {
        Long phoneId = phoneDao.save(phone);
        List<Long> colorIds = colorDao.saveAll(new ArrayList<>(phone.getColors()));
        phoneDao.savePhoneColorRelations(colorIds, phoneId);
        return phoneId;
    }

    @Override
    public PhoneListResponse findAll(String query, String sortCriteria, String sortOrder,
                                     int page, int phonesPerPage) {
        SortCriteria criteria = SortCriteria.getEnum(sortCriteria);
        int offset = (page - 1) * phonesPerPage;
        PhoneListResponse response = phoneDao
                .findAll(query, criteria, SortOrder.getEnum(sortOrder), offset, phonesPerPage);
        setColors(response.getPhones());
        return response;
    }

    private void setColors(List<PhoneListItem> phones) {
        Map<Long, Set<Color>> colorMap = colorDao
                .findColorsByPhoneIds(phones.stream().map(PhoneListItem::getId).collect(Collectors.toSet()));
        phones.forEach(phone -> phone.setColors(colorMap.get(phone.getId())));
    }
}
