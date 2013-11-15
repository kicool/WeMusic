package net.kicool.services;

import android.util.Log;
import net.kicool.common.utils.LoggerUtil;
import net.kicool.common.utils.NetworkUtil;
import org.jgroups.Address;
import org.jgroups.JChannel;
import org.jgroups.Message;
import org.jgroups.ReceiverAdapter;
import org.jgroups.View;
import org.jgroups.protocols.BARRIER;
import org.jgroups.protocols.FD_ALL;
import org.jgroups.protocols.FD_SOCK;
import org.jgroups.protocols.FRAG2;
import org.jgroups.protocols.MERGE2;
import org.jgroups.protocols.MFC;
import org.jgroups.protocols.PING;
import org.jgroups.protocols.UDP;
import org.jgroups.protocols.UFC;
import org.jgroups.protocols.UNICAST2;
import org.jgroups.protocols.VERIFY_SUSPECT;
import org.jgroups.protocols.pbcast.GMS;
import org.jgroups.protocols.pbcast.NAKACK;
import org.jgroups.protocols.pbcast.STABLE;
import org.jgroups.stack.ProtocolStack;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by kicoolzhang on 11/12/13.
 */
public class PeersMgr extends BaseService {

    private static final String TAG = "PeersMgr";

    private static final String CHAT_CLUSTER = "WeMusic";
    private static final int DELAY_TIME = 1000 * 5;

    JChannel channel;
    boolean inLoop = false;

    List<Address> peersIpAddress = new ArrayList<Address>();

    ExecutorService senderExecutorService = Executors.newSingleThreadExecutor();
    ExecutorService controlerExecutorService = Executors.newSingleThreadExecutor();

    //TODO:
    // *. a better peer id
    // *. peers list
    // *. network state
    // *. session id for different group in a same localnetwork, maybe use different CHAT_CLUSTER?
    // *. Sync time
    // *. Sync action

    public PeersMgr() {
    }

    @Override
    public void init(ServiceProvider provider) {
        super.init(provider);

        try {
            channel = new JChannel(false);
            channel.setName(initUsername());

            ProtocolStack stack = new ProtocolStack();
            channel.setProtocolStack(stack);

            WifiNetworkService service = (WifiNetworkService) ServiceProvider.getInstance().getServiceInstance("WifiNetworkService");
            String address = service.getWifiInfo().getWifiIpAddress();
            LoggerUtil.i(TAG, "bind_addr:" + address);

            stack.addProtocol(new UDP().setValue("bind_addr", InetAddress.getByName(address)))
                    .addProtocol(new PING())
                    .addProtocol(new MERGE2())
                    .addProtocol(new FD_SOCK())
                    .addProtocol(new FD_ALL().setValue("timeout", 12000).setValue("interval", 3000))
                    .addProtocol(new VERIFY_SUSPECT())
                    .addProtocol(new BARRIER())
                    .addProtocol(new NAKACK())
                    .addProtocol(new UNICAST2())
                    .addProtocol(new STABLE())
                    .addProtocol(new GMS())
                    .addProtocol(new UFC())
                    .addProtocol(new MFC())
                    .addProtocol(new FRAG2());
            stack.init();

            channel.setReceiver(new Receiver());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void start() {
        inLoop = true;

        controlerExecutorService.execute(new Runnable() {
            @Override
            public void run() {

                try {
                    channel.connect(CHAT_CLUSTER);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


        controlerExecutorService.execute(new Runnable() {
            @Override
            public void run() {
                loop();
            }
        });

    }

    private String initUsername() {
        //TODO
        String userName = "Haala";
        LoggerUtil.i(TAG, "Username: " + userName);
        return userName;
    }

    private void loop() {
        while (inLoop) {

            if (channel.isConnected()) {
                Date data = new Date();

                sendNtp(data.getTime());

                try {
                    Thread.sleep(DELAY_TIME);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    protected void sendMessage(final String message) {
        senderExecutorService.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    String line = message;
                    Message msg = new Message(null, null, message);
                    channel.send(msg);

                    Log.v(TAG, "Send: -> " + line);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void sendCount(final int count) {
        String line = "COUNT:" + count;
        sendMessage(line);
    }

    public void sendNtp(final long time) {
        String line = "NTP:" + time;
        sendMessage(line);
    }

    @Override
    public void stop() {
        inLoop = false;

        controlerExecutorService.execute(new Runnable() {
            @Override
            public void run() {
                channel.close();
            }
        });

    }

    protected class Receiver extends ReceiverAdapter {

        @Override
        public void getState(OutputStream output) throws Exception {
            super.getState(output);
        }

        @Override
        public void setState(InputStream input) throws Exception {
            super.setState(input);
        }

        @Override
        public void suspect(Address mbr) {
            super.suspect(mbr);
        }

        @Override
        public void block() {
            super.block();
        }

        @Override
        public void unblock() {
            super.unblock();
        }

        @Override
        public void viewAccepted(View new_view) {
            Log.i(TAG, "** view: " + new_view);

            int peersNumber = new_view.size();
            if (peersNumber > 1) {
                List<Address> members = new_view.getMembers();
                peersIpAddress.clear();


                for (Address a : members) {
                    String addr = a.toString();
                    Log.i(TAG, "members:" + addr);

                    if (!addr.equalsIgnoreCase(channel.getAddressAsString())) {
                        peersIpAddress.add(a);
                    }
                }

            }

        }

        @Override
        public void receive(Message msg) {

            String srcAddr = msg.getSrc().toString();
            if (srcAddr.equalsIgnoreCase(channel.getAddressAsString())) {
                return;
            }

            Log.v(TAG, "Recv: -> " + msg.getObject());

            String line = msg.getObject().toString();
            if (line.contains("COUNT")) {
                String messages[] = line.split(":");
                int count = Integer.valueOf(messages[1]);
                Log.i(TAG, "COUNT: -> " + count);
            } else if (line.contains("NTP")) {
                String messages[] = line.split(":");
                long time = Long.valueOf(messages[1]);
                Log.i(TAG, "NTP: -> " + time);
            }
        }
    }

}
