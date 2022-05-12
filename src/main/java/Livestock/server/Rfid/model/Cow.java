package Livestock.server.Rfid.model;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@Table(name="cow")
public class Cow {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "type", columnDefinition = "varchar(255)")
    private String type;

    @Column(name = "birthday")
    private LocalDateTime birthday;

    @Column(name = "create_date")
    private LocalDateTime createDate;

    @Column(name = "color", columnDefinition = "varchar(255)")
    private String color;

    @Column(name = "tag", columnDefinition = "varchar(255)")
    private String tag;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "cow_id", nullable = false, insertable = false, updatable = false)
    private List<Photo> photoList;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "status", columnDefinition = "varchar(255)")
    private CowStatus status;

    //public Long compareTo(@org.jetbrains.annotations.NotNull Cow cow) {
    //    return this.getId() - cow.getId();
    //}
}
