package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class CommentRepositoryTest {

    private final CommentRepository commentRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    private Item item;
    private User author;

    @BeforeEach
    void setUp() {
        author = userRepository.save(User.builder()
                .name("Author")
                .email("author@mail.ru")
                .build());

        User owner = userRepository.save(User.builder()
                .name("Item Owner")
                .email("owner@mail.ru")
                .build());

        item = itemRepository.save(Item.builder()
                .name("Test Item")
                .description("Test Description")
                .available(true)
                .owner(owner)
                .build());
    }

    @Test
    void findAllByItem_ShouldReturnCommentsForItem() {
        Comment comment1 = Comment.builder()
                .text("Comment 1")
                .item(item)
                .author(author)
                .created(LocalDateTime.now())
                .build();

        Comment comment2 = Comment.builder()
                .text("Comment 2")
                .item(item)
                .author(author)
                .created(LocalDateTime.now().plusHours(1))
                .build();

        commentRepository.save(comment1);
        commentRepository.save(comment2);

        List<Comment> comments = commentRepository.findAllByItem(item);

        assertThat(comments)
                .hasSize(2)
                .extracting(Comment::getText)
                .containsExactly("Comment 1", "Comment 2");
    }

    @Test
    void findAllByItem_ShouldReturnEmptyListForItemWithoutComments() {
        List<Comment> comments = commentRepository.findAllByItem(item);

        assertThat(comments).isEmpty();
    }

    @Test
    void save_ShouldSaveComment() {
        Comment comment = Comment.builder()
                .text("Test Comment")
                .item(item)
                .author(author)
                .created(LocalDateTime.now())
                .build();

        Comment savedComment = commentRepository.save(comment);

        assertThat(savedComment).isNotNull();
        assertThat(savedComment.getId()).isNotNull();
        assertThat(savedComment.getText()).isEqualTo("Test Comment");
        assertThat(savedComment.getItem()).isEqualTo(item);
        assertThat(savedComment.getAuthor()).isEqualTo(author);
        assertThat(savedComment.getCreated()).isNotNull();
    }
}