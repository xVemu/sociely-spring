package pl.vemu.sociely.controllers.view;

import com.fasterxml.jackson.annotation.JsonView;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.Route;
import pl.vemu.sociely.controllers.UserController;
import pl.vemu.sociely.entities.UserDTO;
import pl.vemu.sociely.exceptions.user.UserByIdNotFoundException;
import pl.vemu.sociely.mappers.View.Read;

import java.util.List;

@Route("users")
public class UserView extends VerticalLayout {

    private final UserController controller;
    @JsonView(Read.class) // TODO???
    private final Grid<UserDTO> grid = new Grid<>();

    public UserView(UserController controller) {
        this.controller = controller;
        refreshUsers();
        add(
                createGrid()
        );
    }

    private void refreshUsers() {
        List<UserDTO> users = controller.getUsers(null).getContent();
        grid.setItems(users);
    }

    private Component createGrid() {
        grid.setSelectionMode(Grid.SelectionMode.MULTI);
        grid.addColumn(UserDTO::id).setHeader("Id").setSortable(true);
        grid.addColumn(UserDTO::name).setHeader("First name");
        grid.addColumn(UserDTO::surname).setHeader("Last name");
        grid.addColumn(UserDTO::email).setHeader("Email").setSortable(true);
        grid.addColumn(createDeleteButton());
        return grid;
    }

    private ComponentRenderer<Button, UserDTO> createDeleteButton() {
        return new ComponentRenderer<>(userDTO -> {
            var button = new Button("Usuń");
            button.addClickListener(buttonClickEvent -> {
                try {
                    controller.deleteUserById(userDTO.id());
                    refreshUsers();
                } catch (UserByIdNotFoundException e) {
                    e.printStackTrace();
                }
            });
            button.addThemeVariants(ButtonVariant.LUMO_ERROR);
            return button;
        }
        );
    }
}
