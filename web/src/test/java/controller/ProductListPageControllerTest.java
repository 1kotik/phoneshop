package controller;

import com.es.core.model.CartTotals;
import com.es.core.model.PhoneListItem;
import com.es.core.model.PhoneListResponse;
import com.es.core.service.CartService;
import com.es.core.service.PhoneService;
import com.es.core.util.AppConstants;
import com.es.phoneshop.web.controller.pages.ProductListPageController;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductListPageControllerTest {
    @Mock
    private PhoneService phoneService;
    @Mock
    private CartService cartService;
    @InjectMocks
    private ProductListPageController productListPageController;

    @Test
    void shouldFindPhones() {
        String query = "";
        String sortCriteria = "brand";
        String sortOrder = "asc";
        int page = 1;
        int pageSize = 10;
        Model model = new ExtendedModelMap();
        CartTotals cartTotals = new CartTotals(100, BigDecimal.TEN);
        PhoneListResponse phoneListResponse = new PhoneListResponse(getPhoneList(), 1);
        when(phoneService.findAll(query, sortCriteria, sortOrder, page, pageSize))
                .thenReturn(phoneListResponse);
        when(cartService.getCartTotals()).thenReturn(cartTotals);
        String view = productListPageController.showProductList(model, query, sortCriteria, sortOrder, page, pageSize);
        assertEquals(AppConstants.Pages.PRODUCT_LIST, view);
        assertEquals(cartTotals, model.getAttribute(AppConstants.PageAttributes.CART_TOTALS));
        assertEquals(phoneListResponse, model.getAttribute(AppConstants.PageAttributes.PHONE_LIST_RESPONSE));
    }

    private List<PhoneListItem> getPhoneList() {
        PhoneListItem item1 = new PhoneListItem(1L, "brand1", "model1",
                BigDecimal.TEN, BigDecimal.TEN, "image1", Collections.EMPTY_SET);
        PhoneListItem item2 = new PhoneListItem(2L, "brand2", "model2",
                BigDecimal.TEN, BigDecimal.TEN, "image2", Collections.EMPTY_SET);
        return List.of(item1, item2);
    }
}
