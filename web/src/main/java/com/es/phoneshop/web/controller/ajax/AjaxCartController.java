package com.es.phoneshop.web.controller.ajax;

import com.es.core.model.CartTotals;
import com.es.core.service.CartService;
import jakarta.annotation.Resource;
import jakarta.validation.constraints.Min;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/ajaxCart")
public class AjaxCartController {
    @Resource
    private CartService cartService;

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public CartTotals addPhone(
            @RequestParam(name = "phoneId") Long phoneId,
            @RequestParam(name = "quantity") @Min(1) Integer quantity) {
        cartService.addPhone(phoneId, quantity);
        return cartService.getCartTotals();
    }

    @DeleteMapping("/{phoneId}")
    @ResponseStatus(code = HttpStatus.OK)
    public CartTotals removePhone(@PathVariable(name = "phoneId") Long phoneId) {
        cartService.remove(phoneId);
        return cartService.getCartTotals();
    }
}
