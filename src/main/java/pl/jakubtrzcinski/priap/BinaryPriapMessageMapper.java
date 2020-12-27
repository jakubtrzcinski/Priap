package pl.jakubtrzcinski.priap;

import org.springframework.amqp.utils.SerializationUtils;

/**
 * @author Jakub Trzcinski kuba@valueadd.pl
 * @since 27-12-2020
 */
class BinaryPriapMessageMapper implements PriapMessageMapper {
    @Override
    public byte[] map(Object message) {
        return SerializationUtils.serialize(message);
    }

    @Override
    public Object map(byte[] message) {
        return SerializationUtils.deserialize(message);
    }
}
