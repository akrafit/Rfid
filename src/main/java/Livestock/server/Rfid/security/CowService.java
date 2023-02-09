package Livestock.server.Rfid.security;

import Livestock.server.Rfid.dto.CowMoveDto;
import Livestock.server.Rfid.model.*;
import Livestock.server.Rfid.repository.*;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@RequiredArgsConstructor
public class CowService {
    @Value("${upload}")
    private String uploadPath;
    private final CowRepository cowRepository;
    private final PhotoRepository photoRepository;
    private final TagRepository tagRepository;
    private final CowMoveRepository cowMoveRepository;
    private final TagOnDayRepository tagOnDayRepository;

    DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    DateTimeFormatter formatSite = DateTimeFormatter.ofPattern("dd.MM.yy");
    DateTimeFormatter formatMysql = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public Iterable<Cow> findAll() {

        return cowRepository.findAll();
    }

    public Cow getOne(Long id) {

        return cowRepository.getOne(id);
    }

    public ResponseEntity<Object> uploadImage(Long id, MultipartFile image) throws IOException {
        String tripleFolder = uploadPath + generateYearMonthFolder();
        Path path = Paths.get(tripleFolder);
        Files.createDirectories(path);
        File myFile = new File("/.." + tripleFolder + image.getOriginalFilename());
        FileOutputStream fos = new FileOutputStream(myFile);
        fos.write(image.getBytes());
        fos.close();
        String src = tripleFolder + image.getOriginalFilename();
        Cow cow = cowRepository.getOne(id);
        Photo photo = new Photo();
        photo.setCow(cow);
        photo.setSrc(src);
        photoRepository.save(photo);


        return new ResponseEntity<>(src, HttpStatus.OK);
    }

    private String generateTripleFolder() {
        String letters = "abcdefghijklmnopqrstuvwxyz";
        String[] folder = new String[6];
        for (int i = 0; i < 6; i++) {
            Random random = new Random();
            int j = random.nextInt(letters.length());
            char ch = letters.charAt(j);
            folder[i] = String.valueOf(ch);
        }
        return folder[0] + folder[1] + "/" + folder[2] + folder[3] + "/" + folder[4] + folder[5] + "/";
    }

    private String generateYearMonthFolder() {
        LocalDateTime now = LocalDateTime.now();
        return now.getYear() + "/" + now.getMonth() + "/" + now.getDayOfMonth() + "/";
    }

    public Map<String, Object> addOrUpdateCow(JSONObject jsonObject) {
        Map<String, Object> map = new HashMap<>();
        String msg;

        Long id = jsonObject.getLong("id");
        String tagEPC = jsonObject.getString("tag");
        String type = jsonObject.getString("type");
        String color = jsonObject.getString("color");
        String birthday = jsonObject.getString("birthday");

        //update
        Cow cow;
        if (id != null && cowRepository.findById(id).isPresent()) {
            cow = cowRepository.findById(id).get();
            msg = "Данные коровы изменены.";

        } else {
            //addNew
            cow = new Cow();
            msg = "Новая корова добавлена.";

        }
        Tag tag = tagRepository.findByEpc(tagEPC);
        if (tag != null && !Objects.equals(cow.getId(), id)) {
            map.put("massage", "Такой тег уже есть");
            return map;
        }
        Tag newTag = new Tag();
        if (tagEPC != null) {
            newTag.setEpc(tagEPC);
            cow.setTag(newTag);
            msg = msg + " Метка: " + tagEPC + " добавлена.";
        }
        cow.setType(type);
        cow.setColor(color);
        cow.setCreateDate(LocalDateTime.now());
        cow.setStatus(CowStatus.LIVE);
        cow.setBirthday(LocalDate.parse(birthday, format).atStartOfDay());
        map.put("massage", msg);
        cowRepository.save(cow);
        updateTagOnDay();
        return map;
    }

    private void updateTagOnDay() {
        List<Cow> cowList = cowRepository.findAllByLive(CowStatus.LIVE);
        String today = LocalDate.now().format(format);
        System.out.println(today);
        TagOnDay tagOnDay;
        if (tagOnDayRepository.findByDate(today).isPresent()) {
            tagOnDay = tagOnDayRepository.findByDate(today).get();
        } else {
            tagOnDay = new TagOnDay();
            tagOnDay.setDate(LocalDate.now().format(format));
        }
        List<Long> cowIdList = new ArrayList<>();
        cowList.forEach(cow -> cowIdList.add(cow.getId()));
        tagOnDay.setIdList(cowIdList.toString());
        tagOnDayRepository.save(tagOnDay);

    }


    public ArrayList<CowMoveDto> findAllCowMove(LocalDateTime startPoint) {
        if (startPoint == null) {
            startPoint = LocalDateTime.now().minusDays(1);
        }
        LocalDate day = LocalDate.from(startPoint.toLocalDate().atStartOfDay());
        ArrayList<CowMoveDto> traffic = new ArrayList<>();
        LocalTime localTime = LocalTime.of(12, 0);
        int countAllList;
        for (int i = 0; i < 60; i++) {
            String pointTime = LocalDateTime.of(day, localTime).format(formatMysql);
            String startTime = day.atStartOfDay().format(formatMysql);
            String endTime = day.plusDays(1).atStartOfDay().format(formatMysql);
            int countMorning = cowMoveRepository.countAllCowMove(startTime, pointTime);
            int countEvening = cowMoveRepository.countAllCowMove(pointTime, endTime);

            TagOnDay tagOnDay;
            if (tagOnDayRepository.findByDate(day.format(format)).isPresent()) {
                tagOnDay = tagOnDayRepository.findByDate(day.format(format)).get();
            } else {
                tagOnDay = tagOnDayRepository.findByLastDate();
            }
            countAllList = sizeList(tagOnDay.getIdList());
            if (countMorning > 0 | countEvening > 0) {
                CowMoveDto cowMoveDto = new CowMoveDto();
                cowMoveDto.setDate(day.format(formatSite));
                cowMoveDto.setAllList(countAllList);
                cowMoveDto.setMorning(countMorning);
                cowMoveDto.setEvening(countEvening);
                cowMoveDto.setMorningOut(countAllList - countMorning);
                cowMoveDto.setEveningOut(countAllList - countEvening);
                traffic.add(cowMoveDto);
            }
            //System.out.println(countMorning + " ++++++" + startTime + "+++ "+ pointTime +" +++" + endTime + "++++ " + countEvening);
            day = day.minusDays(1);
        }
        return traffic;
    }

    private int sizeList(String idList) {
        String string = idList.replaceAll("[\\[|\\]]","");
        List<String> ary = List.of(string.split(","));
        return ary.size();
    }

    public List<Cow> findCowWithParam(String date, String type) {
        LocalTime localTime = LocalTime.of(12, 0);
        LocalDate localDate = LocalDate.parse(date, formatSite);
        TagOnDay tagOnDay;
        if (tagOnDayRepository.findByDate(localDate.format(format)).isPresent()) {
            tagOnDay = tagOnDayRepository.findByDate(localDate.format(format)).get();
        } else {
            return null;
        }
        String pointTime = LocalDateTime.of(localDate, localTime).format(formatMysql);
        String startTime = localDate.atStartOfDay().format(formatMysql);
        String endTime = localDate.plusDays(1).atStartOfDay().format(formatMysql);
        String start;
        String end;
        List<Cow> cowList = new ArrayList<>();
        boolean missing;
        switch (type) {
            case ("morning"):
                start = startTime;
                end = pointTime;
                missing = false;
                break;
            case ("evening"):
                start = pointTime;
                end = endTime;
                missing = false;
                break;
            case ("morningout"):
                start = startTime;
                end = pointTime;
                missing = true;
                break;
            case ("eveningout"):
                start = pointTime;
                end = endTime;
                missing = true;
                break;
            default:
                return null;
        }

        String string = tagOnDay.getIdList().replaceAll("[\\[|\\]]","");
        String[] ary = string.split(",");
        List<Long> outputList = new ArrayList<>();
        Arrays.stream(ary).forEach(a ->{
            outputList.add(Long.parseLong(a.trim()));
        });

        List<CowMove> cowMove = cowMoveRepository.findAllCowMoveByTime(start, end);
        Map<Long, CowMove> cowMoveMap = new HashMap<>();
        cowMove.forEach(cowM -> {
            cowMoveMap.put(cowM.getCow().getId(), cowM);
        });
        if (missing) {
            outputList.forEach(id -> {
                if (!cowMoveMap.containsKey(id)) {
                    Optional<Cow> cowOptional = cowRepository.findById(id);
                    cowOptional.ifPresent(cowList::add);
                }
            });
        } else {
            outputList.forEach(id -> {
                if (cowMoveMap.containsKey(id)) {
                    cowList.add(cowMoveMap.get(id).getCow());
                }
            });
        }
        return cowList;
    }

    public Map<String, String> createInfo(String date, String type) {
        Map<String,String> info = new HashMap<>();
        LocalDate localDate = LocalDate.parse(date, formatSite);
        String formattedDate = localDate.format(DateTimeFormatter.ofPattern("dd MMMM yyyy"));
        info.put("date", formattedDate + " года");
        switch (type) {
            case ("morning"):
                info.put("title", "Список прошедших утром на ");
                break;
            case ("evening"):
                info.put("title", "Список прошедших вечером на ");
                break;
            case ("morningout"):
                info.put("title", "Список не прошедших утром на ");
                break;
            case ("eveningout"):
                info.put("title", "Список не прошедших вечером на ");
                break;
            default:
                return null;
        }
        return info;
    }
}

