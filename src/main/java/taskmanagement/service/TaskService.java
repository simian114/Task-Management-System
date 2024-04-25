package taskmanagement.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import taskmanagement.domain.Comment;
import taskmanagement.domain.Task;
import taskmanagement.domain.User;
import taskmanagement.repository.CommentRepository;
import taskmanagement.repository.TaskRepository;
import taskmanagement.repository.UserRepository;
import taskmanagement.web.dto.CommentDto;
import taskmanagement.web.dto.TaskDto;
import taskmanagement.web.dto.TaskWithCommentCountDto;
import taskmanagement.web.dto.request.AssignTaskRequest;
import taskmanagement.web.dto.request.ChangeTasksStatusRequest;
import taskmanagement.web.exception.exceptions.Forbidden.ForbiddenException;
import taskmanagement.web.exception.exceptions.notFound.NotFoundException;
import taskmanagement.web.mapper.CommentMapper;
import taskmanagement.web.mapper.TaskMapper;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class TaskService {
    private final TaskRepository taskRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;

    private final TaskMapper taskMapper;
    private final CommentMapper commentMapper;

    @Transactional
    public List<TaskWithCommentCountDto> showAllTasksBy(String authorEmail, String assigneeEmail) {
        // how to make this beautiful?
        List<Task> list = authorEmail.isEmpty() && assigneeEmail.isEmpty()
                ? this.taskRepository.findAllByOrderByIdDesc()
                : !authorEmail.isEmpty() && assigneeEmail.isEmpty()
                ? this.taskRepository.findAllByAuthorEmailIgnoreCaseOrderByIdDesc(authorEmail)
                : authorEmail.isEmpty()
                ? this.taskRepository.findAllByAssigneeEmailIgnoreCaseOrderByIdDesc(assigneeEmail)
                : this.taskRepository.findAllByAuthorEmailIgnoreCaseAndAssigneeEmailIgnoreCaseOrderByIdDesc(
                authorEmail,
                assigneeEmail
        );
        return list.stream().map(this.taskMapper::toTaskWithCommentCountDto).toList();
    }

    @Transactional
    public TaskDto createTask(TaskDto taskDto, User currentUser)
            throws NotFoundException {
        Task task = this.taskMapper.toEntity(taskDto);
        task.setAuthor(this.userRepository.findByEmailIgnoreCase(currentUser.getEmail())
                .orElseThrow(() -> new NotFoundException("User not found")));
        return this.taskMapper.toDto(this.taskRepository.save(task));
    }

    @Transactional
    public TaskDto assignTask(long taskId, AssignTaskRequest assignTaskRequest, User currentUser)
            throws ForbiddenException, NotFoundException {
        // find task by taskId
        Task task = this.taskRepository.findById(taskId)
                .orElseThrow(() -> new NotFoundException("Task not found"));
        // find user by assignTaskRequest
        User assignee = "none".equals(assignTaskRequest.getAssignee())
                ? null
                : this.userRepository.findByEmailIgnoreCase(assignTaskRequest.getAssignee())
                .orElseThrow(() -> new NotFoundException("User not found"));

        task.changeAssignee(currentUser, assignee);

        Task saved = this.taskRepository.save(task);

        return this.taskMapper.toDto(saved);
    }

    @Transactional
    public TaskDto changeTaskStatus(long taskId, ChangeTasksStatusRequest changeTaskStatusRequest, User currentUser)
            throws ForbiddenException, NotFoundException {
        Task task = this.taskRepository.findById(taskId)
                .orElseThrow(() -> new NotFoundException("Task not found"));

        task.changeStatus(currentUser, changeTaskStatusRequest.getStatus());

        Task save = this.taskRepository.save(task);

        return this.taskMapper.toDto(save);
    }

    @Transactional
    public void addCommentTo(long taskId, CommentDto commentDto, User currentUser)
            throws NotFoundException {
        Task task = this.taskRepository.findById(taskId)
                .orElseThrow(() -> new NotFoundException("Task not found"));

        Comment comment = this.commentMapper.toEntity(commentDto, currentUser, task);
        this.commentRepository.save(comment);
    }

    @Transactional
    public List<CommentDto> showAllCommentOf(long taskId) throws NotFoundException {
        if (!this.taskRepository.existsById(taskId)) {
            throw new NotFoundException("Task not found");
        }
        this.taskRepository.findById(taskId).orElseThrow(() -> new NotFoundException("Task not found"));
        return this.commentRepository.findAllByTaskIdOrderByIdDesc(taskId).stream()
                .map(this.commentMapper::toDto)
                .toList();
    }
}
