package ru.practicum.shareit.item;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.user.UserMapper;

import java.util.List;

@Mapper(componentModel = "spring",
        uses = {UserMapper.class, ItemMapper.class})
public interface CommentMapper {

    @Mapping(target = "item.id", source = "itemId")
    @Mapping(target = "author.id", source = "userId")
    @Mapping(target = "created", expression = "java(java.time.LocalDateTime.now())")
    Comment toComment(CommentCreateRequest request, Long itemId, Long userId);

    @Mapping(target = "authorName", source = "author.name")
    @Mapping(target = "itemName", source = "item.name")
    CommentResponse toCommentResponse(Comment comment);

    @Mapping(target = "authorName", source = "author.name")
    @Mapping(target = "itemName", source = "item.name")
    List<CommentResponse> toCommentResponseList(List<Comment> comments);
}
