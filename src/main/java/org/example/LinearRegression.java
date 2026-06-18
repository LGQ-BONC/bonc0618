package org.example;

/**
 * 线性回归算法实现
 * 使用最小二乘法进行一元线性回归: y = wx + b
 */
public class LinearRegression {
    
    private double weight;  // 权重 w
    private double bias;    // 偏置 b
    
    /**
     * 训练模型 - 使用最小二乘法计算最优参数
     * @param x 输入特征数组
     * @param y 目标值数组
     */
    public void fit(double[] x, double[] y) {
        if (x == null || y == null || x.length != y.length || x.length == 0) {
            throw new IllegalArgumentException("输入数据无效");
        }
        
        int n = x.length;
        double sumX = 0, sumY = 0, sumXY = 0, sumXX = 0;
        
        // 计算各项求和
        for (int i = 0; i < n; i++) {
            sumX += x[i];
            sumY += y[i];
            sumXY += x[i] * y[i];
            sumXX += x[i] * x[i];
        }
        
        // 使用最小二乘法公式计算 w 和 b
        // w = (n * Σxy - Σx * Σy) / (n * Σx² - (Σx)²)
        // b = (Σy - w * Σx) / n
        double denominator = n * sumXX - sumX * sumX;
        
        if (Math.abs(denominator) < 1e-10) {
            throw new ArithmeticException("无法计算：分母接近零，数据可能存在共线性问题");
        }
        
        this.weight = (n * sumXY - sumX * sumY) / denominator;
        this.bias = (sumY - this.weight * sumX) / n;
    }
    
    /**
     * 预测单个值
     * @param x 输入特征值
     * @return 预测结果
     */
    public double predict(double x) {
        return weight * x + bias;
    }
    
    /**
     * 批量预测
     * @param x 输入特征数组
     * @return 预测结果数组
     */
    public double[] predict(double[] x) {
        double[] predictions = new double[x.length];
        for (int i = 0; i < x.length; i++) {
            predictions[i] = predict(x[i]);
        }
        return predictions;
    }
    
    /**
     * 计算均方误差 (MSE)
     * @param x 输入特征数组
     * @param y 真实值数组
     * @return 均方误差
     */
    public double meanSquaredError(double[] x, double[] y) {
        if (x.length != y.length) {
            throw new IllegalArgumentException("输入数组长度不一致");
        }
        
        double mse = 0;
        for (int i = 0; i < x.length; i++) {
            double error = predict(x[i]) - y[i];
            mse += error * error;
        }
        return mse / x.length;
    }
    
    /**
     * 计算R²决定系数
     * @param x 输入特征数组
     * @param y 真实值数组
     * @return R²值（越接近1表示拟合效果越好）
     */
    public double r2Score(double[] x, double[] y) {
        if (x.length != y.length) {
            throw new IllegalArgumentException("输入数组长度不一致");
        }
        
        // 计算平均值
        double meanY = 0;
        for (double value : y) {
            meanY += value;
        }
        meanY /= y.length;
        
        // 计算总平方和 (SST) 和残差平方和 (SSR)
        double ssTotal = 0;
        double ssResidual = 0;
        
        for (int i = 0; i < x.length; i++) {
            double predicted = predict(x[i]);
            ssTotal += Math.pow(y[i] - meanY, 2);
            ssResidual += Math.pow(y[i] - predicted, 2);
        }
        
        if (Math.abs(ssTotal) < 1e-10) {
            return 0;
        }
        
        return 1 - (ssResidual / ssTotal);
    }
    
    /**
     * 获取权重
     * @return weight
     */
    public double getWeight() {
        return weight;
    }
    
    /**
     * 获取偏置
     * @return bias
     */
    public double getBias() {
        return bias;
    }
    
    /**
     * 返回模型信息
     * @return 模型字符串表示
     */
    @Override
    public String toString() {
        return String.format("LinearRegression: y = %.4fx + %.4f", weight, bias);
    }
}
