package az.code.model;

import az.code.util.Color;
import lombok.*;

import javax.persistence.*;

import static az.code.util.Printer.colorString;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "person_details", schema = "hibernate_new")
@Entity
public class PersonDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String address;
    @Column(name = "phone_number")
    private String phoneNumber;
    private String email;
    @OneToOne(mappedBy = "detail")
    private Person person;

    @Override
    public String toString() {
        return "ID: %s | ADDRESS: %-30s | PHONE-NUMBER: %s | EMAIL: %s | BELONGS TO: %s".formatted(
                colorString(Color.BLUE, this.id),
                colorString(Color.GREEN, this.address),
                colorString(Color.GREEN, this.phoneNumber),
                colorString(Color.GREEN, this.email),
                colorString(Color.PURPLE, person.getName())
        );
    }
}
