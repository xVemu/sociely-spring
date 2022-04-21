package pl.vemu.sociely.dtos;


import com.fasterxml.jackson.annotation.JsonInclude;

public record UserDtoResponse(
        Long id,
        String name,
        @JsonInclude(JsonInclude.Include.NON_NULL)
        String surname,
        String email
) {

}
