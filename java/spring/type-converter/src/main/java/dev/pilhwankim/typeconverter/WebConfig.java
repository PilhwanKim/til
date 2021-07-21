package dev.pilhwankim.typeconverter;

import dev.pilhwankim.typeconverter.converter.IntegerToStringConverter;
import dev.pilhwankim.typeconverter.converter.IpPortToStringConvertor;
import dev.pilhwankim.typeconverter.converter.StringToIntegerConverter;
import dev.pilhwankim.typeconverter.converter.StringToIpPortConvertor;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new StringToIntegerConverter());
        registry.addConverter(new IntegerToStringConverter());
        registry.addConverter(new IpPortToStringConvertor());
        registry.addConverter(new StringToIpPortConvertor());
    }
}
