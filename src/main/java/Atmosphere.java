import static java.lang.Math.*;

public class Atmosphere {

    Atmosphere(){this.getAtmParams(InitData.getH());}
    private  int H0;     // геопотенциальная высота на нижней границе [м]

    private double
            T0;         // температура на нижней границе диапазона высот [K]
    private double b;   // градиент термодинамической температуры [б/р]
    private double p0;  // давление на нижней границе [Па]

    private double H;  // геопотенциальная высота [м]

    private double g;  // ускорение свободного падения на данной высоте [м^2/c^2]

    public double getH() {
        return H;
    }

    public double getG() {
        return g;
    }

    public double getT() {
        return T;
    }

    public double getP() {
        return p;
    }

    public double getRo() {
        return ro;
    }

    public double getA() {
        return a;
    }

    private double T;  // температура на данной высоте [K]
    private double p;  // давление [Па]
    private double ro; // плотность [кг / м^3]
    private double a;  // скорость звука (скорость распространения бесконечно малого возмущения в газе) [м / с]

    public void getAtmParams(double y) {
        int r = 6356767; // условный радиус Земли [м]
        this.H = ((double) r * y) / ((double) r + y);
        double g0 = 9.80665F;
        this.g = g0 * pow((double) r / (r + y), 2);

        this.updateParams(H); // обновляет T0, H0, p0, b для данной H

        this.T = (T0 + b * (H - (double) H0));

        double r1 = 287.053F; // универсальная газовая постоянная [Дж / (К*кмоль)]
        if (b != 0) {
            p = pow(10, log10(p0) - g0 * log10((T0 + b * (H - (double) H0)) / T0) / (b * r1));
        } else {
            p = pow(10, log10(p0) - 0.434294 * g0 * (H - (double) H0) / (r1 * T));
        }

        ro = p / (r1 * T);
        a = 20.0467 * sqrt(T);

        H = round(H);
        g = round(g * 10000) / 10000;
    }


    private void updateParams(double H) {
        if (H < -2000 || H >= 94000) {
            System.out.println("Altitude must be in [-2000, 95000]\nH: " + H + "\n");
        } else if (H < 0) {
            T0 = 301.15;
            b = -0.0065;
            H0 = -2000;
            p0 = 1.27774E5;
        } else if (H < 11000) {
            T0 = 288.15;
            b = -0.0065;
            H0 = 0;
            p0 = 1.01325E5;
        } else if (H < 20000) {
            T0 = 216.65;
            b = 0;
            H0 = 11000;
            p0 = 2.26320E4;
        } else if (H < 32000) {
            T0 = 216.650;
            b = 0.001;
            H0 = 20000;
            p0 = 5.47487E3;
        } else if (H < 47000) {
            T0 = 228.65;
            b = 0.0028;
            H0 = 32000;
            p0 = 8.68014E2;
        } else if (H < 51000) {
            T0 = 270.65;
            b = 0;
            H0 = 47000;
            p0 = 1.10906E2;
        } else if (H < 71000) {
            T0 = 270.65;
            b = -0.0028;
            H0 = 51000;
            p0 = 6.69384E1;
        } else if (H < 85000) {
            T0 = 214.65;
            b = -0.0020;
            H0 = 71000;
            p0 = 3.95639;
        } else {
            T0 = 186.65;
            b = 0;
            H0 = 85000;
            p0 = 3.63702E-1;
        }
    }

}
