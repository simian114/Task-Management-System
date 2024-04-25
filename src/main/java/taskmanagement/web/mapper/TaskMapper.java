package taskmanagement.web.mapper;

import org.springframework.stereotype.Component;
import taskmanagement.domain.Task;
import taskmanagement.web.dto.TaskDto;
import taskmanagement.web.dto.TaskWithCommentCountDto;

@Component
public class TaskMapper {
    public Task toEntity(TaskDto taskDto) {
        Task task = new Task();
        task.setTitle(taskDto.getTitle());
        task.setDescription(taskDto.getDescription());
        // status..
        if (taskDto.getStatus() != null) {
            task.setStatus(taskDto.getStatus());
        }
        return task;
    }

    public TaskWithCommentCountDto toTaskWithCommentCountDto(Task task) {
        TaskWithCommentCountDto taskWithCommentCountDto = new TaskWithCommentCountDto();
        taskWithCommentCountDto.setId("" + task.getId());
        taskWithCommentCountDto.setTitle(task.getTitle());
        taskWithCommentCountDto.setDescription(task.getDescription());
        taskWithCommentCountDto.setStatus(task.getStatus());
        if (task.getAuthor() != null) {
            taskWithCommentCountDto.setAuthor(task.getAuthor().getEmail().toLowerCase());
        }
        taskWithCommentCountDto.setTotalComments(task.getTotalComments());
        taskWithCommentCountDto.setAssignee(
                task.getAssignee() != null
                        ? task.getAssignee().getEmail().toLowerCase()
                        : "none"
        );
        return taskWithCommentCountDto;
    }

    public TaskDto toDto(Task task) {
        TaskDto taskDto = new TaskDto();
        taskDto.setId("" + task.getId());
        taskDto.setTitle(task.getTitle());
        taskDto.setDescription(task.getDescription());
        taskDto.setStatus(task.getStatus());
        if (task.getAuthor() != null) {
            taskDto.setAuthor(task.getAuthor().getEmail().toLowerCase());
        }
        taskDto.setAssignee(
                task.getAssignee() != null
                        ? task.getAssignee().getEmail().toLowerCase()
                        : "none"
        );
        return taskDto;
    }
}
