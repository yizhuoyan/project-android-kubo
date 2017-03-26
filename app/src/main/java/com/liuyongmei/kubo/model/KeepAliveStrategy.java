package com.liuyongmei.kubo.model;

/**
 * 心跳
 * 
 * @author Administrator
 *
 */
public class KeepAliveStrategy {
	private Thread thread = new Thread() {
		@Override
		public void run() {
			try {
				while (!this.isInterrupted()) {
					Thread.sleep(3000);
					KeepAliveStrategy.this.messageSender.sendCommand(command);
				}
			} catch (Exception e) {
			}
		}
	};
	private DataSender messageSender;
	private byte[] command;

	public KeepAliveStrategy() {
		this.command = CommandBuilder.keepAlive();
	}

	public void start(DataSender s) {
		this.messageSender = s;
		this.thread.start();
	}
	public void stop(){
		this.thread.interrupt();
		this.thread=null;
	}

}
