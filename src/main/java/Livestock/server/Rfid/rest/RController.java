package Livestock.server.Rfid.rest;

import Livestock.server.Rfid.model.Cow;
import Livestock.server.Rfid.model.CowStatus;
import Livestock.server.Rfid.security.CowService;
import com.alibaba.fastjson.JSONObject;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestController
@RequestMapping("/api")
public class RController {

    DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private final CowService cowService;

    public RController(CowService cowService) {
        this.cowService = cowService;
    }

    @PostMapping(value = "/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Object> uploadImage(@RequestParam("file") MultipartFile image, @RequestParam("id") Long id) throws IOException, NoSuchAlgorithmException {
        return cowService.uploadImage(id, image);
    }

    @PostMapping("/addcow")
    public String addCow(@Valid @RequestBody JSONObject jsonObject) {
        System.out.println(jsonObject);

        String tag = jsonObject.getString("tag");
        String type = jsonObject.getString("type");
        String color = jsonObject.getString("color");
        String birthday = jsonObject.getString("birthday");
        Cow cow = new Cow();
        cow.setTag(tag.trim());
        cow.setType(type);
        cow.setColor(color);
        cow.setCreateDate(LocalDateTime.now());
        cow.setStatus(CowStatus.LIVE);
        cow.setBirthday(LocalDate.parse(birthday,format).atStartOfDay());
        cowService.addCow(cow);



        return "cow";
    }
}
