package Livestock.server.Rfid.repository;

import Livestock.server.Rfid.model.Cow;
import Livestock.server.Rfid.model.CowStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface CowRepository extends JpaRepository<Cow, Long> {

    @Query("select c from Cow c where c.status = :live")
    List<Cow> findAllByLive(CowStatus live);

}