package com.example.springboot_demo.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PatternName {
    public static void main(String[] args){
        System.out.println("是否全为数字:"+CheckPattern("4536789","\\d*"));
        System.out.println("是否全为字母:"+CheckPattern("chjk","[A-Za-z]*"));
        System.out.println("是否全为中文:"+CheckPattern("放首歌","[\\u4e00-\\u9fa5]*"));
        System.out.println("允许范围字母数字下划线:"+CheckPattern("sdacfyu970_","^\\w+$"));
    }

    public static boolean CheckPattern(String input,String reg){
        Pattern compile = Pattern.compile(reg);
        Matcher matcher = compile.matcher(input);
        return matcher.matches();
    }
}
