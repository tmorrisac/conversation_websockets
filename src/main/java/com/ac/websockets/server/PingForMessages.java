package com.ac.websockets.server;

import com.ac.websockets.model.Conversation;
import com.ac.websockets.model.Message;
import com.google.gson.Gson;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Tuple;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import javax.ejb.*;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

@Singleton
@Startup
public class PingForMessages {
    @Resource
    private TimerService timerService;
    private Timer timer;
    private Jedis jedis;
    private Gson gson = new Gson();
    private static final String CONVERSATION_REDIS_KEY = "conversations";

    @PostConstruct
    public void init() {
        /**
         * fires 5 secs after creation interval = 5 secs non-persistent
         * no-additional (custom) info
         */
        timer = timerService.createIntervalTimer(5000, 5000, new TimerConfig(null, false)); //trigger every 5 seconds
        Logger.getLogger(PingForMessages.class.getName()).log(Level.INFO, "Timer initiated");
        jedis = new Jedis("192.168.99.100", 6379, 10000);

    }

    @Inject
    @MessageDataQualifer
    private Event<String> msgEvent;

    @Timeout
    public void timeout(Timer timer) {
        Logger.getLogger(PingForMessages.class.getName()).log(Level.INFO, "Timer fired at {0}", new Date());
        Set<Tuple> zrangeByScoreWithScores = jedis.zrevrangeByScoreWithScores(CONVERSATION_REDIS_KEY, Integer.MAX_VALUE, 1);

        List<Message> messages = new ArrayList<>();

        for (Tuple tuple : zrangeByScoreWithScores) {
            messages.add(Message.builder()
                    .content(tuple.getElement())
                    .build());
        }

        Conversation conversation = Conversation.builder()
                .messages(messages)
                .build();

        msgEvent.fire(gson.toJson(conversation));
    }

    @PreDestroy
    public void close() {
        timer.cancel();
        jedis.close();
        Logger.getLogger(PingForMessages.class.getName()).log(Level.INFO, "Application shutting down. Timer purged, Jedis client closed");
    }
}
