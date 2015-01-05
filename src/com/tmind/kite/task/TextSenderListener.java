package com.tmind.kite.task;

import java.util.Timer;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class TextSenderListener implements ServletContextListener {
  
  private Timer timer = null;

  public void contextInitialized(ServletContextEvent event) {
    timer = new Timer(true);
    //设置任务计划，启动和间隔时间, 60*1000 = 1分钟
    timer.schedule(new TextSenderTask(), 0, 60*1000);
  }

  public void contextDestroyed(ServletContextEvent event) {
    timer.cancel();
  }
  
}