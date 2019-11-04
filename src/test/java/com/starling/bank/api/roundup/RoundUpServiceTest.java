package com.starling.bank.api.roundup;

import org.junit.jupiter.api.Test;
import org.junit.platform.commons.annotation.Testable;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@Testable
public class RoundUpServiceTest {

    @Test
    void startOfWeekDate(){
        LocalDate d = LocalDate.of(2019, 10,15);
        LocalDate m = RoundUpService.startOfWeekDate(d);
        assertThat(m.toString()).isEqualTo(d.plusDays(-1).toString());
    }

    @Test
    void roundUpAmount() {
        assertThat(RoundUpService.roundUpAmount(new Integer(99)).intValue()).isEqualTo(1);
        assertThat(RoundUpService.roundUpAmount(new Integer(1)).intValue()).isEqualTo(99);
        assertThat(RoundUpService.roundUpAmount(new Integer(50)).intValue()).isEqualTo(50);
        assertThat(RoundUpService.roundUpAmount(new Integer(199)).intValue()).isEqualTo(1);
        assertThat(RoundUpService.roundUpAmount(new Integer(299)).intValue()).isEqualTo(1);
        assertThat(RoundUpService.roundUpAmount(new Integer(9)).intValue()).isEqualTo(91);
        assertThat(RoundUpService.roundUpAmount(new Integer(91)).intValue()).isEqualTo(9);
    }

}
