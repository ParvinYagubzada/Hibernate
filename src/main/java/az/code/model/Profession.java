package az.code.model;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "professions", schema = "hibernate")
@Entity
public class Profession {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    @ToString.Exclude
    @OneToMany(mappedBy = "profession")
    private List<Person> persons;
}
