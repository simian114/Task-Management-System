package taskmanagement.web.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import taskmanagement.domain.User;
import taskmanagement.service.TaskService;
import taskmanagement.web.argumentResolver.annotation.LoginUser;
import taskmanagement.web.dto.CommentDto;
import taskmanagement.web.dto.TaskDto;
import taskmanagement.web.dto.request.AssignTaskRequest;
import taskmanagement.web.dto.request.ChangeTasksStatusRequest;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class TaskController {
    private final TaskService taskService;

    @GetMapping("/tasks")
    public ResponseEntity<?> showAllTasks(@RequestParam(value = "author", required = false, defaultValue = "") String author,
                                          @RequestParam(value = "assignee", required = false, defaultValue = "") String assignee
    ) {
        return ResponseEntity.ok(this.taskService.showAllTasksBy(author, assignee));
    }

    @PostMapping("/tasks")
    public ResponseEntity<?> createTask(@Valid @RequestBody TaskDto taskDto,
                                        @LoginUser User currentUser
    ) {
        return ResponseEntity.ok(this.taskService.createTask(taskDto, currentUser));
    }

    @PutMapping("/tasks/{taskId}/assign")
    public ResponseEntity<?> assignTask(@PathVariable long taskId,
                                        @Valid @RequestBody AssignTaskRequest assignTaskRequest,
                                        @LoginUser User currentUser
    ) {
        return ResponseEntity.ok(this.taskService.assignTask(taskId, assignTaskRequest, currentUser));
    }

    @PutMapping("/tasks/{taskId}/status")
    public ResponseEntity<?> changeTaskStatus(@PathVariable long taskId,
                                              @Valid @RequestBody ChangeTasksStatusRequest changeTaskStatusRequest,
                                              @LoginUser User currentUser
    ) {
        return ResponseEntity.ok(this.taskService.changeTaskStatus(taskId, changeTaskStatusRequest, currentUser));
    }

    @PostMapping("/tasks/{taskId}/comments")
    public ResponseEntity<?> createCommentToTask(@PathVariable long taskId,
                                                 @Valid @RequestBody CommentDto commentDto,
                                                 @LoginUser User currentUser
    ) {
        this.taskService.addCommentTo(taskId, commentDto, currentUser);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/tasks/{taskId}/comments")
    public ResponseEntity<?> showTaskAllComments(@PathVariable("taskId") long taskId) {
        return ResponseEntity.ok(this.taskService.showAllCommentOf(taskId));
    }
}
