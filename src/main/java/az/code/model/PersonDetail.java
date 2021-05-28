package az.code.model;

import lombok.*;

import javax.persistence.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "person_details", schema = "hibernate")
@Entity
public class PersonDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;
    String address;
    @Column(name = "phone_number")
    String phoneNumber;
    String email;
    @ToString.Exclude
    @OneToOne(mappedBy = "detail")
    Person person;
}
