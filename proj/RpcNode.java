import java.util.logging.Logger;

import com.google.protobuf.ByteString;

/**
 * Implementation of the Rpc node for the CSE552 project.
 */

/**
 *
 *
 * @author Arunabha Ghosh
 */
public class RpcNode extends RIONode {
	private static final Logger logger = Logger.getLogger(RpcNode.class.getName());

	private Server server;

	@Override
	public void onRIOReceive(Integer from, int protocol, byte[] msg) {
		logInfo(String.format("onRIOReceive from %d, protocol %d", from, protocol));
		server.handleMessage(from, protocol, msg);
	}

	@Override
	public void start() {
		logInfo(String.format("starting"));
		server = new Server(addr);
	}

	@Override
	public void onCommand(String command) {
		logInfo(String.format("got command %s", command));
		MessageTypes.CreateFileRequest.Builder builder = MessageTypes.CreateFileRequest.newBuilder();
		builder.setFileName("TestFile");
		sendMessage(1, Protocol.RIOTEST_PKT, builder.build().toByteString(),
				MessageTypes.TransportEnvelope.MessageType.CreateFileRequest);
	}

	private void sendMessage(int to, int protocol, ByteString msg,
			MessageTypes.TransportEnvelope.MessageType msgType) {
		MessageTypes.TransportEnvelope.Builder builder = MessageTypes.TransportEnvelope.newBuilder();
		builder.setMessageType(msgType);
		builder.setMessageBytes(msg);
		RIOSend(to, protocol, builder.build().toByteArray());
	}

	private void logInfo(String logString) {
		logger.info(String.format("Node-%d: %s", addr, logString));
	}

	private void logWarning(String logString) {
		logger.warning(String.format("Node-%d: %s", addr, logString));
	}
}
