package dev.pilhwankim.typeconverter.converter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;

@Slf4j
public class IntegerToStringConverter implements Converter<Integer, String> {

    @Override
    public String convert(Integer source) {
        log.info("convert source={}", source);
        //"127.0.0.1:8080" -> IpPort 객체
        return String.valueOf(source);
    }
}
