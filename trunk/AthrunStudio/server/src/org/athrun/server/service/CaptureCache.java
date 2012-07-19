package org.athrun.server.service;

public class CaptureCache {
	
	// 截屏数据容量（高清时是40多K）
	private final byte[] captureBuffer = new byte[500000];

	private long snapTimeConsume = 0;
	
	private long lastCaptureTime = 0;
	
	private CaptureCache() {
		
	}
	
	private static ThreadLocal<CaptureCache> captureCache = new ThreadLocal<CaptureCache>(){
		protected synchronized CaptureCache initialValue() {
			return new CaptureCache();
		}
	};
	
	public static CaptureCache get() {
		return (CaptureCache)(captureCache.get());
	}

	public long getSnapTimeConsume() {
		return snapTimeConsume;
	}

	public void setSnapTimeConsume(long snapTimeConsume) {
		this.snapTimeConsume = snapTimeConsume;
	}

	public long getLastCaptureTime() {
		return lastCaptureTime;
	}

	public void setLastCaptureTime(long lastCaptureTime) {
		this.lastCaptureTime = lastCaptureTime;
	}

	public byte[] getCaptureBuffer() {
		return captureBuffer;
	}
}


