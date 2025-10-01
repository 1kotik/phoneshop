package com.es.core.dao;

import com.es.core.model.Color;

import java.util.List;

public interface ColorDao {
    List<Long> saveAll(List<Color> colors);
    List<Color> findAll();
}
