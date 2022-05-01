package pl.vemu.sociely.views;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import pl.vemu.sociely.dtos.request.UserDtoRequest;
import pl.vemu.sociely.exceptions.user.UserWithEmailAlreadyExist;
import pl.vemu.sociely.services.UserService;

import javax.validation.Valid;

@Controller
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserView {

    private final UserService service;

    @GetMapping("signup")
    public String signup(Model model) {
        model.addAttribute("user", new UserDtoRequest());
        return "signup";
    }

    @PostMapping("signup")
    public String signup(@Valid @ModelAttribute("user") UserDtoRequest user, BindingResult result) {
        if (result.hasErrors()) return "signup";
        try {
            service.add(user);
            return "redirect:/api/users";
        } catch (UserWithEmailAlreadyExist e) {
            e.printStackTrace();
            result.addError(new FieldError("user", "email", e.getMessage()));
            return "signup";
        }
    }

    /**
     * Makes string null if is empty
     */
    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
    }

}
