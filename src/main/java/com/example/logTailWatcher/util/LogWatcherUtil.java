package com.example.logTailWatcher.util;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class LogWatcherUtil {

    public static final String FILE_PATH = "src/main/resources/logFile.log";
    public static final int NO_OF_LINES = 10;

    public void readLastTenLines(WebSocketSession session) throws IOException {

        RandomAccessFile randomAccessFile = new RandomAccessFile(FILE_PATH, "r");
        long length = randomAccessFile.length();
        int noOfLines = 0;
        StringBuilder lastFewLines = new StringBuilder();
        long prevPosition = length;
        while(true){
            long pos = Math.max(0,prevPosition-20);
            randomAccessFile.seek(pos);
            lastFewLines = new StringBuilder();

            String line = null;
            int lineCount = 0;

            while((line = randomAccessFile.readLine()) != null && lineCount<=10){
                lastFewLines.append(line);
                lastFewLines.append("\n");
                lineCount++;
            }

            if(lineCount >= 10 || pos == 0){
                break;
            }
            prevPosition = pos;
        }

        session.sendMessage(new TextMessage(lastFewLines.toString()));

    }

    public void sendLatestUpdates(WebSocketSession session) throws IOException {
        Thread thread = new Thread(() -> {
            try{
                RandomAccessFile randomAccessFile = new RandomAccessFile(FILE_PATH, "r");
                //one word = 10 bytes, one sentence = 100 bytes, 10 sentences = 1024 bytes = 1kb
                long length = randomAccessFile.length();
                randomAccessFile.seek(length);

                //from the last line onwards, keep on reading the lines one by one with a few seconds delay and send it all the sessions via sendMessage
                while(true){
                    String line = null;
                    while((line = randomAccessFile.readLine()) != null){
                        session.sendMessage(new TextMessage(line));
                    }
                    Thread.sleep(2000);
                }
            }
            catch(Exception ex){
                ex.printStackTrace();
            }
        });
        //set the thread as daemon so that it runs in background and doesn't consume many CPU resources
        thread.setDaemon(true);
        thread.start();

    }

}
