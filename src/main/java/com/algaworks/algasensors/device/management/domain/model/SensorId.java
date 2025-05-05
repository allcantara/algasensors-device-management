package com.algaworks.algasensors.device.management.domain.model;

import io.hypersistence.tsid.TSID;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

import static java.util.Objects.requireNonNull;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SensorId implements Serializable {

    private TSID value;

    public SensorId(TSID value) {
        requireNonNull(value);
        this.value = value;
    }

    public SensorId(Long value) {
        requireNonNull(value);
        this.value = TSID.from(value);
    }

    public SensorId(String value) {
        requireNonNull(value);
        this.value = TSID.from(value);
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
