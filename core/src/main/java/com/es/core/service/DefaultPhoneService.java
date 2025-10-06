package com.es.core.service;

import com.es.core.dao.ColorDao;
import com.es.core.dao.PhoneDao;
import com.es.core.exception.PhoneNotFoundException;
import com.es.core.model.Phone;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

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
    public List<Phone> findAll(int offset, int limit) {
        return phoneDao.findAll(offset, limit);
    }
}
