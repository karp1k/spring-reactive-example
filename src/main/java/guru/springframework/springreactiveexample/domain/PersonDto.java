package guru.springframework.springreactiveexample.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author kas
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PersonDto {


    public PersonDto(Person person) {
        this.firstName = person.getFirstName();
        this.lastName = person.getLastName();
    }

    private String firstName;
    private String lastName;

    public String getFullName() {
        return "My name is " + firstName + " " + lastName + ". From DTO.";
    }
}
