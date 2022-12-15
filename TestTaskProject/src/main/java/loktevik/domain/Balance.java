package loktevik.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name="BALANCE")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Balance {

    @Id
    private Long id;
    private Long amount;
}
