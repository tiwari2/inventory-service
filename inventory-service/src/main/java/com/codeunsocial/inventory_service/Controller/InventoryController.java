package com.codeunsocial.inventory_service.Controller;

import com.codeunsocial.inventory_service.dto.InventoryResponse;
import com.codeunsocial.inventory_service.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventory")
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryService inventoryService;

    /**
     *  With @PathVaribale Url will be like This
     *  http://localhost:8082/api/inventory/iphone-13,iphone13-red
     *
     *  With @RequestParam Url will be
     *  http://localhost:8082/api/inventory?skuCode=iphone13&skuCode=iphone13-red
     *
     *  as @RequestParam is more Redable and compatable for Large number of Sku so we use @RequestParam
     */


    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public List<InventoryResponse> isInStock(@RequestParam List<String> skuCode){
        return inventoryService.isInStock(skuCode);
    }
}
