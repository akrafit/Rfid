package Livestock.server.Rfid.repository;

import Livestock.server.Rfid.model.CowMove;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

public interface CowMoveRepository extends JpaRepository<CowMove, Long> {
    @Query("select count(c) from CowMove c where c.dateTime between :start  and :end")
    int countAllCowMove(@Param("start") String start, @Param("end")String end);

    @Query("select c from CowMove c where c.dateTime between :start  and :end")
    List<CowMove> findAllCowMoveByTime(@Param("start")String start,@Param("end") String end);
}