package com.maybank.platform.services.util.codeTest;

import cn.hutool.core.util.ObjectUtil;

public class CodeTest {
    public static void main(String[] args) {
        String str = "ABCD";
        System.out.println(method1(str));
    }

    public static String method1(String str) {
        StringBuilder sb = new StringBuilder();
        char[] chars = str.toCharArray();
        for(int i = chars.length -1; i >=0; i--){
            sb.append(chars[i]);
        }
        return sb.toString();
    }
}
