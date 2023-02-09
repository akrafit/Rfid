package Livestock.server.Rfid.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class CowMoveDto {
    private String date;
    private int allList;
    private int morning;
    private int morningOut;
    private int evening;
    private int eveningOut;

}
