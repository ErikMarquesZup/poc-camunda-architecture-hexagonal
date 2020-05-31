package br.com.hexagonal.architecture.journey.rocksdb.repository;

import br.com.hexagonal.architecture.journey.rocksdb.kv.exception.FindFailedException;
import br.com.hexagonal.architecture.journey.rocksdb.kv.exception.SaveFailedException;
import br.com.hexagonal.architecture.journey.rocksdb.mapper.exception.DeserializationException;
import br.com.hexagonal.architecture.journey.rocksdb.mapper.exception.SerDeException;

import java.io.IOException;
import java.util.Collection;
import java.util.Optional;

public interface RocksDBKeyValue {

    void save(String key, String value) throws IOException, SaveFailedException;

    Collection<String> findAll() throws DeserializationException;

    Optional<String> findByKey(String key) throws SerDeException, FindFailedException;

    void setCompleteTask(String taskId, String processInstanceId) throws IOException, SaveFailedException;

    Optional<String> getProcessInstanceInfo(String processInstanceId, String taskId) throws IOException;
}
