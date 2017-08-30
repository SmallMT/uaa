package com.easted.sy.user.archieves.uaa.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;

import java.security.KeyPair;

@EnableAuthorizationServer
@Configuration
public class AuthorizationServerConfiguration extends AuthorizationServerConfigurerAdapter
{
	private static final int ACCESS_TOKEN_VALIDITY_SECONDS = 60;

	@Autowired
	private AuthenticationManager authenticationManager;


	@Bean
	public JwtAccessTokenConverter accessTokenConverter()
	{
		//// $ keytool -genkey -keyalg RSA -alias app -keystore keystore.jks -storepass keystorepass

		JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
		KeyPair keyPair = new KeyStoreKeyFactory(new ClassPathResource("keystore.jks"), "keystorepass".toCharArray()).getKeyPair("app");
		converter.setKeyPair(keyPair);

		return converter;
	}

	private DefaultTokenServices tokenServices(AuthorizationServerEndpointsConfigurer endpoints)
	{
		DefaultTokenServices tokenServices = new DefaultTokenServices();
        tokenServices.setTokenStore(endpoints.getTokenStore());
        tokenServices.setTokenEnhancer(accessTokenConverter());
        tokenServices.setSupportRefreshToken(true);
        tokenServices.setAccessTokenValiditySeconds(ACCESS_TOKEN_VALIDITY_SECONDS);

        return tokenServices;
	}

	@Override
	public void configure(AuthorizationServerSecurityConfigurer security) throws Exception
	{
		security
			.tokenKeyAccess("permitAll()")
			.checkTokenAccess("isAuthenticated()");
	}

	@Override
	public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception
	{
		endpoints
			.authenticationManager(authenticationManager)
		//	.userDetailsService(userDetailsService)
			.tokenServices(tokenServices(endpoints));
//			.accessTokenConverter(accessTokenConverter());
	}

	@Override
	public void configure(ClientDetailsServiceConfigurer clients) throws Exception
	{
		clients.inMemory().withClient("demo")
            .secret("demo")
            .scopes("read","write")
            .redirectUris("http://localhost:8082/client/myInfor")
            .authorizedGrantTypes("authorization_code","refresh_token","implicit","password","client_credentials")
            .autoApprove(true);
	}
}
