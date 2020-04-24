import com.neovisionaries.ws.client.WebSocket;
import com.neovisionaries.ws.client.WebSocketAdapter;
import com.prime.net.PWebSocket;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;


public class Test{
    private PWebSocket pWebSocket;
    private final String WSADDRESS = "ws://127.0.0.1:8088";

    //@BeforeAll
    public Test(){
        pWebSocket = new PWebSocket(WSADDRESS, new WebSocketAdapter() {
            @Override public void onTextMessage(WebSocket ws, String incomingMessage) {
                if(incomingMessage.contains("~!~")) {
                    //process commands
                    return;
                }
            }
        });
    }

    @org.junit.jupiter.api.Test
    public void connect() throws Exception{
        pWebSocket.connect();
    }

}
