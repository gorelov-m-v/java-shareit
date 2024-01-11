package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import ru.practicum.shareit.validator.Create;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
public class ItemWithRequestResponseDto {
    private Long id;
    @NotBlank(groups = Create.class, message = "Имя должно быть заполнено.")
    private String name;
    @NotBlank(groups = Create.class, message = "Описание должно быть заполнено.")
    private String description;
    @NotNull(groups = Create.class)
    private Boolean available;
    private Long ownerId;
    private Long requestId;
}
