package com.lsh.swagger.zookeeper;

import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.remoting.zookeeper.ChildListener;
import com.alibaba.dubbo.remoting.zookeeper.StateListener;
import com.alibaba.dubbo.remoting.zookeeper.support.AbstractZookeeperClient;
import org.I0Itec.zkclient.IZkChildListener;
import org.I0Itec.zkclient.IZkStateListener;
import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.exception.ZkNoNodeException;
import org.I0Itec.zkclient.exception.ZkNodeExistsException;
import org.apache.zookeeper.Watcher;

import java.util.List;

/**
 * Created by zhangqiang on 17/5/2.
 */
public class MyZkClient extends AbstractZookeeperClient<IZkChildListener> {

    private final ZkClient client;

    private volatile Watcher.Event.KeeperState state = Watcher.Event.KeeperState.SyncConnected;

    public MyZkClient(URL url){
        super(url);
        client = new ZkClient(
                url.getBackupAddress(),
                url.getParameter(Constants.SESSION_TIMEOUT_KEY, Constants.DEFAULT_SESSION_TIMEOUT),
                url.getParameter(Constants.TIMEOUT_KEY, Constants.DEFAULT_REGISTRY_CONNECT_TIMEOUT));
        client.subscribeStateChanges(new IZkStateListener() {
            public void handleStateChanged(Watcher.Event.KeeperState state) throws Exception {
                MyZkClient.this.state = state;
                if (state == Watcher.Event.KeeperState.Disconnected) {
                    stateChanged(StateListener.DISCONNECTED);
                } else if (state == Watcher.Event.KeeperState.SyncConnected) {
                    stateChanged(StateListener.CONNECTED);
                }
            }
            public void handleNewSession() throws Exception {
                stateChanged(StateListener.RECONNECTED);
            }
        });
    }


    protected void stateChanged(int state) {
        for (StateListener sessionListener : getSessionListeners()) {
            sessionListener.stateChanged(state);
        }
    }

    public void createPersistent(String path) {
        try {
            client.createPersistent(path, true);
        } catch (ZkNodeExistsException e) {
        }
    }

    public void createEphemeral(String path) {
        try {
            client.createEphemeral(path);
        } catch (ZkNodeExistsException e) {
        }
    }

    public void delete(String path) {
        try {
            client.delete(path);
        } catch (ZkNoNodeException e) {
        }
    }

    public List<String> getChildren(String path) {
        try {
            return client.getChildren(path);
        } catch (ZkNoNodeException e) {
            return null;
        }
    }

    public boolean isConnected() {
        return state == Watcher.Event.KeeperState.SyncConnected;
    }

    public void doClose() {
        client.close();
    }

    public IZkChildListener createTargetChildListener(String path, final ChildListener listener) {
        return new IZkChildListener() {
            public void handleChildChange(String parentPath, List<String> currentChilds)
                    throws Exception {
                listener.childChanged(parentPath, currentChilds);
            }
        };
    }

    public List<String> addTargetChildListener(String path, final IZkChildListener listener) {
        return client.subscribeChildChanges(path, listener);
    }

    public void removeTargetChildListener(String path, IZkChildListener listener) {
        client.unsubscribeChildChanges(path,  listener);
    }


    public void createEphemeral(String path,Object data){
        client.createEphemeral(path,data);
    }

    public boolean exists(String path){
        return client.exists(path);
    }
}
