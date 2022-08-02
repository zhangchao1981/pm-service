package com.iscas.pm.common.core.util.validation;

import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import lombok.SneakyThrows;

import java.lang.invoke.SerializedLambda;
import java.lang.reflect.Method;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;


/**
 * @author by  lichang
 * @date 2022/8/2.
 */
public class TreeUtil {

    /**
     * @param list          树结构的基础数据集
     * @param getIdFn       获取主键的函数
     * @param getParentIdFn 获取父节点的函数
     * @param getChildrenFn 获取子集的函数
     * @param <T>           t
     * @param <R>           r
     * @return t
     */
    @SneakyThrows
    public static <T, R> List<T> treeOut(List<T> list, Function<T, R> getIdFn, Function<T, R> getParentIdFn, SFunction<T, R> getChildrenFn) {
        /*所有元素的Id*/
        List<Object> ids = list.stream().map(getIdFn).collect(Collectors.toList());
        /*查出所有顶级节点*/
        List<T> topLevel = list.stream().filter(x -> {
            R apply = getParentIdFn.apply(x);
            return !ids.contains(apply);
        }).collect(Collectors.toList());
        return TreeUtil.recursion(topLevel, list, getIdFn, getParentIdFn, getChildrenFn);
    }


    @SneakyThrows
    private static <T, R> List<T> recursion(List<T> superLevel, List<T> list, Function<T, R> getIdFn, Function<T, R> getParentIdFn, SFunction<T, R> getChildrenFn) {
        //获取setChildren的Method
        Method writeReplaceMethod = getChildrenFn.getClass().getDeclaredMethod("writeReplace");
        boolean accessible = writeReplaceMethod.isAccessible();
        writeReplaceMethod.setAccessible(true);
        SerializedLambda serializedLambda = (SerializedLambda) writeReplaceMethod.invoke(getChildrenFn);
        writeReplaceMethod.setAccessible(accessible);
        String setMethodName = serializedLambda.getImplMethodName().replaceFirst("g", "s");
        Method setMethod = Class.forName(serializedLambda.getImplClass().replace("/", ".")).getDeclaredMethod(setMethodName, List.class);

        for (T t : superLevel) {
            List<T> children = list.stream().filter(x -> {
                R apply = getParentIdFn.apply(x);
                R apply1 = getIdFn.apply(t);
                return apply.equals(apply1);
            }).collect(Collectors.toList());
            if (children.size() <= 0) {
                continue;
            }

            List<T> recursion = recursion(children, list, getIdFn, getParentIdFn, getChildrenFn);
            setMethod.invoke(t, recursion);
        }
        return superLevel;
    }
}
