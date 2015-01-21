package com.tmind.kite.task;

import java.util.concurrent.BlockingQueue;

import org.apache.log4j.Logger;

import com.tmind.kite.model.TxtMsgModel;
import com.tmind.kite.utils.TxtHandler;


public class TxtMessagePoster implements Runnable {

	protected static final Logger logger = Logger.getLogger(TxtMessagePoster.class);
	
	private final BlockingQueue<TxtMsgModel> queue;
	
	public TxtMessagePoster(BlockingQueue<TxtMsgModel> queue){
		this.queue = queue;
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		while (!this.isInterrupted()) {// 线程未中断执行循环  
			TxtMsgModel txtModel = queue.poll();
           if(txtModel!=null){
        	   //logger.debug("发送短信内容为:"+model.getContent());
        	   TxtHandler.sendTxt(txtModel.getTelno(), txtModel.getContent());
           }else{
        	   //等待90秒
        	   logger.debug("队列为空,进入等待!");
        	   try {
   				Thread.sleep(90*1000);
	   			} catch (InterruptedException e) {
	   				// TODO Auto-generated catch block
	   				e.printStackTrace();
	   			}
           }
           
        }  
	}
	
	private boolean isInterrupted(){
		return false;
	}
}
