package taskmanagement.web.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import taskmanagement.constants.TaskStatusEnum;

@Getter
@Setter
public class TaskDto {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String id;

    @NotBlank
    private String title;

    @NotBlank
    private String description;

    // response only
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private TaskStatusEnum status;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String author;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String assignee;
}
