package ra.security.model.domain;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
;
import java.util.Date;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Promotion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String promotionCode;
    private int discountValue;
    @Temporal(TemporalType.DATE)//không bao gồm giờ phút giây
    private Date startDate;
    @Temporal(TemporalType.DATE)
    private Date endDate;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "promotion")
    private List<Order> order;
    private int quantity;
}
