package br.com.hexagonal.architecture.journey.rocksdb;

import br.com.hexagonal.architecture.journey.rocksdb.configuration.RocksDBConfiguration;
import br.com.hexagonal.architecture.journey.rocksdb.kv.exception.FindFailedException;
import br.com.hexagonal.architecture.journey.rocksdb.kv.exception.SaveFailedException;
import br.com.hexagonal.architecture.journey.rocksdb.mapper.exception.DeserializationException;
import br.com.hexagonal.architecture.journey.rocksdb.mapper.exception.SerDeException;
import br.com.hexagonal.architecture.journey.rocksdb.repository.RocksDBKeyValue;
import br.com.hexagonal.architecture.journey.rocksdb.repository.RocksDBKeyValueRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import message.model.DomainEvent;
import message.model.DomainEventList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collection;
import java.util.Optional;

import static br.com.hexagonal.architecture.journey.rocksdb.constant.TypeComponent.END_EVENT;
import static br.com.hexagonal.architecture.journey.rocksdb.constant.TypeComponent.USER_TASK;

@Component
public class RocksDBKeyValueService extends RocksDBKeyValueRepository<String, String> implements RocksDBKeyValue  {

    private static final Logger LOGGER = LoggerFactory.getLogger(RocksDBKeyValueService.class);

    private ObjectMapper objectMapper;

    public RocksDBKeyValueService(ObjectMapper objectMapper) {
       super(new RocksDBConfiguration("/src/main/resources/data/repositories", "db"));
       this.objectMapper = objectMapper;
    }

    @Override
    public void save(String key, String value) throws IOException, SaveFailedException {
        Optional<String> valuesCurrent = null;
        try {
            valuesCurrent = this.findByKey(key);
        } catch (SerDeException | FindFailedException e) {
            e.printStackTrace();
        }

        DomainEventList tasksCurrents = null;
        if (valuesCurrent.isPresent()) {
            tasksCurrents = this.objectMapper.readValue(valuesCurrent.get(), DomainEventList.class);
            DomainEventList taskNow = this.objectMapper.readValue(value, DomainEventList.class);
            tasksCurrents.getDomainEvents().addAll(taskNow.getDomainEvents());
            value = this.objectMapper.writeValueAsString(tasksCurrents);
        }

        super.save(key, value);
    }

    @Override
    public Collection<String> findAll() throws DeserializationException {
        return super.findAll();
    }

    @Override
    public Optional<String> findByKey(String key) throws SerDeException, FindFailedException {
        return super.findByKey(key);
    }

    public void setCompleteTask(String taskId, String processInstanceId) throws IOException, SaveFailedException {
        Optional<String> valuesCurrent = null;
        try {
            valuesCurrent = this.findByKey(processInstanceId);
        } catch (SerDeException | FindFailedException e) {
            e.printStackTrace();
        }

        DomainEventList tasksCurrents = null;
        if (valuesCurrent.isPresent()) {
            tasksCurrents = this.objectMapper.readValue(valuesCurrent.get(), DomainEventList.class);
            tasksCurrents.getDomainEvents().stream().forEach(item -> completed(taskId, item));
            valuesCurrent = Optional.ofNullable(this.objectMapper.writeValueAsString(tasksCurrents));
        }

        super.save(processInstanceId, valuesCurrent.get());
    }

    private void completed(String taskId, DomainEvent item) {
        if (taskId.equals(item.getTaskId())) {
            item.setTaskComplete(Boolean.TRUE);
        }
    }

    public Optional<String> getProcessInstanceInfo(String processInstanceId, String taskId) throws IOException {
        try {
            Optional<String> values = super.findByKey(processInstanceId);
            if (values.isPresent()) {
                DomainEventList kafkaExternalTasks = this.objectMapper.readValue(values.get(), DomainEventList.class);
                Optional<DomainEvent> userTask = kafkaExternalTasks.getDomainEvents().stream().filter(item -> isUserTask(item, taskId)).findAny();
                if (userTask.isPresent()) {
                    return Optional.ofNullable(this.objectMapper.writeValueAsString(userTask.get()));
                }
                Optional<DomainEvent> endEvent = kafkaExternalTasks.getDomainEvents().stream().filter(this::isEndEvent).findAny();
                if (endEvent.isPresent()) {
                    return Optional.ofNullable(this.objectMapper.writeValueAsString(endEvent.get()));
                }
            }
        } catch (SerDeException | FindFailedException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public boolean isEndEvent(String processInstanceId) throws FindFailedException, IOException {
        Optional<String> values = super.findByKey(processInstanceId);
        if (values.isPresent()) {
            DomainEventList kafkaExternalTasks = this.objectMapper.readValue(values.get(), DomainEventList.class);
            Optional<DomainEvent> endEvent = kafkaExternalTasks.getDomainEvents().stream().filter(this::isEndEvent).findAny();
            if (endEvent.isPresent()) {
                return true;
            }
        }
        return false;
    }

    private boolean isEndEvent(DomainEvent item) {
        return END_EVENT.getEvent().equals(item.getType());
    }

    private boolean isUserTask(DomainEvent item, String taskId) {
        return isNotEqualsTaskId(item, taskId) && USER_TASK.getEvent().equals(item.getType()) && !item.isTaskComplete();
    }

    private boolean isNotEqualsTaskId(DomainEvent item, String taskId) {
        return taskId == null || !taskId.equals(item.getTaskId());
    }
}
