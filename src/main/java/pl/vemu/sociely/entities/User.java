package pl.vemu.sociely.entities;

import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@ToString
@Entity
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
public class User {

    public User(String name, String surname, String email, String password) {
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.password = password;
    }

//    @Default
//    public User(Long id, String name, String surname, String email, String password) {
//        this.id = id;
//        this.name = name;
//        this.surname = surname;
//        this.email = email;
//        this.password = password;
//    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String surname;

    @Column(
            nullable = false,
            length = 320
    )
    private String email;

    @Column(
            nullable = false,
            columnDefinition = "char(60)"
    )
    private String password;

    @OneToMany(mappedBy = "user")
    private List<Post> posts;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        User user = (User) o;
        return id != null && Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}