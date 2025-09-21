package com.example.researcharticles.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class TagRequest {

    @NotBlank(message = "Tag name can not be blank")
    @Size(max = 255, message = "Tag name must be at most 255 characters")
    private String name;
}
