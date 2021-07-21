package dev.pilhwankim.typeconverter;

import dev.pilhwankim.typeconverter.converter.IntegerToStringConverter;
import dev.pilhwankim.typeconverter.converter.IpPortToStringConvertor;
import dev.pilhwankim.typeconverter.converter.StringToIntegerConverter;
import dev.pilhwankim.typeconverter.converter.StringToIpPortConvertor;
import dev.pilhwankim.typeconverter.formatter.MyNumberFormatter;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addFormatters(FormatterRegistry registry) {
//        우선순위 때문에 주석처리함
//        registry.addConverter(new StringToIntegerConverter());
//        registry.addConverter(new IntegerToStringConverter());
        registry.addConverter(new IpPortToStringConvertor());
        registry.addConverter(new StringToIpPortConvertor());

        // 포멧터 추가
        registry.addFormatter(new MyNumberFormatter());
    }
}
