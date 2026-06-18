package org.example;

import java.util.*;

/**
 * K-Means聚类算法实现
 */
public class KMeansClustering {
    
    /**
     * 数据点类
     */
    static class DataPoint {
        double[] features;
        int cluster;
        
        public DataPoint(double[] features) {
            this.features = features;
            this.cluster = -1;
        }
        
        public int getDimensions() {
            return features.length;
        }
    }
    
    /**
     * 计算两个点之间的欧氏距离
     */
    private static double euclideanDistance(double[] point1, double[] point2) {
        if (point1.length != point2.length) {
            throw new IllegalArgumentException("维度不匹配");
        }
        
        double sum = 0.0;
        for (int i = 0; i < point1.length; i++) {
            sum += Math.pow(point1[i] - point2[i], 2);
        }
        return Math.sqrt(sum);
    }
    
    /**
     * 初始化质心（随机选择K个点作为初始质心）
     */
    private static double[][] initializeCentroids(DataPoint[] dataPoints, int k, Random random) {
        int n = dataPoints.length;
        int dimensions = dataPoints[0].getDimensions();
        double[][] centroids = new double[k][dimensions];
        
        // 随机选择k个点作为初始质心
        Set<Integer> usedIndices = new HashSet<>();
        for (int i = 0; i < k; i++) {
            int index;
            do {
                index = random.nextInt(n);
            } while (usedIndices.contains(index));
            
            usedIndices.add(index);
            System.arraycopy(dataPoints[index].features, 0, centroids[i], 0, dimensions);
        }
        
        return centroids;
    }
    
    /**
     * 将每个数据点分配到最近的质心
     */
    private static void assignClusters(DataPoint[] dataPoints, double[][] centroids) {
        for (DataPoint point : dataPoints) {
            double minDistance = Double.MAX_VALUE;
            int closestCluster = 0;
            
            for (int i = 0; i < centroids.length; i++) {
                double distance = euclideanDistance(point.features, centroids[i]);
                if (distance < minDistance) {
                    minDistance = distance;
                    closestCluster = i;
                }
            }
            
            point.cluster = closestCluster;
        }
    }
    
    /**
     * 更新质心位置
     */
    private static double[][] updateCentroids(DataPoint[] dataPoints, int k) {
        if (dataPoints.length == 0) {
            throw new IllegalArgumentException("数据点不能为空");
        }
        
        int dimensions = dataPoints[0].getDimensions();
        double[][] newCentroids = new double[k][dimensions];
        int[] clusterCounts = new int[k];
        
        // 计算每个簇中所有点的坐标和
        for (DataPoint point : dataPoints) {
            for (int j = 0; j < dimensions; j++) {
                newCentroids[point.cluster][j] += point.features[j];
            }
            clusterCounts[point.cluster]++;
        }
        
        // 计算平均值
        for (int i = 0; i < k; i++) {
            if (clusterCounts[i] > 0) {
                for (int j = 0; j < dimensions; j++) {
                    newCentroids[i][j] /= clusterCounts[i];
                }
            }
        }
        
        return newCentroids;
    }
    
    /**
     * 检查质心是否收敛
     */
    private static boolean hasConverged(double[][] oldCentroids, double[][] newCentroids, double tolerance) {
        for (int i = 0; i < oldCentroids.length; i++) {
            if (euclideanDistance(oldCentroids[i], newCentroids[i]) > tolerance) {
                return false;
            }
        }
        return true;
    }
    
    /**
     * K-Means聚类主算法
     * 
     * @param dataPoints 数据点数组
     * @param k 簇的数量
     * @param maxIterations 最大迭代次数
     * @param tolerance 收敛阈值
     * @return 聚类后的数据点
     */
    public static DataPoint[] kMeans(DataPoint[] dataPoints, int k, int maxIterations, double tolerance) {
        if (dataPoints == null || dataPoints.length == 0) {
            throw new IllegalArgumentException("数据点不能为空");
        }
        if (k <= 0 || k > dataPoints.length) {
            throw new IllegalArgumentException("K值无效");
        }
        
        Random random = new Random(42); // 固定种子以保证可重复性
        
        // 初始化质心
        double[][] centroids = initializeCentroids(dataPoints, k, random);
        
        System.out.println("开始K-Means聚类，K=" + k);
        
        for (int iteration = 0; iteration < maxIterations; iteration++) {
            // 分配簇
            assignClusters(dataPoints, centroids);
            
            // 保存旧质心
            double[][] oldCentroids = centroids.clone();
            
            // 更新质心
            centroids = updateCentroids(dataPoints, k);
            
            // 检查是否收敛
            if (hasConverged(oldCentroids, centroids, tolerance)) {
                System.out.println("在第 " + (iteration + 1) + " 次迭代后收敛");
                break;
            }
            
            System.out.println("完成第 " + (iteration + 1) + " 次迭代");
        }
        
        return dataPoints;
    }
    
    /**
     * 打印聚类结果
     */
    public static void printResults(DataPoint[] dataPoints, int k) {
        System.out.println("\n===== 聚类结果 =====");
        
        // 按簇分组
        Map<Integer, List<DataPoint>> clusters = new HashMap<>();
        for (int i = 0; i < k; i++) {
            clusters.put(i, new ArrayList<>());
        }
        
        for (DataPoint point : dataPoints) {
            clusters.get(point.cluster).add(point);
        }
        
        // 打印每个簇的信息
        for (int i = 0; i < k; i++) {
            List<DataPoint> cluster = clusters.get(i);
            System.out.println("\n簇 " + i + " (包含 " + cluster.size() + " 个点):");
            for (DataPoint point : cluster) {
                System.out.print("  [");
                for (int j = 0; j < point.features.length; j++) {
                    System.out.printf("%.2f", point.features[j]);
                    if (j < point.features.length - 1) {
                        System.out.print(", ");
                    }
                }
                System.out.println("]");
            }
        }
    }
    
    /**
     * 示例使用
     */
    public static void main(String[] args) {
        // 创建示例数据点（2维数据）
        DataPoint[] dataPoints = {
            new DataPoint(new double[]{1.0, 2.0}),
            new DataPoint(new double[]{1.5, 1.8}),
            new DataPoint(new double[]{1.2, 2.1}),
            new DataPoint(new double[]{5.0, 8.0}),
            new DataPoint(new double[]{5.5, 8.2}),
            new DataPoint(new double[]{5.2, 7.8}),
            new DataPoint(new double[]{9.0, 1.0}),
            new DataPoint(new double[]{9.5, 1.2}),
            new DataPoint(new double[]{8.8, 0.8}),
            new DataPoint(new double[]{9.2, 1.5})
        };
        
        System.out.println("原始数据：");
        for (int i = 0; i < dataPoints.length; i++) {
            System.out.printf("点%d: [%.2f, %.2f]%n", i+1, 
                dataPoints[i].features[0], dataPoints[i].features[1]);
        }
        
        // 执行K-Means聚类，分为3个簇
        int k = 3;
        int maxIterations = 100;
        double tolerance = 0.0001;
        
        DataPoint[] result = kMeans(dataPoints, k, maxIterations, tolerance);
        
        // 打印结果
        printResults(result, k);
    }
}
