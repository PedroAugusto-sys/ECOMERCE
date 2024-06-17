package com.gyncomerce.ecom.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class ByteArrayConverter implements AttributeConverter<byte[], byte[]> {

    @Override
    public byte[] convertToDatabaseColumn(byte[] attribute) {
        return attribute;
    }

    @Override
    public byte[] convertToEntityAttribute(byte[] dbData) {
        return dbData;
    }
}
