package pl.vemu.sociely.views;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import pl.vemu.sociely.dtos.request.UserDtoRequest;
import pl.vemu.sociely.exceptions.user.UserWithEmailAlreadyExist;
import pl.vemu.sociely.services.UserService;

import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
public class UserView {

    private final UserService service;
    private final AuthenticationManager authenticationManager;

    @GetMapping("/signup")
    public String signup(Model model) {
        model.addAttribute("user", new UserDtoRequest());
        return "signup";
    }

    @PostMapping("/signup")
    public String signup(@Valid @ModelAttribute("user") UserDtoRequest user, BindingResult result) {
        if (result.hasErrors()) return "signup";
        try {
            service.add(user);
            var token = new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword());
            var auth = authenticationManager.authenticate(token);
            SecurityContextHolder.getContext().setAuthentication(auth);
            return "redirect:/";
        } catch (UserWithEmailAlreadyExist e) {
            e.printStackTrace();
            result.addError(new FieldError("user", "email", e.getMessage()));
            return "signup";
        }
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    /**
     * Makes string null if is empty
     */
    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
    }

}
