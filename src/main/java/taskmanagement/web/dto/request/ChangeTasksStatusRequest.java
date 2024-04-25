package taskmanagement.web.dto.request;

import lombok.Getter;
import lombok.Setter;
import taskmanagement.constants.TaskStatusEnum;

@Getter
@Setter
public class ChangeTasksStatusRequest {
    private TaskStatusEnum status;
}
