package pl.vemu.sociely.dtos.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class PostDtoRequest {

    @NotBlank
    private String text;
}