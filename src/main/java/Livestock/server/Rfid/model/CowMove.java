package Livestock.server.Rfid.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Setter
@Getter
@Table(name = "cow_move")
@NoArgsConstructor
public class CowMove {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "cow_id")
    private Cow cow;

    @Column(name = "date_time", columnDefinition = "TIMESTAMP")
    private String dateTime;
}
