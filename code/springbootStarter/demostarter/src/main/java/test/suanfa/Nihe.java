package test.suanfa;

import java.util.*;

@SuppressWarnings("all")
public class Nihe {

    /**
     * @param args
     */
    public static void main(String[] args) {
        // TODO Auto-generated method stub
        int n, m, i, j, k;
        System.out.println("输入x的个数");
        Scanner sc = new Scanner(System.in);
        n = sc.nextInt();
        double a[] = new double[n];
        double b[] = new double[n];
        System.out.println("输入x");
        Scanner str1 = new Scanner(System.in);
        for (i = 0; i < n; i++)
            a[i] = str1.nextDouble();// 数组a存储x的值
        System.out.println("输入y");
        Scanner str2 = new Scanner(System.in);
        for (i = 0; i < n; i++)
            b[i] = str2.nextDouble();// 数组b存储y的值
        double sumx = 0;
        double sumy = 0;
        for (i = 0; i < n; i++) {
            sumx += a[i];// x的类和；
            sumy += b[i];// y的类和；
        }
        // System.out.println("x的类和"+sumx);
        // System.out.println("y的类和"+sumy);
        System.out.println("输入拟合次数");
        Scanner str3 = new Scanner(System.in);
        m = str3.nextInt();
        int s = 2 * m;
        double Sumx[] = new double[s];
        for (i = 0; i < s; i++) {
            double sum = 0;
            for (j = 0; j < n; j++) {
                double r = 1;
                for (k = 0; k <= i; k++)
                    r = r * a[j];
                sum += r;
            }

            Sumx[i] = sum;
        }
        /*
         * for(i=0;i<s;i++){ System.out.print(Sumx[i]+"  "); }
         */
        // System.out.println();
        double Sumxy[] = new double[m];
        for (i = 0; i < m; i++) {
            double sumxy = 0;
            for (j = 0; j < n; j++) {
                double p = 1;
                double w = 0;
                for (k = 0; k <= i; k++)
                    p = p * a[j];
                w = p * b[j];
                sumxy += w;
            }
            Sumxy[i] = sumxy;
        }
        /*
         * for(i=0;i<m;i++){ System.out.print(Sumxy[i]+"  "); }
         */
        // System.out.println();
        int t = m + 1;
        int q = m + 2;
        double A[][] = new double[t][q];
        A[0][0] = n;
        A[0][q - 1] = sumy;
        for (j = 1; j < q - 1; j++)
            A[0][j] = Sumx[j - 1];
        for (i = 1; i < t; i++)
            for (j = 0; j < q - 1; j++)
                A[i][j] = Sumx[i + j - 1];
        for (i = 1; i < t; i++)
            A[i][q - 1] = Sumxy[i - 1];
        /*
         * for (i = 0; i < t; i++) { int count1 = 0; for (j= 0; j < q;j++) {
         * System.out.print(A[i][j] + "  "); count1++; if (count1 == q)
         * System.out.println(); } }
         */
        for (k = 0; k < t; k++) {
            for (i = k + 1; i < t; i++) {
                double L = A[i][k] / A[k][k];
                for (j = 0; j < q; j++)
                    A[i][j] = A[i][j] - L * A[k][j];
            }
        }
        for (k = t - 1; k >= 0; k--) {
            for (i = k - 1; i >= 0; i--) {
                double L = A[i][k] / A[k][k];
                for (j = q - 1; j >= k; j--)
                    A[i][j] = A[i][j] - L * A[k][j];
            }
        } // 求多项式的系数
        /*
         * for (i = 0; i < t; i++) { int count1 = 0; for (j= 0; j < q;j++) {
         * System.out.print(A[i][j] + "  "); count1++; if (count1 == q)
         * System.out.println(); } }
         */
        double r[] = new double[t];
        for (i = 0; i < t; i++) {
            r[i] = A[i][q - 1] / A[i][i];
            // System.out.println("x"+i+"="+r[i]);
        }
        double x;
        System.out.println("输入计算的值");
        Scanner sc1 = new Scanner(System.in);
        x = sc1.nextDouble();

        double SUM = 0;
        double Z[] = new double[t];
        for (i = 0; i < t; i++) {
            double z = 1;
            for (j = 1; j <= i; j++)
                z = z * x;
            Z[i] = z * r[i];

        }
        for (i = 0; i < t; i++)
            SUM += Z[i];
        System.out.println("f(" + x + ")" + "=" + SUM);
    }
}
