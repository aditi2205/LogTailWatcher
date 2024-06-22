package com.example.logTailWatcher;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class LogTailWatcherApplication {

	public static void main(String[] args) {
		SpringApplication.run(LogTailWatcherApplication.class, args);
	}

}

/*

CustomLogWatcher -> extend my AbstractWebSocketHandler
store the client sessions
on start, send last 10 lines
and on update send rest of the updates

WebSocketConfig class


Client that will initiate connection with my websocket

Log file
*/