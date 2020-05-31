package br.com.hexagonal.architecture.journey.rocksdb.mapper;

public abstract class RocksDBMapperFactory {

    public static <T> RocksDBMapper<T> mapperFor(final Class<T> type) {
        return new RocksDBMapper<T>(type);
    }
}
