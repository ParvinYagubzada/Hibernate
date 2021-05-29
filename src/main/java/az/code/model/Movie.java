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
@Builder(access = AccessLevel.PUBLIC)
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "movies", schema = "hibernate_new")
@Entity
public class Movie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private Integer duration;
    @Column(name = "release_date")
    private LocalDate releaseDate;
    @ManyToMany
    @JoinTable(name = "relation_movie_genre",
            joinColumns = @JoinColumn(name = "movie_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "genre_id", referencedColumnName = "id")
    )
    private List<Genre> genres;

    @Override
    public String toString() {
        String genres = this.getGenres().stream().map(Genre::getName).collect(Collectors.joining(", "));
        return "ID: %s | NAME: %-30s | DURATION: %s | RELEASE DATE: %s | GENRES: %s".formatted(
                colorString(Color.BLUE, this.id),
                colorString(Color.GREEN, this.name),
                colorString(Color.GREEN, this.duration),
                colorString(Color.GREEN, this.releaseDate),
                colorString(Color.PURPLE, genres)
        );
    }
}
