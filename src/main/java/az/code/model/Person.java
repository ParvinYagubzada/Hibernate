package az.code.model;

import lombok.*;

import javax.persistence.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "persons", schema = "hibernate")
@Entity
public class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;
    String name;
    String surname;
    String patronymic;
    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name = "profession_id", referencedColumnName = "id")
    Profession profession;
    @ToString.Exclude
    @OneToOne
    @MapsId
    PersonDetail detail;
}
