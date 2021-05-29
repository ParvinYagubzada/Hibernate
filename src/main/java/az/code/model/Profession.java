package az.code.model;

import az.code.util.Color;
import lombok.*;

import javax.persistence.*;
import java.util.List;
import java.util.stream.Collectors;

import static az.code.util.Printer.colorString;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "professions", schema = "hibernate_new")
@Entity
public class Profession {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    @OneToMany(mappedBy = "profession")
    private List<Person> persons;

    @Override
    public String toString() {
        String people = this.getPersons().stream().map(Person::getName).collect(Collectors.joining(", "));
        return "ID: %s | NAME: %-30s | MOVIES: %s".formatted(
                colorString(Color.BLUE, this.id),
                colorString(Color.GREEN, this.name),
                colorString(Color.PURPLE, people)
        );
    }
}
