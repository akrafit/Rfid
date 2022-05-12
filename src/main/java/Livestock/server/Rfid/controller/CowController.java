package Livestock.server.Rfid.controller;

import Livestock.server.Rfid.dto.CowDto;
import Livestock.server.Rfid.model.Cow;
import Livestock.server.Rfid.model.Photo;
import Livestock.server.Rfid.security.CowService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
public class CowController {
    private final CowService cowService;

    public CowController(CowService cowService) {
        this.cowService = cowService;
    }

    @GetMapping("/cow")
    public String main(Model model) {
        Iterable<Cow> cowIterable = cowService.findAll();
        ArrayList<CowDto> cows = new ArrayList<>();
        for (Cow cow : cowIterable){
            cows.add(new CowDto(cow));
        }
       // cows.sort((cow, cow2) -> Math.toIntExact(cow.compareTo(cow2)));
        model.addAttribute("cows", cows);

        return "cow";
    }

    @GetMapping("/api/slider/{id}")
    public String getCowImage(@PathVariable(value = "id") Long id, Model model){
        Cow cow = cowService.getOne(id);
        if(cow != null){
            List<Photo> photoList = cow.getPhotoList();
            if(photoList.isEmpty()){
                return null;
            }
            ArrayList<Photo> cowImages = new ArrayList<>(photoList);
            // cows.sort((cow, cow2) -> Math.toIntExact(cow.compareTo(cow2)));
            model.addAttribute("cowImages", cowImages);

            return "slider";
        }else{
            return null;
        }

    }


}
