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
@Table(name = "genres", schema = "hibernate_new")
@Entity
public class Genre {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    @ManyToMany(mappedBy = "genres")
    private List<Movie> movies;

    @Override
    public String toString() {
        String movies = this.getMovies().stream().map(Movie::getName).collect(Collectors.joining(", "));
        return "ID: %s | NAME: %-30s | MOVIES: %s".formatted(
                colorString(Color.BLUE, this.id),
                colorString(Color.GREEN, this.name),
                colorString(Color.PURPLE, movies)
        );
    }
}
