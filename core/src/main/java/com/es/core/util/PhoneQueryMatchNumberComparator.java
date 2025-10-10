package com.es.core.util;

import com.es.core.model.PhoneListItem;

import java.util.Comparator;

public class PhoneQueryMatchNumberComparator implements Comparator<PhoneListItem> {
    private String[] queryParts;

    public PhoneQueryMatchNumberComparator(String[] queryParts) {
        this.queryParts = queryParts;
    }

    @Override
    public int compare(PhoneListItem phone1, PhoneListItem phone2) {
        long queryMatchNumber1 = PhoneQueryUtils.getQueryMatchNumber(phone1.getBrand(), phone1.getModel(), queryParts);
        long queryMatchNumber2 = PhoneQueryUtils.getQueryMatchNumber(phone2.getBrand(), phone2.getModel(), queryParts);
        return Long.compare(queryMatchNumber2, queryMatchNumber1);
    }
}
