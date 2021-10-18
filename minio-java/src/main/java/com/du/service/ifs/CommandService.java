package com.du.service.ifs;

/**
 * [一句话描述该类的功能]
 *
 * @author : Du Yingjie (2548425238@qq.com)
 * @version : [v1.0]
 * @date : [2021/9/18 14:23]
 */
public interface CommandService {

    /**
     * 参数为shell命令，成功返回执行结果，失败返回异常信息
     * @param cmd 执行的shell命令
     * @return 命令执行结果
     */
    void executeCmd(String cmd);

}
