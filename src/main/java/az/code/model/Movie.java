package az.code.model;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "movies", schema = "hibernate")
@Entity
public class Movie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;
    String name;
    Integer duration;
    @Column(name = "release_date")
    LocalDate releaseDate;
    @ToString.Exclude
    @ManyToMany
    @JoinTable(name = "relation_movie_genre",
            joinColumns = @JoinColumn(name = "movie_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "genre_id", referencedColumnName = "id")
    )
    List<Genre> genres;
}
