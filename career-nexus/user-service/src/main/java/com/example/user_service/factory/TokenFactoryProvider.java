package com.example.user_service.factory;

import com.example.user_service.model.TokenType;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class TokenFactoryProvider {

    private final Map<TokenType, TokenFactory> factories;

    public TokenFactoryProvider(List<TokenFactory> factoryList) {
        this.factories = factoryList.stream()
                .collect(Collectors.toMap(
                        TokenFactory::getTokenType,
                        factory -> factory
                ));
    }

    public TokenFactory getFactory(TokenType tokenType) {
        TokenFactory factory = factories.get(tokenType);
        if (factory == null) {
            throw new IllegalArgumentException("No factory found for token type: " + tokenType);
        }
        return factory;
    }

    public void registerFactory(TokenFactory factory) {
        factories.put(factory.getTokenType(), factory);
    }
}