package com.codeunsocial.order_service.service;

import com.codeunsocial.order_service.dto.InventoryResponse;
import com.codeunsocial.order_service.dto.OrderLineItemsDto;
import com.codeunsocial.order_service.dto.OrderRequest;
import com.codeunsocial.order_service.model.Order;
import com.codeunsocial.order_service.model.OrderLineItemList;
import com.codeunsocial.order_service.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.aspectj.weaver.ast.Or;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;
    private final WebClient webClient;

    public void placeOrder(OrderRequest orderRequest){
        Order order = new Order();
        order.setOrderNumber(UUID.randomUUID().toString());
        List<OrderLineItemList> orderLineItems = orderRequest.getOrderLineItemsDtoList().stream().map(this::mapToDto)
                .toList();
        order.setOrderLineItemList(orderLineItems);

        List<String> skuCodes = order.getOrderLineItemList().stream()
                .map(OrderLineItemList :: getSkuCode)
                .toList();

        //Calling Inventory Service and Place Order if Product is in Stock
        InventoryResponse[] inventoryResponsesArray = webClient.get()
                .uri("http://localhost:8082/api/inventory",
                        uriBuilder -> uriBuilder.queryParam("skuCode", skuCodes).build())
                .retrieve()
                .bodyToMono(InventoryResponse[].class)
                .block();
        boolean allProductsInStock = Arrays.stream(inventoryResponsesArray).allMatch(inventoryResponse -> inventoryResponse.isInStock());

        if(allProductsInStock){
            orderRepository.save(order);
        }else{
            throw new IllegalArgumentException("Product is not in stock, Please try again later");
        }

    }

    private OrderLineItemList mapToDto(OrderLineItemsDto orderLineItemsDto) {
        OrderLineItemList orderLineItemList = new OrderLineItemList();
        orderLineItemList.setPrice(orderLineItemsDto.getPrice());
        orderLineItemList.setQuantity(orderLineItemsDto.getQuantity());
        orderLineItemList.setSkuCode(orderLineItemsDto.getSkuCode());
        return orderLineItemList;
    }


}
