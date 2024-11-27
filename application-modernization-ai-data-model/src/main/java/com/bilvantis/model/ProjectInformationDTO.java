package com.bilvantis.model;

import com.bilvantis.util.OnCreate;
import com.bilvantis.util.OnUpdate;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

import static com.bilvantis.util.ModelConstants.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectInformationDTO extends BaseDTO {
    @Null(groups = OnCreate.class, message = MESSAGE_PROJECT_ID_ON_CREATE)
    @NotNull(groups = OnUpdate.class, message = MESSAGE_PROJECT_ID_ON_UPDATE)
    private String id;

    @NotBlank(message = PROJECT_NAME_NOT_NULL)
    @Pattern(regexp = REGEX_PATTERN_LTN, message = SPECIAL_CHARACTERS_PROJECT_NAME)
    @Size(max = MAX_SIZE, message = PROJECT_NAME_LENGTH_MESSAGE)
    private String name;

    @NotBlank(message = PROJECT_DESCRIPTION_NOT_NULL)
    @Pattern(regexp = REGEX_PATTERN_LTN, message = SPECIAL_CHARACTERS_PROJECT_DESCRIPTION)
    @Size(max = MAX_SIZE, message = PROJECT_NAME_LENGTH_MESSAGE)
    private String description;
    private String ownerId;
    private String status;
    private String language;
    private String version;
    private List<UserInformationDTO> taggedUsers;

}
