package Livestock.server.Rfid.dto;

import Livestock.server.Rfid.model.Cow;
import lombok.Data;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

@Data
public class CowDto {
    private Long id;
    private String tag;
    private String type;
    private String color;
    private long birthday;
    private String createDate;
    private String status;
    private String link;

    DateTimeFormatter format = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    public CowDto(Cow cow) {
        LocalDateTime now = LocalDateTime.now();
        this.id = cow.getId();
        this.tag = cow.getTag();
        this.color = cow.getColor();
        this.type = cow.getType();
        if (cow.getBirthday() != null) {
            this.birthday = ChronoUnit.MONTHS.between(cow.getBirthday(), now);
        }else{
            this.birthday = 0;
        }
        this.createDate = cow.getCreateDate().format(format);
        this.status = cow.getStatus().toString();
        if(!cow.getPhotoList().isEmpty()){
            this.link = "<img class=\"bull\" src=\"/upload/bull.jpg\" onclick=\"showimg(" + cow.getId() + ")\">";
        }else{
            this.link = "-";
        }

        //this.img = cow.getPhotoList().get(0).getSrc();
    }





}
