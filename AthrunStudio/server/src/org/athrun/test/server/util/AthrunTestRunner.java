package org.athrun.test.server.util;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import org.athrun.ddmlib.AdbCommandRejectedException;
import org.athrun.ddmlib.AndroidDebugBridge;
import org.athrun.ddmlib.IDevice;
import org.athrun.ddmlib.RawImage;
import org.athrun.ddmlib.ShellCommandUnresponsiveException;
import org.athrun.ddmlib.TimeoutException;
import org.athrun.server.service.CaptureManager;

public class AthrunTestRunner {

	static String monkeyServer = "127.0.0.1";
	static int monkeyPort = 1234;
	static Socket monkeySocket = null;

	static IDevice monkeyDevice;

	static BufferedReader monkeyReader;
	static BufferedWriter monkeyWriter;
	static String monkeyResponse;

	// Obtain a suitable logger.
	private static Logger logger = Logger.getLogger("com.android.monkeyrunner");

	// delay between key events
	final static int KEY_INPUT_DELAY = 1000;
	
	public static void runTestScript(String script) {
		try {
			sendMonkeyEvent(script);
			sendMonkeyEvent("quit");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void start(IDevice device) {
		logger.info("initAdb");
		
	    try {
			initAdbConnection(device);
		} catch (TimeoutException e) {
			e.printStackTrace();
		} catch (AdbCommandRejectedException e) {
			e.printStackTrace();
		} catch (ShellCommandUnresponsiveException e) {
			e.printStackTrace();
		}
	    
	    logger.info("openMonkeyConnection");
	    openMonkeyConnection();
	}
	
	public static void stop() {
	    logger.info("closeMonkeyConnection");
	    closeMonkeyConnection(); 
	}
	
	public static void listenTcRunning(String snapshotDir, String serialNumber) {
		
		try {
			while(true) {
				String line = monkeyReader.readLine();
				System.out.println("receive line: " + line);
				if (line != null) {
					if (line.equals("run::over-req")) {
						monkeyWriter.write("run::over-ack" + "\n");
						monkeyWriter.flush();
						break;
					}
					
					OutputStream os = null;
					try {
						// 指示服务端截屏
						if (line.equals("run::snapshot-req")) {
							String fileName = new Date().getTime() + ".jpg";
							os = new FileOutputStream(new File(snapshotDir + File.separator + fileName));
							CaptureManager.getInstance().register(serialNumber, os);
							
							monkeyWriter.write("run::snapshot-ack" + "\n");
							monkeyWriter.flush();
						}
					} finally {
						if (os != null) {
							os.close();
						}
					}
				} else {
					try {
						Thread.sleep(2000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					
					System.out.println("line is null!!");
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}

	/***
	 * Initialize an adb session with a device connected to the host
	 * 
	 * @throws AdbCommandRejectedException
	 * @throws TimeoutException
	 * @throws ShellCommandUnresponsiveException
	 * 
	 */
	public static void initAdbConnection(IDevice device) throws TimeoutException,
			AdbCommandRejectedException, ShellCommandUnresponsiveException {
		//String adbLocation = "adb";
		//boolean device = false;
		//boolean emulator = false;
		//String serial = null;

		//AndroidDebugBridge.init(false /** debugger support */
		//);

		try {
			/*
			AndroidDebugBridge bridge = AndroidDebugBridge.createBridge(
					adbLocation, true
			);

			// we can't just ask for the device list right away, as the internal
			// thread getting
			// them from ADB may not be done getting the first list.
			// Since we don't really want getDevices() to be blocking, we wait
			// here manually.
			int count = 0;
			while (bridge.hasInitialDeviceList() == false) {
				try {
					Thread.sleep(100);
					count++;
				} catch (InterruptedException e) {
					// pass
				}

				// let's not wait > 10 sec.
				if (count > 100) {
					System.err.println("Timeout getting device list!");
					return;
				}
			}

			// now get the devices
			IDevice[] devices = bridge.getDevices();

			if (devices.length == 0) {
				printAndExit("No devices found!", true
				);
			}

			monkeyDevice = null;

			if (emulator || device) {
				for (IDevice d : devices) {
					// this test works because emulator and device can't both be
					// true at the same
					// time.
					if (d.isEmulator() == emulator) {
						// if we already found a valid target, we print an error
						// and return.
						if (monkeyDevice != null) {
							if (emulator) {
								printAndExit(
										"Error: more than one emulator launched!",
										true
								);
							} else {
								printAndExit(
										"Error: more than one device connected!",
										true
								);
							}
						}
						monkeyDevice = d;
					}
				}
			} else if (serial != null) {
				for (IDevice d : devices) {
					if (serial.equals(d.getSerialNumber())) {
						monkeyDevice = d;
						break;
					}
				}
			} else {
				if (devices.length > 1) {
					printAndExit(
							"Error: more than one emulator or device available!",
							true
					);
				}
				monkeyDevice = devices[0];
			}
			*/
			
			monkeyDevice = device;
			System.out.println("createForward: " + monkeyPort);
			monkeyDevice.createForward(monkeyPort, monkeyPort);
			//String command = "monkey --port " + monkeyPort;
			//monkeyDevice.executeShellCommand(command, new NullOutputReceiver());

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/***
	 * Open a tcp session over adb with the device to communicate monkey
	 * commands
	 */
	public static void openMonkeyConnection() {
		try {
			InetAddress addr = InetAddress.getByName(monkeyServer);
			monkeySocket = new Socket(addr, monkeyPort);
			monkeyWriter = new BufferedWriter(new OutputStreamWriter(
					monkeySocket.getOutputStream()));
			monkeyReader = new BufferedReader(new InputStreamReader(
					monkeySocket.getInputStream()));
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/***
	 * Close tcp session with the monkey on the device
	 * 
	 */
	public static void closeMonkeyConnection() {
		try {
			monkeyReader.close();
			monkeyWriter.close();
			monkeySocket.close();
			//AndroidDebugBridge.terminate();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/***
	 * Grabs the current state of the screen stores it as a png
	 * 
	 * @param tag
	 *            filename or tag descriptor of the screenshot
	 * @throws AdbCommandRejectedException
	 * @throws TimeoutException
	 */
	public static void grabscreen(String tag) throws IOException,
			TimeoutException, AdbCommandRejectedException {
		tag += ".png";

		try {
			Thread.sleep(1000);
			getDeviceImage(monkeyDevice, tag, false);
		} catch (InterruptedException e) {
		}
	}

	/***
	 * Tap function for scripts to call at a particular x and y location
	 * 
	 * @param x
	 *            x-coordinate
	 * @param y
	 *            y-coordinate
	 */
	public static boolean tap(int x, int y) throws IOException {
		String command = "tap " + x + " " + y;
		boolean result = sendMonkeyEvent(command);
		return result;
	}

	/***
	 * Press function for scripts to call on a particular button or key
	 * 
	 * @param key
	 *            key to press
	 */
	public static boolean press(String key) throws IOException {
		return press(key, true);
	}

	/***
	 * Press function for scripts to call on a particular button or key
	 * 
	 * @param key
	 *            key to press
	 * @param print
	 *            whether to send output to user
	 */
	private static boolean press(String key, boolean print) throws IOException {
		String command = "press " + key;
		boolean result = sendMonkeyEvent(command, print, true);
		return result;
	}

	/***
	 * dpad down function
	 */
	public static boolean down() throws IOException {
		return press("dpad_down");
	}

	/***
	 * dpad up function
	 */
	public static boolean up() throws IOException {
		return press("dpad_up");
	}

	/***
	 * Function to type text on the device
	 * 
	 * @param text
	 *            text to type
	 */
	public static boolean type(String text) throws IOException {
		boolean result = false;
		// text might have line ends, which signal new monkey command, so we
		// have to eat and reissue
		String[] lines = text.split("[\\r\\n]+");
		for (String line : lines) {
			result = sendMonkeyEvent("type " + line + "\n");
		}
		// return last result. Should never fail..?
		return result;
	}

	/***
	 * Function to get a static variable from the device
	 * 
	 * @param name
	 *            name of static variable to get
	 */
	public static boolean getvar(String name) throws IOException {
		return sendMonkeyEvent("getvar " + name + "\n");
	}

	/***
	 * Function to get the list of static variables from the device
	 */
	public static boolean listvar() throws IOException {
		return sendMonkeyEvent("listvar \n");
	}

	/***
	 * This function is the communication bridge between the host and the
	 * device. It sends monkey events and waits for responses over the adb tcp
	 * socket. This version if for all scripted events so that they get recorded
	 * and reported to user.
	 * 
	 * @param command
	 *            the monkey command to send to the device
	 */
	private static boolean sendMonkeyEvent(String command) throws IOException {
		return sendMonkeyEvent(command, true, true);
	}

	/***
	 * This function allows the communication bridge between the host and the
	 * device to be invisible to the script. for internal needs. It splits a
	 * command into monkey events and waits for responses for each over an adb
	 * tcp socket. Returns on an error, else continues and sets up last
	 * response.
	 * 
	 * @param command
	 *            the monkey command to send to the device
	 * @param print
	 *            whether to print out the responses to the user
	 * @param record
	 *            whether to put the command in the xml file that stores test
	 *            outputs
	 */
	private static boolean sendMonkeyEvent(String command, Boolean print,
			Boolean record) throws IOException {
		command = command.trim();
		if (print)
			System.out.println("MonkeyCommand: " + command);

		logger.info("Monkey Command: " + command + ".");

		// send a single command and get the response
		monkeyWriter.write(command + "\n");
		monkeyWriter.flush();
		monkeyResponse = monkeyReader.readLine();

		if (monkeyResponse != null) {
			// if a command returns with a response
			if (print)
				System.out.println("MonkeyServer: " + monkeyResponse);
			if (record) {
				// recordResponse(monkeyResponse);
			}
			logger.info("Monkey Response: " + monkeyResponse + ".");

			// return on error
			if (monkeyResponse.startsWith("ERROR"))
				return false;

			// return on ok
			if (monkeyResponse.startsWith("OK"))
				return true;

			// return on something else?
			return false;
		}
		// didn't get a response...
		if (print)
			System.out.println("MonkeyServer: ??no response");
		if (record) {
			// recordResponse("??no response");
		}
		logger.info("Monkey Response: ??no response.");

		// return on no response
		return false;
	}

	/**
	 * Grab an image from an ADB-connected device.
	 * 
	 * @throws AdbCommandRejectedException
	 * @throws TimeoutException
	 */
	private static void getDeviceImage(IDevice device, String filepath,
			boolean landscape) throws IOException, TimeoutException,
			AdbCommandRejectedException {
		RawImage rawImage;

		System.out.println("Grabbing Screeshot: " + filepath + ".");

		try {
			rawImage = device.getScreenshot();
		} catch (IOException ioe) {

			printAndExit("Unable to get frame. buffer: " + ioe.getMessage(),
					true /** terminate */
			);
			return;
		}

		// device/adb not available?
		if (rawImage == null) {

			return;
		}

		assert rawImage.bpp == 16;

		BufferedImage image;

		logger.info("Raw Image - height: " + rawImage.height + ", width: "
				+ rawImage.width);

		if (landscape) {
			// convert raw data to an Image
			image = new BufferedImage(rawImage.height, rawImage.width,
					BufferedImage.TYPE_INT_ARGB);

			byte[] buffer = rawImage.data;
			int index = 0;
			for (int y = 0; y < rawImage.height; y++) {
				for (int x = 0; x < rawImage.width; x++) {

					int value = buffer[index++] & 0x00FF;
					value |= (buffer[index++] << 8) & 0x0FF00;

					int r = ((value >> 11) & 0x01F) << 3;
					int g = ((value >> 5) & 0x03F) << 2;
					int b = ((value >> 0) & 0x01F) << 3;

					value = 0xFF << 24 | r << 16 | g << 8 | b;

					image.setRGB(y, rawImage.width - x - 1, value);
				}
			}
		} else {
			// convert raw data to an Image
			image = new BufferedImage(rawImage.width, rawImage.height,
					BufferedImage.TYPE_INT_ARGB);

			byte[] buffer = rawImage.data;
			int index = 0;
			for (int y = 0; y < rawImage.height; y++) {
				for (int x = 0; x < rawImage.width; x++) {

					int value = buffer[index++] & 0x00FF;
					value |= (buffer[index++] << 8) & 0x0FF00;

					int r = ((value >> 11) & 0x01F) << 3;
					int g = ((value >> 5) & 0x03F) << 2;
					int b = ((value >> 0) & 0x01F) << 3;

					value = 0xFF << 24 | r << 16 | g << 8 | b;

					image.setRGB(x, y, value);
				}
			}
		}

		if (!ImageIO.write(image, "png", new File(filepath))) {
			throw new IOException("Failed to find png writer");
		}

	}

	private static void printAndExit(String message, boolean terminate) {
		System.out.println(message);
		if (terminate) {
			AndroidDebugBridge.terminate();
		}
		System.exit(1);
	}
}
