package az.code.model;

import az.code.util.Color;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import static az.code.util.Printer.colorString;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "persons", schema = "hibernate_new")
@Entity
public class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private String surname;
    private String patronymic;
    @Column(name = "birthdate")
    private LocalDate birthDate;
    @ManyToOne
    @JoinColumn(name = "profession_id", referencedColumnName = "id")
    private Profession profession;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "detail_id", referencedColumnName = "id")
    private PersonDetail detail;

    @ManyToMany(mappedBy = "persons")
    private List<Movie> movies;

    @Override
    public String toString() {
        String movies = this.movies != null ? this.getMovies().stream().map(Movie::getName).collect(Collectors.joining(", ")) : "NOT DEFINED";
        return "ID: %s | NAME: %-30s | SURNAME: %s | PATRONYMIC: %s | BIRTHDATE: %s | PROFESSION: %s | MOVIES: %-25s | DETAILS: %s".formatted(
                colorString(Color.BLUE, this.id),
                colorString(Color.GREEN, this.name),
                colorString(Color.GREEN, this.surname),
                colorString(Color.GREEN, this.patronymic),
                colorString(Color.GREEN, this.birthDate),
                colorString(Color.GREEN, this.profession.getName()),
                colorString(Color.GREEN, movies),
                colorString(Color.GREEN, String.join("|",
                        this.detail.getAddress(),
                        this.detail.getEmail(),
                        this.detail.getPhoneNumber())
                ));
    }
}
