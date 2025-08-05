package ru.practicum.shareit.request;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestResponse;
import ru.practicum.shareit.user.UserMapper;

@Mapper(componentModel = "spring", uses = UserMapper.class)
public interface ItemRequestMapper {

    @Mapping(target = "requestor.id", source = "userId")
    @Mapping(target = "created", ignore = true)
    ItemRequest toItemRequest(Long userId, ItemRequestDto itemRequestDto);

    @Mapping(target = "requestorId", source = "requestor.id")
    @Mapping(target = "items", ignore = true)
    ItemRequestResponse toItemRequestResponse(ItemRequest itemRequest);
}


