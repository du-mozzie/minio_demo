package com.du.service;

import com.du.service.ifs.CommandService;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * @author : Du YingJie (2548425238@qq.com)
 * @version : [v1.0]
 * @date : [2021/9/18 14:24]
 */
@Service
public class CommandServiceImpl implements CommandService {

    @Override
    public void executeCmd(String cmd) {
        try {
            Runtime rt = Runtime.getRuntime();
            Process proc = rt.exec(cmd, null, null);
            InputStream stderr = proc.getInputStream();
            InputStreamReader isr = new InputStreamReader(stderr, "UTF-8");
            BufferedReader br = new BufferedReader(isr);
            String line = "";
            while ((line = br.readLine()) != null) {
                System.out.println(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
