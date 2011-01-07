import java.util.logging.Logger;

import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;

/**
 * Implementation of the server specific functionality.
 *
 * @author Arunabha Ghosh
 */
public class Server {
	private static final Logger logger = Logger.getLogger(Server.class.getName());
	private final int nodeAddress;

	public Server(int nodeAddress) {
		this.nodeAddress = nodeAddress;
	}

	/**
	 * Called by the RPCNode to handle incoming messages. Figures out the message
	 * type and dispatches accordingly. Same arguments as
	 * {@code RIONode#onRIOReceive}.
	 *
	 * @param from
	 * @param protocol
	 * @param msg
	 */
	public void handleMessage(Integer from, int protocol, byte[] msg) {
		// Try to get the TransportEnvelope.
		try {
			MessageTypes.TransportEnvelope envelope = MessageTypes.TransportEnvelope.parseFrom(msg);
			switch (envelope.getMessageType()) {
			case CreateFileRequest:
				handleCreateFileRequest(from, protocol, envelope.getMessageBytes());
			}
		} catch (InvalidProtocolBufferException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logWarning("Could not parse TransportEnvelope, dropping");
		}
	}

	private void handleCreateFileRequest(Integer from, int protocol, ByteString msg) {
		try {
			MessageTypes.CreateFileRequest createFileRequest =
				MessageTypes.CreateFileRequest.parseFrom(msg);
			logInfo(String.format("Got CreateFileRequest %s", createFileRequest.toString()));
		} catch (InvalidProtocolBufferException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logWarning("Could not parse CreateFileRequest, dropping");
		}
	}

	private void logInfo(String logString) {
		logger.info(String.format("Server-%d: %s", nodeAddress, logString));
	}

	private void logWarning(String logString) {
		logger.warning(String.format("Server-%d: %s", nodeAddress, logString));
	}
}
