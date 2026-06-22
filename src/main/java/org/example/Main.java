package org.example;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        System.out.println("九九乘法表：");
        System.out.println("========================");
        
        // 外层循环控制行数
        for (int i = 1; i <= 9; i++) {
            // 内层循环控制每行的列数
            for (int j = 1; j <= i; j++) {
                // 输出乘法表达式，使用制表符对齐
                System.out.print(j + "×" + i + "=" + (i * j) + "\t");
            }
            // 每行结束后换行
            System.out.println();
        }
        
        System.out.println("========================");
        System.out.println("0622");
    }
}