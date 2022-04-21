package top.srcrs.bilibili.util;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.FileReader;

/**
 * 将日志消息发送到用户的server酱
 * @author srcrs
 * @Time 2020-10-22
 */
public class SendServer {
    /** 获取日志记录器对象 */
    private static final Logger LOGGER = LoggerFactory.getLogger(SendServer.class);

    /**
     * 发送消息给用户，如果绑定了微信，会发送到微信上。
     * @param sckey 需要从server酱的官网注册获取
     * @author srcrs
     * @Time 2020-10-22
     */
    public static void send(String sckey){
        /** 将要推送的数据 */
        String desp = readLog("logs/logback.log");
        String body = "text="+"BilibiliTask运行结果"+"&desp="+desp;
        StringEntity entityBody = new StringEntity(body,"UTF-8");
        HttpClient client = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost("https://sc.ftqq.com/"+sckey+".send");
        httpPost.addHeader("Content-Type","application/x-www-form-urlencoded");
        httpPost.setEntity(entityBody);
        HttpResponse resp = null;
        String respContent = null;
        try{
            resp = client.execute(httpPost);
            HttpEntity entity=null;
            if(resp.getStatusLine().getStatusCode()<400){
                entity = resp.getEntity();
            } else{
                entity = resp.getEntity();
            }
            respContent = EntityUtils.toString(entity, "UTF-8");
            LOGGER.info("server酱推送正常");
        } catch (Exception e){
            LOGGER.error("server酱发送失败 -- "+e);
        }
    }

    /**
     * 读取输出到文件中的日志
     * @param pathName 日志文件的名字，包括路径
     * @return String 将日志拼接成了字符串
     * @author srcrs
     * @Time 2020-10-22
     */
    public static String readLog(String pathName){
        /** str代表要发送的数据 */
        String str = "";
        FileReader reader = null;
        BufferedReader br = null;
        try{
            reader = new FileReader(pathName);
            br = new BufferedReader(reader);
            String line;
            while ((line = br.readLine()) != null){
                str += line + "\n\n";
            }
            reader.close();
            br.close();
        } catch (Exception e){
            LOGGER.error("读日志文件时出错 -- "+e);
        }
        return str;
    }
}
