package com.example.logTailWatcher.config;

import com.example.logTailWatcher.service.LogWatcherSocketHandler;
import com.example.logTailWatcher.util.LogWatcherUtil;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(new LogWatcherSocketHandler(new LogWatcherUtil()), "/log").setAllowedOrigins("*");
    }
}
