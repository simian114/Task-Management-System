package taskmanagement.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Formula;
import taskmanagement.constants.TaskStatusEnum;
import taskmanagement.web.exception.exceptions.Forbidden.ForbiddenException;

@Getter
@Setter
@Entity
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false, name = "task_id")
    private long id;

    private String title;

    private String description;

    @Enumerated(EnumType.STRING)
    private TaskStatusEnum status = TaskStatusEnum.CREATED;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "author_id")
    private User author;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "assignee_id")
    private User assignee;

    @Formula("(SELECT COUNT(*) FROM Comment c where c.task_id = task_id)")
    private int totalComments;

    // domain logic
    @JsonIgnore
    public void changeAssignee(User currentUser, User assignee)
            throws ForbiddenException {
        if (currentUser.getUserId() != this.author.getUserId()) {
            throw new ForbiddenException("author만이 할당할 수 있는 권한을 가지고 있습니다.");
        }
        this.assignee = assignee;
    }

    @JsonIgnore
    public void changeStatus(User currentUser, TaskStatusEnum newStatus)
            throws ForbiddenException {
        if (this.author.getUserId() != currentUser.getUserId()
                && (this.assignee == null || (this.assignee.getUserId() != currentUser.getUserId()))
        ) {
            throw new ForbiddenException("author / assignee만이 상태를 변경할 수 있습니다.");
        }
        this.status = newStatus;
    }
}
