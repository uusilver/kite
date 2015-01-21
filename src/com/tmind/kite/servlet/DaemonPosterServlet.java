package com.tmind.kite.servlet;

/*
 * @Author: VL
 * 启动守护进程，监控短信队列，如果有则发出短信，如果没有则等待一段时候后重新扫描
 * 
 */
import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.tmind.kite.model.TxtMsgModel;
import com.tmind.kite.task.TxtMessagePoster;

public class DaemonPosterServlet extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2068159356119942540L;

	protected static final Logger logger = Logger.getLogger(DaemonPosterServlet.class);

	private static final BlockingQueue<TxtMsgModel> queue = new LinkedBlockingQueue<TxtMsgModel>();

	public void init() {
		logger.debug("DaemonPoster start....");
		ExecutorService exec = Executors.newSingleThreadExecutor();
		exec.execute(new TxtMessagePoster(queue));
	}

	public void doGet(HttpServletRequest httpservletrequest,
			HttpServletResponse httpservletresponse) throws ServletException,
			IOException {
	}

	public static void addIntoQueue(TxtMsgModel txtModel) {
		queue.offer(txtModel);
	}
}
