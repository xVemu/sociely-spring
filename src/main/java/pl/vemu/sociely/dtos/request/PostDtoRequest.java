package pl.vemu.sociely.dtos.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class PostDtoRequest {

    @NotBlank
    private String text;
}