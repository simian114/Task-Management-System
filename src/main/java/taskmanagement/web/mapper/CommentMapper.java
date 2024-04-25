package taskmanagement.web.mapper;

import org.springframework.stereotype.Component;
import taskmanagement.domain.Comment;
import taskmanagement.domain.Task;
import taskmanagement.domain.User;
import taskmanagement.web.dto.CommentDto;

@Component
public class CommentMapper {
    public Comment toEntity(CommentDto commentDto, User user, Task task) {
        return Comment.builder()
                .text(commentDto.getText())
                .user(user)
                .task(task)
                .build();
    }

    public CommentDto toDto(Comment comment) {
        return CommentDto.builder()
                .id("" + comment.getId())
                .text(comment.getText())
                .author(comment.getUser().getEmail())
                .taskId("" + comment.getTask().getId())
                .build();
    }
}
