package dev.leonkim.proxy;

import dev.leonkim.proxy.config.AppV1Config;
import dev.leonkim.proxy.config.AppV2Config;
import dev.leonkim.proxy.config.v1_proxy.ConcreteProxyConfig;
import dev.leonkim.proxy.config.v1_proxy.InterfaceProxyConfig;
import dev.leonkim.proxy.config.v2_dynamicproxy.DynamicProxyBasicConfig;
import dev.leonkim.proxy.config.v2_dynamicproxy.DynamicProxyFilterConfig;
import dev.leonkim.proxy.config.v3_proxyfactory.ProxyFactoryConfigV1;
import dev.leonkim.proxy.config.v3_proxyfactory.ProxyFactoryConfigV2;
import dev.leonkim.proxy.trace.logtrace.LogTrace;
import dev.leonkim.proxy.trace.logtrace.ThreadLocalLogTrace;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

@Import(ProxyFactoryConfigV2.class)
//@Import(ProxyFactoryConfigV1.class)
//@Import(DynamicProxyFilterConfig.class)
//@Import(DynamicProxyBasicConfig.class)
//@Import(ConcreteProxyConfig.class)
//@Import(InterfaceProxyConfig.class)
//@Import({AppV1Config.class, AppV2Config.class})
@SpringBootApplication(scanBasePackages = "dev.leonkim.proxy.app")
public class SpringCoreAdvanced2Application {

	public static void main(String[] args) {
		SpringApplication.run(SpringCoreAdvanced2Application.class, args);
	}

	@Bean
	public LogTrace logTrace() {
		return new ThreadLocalLogTrace();
	}

}
