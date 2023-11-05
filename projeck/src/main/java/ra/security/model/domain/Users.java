

package ra.security.model.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
//public class Users {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//    private String name;
//    @Column(unique = true)
//    private String username;
//    @JsonIgnore
//    private String password;
//    private String email;
//    private String phone;
//    private String address;
//    private boolean status=true;
//    @ManyToMany(fetch = FetchType.EAGER)
//    @JoinTable(name = "user_role"
//            ,joinColumns = @JoinColumn(name = "user_id")
//            ,inverseJoinColumns = @JoinColumn(name="role_id"))
//    private Set<Role> roles = new HashSet<>();
//    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
//    private List<Order> orders;
////
////    @ManyToMany(fetch = FetchType.LAZY)
////    @JoinTable(
////            name = "user_favorite_products",
////            joinColumns = @JoinColumn(name = "user_id"),
////            inverseJoinColumns = @JoinColumn(name = "product_id")
////    )
//    @OneToMany(mappedBy = "user")
//    private List<FavoriteProduct> favoriteProducts=new ArrayList<>();
//    @OneToMany(cascade = CascadeType.ALL,mappedBy = "user")
//    private List<Review> reviewList=new ArrayList<>();
//}
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String firstName;
    private String lastName;
    @Column(unique = true)
    private String email;
    //    @JsonIgnore
//    private String password;
    private String phone;
    private String address;
    private boolean sex;
    private Date dob;
    private int roleId;

    // Constructors, getters, and setters
}
