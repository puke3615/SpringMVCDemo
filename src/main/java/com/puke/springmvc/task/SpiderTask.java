package com.puke.springmvc.task;

import com.puke.springmvc.HomeController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Locale;

/**
 * @author zijiao
 * @version 18/1/4
 *          <p>
 *          [秒] [分] [小时] [日] [月] [周] [年]
 *          参: https://www.cnblogs.com/liaojie970/p/5913272.html
 */
// 取消注释, 开启Task
// @Component
public class SpiderTask {

    private static final Logger LOG = LoggerFactory.getLogger(HomeController.class);
    private static final URL currentURL = Thread.currentThread().getContextClassLoader().getResource("scripts/spider.py");

    // 每分钟的10秒执行
    @Scheduled(cron = "10 * * * * ?")
    public void job1() {
        LOG.info("hello。。。。");
    }

    // 每秒执行一次
//    @Scheduled(fixedRate = 1000)
    public void job() {
        LOG.info("world");
    }

    // 每分钟一次
//    @Scheduled(cron = "0 * * * * ?")
    @Scheduled(fixedRate = 1000)
    public void startupSpider() {
        if (currentURL == null) {
            return;
        }
        try {
            String command = String.format(Locale.getDefault(), "python %s", currentURL.getPath());
            Process process = Runtime.getRuntime().exec(command);
            String result = new String(is2Bytes(process.getInputStream()));
            LOG.info(String.format(Locale.getDefault(), "[%s]: %s", command, result));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static byte[] is2Bytes(InputStream inputStream) {
        if (inputStream == null) {
            return null;
        }
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            byte[] buffer = new byte[4096];
            int index;
            while ((index = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, index);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return outputStream.toByteArray();
    }

}
