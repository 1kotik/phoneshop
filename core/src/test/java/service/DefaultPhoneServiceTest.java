package service;

import com.es.core.dao.ColorDao;
import com.es.core.dao.PhoneDao;
import com.es.core.enums.SortCriteria;
import com.es.core.enums.SortOrder;
import com.es.core.exception.PhoneNotFoundException;
import com.es.core.model.Phone;
import com.es.core.model.PhoneListResponse;
import com.es.core.service.DefaultPhoneService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import util.PhoneTestUtils;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DefaultPhoneServiceTest {
    @Mock
    private PhoneDao phoneDao;
    @Mock
    private ColorDao colorDao;
    @InjectMocks
    private DefaultPhoneService defaultPhoneService;

    @Test
    void shouldGetPhone() {
        Long phoneId = 1L;
        when(phoneDao.get(phoneId)).thenReturn(Optional.of(PhoneTestUtils.getPhone(phoneId)));
        Phone phone = defaultPhoneService.get(phoneId);
        assertEquals(phoneId, phone.getId());
    }

    @Test
    void shouldThrowPhoneNotFoundException() {
        when(phoneDao.get(1L)).thenReturn(Optional.empty());
        assertThrows(PhoneNotFoundException.class, () -> defaultPhoneService.get(1L));
    }

    @Test
    void shouldSavePhone() {
        Phone phone = PhoneTestUtils.getPhone(1L);
        when(phoneDao.save(phone)).thenReturn(phone.getId());
        Long id = defaultPhoneService.save(phone);
        assertEquals(phone.getId(), id);
    }

    @ParameterizedTest(name = "{index} - query is {0}")
    @NullSource
    @ValueSource(strings = "")
    void shouldGetPhoneListWhenQueryIsEmptyOrNull(String query) {
        when(phoneDao.findAll(SortCriteria.QUERY, SortOrder.ASC)).thenReturn(PhoneTestUtils.getPhoneList());
        PhoneListResponse response = defaultPhoneService
                .findAll(query, "query", "asc", 1, 10);
        assertEquals(1, response.getTotalPages());
        assertEquals(PhoneTestUtils.getPhoneList().size(), response.getPhones().size());
    }

    @Test
    void shouldGetPhoneListWhenQueryIsNotEmpty() {
        when(phoneDao.findAll(SortCriteria.QUERY, SortOrder.ASC)).thenReturn(PhoneTestUtils.getPhoneList());
        PhoneListResponse response = defaultPhoneService
                .findAll("brand1", "query", "asc", 1, 10);
        assertEquals(1, response.getTotalPages());
        assertEquals(1, response.getPhones().size());
    }
}
