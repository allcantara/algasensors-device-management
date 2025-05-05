package com.algaworks.algasensors.device.management;

import com.algaworks.algasensors.device.management.common.IdGenerator;
import io.hypersistence.tsid.TSID;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;

class TSIDTest {

    @Test
    void shouldGenerateTSID() {
        TSID tsid = IdGenerator.generateTsid();
        assertThat(tsid.getInstant())
                .isCloseTo(Instant.now(), within(1, ChronoUnit.MINUTES));
    }

}
