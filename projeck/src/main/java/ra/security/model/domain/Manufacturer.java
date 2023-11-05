package ra.security.model.domain;


import lombok.AllArgsConstructor;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import java.util.List;


@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Manufacturer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
private boolean status=true;
    @OneToMany(cascade = CascadeType.ALL ,fetch = FetchType.LAZY,mappedBy = "manufacturer")
    private List<Product> products;
}