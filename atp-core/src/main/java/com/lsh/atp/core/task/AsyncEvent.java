package com.lsh.atp.core.task;

import com.google.common.eventbus.AsyncEventBus;
import com.google.common.eventbus.EventBus;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.concurrent.Executors;

/**
 * Project Name: lsh-atp
 * Created by zhangqiang
 * Date: 16/12/16
 * 北京链商电子商务有限公司
 * Package com.lsh.atp.core.task
 * desc: 异步任务静态类
 */
@Component
@Lazy(value = false)
public class AsyncEvent{

    private static AsyncEventBus eventBus;

    @Value("${async.thread.num:10}")
    private int threadNum;

    @Autowired
    private AsyncEventListener obj;

    public AsyncEvent(){
    }

    @PostConstruct
    public void init(){
        eventBus = new AsyncEventBus(Executors.newFixedThreadPool(this.threadNum));
        eventBus.register(this.obj);
    }

    public static void post(Object obj){
        AsyncEvent.eventBus.post(obj);
    }

}
