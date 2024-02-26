package org.delivery.api.domain.storemenu.business;

import lombok.RequiredArgsConstructor;
import org.delivery.api.domain.store.service.StoreService;
import org.delivery.common.annotation.Business;
import org.delivery.api.domain.storemenu.controller.model.StoreMenuRegisterRequest;
import org.delivery.api.domain.storemenu.controller.model.StoreMenuResponse;
import org.delivery.api.domain.storemenu.converter.StoreMenuConverter;
import org.delivery.api.domain.storemenu.service.StoreMenuService;
import org.delivery.db.store.StoreEntity;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Business
public class StoreMenuBusiness {

    private final StoreService storeService;

    private final StoreMenuService storeMenuService;
    private final StoreMenuConverter storeMenuConverter;

    public StoreMenuResponse register(StoreMenuRegisterRequest request) {
        var storeEntity = storeService.getStoreWithThrow(request.getStoreId());
        var entity = storeMenuConverter.toEntity(storeEntity, request);
        var newEntity = storeMenuService.register(entity);
        var response = storeMenuConverter.toResponse(newEntity);

        return response;
    }

    public List<StoreMenuResponse> search(Long storeId) {
        var list = storeMenuService.getStoreMenuByStoreId(storeId);

        return list.stream()
                .map(storeMenuConverter::toResponse)
                .collect(Collectors.toList());
    }
}
