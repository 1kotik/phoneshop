package com.es.core.dao;

import com.es.core.model.Color;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface ColorDao {
    List<Long> saveAll(List<Color> colors);
    List<Color> findAll();
    Map<Long, Set<Color>> findColorsByPhoneIds(Set<Long> phoneIds);
}
