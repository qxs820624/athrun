package pl.polidea.instrumentation;

public interface TestCmdReceiver {
	void receiveCmd(String cmd);
	void receiveOver();
	void snapShotOver();
}
