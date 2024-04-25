package taskmanagement.web.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommentDto {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String id;

    @JsonProperty(value = "task_id", access = JsonProperty.Access.READ_ONLY)
    private String taskId;

    @NotBlank
    private String text;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String author;
}
