package com.easted.sy.user.archieves.uaa.config;

import com.easted.sy.user.archieves.uaa.repository.ClientDetailsRepository;
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
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.ClientRegistrationException;
import org.springframework.security.oauth2.provider.approval.ApprovalStore;
import org.springframework.security.oauth2.provider.approval.TokenApprovalStore;
import org.springframework.security.oauth2.provider.client.BaseClientDetails;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;

import java.security.KeyPair;
import java.util.Arrays;
import java.util.Optional;

@EnableAuthorizationServer
@Configuration
public class AuthorizationServerConfiguration extends AuthorizationServerConfigurerAdapter
{
	private static final int ACCESS_TOKEN_VALIDITY_SECONDS = 60;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private ClientDetailsRepository clientDetailsRepository;


	@Bean
	public JwtAccessTokenConverter accessTokenConverter()
	{
		//// $ keytool -genkey -keyalg RSA -alias app -keystore keystore.jks -storepass keystorepass

		JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
		KeyPair keyPair = new KeyStoreKeyFactory(new ClassPathResource("keystore.jks"), "keystorepass".toCharArray()).getKeyPair("app");
		converter.setKeyPair(keyPair);

		return converter;
	}

    @Bean
    public TokenStore tokenStore() {
        return new JwtTokenStore(accessTokenConverter());
    }


    private DefaultTokenServices tokenServices(AuthorizationServerEndpointsConfigurer endpoints)
	{
		DefaultTokenServices tokenServices = new DefaultTokenServices();
        tokenServices.setTokenStore(endpoints.getTokenStore());
        tokenServices.setTokenEnhancer(accessTokenConverter());
        tokenServices.setSupportRefreshToken(true);
        tokenServices.setReuseRefreshToken(false);
 //       tokenServices.setAccessTokenValiditySeconds(ACCESS_TOKEN_VALIDITY_SECONDS);

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
//			.userDetailsService(userDetailsService)
			.tokenServices(tokenServices(endpoints))
			.accessTokenConverter(accessTokenConverter())
            .reuseRefreshTokens(false);
	}

	@Bean
    public ClientDetailsService getClientDetails(){

	    return new ClientDetailsService() {
            @Override
            public ClientDetails loadClientByClientId(String clientId) throws ClientRegistrationException {
                Optional<com.easted.sy.user.archieves.uaa.domain.ClientDetails> clientDetailsOptional=clientDetailsRepository.findAllByAppId(clientId);
                com.easted.sy.user.archieves.uaa.domain.ClientDetails clientDetails=clientDetailsOptional.get();
                BaseClientDetails baseClientDetails= new BaseClientDetails(clientDetails.getAppId(),clientDetails.getResourceIds(),clientDetails.getScope(),clientDetails.getGrantTypes(),null,clientDetails.getRedirectUrl());
                baseClientDetails.setClientSecret(clientDetails.getAppSecret());
                String autoApproveScopes=clientDetails.getAutoApproveScopes();
                if (autoApproveScopes!=null){
                    baseClientDetails.setAutoApproveScopes(Arrays.asList(autoApproveScopes.split(",")));

                }
                baseClientDetails.setRefreshTokenValiditySeconds(clientDetails.getRefreshTokenValidity());
                baseClientDetails.setAccessTokenValiditySeconds(clientDetails.getAccessTokenValidity());
                return baseClientDetails;
            }
        };
    };

	@Bean
	public ApprovalStore approvalStore(){
	    TokenApprovalStore tokenApprovalStore=new TokenApprovalStore();
	    tokenApprovalStore.setTokenStore(tokenStore());
	  return   tokenApprovalStore;
    }

	@Override
	public void configure(ClientDetailsServiceConfigurer clients) throws Exception
	{
//		clients.inMemory()
//            .withClient("demo")
//            .secret("demo")
//            .scopes("read","write")
//            .redirectUris("http://localhost:8082/client/myInfor")
//            .authorizedGrantTypes("authorization_code","refresh_token","implicit","password","client_credentials")
//            .autoApprove(true);

        clients.withClientDetails(getClientDetails());
	}
}
