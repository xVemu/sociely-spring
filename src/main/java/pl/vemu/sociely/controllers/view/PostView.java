package pl.vemu.sociely.controllers.view;

import com.fasterxml.jackson.annotation.JsonView;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.Route;
import pl.vemu.sociely.controllers.PostController;
import pl.vemu.sociely.entities.PostDTO;
import pl.vemu.sociely.mappers.View;

import java.util.Date;
import java.util.List;

@Route("posts")
public class PostView extends VerticalLayout {

    private final PostController controller;
    @JsonView(View.Read.class) // TODO???
    private final Grid<PostDTO> grid = new Grid<>();

    public PostView(PostController controller) {
        this.controller = controller;
        refreshPosts();
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        createGrid();
        createAddForm();
    }

    private void refreshPosts() {
        List<PostDTO> users = controller.getPosts(null).getContent();
        grid.setItems(users);
    }

    private void createGrid() {
        grid.setSelectionMode(Grid.SelectionMode.MULTI);
        grid.addColumn(PostDTO::id).setHeader("Id").setSortable(true);
        grid.addColumn(PostDTO::text).setHeader("Text");
        grid.addColumn(PostDTO::creationDate).setHeader("Creation Date").setSortable(true);
        grid.addColumn(PostDTO::userName).setHeader("UserName").setSortable(true);
        grid.addColumn(createDeleteButton());
        add(grid);
    }

    private ComponentRenderer<Button, PostDTO> createDeleteButton() {
        return new ComponentRenderer<>(postDTO -> {
            var button = new Button("Usuń");
            button.addClickListener(buttonClickEvent -> {
                controller.deletePostById(postDTO.id());
                refreshPosts();
            });
            button.addThemeVariants(ButtonVariant.LUMO_ERROR);
            return button;
        }
        );
    }

    private Dialog createDialog() {
        Dialog dialog = new Dialog();
        dialog.setWidth("25%");
        // fixes closing dialog on outside click and reopening it
        dialog.addDialogCloseActionListener(dialogCloseActionEvent -> dialog.close());

        FormLayout formLayout = new FormLayout();

        TextArea textArea = new TextArea("Tekst");
        textArea.setMinHeight("250px");
        formLayout.add(textArea);

        Button button = new Button("Zapisz");
        button.addThemeVariants(ButtonVariant.LUMO_SUCCESS, ButtonVariant.LUMO_PRIMARY);
        button.addClickListener(buttonClickEvent -> {
            PostDTO postToSave = new PostDTO(textArea.getValue(), new Date(), 1L);
            controller.addPost(postToSave);
            refreshPosts();
            dialog.close();
        });

        dialog.add(formLayout);
        dialog.add(button);
        return dialog;
    }

    private void createAddForm() {
        Dialog dialog = createDialog();

        Button button = new Button("Dodaj!");
        button.addClickListener(buttonClickEvent -> dialog.open());

        add(button, dialog);
    }

}
