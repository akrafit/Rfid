package Livestock.server.Rfid.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name="photo")
public class Photo {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "src", columnDefinition = "varchar(255)")
    private String src;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "cow_id")
    private Cow cow;
}
