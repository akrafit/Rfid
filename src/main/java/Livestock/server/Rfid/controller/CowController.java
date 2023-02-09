package Livestock.server.Rfid.controller;

import Livestock.server.Rfid.dto.CowDto;
import Livestock.server.Rfid.dto.CowMoveDto;
import Livestock.server.Rfid.model.Cow;
import Livestock.server.Rfid.model.CowMove;
import Livestock.server.Rfid.model.Photo;
import Livestock.server.Rfid.security.CowService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
        model.addAttribute("date", " " + cows.size() + " шт");
        model.addAttribute("title", "Список коров");
        return "cow";
    }

    @GetMapping("/traffic")
    public String traffic(Model model) {
        ArrayList<CowMoveDto> drives = cowService.findAllCowMove(null);
        model.addAttribute("drives", drives);
        return "traffic";
    }

    @GetMapping("/cow/search")
    public String trafficWithParam(@RequestParam(name = "date") String date, @RequestParam(name = "type") String type, Model model) {
        List<Cow> cowList = cowService.findCowWithParam(date,type);
        if(cowList.isEmpty())
        {
            return "traffic";
        }
        ArrayList<CowDto> cows = new ArrayList<>();
        for (Cow cow : cowList){
            cows.add(new CowDto(cow));
        }
        Map<String,String> info = cowService.createInfo(date,type);
        model.addAttribute("cows", cows);
        model.addAttribute("date", info.get("date") + " " +cows.size() + " шт.");
        model.addAttribute("title", info.get("title"));
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
