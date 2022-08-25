package com.iscas.pm.api.service.impl;

import com.iscas.pm.api.mapper.project.ProjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.Scanner;

/**
 * @Author： zhangchao
 * @Date： 2022/7/18
 * @Description：
 */

class Solution {
    public String testSelect(int n, String A,String B){
        char[] chars = new char[2*n];
        int j=0;
        for(int i=0;i<A.length();i++,j++){
            chars[j]=A.charAt(i);
            chars[j+1] = B.charAt(i);
        }
        return new String(chars);
    }
}






