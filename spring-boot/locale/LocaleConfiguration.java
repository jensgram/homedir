package locale;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Locale;

/**
 * Configures locale support via pre-configured locales.
 *
 * This will make {@link LocaleContextHolder} aware of the current (request-wide) and default
 * (application-wide) locales via {@code LocaleContextHolder.getLocale()} and
 * {@code LocaleContextHolder.getLocale(null)}, respectively.
 */
@Configuration
public class LocaleConfiguration extends WebMvcConfigurationSupport {
  private static final Locale DEFAULT_LOCALE = Locale.ENGLISH;
  private static final List<Locale> SUPPORTED_LOCALES = List.of(
    DEFAULT_LOCALE
    // More locales here.
  );

  @Bean
  public LocaleResolver localeResolver() {
    LocaleContextHolder.setDefaultLocale(DEFAULT_LOCALE);

    AcceptHeaderLocaleResolver lr = new AcceptHeaderLocaleResolver();
    lr.setSupportedLocales(SUPPORTED_LOCALES);
    lr.setDefaultLocale(DEFAULT_LOCALE);

    return lr;
  }

  @Override
  protected void addInterceptors(InterceptorRegistry registry) {
    registry.addInterceptor(new ContentLanguageHeaderHandlerInterceptor());
  }

  /**
   * Simple interceptor setting the effective locale of the response in the {@code Content-Language} header.
   */
  private static class ContentLanguageHeaderHandlerInterceptor extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
      response.addHeader(HttpHeaders.CONTENT_LANGUAGE, LocaleContextHolder.getLocale().toLanguageTag());

      return super.preHandle(request, response, handler);
    }
  }
}
