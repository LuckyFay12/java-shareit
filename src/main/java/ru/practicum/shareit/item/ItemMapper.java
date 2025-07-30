package ru.practicum.shareit.item;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;
import ru.practicum.shareit.item.dto.ItemCreateRequest;
import ru.practicum.shareit.item.dto.ItemResponse;
import ru.practicum.shareit.item.dto.ItemUpdateRequest;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserMapper;

@Mapper(componentModel = "spring", uses = {UserMapper.class, CommentMapper.class})
public abstract class ItemMapper {

    @Autowired
    protected CommentRepository commentRepository;
    @Autowired
    protected CommentMapper commentMapper;

    @Mapping(target = "owner.id", source = "userId")
    @Mapping(target = "itemRequest", ignore = true)
    public abstract Item toItem(ItemCreateRequest request, Long userId);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "owner.id", source = "userId")
    public abstract Item toItem(ItemUpdateRequest request, Long userId);


    @Mapping(target = "ownerName", source = "owner.name")
    @Mapping(target = "requestId", source = "itemRequest.id")
    @Mapping(target = "comments", ignore = true)
    @Mapping(target = "lastBooking", ignore = true)
    @Mapping(target = "nextBooking", ignore = true)
    public abstract ItemResponse toItemResponse(Item item);

    @AfterMapping
    protected void fillComments(Item item, @MappingTarget ItemResponse itemResponse) {
        itemResponse.setComments(commentMapper.toCommentResponseList(commentRepository.findAllByItem(item)));
    }
}







