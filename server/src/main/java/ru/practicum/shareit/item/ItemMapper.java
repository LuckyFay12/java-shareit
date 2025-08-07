package ru.practicum.shareit.item;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import ru.practicum.shareit.item.dto.CommentResponse;
import ru.practicum.shareit.item.dto.ItemCreateRequest;
import ru.practicum.shareit.item.dto.ItemResponse;
import ru.practicum.shareit.item.dto.ItemUpdateRequest;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserMapper;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", uses = {UserMapper.class, CommentMapper.class})
public abstract class ItemMapper {

    @Autowired
    @Lazy
    protected CommentRepository commentRepository;
    @Autowired
    protected CommentMapper commentMapper;

    @Mapping(target = "owner.id", source = "userId")
    @Mapping(target = "itemRequest.id", source = "request.requestId")
    public abstract Item toItem(ItemCreateRequest request, Long userId);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "owner.id", source = "userId")
    @Mapping(target = "itemRequest", ignore = true)
    public abstract Item toItem(ItemUpdateRequest request, Long userId);


    @Mapping(target = "ownerName", source = "owner.name")
    @Mapping(target = "requestId", source = "itemRequest.id")
    @Mapping(target = "comments", expression = "java(loadComments(item))")
    @Mapping(target = "lastBooking", ignore = true)
    @Mapping(target = "nextBooking", ignore = true)
    public abstract ItemResponse toItemResponse(Item item);

    protected List<CommentResponse> loadComments(Item item) {
        if (item.getId() == null) return Collections.emptyList();
        return commentRepository.findAllByItem(item).stream()
                .map(commentMapper::toCommentResponse)
                .collect(Collectors.toList());
    }
}




