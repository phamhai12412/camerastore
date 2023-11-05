package ra.security.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import ra.security.security.jwt.CustomAccessDeniedHandler;
import ra.security.security.jwt.JwtEntryPoint;
import ra.security.security.jwt.JwtTokenFilter;
import ra.security.security.user_principle.UserDetailService;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true) // phân quyền trực tiếp trên controller
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private CustomAccessDeniedHandler customAccessDeniedHandler;
    @Autowired
    private UserDetailService userDetailService;
    @Autowired
    private JwtEntryPoint jwtEntryPoint;
    @Bean
    public JwtTokenFilter jwtTokenFilter(){
        return new JwtTokenFilter();
    }

    @Bean
    public static PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    @Bean(BeanIds.AUTHENTICATION_MANAGER)
    @Override
    protected AuthenticationManager authenticationManager()  {
        try {
            return super.authenticationManager();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth)  {
        try {
            auth.userDetailsService(userDetailService).passwordEncoder(passwordEncoder());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void configure(HttpSecurity http)  {
        // cáu hình phân qyền đường dẫn
        try {
            http.cors().and().csrf().disable() // tắt cáu hình csrf
                    .authorizeRequests()
                    .antMatchers("/api/auth/**").permitAll()
                                    .antMatchers("/api/admin/use/**").hasAnyAuthority("ROLE_ADMIN","ROLE_USER")// cau hinh theo pattern
                                    .antMatchers("/admin/orders**").hasAuthority("ROLE_ADMIN") // cau hinh theo pattern
                                    .antMatchers("/api/admin/category/**").hasAuthority("ROLE_ADMIN") // cau hinh theo pattern
                                    .antMatchers("/api/admin/manufacturers/**").hasAuthority("ROLE_ADMIN") // cau hinh theo pattern
                                    .antMatchers("/api/admin/products/**").hasAuthority("ROLE_ADMIN") // cau hinh theo pattern
                                    .antMatchers("/api/admin/promotions/**").hasAuthority("ROLE_ADMIN") // cau hinh theo pattern
                                    .antMatchers("/api/use/favorites/**").hasAuthority("ROLE_USER") // cau hinh theo pattern
                                    .antMatchers("/api/user/orders/**").hasAuthority("ROLE_USER") // cau hinh theo pattern
                                    .antMatchers("/api/user/reviews/**").hasAuthority("ROLE_USER") // cau hinh theo pattern
                    .anyRequest().authenticated() // các đường dân khác phả được xác thực
                    .and()
                    .exceptionHandling().accessDeniedHandler(customAccessDeniedHandler).authenticationEntryPoint(jwtEntryPoint)
                    .and()
                    .sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS); // yếu cầu người dùng luôn xác thức bằng token
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        http.addFilterBefore(jwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
    }

}