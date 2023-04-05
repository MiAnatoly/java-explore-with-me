package ru.practicum.ewmservice.dto.compilation;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NewCompilationDto {
    @NotNull
    private List<Long> events;
    @NotNull
    private Boolean pinned;
    @NotBlank
    private  String title;
}
