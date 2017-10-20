package com.lsh.swagger.register;

import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.config.ProtocolConfig;
import com.lsh.swagger.Protocol;
import com.lsh.swagger.SwaggerConfig;
import com.lsh.swagger.zookeeper.MyZkClient;

import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;

/**
 * Created by zhangqiang on 17/5/2.
 */
public class RPCSwaggerRegister implements SwaggerRegister{

    private MyZkClient zkClient;

    public RPCSwaggerRegister(URL url){
        this.zkClient = new MyZkClient(url);
    }


    @Override
    public void registy(String className, Method method, Class[] params) {

        Set<ProtocolConfig> set = SwaggerConfig.protocolConfigMap.get(Protocol.RPC);
        String host = null;
        Integer port = null;
        for( ProtocolConfig config : set){
            if(config.getHost() != null){
                host = config.getHost();
            }

            if(config.getPort() != null){
                port = config.getPort();
            }
        }

        if("127.0.0.1".equals(host)){
            try {
                host = getIpAdd();
            } catch (SocketException e) {
                e.printStackTrace();
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }
        }

        String basePath = "/swagger" + "/" + className +  "/" + host + ":" + port;
        if(!zkClient.exists(basePath)){
            zkClient.createPersistent(basePath);
        }

        List<String> paramList = new ArrayList();
        for(Class clazz : params){
            paramList.add(clazz.toString());
        }
        if(zkClient.exists(basePath + "/" + method.getName())){
           zkClient.delete(basePath + "/" + method.getName());
        }
        this.zkClient.createEphemeral(basePath + "/" + method.getName(),paramList);
    }


    public  String getIpAdd() throws SocketException, UnknownHostException{
        String ip="";
        for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
            NetworkInterface intf = en.nextElement();
            String name = intf.getName();
            if (!name.contains("docker") && !name.contains("lo")) {
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
                    //获得IP
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        String ipaddress = inetAddress.getHostAddress().toString();
                        if (!ipaddress.contains("::") && !ipaddress.contains("0:0:") && !ipaddress.contains("fe80")) {

                            System.out.println(ipaddress);
                            if(!"127.0.0.1".equals(ip)){
                                ip = ipaddress;
                            }
                        }
                    }
                }
            }
        }
        return ip;
    }

}
