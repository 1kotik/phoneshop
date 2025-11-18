package com.es.phoneshop.web.controller.pages;

import com.es.core.model.ErrorItem;
import com.es.core.service.CartService;
import com.es.core.util.AppConstants;
import com.es.phoneshop.web.model.B2BCartForm;
import com.es.phoneshop.web.model.B2BErrorDto;
import com.es.phoneshop.web.utils.ParameterExtractor;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Controller
@RequestMapping("/cart/b2b")
public class B2BCartPage {
    private final CartService cartService;

    @Autowired
    public B2BCartPage(final CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping
    public String getB2BCartPage(Model model) {
        model.addAttribute(AppConstants.PageAttributes.CART_TOTALS, cartService.getCartTotals());
        if (!model.containsAttribute("b2bCartForm")) {
            model.addAttribute("b2bCartForm", new B2BCartForm());
        }
        return AppConstants.Pages.B2B_CART;
    }

    @PostMapping
    public String addItems(
            @ModelAttribute @Valid B2BCartForm b2bCartForm,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes,
            HttpServletRequest request) {
        Map<String, String> modelParamNameValueMap = new HashMap<>();
        Map<String, Integer> items = collectItemsFromB2BCartForm(b2bCartForm, request, modelParamNameValueMap);
        Map<String, ErrorItem> validationErrors = extractErrors(bindingResult, modelParamNameValueMap);
        Map<String, ErrorItem> insertErrors = cartService.b2bInsert(items);
        validationErrors.putAll(insertErrors);
        Map<String, B2BErrorDto> allErrors = changeValidationErrorKeysToCorrectParamNames(validationErrors, modelParamNameValueMap);
        List<String> successMessages = getSuccessMessages(allErrors, items);
        if (!allErrors.isEmpty()) {
            redirectAttributes.addFlashAttribute("b2bCartFormErrors", allErrors);
            redirectAttributes.addFlashAttribute("b2bCartForm", b2bCartForm);
        }
        redirectAttributes.addFlashAttribute("successMessages", successMessages);
        return AppConstants.Pages.REDIRECT_B2B_CART;
    }

    private Map<String, Integer> collectItemsFromB2BCartForm(B2BCartForm b2bCartForm, HttpServletRequest request,
                                                             Map<String, String> modelParamNameValueMap) {
        int rowNum = Integer.parseInt(request.getParameter("rowNum"));
        Map<String, Integer> formItems = b2bCartForm.getItems();
        Map<String, Integer> items = new HashMap<>();
        for (int i = 0; i <= rowNum; i++) {
            String productModelParameterName = String.format("model%d", i);
            String productModel = request.getParameter(productModelParameterName);
            Integer quantity = formItems.get(productModelParameterName);
            modelParamNameValueMap.put(productModelParameterName, productModel);
            if (quantity != null) {
                items.put(productModel, quantity);
            }
        }
        return items;
    }

    private Map<String, ErrorItem> extractErrors(BindingResult bindingResult,
                                                 Map<String, String> modelParamNameValueMap) {
        Map<String, ErrorItem> validationErrors = new HashMap<>();
        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            String itemCode = ParameterExtractor.extractItemCodeFromBindingResultField(fieldError.getField());
            String modelName = modelParamNameValueMap.get(itemCode);
            if (modelName != null && !modelName.isEmpty()) {
                validationErrors.put(itemCode, new ErrorItem(fieldError.getRejectedValue(), AppConstants.ErrorMessages.INVALID_FORMAT));
            }
        }
        return validationErrors;
    }

    private Map<String, B2BErrorDto> changeValidationErrorKeysToCorrectParamNames(
            Map<String, ErrorItem> validationErrors,
            Map<String, String> modelParamNameValueMap) {
        Map<String, B2BErrorDto> result = new HashMap<>();
        for (Map.Entry<String, String> entry : modelParamNameValueMap.entrySet()) {
            String paramName = entry.getKey();
            String modelName = entry.getValue();
            ErrorItem errorItem = validationErrors.get(modelName);
            if (errorItem == null) {
                errorItem = validationErrors.get(paramName);
            }
            if (errorItem != null) {
                result.put(paramName, new B2BErrorDto(modelName, String.valueOf(errorItem.getEnteredValue()), errorItem.getMessage()));
            }
        }
        return result;
    }

    private List<String> getSuccessMessages(Map<String, B2BErrorDto> errors, Map<String, Integer> items) {
        List<String> successMessages = new ArrayList<>();
        Set<String> models = new HashSet<>(items.keySet());
        for (Map.Entry<String, B2BErrorDto> entry : errors.entrySet()) {
            B2BErrorDto error = entry.getValue();
            String model = error.getEnteredModel();
            models.remove(model);
        }
        models.forEach(model -> successMessages.add(String.format("%s product added successfully", model)));
        return successMessages;
    }
}
