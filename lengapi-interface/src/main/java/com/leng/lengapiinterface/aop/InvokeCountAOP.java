package com.leng.lengapiinterface.aop;

import org.springframework.web.bind.annotation.RestControllerAdvice;


/**
 * AOP 面向切面编程
 * 统计接口调用次数
 */
@RestControllerAdvice
public class InvokeCountAOP {

//    @Resource
//    private UserInterfaceInfoService userInterfaceInfoService;

    // 伪代码
    // 定义切面出发的时机 (什么时候执行方法) Controller 接口的方法执行成功后,执行下述方法
//    public void doInvokeCount(xxx) {
//        // 调用方法
//        object.proceed();
//        // 2. 调用成功后, 次数 + 1
//        UserInterfaceInfoService.InvokeCount();
//        // 3. 更新到数据库
//    }
}
