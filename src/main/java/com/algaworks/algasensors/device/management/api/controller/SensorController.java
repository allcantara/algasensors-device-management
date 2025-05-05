package com.algaworks.algasensors.device.management.api.controller;

import com.algaworks.algasensors.device.management.api.model.SensorInput;
import com.algaworks.algasensors.device.management.api.model.SensorOutput;
import com.algaworks.algasensors.device.management.domain.model.Sensor;
import com.algaworks.algasensors.device.management.domain.model.SensorId;
import com.algaworks.algasensors.device.management.domain.repository.SensorRepository;
import io.hypersistence.tsid.TSID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import static com.algaworks.algasensors.device.management.common.IdGenerator.generateTsid;
import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

@RestController
@RequestMapping("/api/sensors")
@RequiredArgsConstructor
public class SensorController {

    private final SensorRepository sensorRepository;

    @DeleteMapping("{sensorId}/enable")
    @ResponseStatus(HttpStatus.CREATED)
    public void deactivate(@PathVariable TSID sensorId) {
        Sensor sensor = getSensorByIdOrElseThrow(sensorId);

        sensor.setEnabled(FALSE);

        sensorRepository.saveAndFlush(sensor);
    }

    @PutMapping("{sensorId}/enable")
    @ResponseStatus(HttpStatus.CREATED)
    public void activate(@PathVariable TSID sensorId) {
        Sensor sensor = getSensorByIdOrElseThrow(sensorId);

        sensor.setEnabled(TRUE);

        sensorRepository.saveAndFlush(sensor);
    }

    @PutMapping("{sensorId}")
    public SensorOutput update(@PathVariable TSID sensorId, @RequestBody SensorInput input) {
        Sensor sensor = getSensorByIdOrElseThrow(sensorId);

        sensor.setName(input.getName());
        sensor.setIp(input.getIp());
        sensor.setLocation(input.getLocation());
        sensor.setProtocol(input.getProtocol());
        sensor.setModel(input.getModel());
        sensor.setEnabled(input.getEnabled());

        sensor = sensorRepository.saveAndFlush(sensor);
        return convertToOutput(sensor);
    }

    @DeleteMapping("{sensorId}")
    public void delete(@PathVariable TSID sensorId) {
        Sensor sensor = getSensorByIdOrElseThrow(sensorId);
        sensorRepository.delete(sensor);
    }

    @GetMapping
    public Page<SensorOutput> search(Pageable pageable) {
        Page<Sensor> sensors = sensorRepository.findAll(pageable);
        return sensors.map(this::convertToOutput);
    }

    @GetMapping("{sensorId}")
    public SensorOutput getOne(@PathVariable TSID sensorId) {
        Sensor sensor = getSensorByIdOrElseThrow(sensorId);
        return convertToOutput(sensor);
    }

    private Sensor getSensorByIdOrElseThrow(TSID sensorId) {
        return sensorRepository.findById(new SensorId(sensorId))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SensorOutput create(@RequestBody SensorInput input) {
        Sensor sensor = Sensor.builder()
                .id(new SensorId(generateTsid()))
                .name(input.getName())
                .ip(input.getIp())
                .location(input.getLocation())
                .protocol(input.getProtocol())
                .model(input.getModel())
                .enabled(FALSE)
                .build();

        sensor = sensorRepository.saveAndFlush(sensor);

        return convertToOutput(sensor);
    }

    private SensorOutput convertToOutput(Sensor sensor) {
        return SensorOutput.builder()
                .id(sensor.getId().getValue())
                .name(sensor.getName())
                .ip(sensor.getIp())
                .location(sensor.getLocation())
                .protocol(sensor.getProtocol())
                .model(sensor.getModel())
                .enabled(sensor.getEnabled())
                .build();
    }

}
