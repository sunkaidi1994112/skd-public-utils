package com.skd.one.common.aop.aop;

import com.alibaba.fastjson.JSONObject;
import com.skd.one.common.aop.anno.GetFrontParams;
import com.skd.one.common.exception.BizBaseException;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * 给前端给会
 * @author skd
 * @date 2021/9/10 16:41
 */
@Component
@Aspect
public class ParamCorrect {
    //定义基本类型
    private static String[] types = {"java.lang.Integer", "java.lang.Double","java.math.BigInteger",
            "java.lang.Float", "java.lang.Long", "java.lang.Short",
            "java.lang.Byte", "java.lang.Boolean", "java.lang.Char",
            "java.lang.String", "int", "double", "long", "short", "byte",
            "boolean", "char", "float"};

    @Pointcut("within( com.skd.one.controller..*)")
    public void  pointcut(){
        //所有controller下面的方法都能扫描到
    }
    @Around("pointcut()")
    public Object around(ProceedingJoinPoint joinPoint){
        Object proceed=null;
        try {
            //获取传进来的参数
            MethodSignature signature = (MethodSignature) joinPoint.getSignature();
            //获取参数类型
            Class[] parameterTypes = signature.getParameterTypes();
            //获取参数名称
            String[] parameterNames = signature.getParameterNames();
            //获取参数值
            Object[] args = joinPoint.getArgs();
//            Object[] args = joinPoint.getArgs();
            for (int i=0;i<parameterTypes.length;i++) {
//            for (Object each:args) {
                String typeName = parameterTypes[i].getName();
                //获取注解里面的参数
                String[] items = ((MethodSignature)joinPoint.getSignature()).getMethod().getAnnotation(GetFrontParams.class).items();
                for (String t : types) {
                    //1 判断是否是基础类型
                    if (t.equals(typeName)) {
                        //如果是基础类型，检查注解里面有没有必填项如果为空抛出异常
//                        if(Arrays.asList(items).contains(typeName)){
                        if(Arrays.asList(items).contains(parameterNames[i])){
                            //如果参数值为空
                            if (args[i]==null){
                                throw new BizBaseException(parameterNames[i]+"不能为null");
                            }
                        }
                    }else{
                        //如果是实体类
                        Class parameterType = parameterTypes[i];
                        
                        Object arg = args[i];
                        String s = arg.toString();
                        System.out.println(s);
//                        Class<?> aClass = Class.forName(parameterType.getName());
                        String json = JSONObject.toJSONString(arg);
                        JSONObject pa=JSONObject.parseObject(json);
                        for (String each:items) {
                            String string = pa.getString(each);
                            if (StringUtils.isBlank(string)){
                                throw new BizBaseException(each+"不能为空");
                            }
                        }
                        //直接退出
                        return null;

//                        parameterType.get("")
                        //2 通过反射获取实体类属性
//                        sb.append(getFieldsValue(arg));
                    }
                }
            }

            proceed =joinPoint.proceed();//1.执行下一个通知(before.after等等),2.执行目标方法

        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        return proceed;
    }
    public void jsonToMap(String json){

    }
}
