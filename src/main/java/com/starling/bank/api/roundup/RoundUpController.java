package com.starling.bank.api.roundup;

import com.starling.bank.api.roundup.pojos.Account;
import com.starling.bank.api.roundup.pojos.TotalSavings;
import com.starling.bank.api.roundup.utils.AccountsConsumer;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.jni.Local;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
public class RoundUpController {

    static final Logger log = LoggerFactory.getLogger(RoundUpController.class);

    static final LocalDate dayOfWeek = LocalDate.of(2019, 10, 15);
    // account password: ud9cCSaGMQ!VM6y


    @PostMapping(value = "/trigger/roundup/{dateWithinWeek}")
    public ResponseEntity<TotalSavings> triggerRoundUp(@PathVariable String dateWithinWeek) {  // YYYY-MM-dd
        RoundUpService service = new RoundUpService();
        LocalDate dayOfWeek = LocalDate.parse(dateWithinWeek);
        if( dayOfWeek.isAfter(LocalDate.now().minusDays(1))) {  // no dates as today and future ones allowed
            return ResponseEntity.badRequest().build();
        }
        service.processRoundUp(dayOfWeek);
        return ResponseEntity.ok().build();
    }
}

