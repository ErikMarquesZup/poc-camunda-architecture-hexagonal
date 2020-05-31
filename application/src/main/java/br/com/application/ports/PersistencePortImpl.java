package br.com.application.ports;

import br.com.hexagonal.architecture.journey.rocksdb.kv.exception.FindFailedException;
import br.com.hexagonal.architecture.journey.rocksdb.kv.exception.SaveFailedException;
import br.com.hexagonal.architecture.journey.rocksdb.mapper.exception.DeserializationException;
import br.com.hexagonal.architecture.journey.rocksdb.mapper.exception.SerDeException;
import br.com.hexagonal.architecture.journey.rocksdb.repository.RocksDBKeyValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collection;
import java.util.Optional;

@Component
public class PersistencePortImpl implements PersistencePort {

    @Autowired
    private RocksDBKeyValue rocksDBKeyValue;

    public PersistencePortImpl(RocksDBKeyValue rocksDBKeyValue) {
        this.rocksDBKeyValue = rocksDBKeyValue;
    }


    @Override
    public void save(String key, String value) throws IOException, SaveFailedException {
        rocksDBKeyValue.save(key, value);
    }

    @Override
    public Collection<String> findAll() throws DeserializationException {
        return rocksDBKeyValue.findAll();
    }

    @Override
    public Optional<String> findByKey(String key) throws SerDeException, FindFailedException {
        return rocksDBKeyValue.findByKey(key);
    }

    @Override
    public void setCompleteTask(String taskId, String processInstanceId) throws IOException, SaveFailedException {
        rocksDBKeyValue.setCompleteTask(taskId, processInstanceId);
    }

    @Override
    public Optional<String> getProcessInstanceInfo(String processInstanceId, String taskId) throws IOException {
        return rocksDBKeyValue.getProcessInstanceInfo(processInstanceId, taskId);
    }
}
