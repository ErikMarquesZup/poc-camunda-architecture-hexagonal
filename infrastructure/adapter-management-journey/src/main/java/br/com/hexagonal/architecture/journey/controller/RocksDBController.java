package br.com.hexagonal.architecture.journey.controller;

import br.com.application.ports.PersistencePort;
import br.com.hexagonal.architecture.journey.rocksdb.kv.exception.FindFailedException;
import br.com.hexagonal.architecture.journey.rocksdb.mapper.exception.SerDeException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.ws.rs.Produces;
import java.util.Collection;
import java.util.Optional;

@RestController
@RequestMapping("instance/rocksDB")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
public class RocksDBController {

    @Autowired
    private final PersistencePort persistencePort;

    @GetMapping("/get/{processInstanceId}")
    @Produces("application/json")
    public ResponseEntity<String> get(@PathVariable String processInstanceId) throws FindFailedException, SerDeException {
        Optional<String> byKey = persistencePort.findByKey(processInstanceId);
        return ResponseEntity.ok(byKey.get());
    }

    @GetMapping("/get")
    @Produces("application/json")
    public ResponseEntity<String> get() throws SerDeException {
        Collection<String> values = persistencePort.findAll();
        return ResponseEntity.ok(values.toString());
    }
}

