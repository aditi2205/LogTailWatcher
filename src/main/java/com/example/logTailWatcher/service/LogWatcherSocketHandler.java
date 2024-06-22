package com.example.logTailWatcher.service;

import com.example.logTailWatcher.util.LogWatcherUtil;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;


import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
public class LogWatcherSocketHandler extends TextWebSocketHandler {

    LogWatcherUtil logWatcherUtil;
    List<WebSocketSession> sessions ;

    public LogWatcherSocketHandler(LogWatcherUtil logWatcherUtil){
        this.logWatcherUtil = logWatcherUtil;
        this.sessions = new CopyOnWriteArrayList<>();
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        //once the session is started, return the last 10 lines of the log file
        //after that keep on sending the frequent updates
        sessions.add(session);
        try{
            logWatcherUtil.readLastTenLines(session);
            logWatcherUtil.sendLatestUpdates(session);
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }

}
