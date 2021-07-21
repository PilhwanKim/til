package dev.pilhwankim.typeconverter.formatter;

import dev.pilhwankim.typeconverter.converter.IpPortToStringConvertor;
import dev.pilhwankim.typeconverter.converter.StringToIpPortConvertor;
import dev.pilhwankim.typeconverter.type.IpPort;
import org.junit.jupiter.api.Test;
import org.springframework.format.support.DefaultFormattingConversionService;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class FormattingConversionServiceTest {
    @Test
    void formattingConversionService() {
        DefaultFormattingConversionService conversionService = new DefaultFormattingConversionService();
        //컨버터 등록
        conversionService.addConverter(new StringToIpPortConvertor());
        conversionService.addConverter(new IpPortToStringConvertor());
        //포멧터 등록
        conversionService.addFormatter(new MyNumberFormatter());

        //컨버터 사용
        IpPort result = conversionService.convert("127.0.0.1:8080", IpPort.class);
        assertThat(result).isEqualTo(new IpPort("127.0.0.1", 8080));
        //포멧터 사용
        assertThat(conversionService.convert(1000, String.class)).isEqualTo("1,000");
        assertThat(conversionService.convert("1000", Long.class)).isEqualTo(1000);
    }
}
