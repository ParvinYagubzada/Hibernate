package az.code.model;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "genres", schema = "hibernate")
@Entity
public class Genre {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;
    String name;
    @ToString.Exclude
    @ManyToMany(mappedBy = "genres")
    List<Movie> movies;
}
