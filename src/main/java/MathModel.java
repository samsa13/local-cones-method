/**
 * Все зависимости для интегрирования (Метод кардано и Метод местных конусов)
 */
public class MathModel {
    private static final Atmosphere atm = new Atmosphere();
    public static final double R = 287.05287;
    public static final double k = 1.4;

    /**
     * считает угол СУ по методу Кардано от Голубева
     * @param beta -  profile angle in radian
     * @return teta_c
     */
    public static double calcCardanoMethod(double beta) {
        double mach = InitData.getM_inf();
        double a1 = ((k - 1) / 2 * Math.pow(mach, 2) + 1) * Math.tan(beta);
        double b1 = 1 - Math.pow(mach, 2);
        double c1 = Math.tan(beta) * (1 + (k + 1) / 2 * Math.pow(mach, 2));
        double a = b1 / a1;
        double b = c1 / a1;
        double c = 1 / a1;
        double p = -Math.pow(a, 2) / 3 + b;
        double q = 2 * Math.pow(a / 3, 3) - a * b / 3 + c;
        double Q = Math.pow(p / 3, 3) + Math.pow(q / 2, 2);
        double x;
        if (Q < 0) {
            double alpha = Math.acos(q / (2 * Math.sqrt(-Math.pow(p / 3, 3))));
            x = 2 * Math.sqrt(-p / 3) * Math.cos(alpha / 3 + Math.PI / 3) - a / 3;
        } else if (Q == 0) {
            x = Math.pow(-q / 2, (double) 1 / 3) - a / 3;
        } else {
            return 0; // если получается 0 то нужно менять диапазон в меньших пределах, у тебя срыв потока
        }
        return Math.atan(x);
    }


    public static double dV_r_dteta(double V_teta) {
        return V_teta;
    }

    public static double dV_teta_dteta(double V_teta, double V_r, double teta) {
        double sqr_a =  getSqrSoundVelocity(V_teta, V_r);
        return (-V_teta / Math.tan(teta) + V_r * (Math.pow(V_teta, 2) / sqr_a - 2))
                / (1 - Math.pow(V_teta, 2) / sqr_a);
    }


    public static double getSqrSoundVelocity(double V_teta, double V_r) {
        double sqr_a_cr = getSqrACritical();
        return (k + 1) / 2 * sqr_a_cr - (k - 1) / 2 * (Math.pow(V_r, 2) + Math.pow(V_teta, 2));
    }

    public static double getSqrACritical() {
        return 2 * k / (k + 1) * R * calcTotalTemperature();
    }

    private static double calcTotalTemperature() {
        return atm.getT() / Math.pow(1 + (k - 1) / 2 * Math.pow(InitData.getM_inf(), 2), -1);
    }

    public static double getV_c_r(double teta_c) {
        return InitData.getV_inf() * Math.cos(teta_c);
    }

    public static double getV_c_teta(double V_c_r, double teta_c) {
        return -V_c_r * (2 + (k - 1) * Math.pow(InitData.getM_inf(), 2) * Math.pow(Math.sin(teta_c), 2)) /
                ((k + 1) * Math.pow(InitData.getM_inf(), 2) * Math.pow(Math.sin(teta_c), 2)) * Math.tan(teta_c);
    }


    public static double getT_k(double V_k) {
        double T_0 = calcTotalTemperature();
        return T_0 * (1 - Math.pow(V_k / getV_max(), 2));
    }

    public static double getA_k(double T_k) {
        return Math.sqrt(k * R * T_k);
    }

    private static double getV_max() {
        double T_0 = calcTotalTemperature();
        return Math.sqrt(2 * k / (k - 1) * R * T_0);
    }

    public static double getM_k(double V_k, double a_k) {
        return V_k / a_k;
    }

    private static double getTotalPressureLoss(double teta_c) {
        double sin_sqr_teta_c = Math.pow(Math.sin(teta_c), 2);
        double sqr_M_inf = Math.pow(InitData.getM_inf(), 2);
        return atm.getP() * Math.pow((k + 1) / (2 * k * sqr_M_inf * sin_sqr_teta_c - (k - 1)), 1 / (k - 1)) *
                Math.pow((k + 1) * sqr_M_inf * sin_sqr_teta_c * ((k - 1) * sqr_M_inf + 2) / (2 * (2 + (k - 1) * sqr_M_inf * sin_sqr_teta_c)), k / (k - 1));
    }

    public static double getP_k(double teta_c, double M_k) {
        double p_0_sht = getTotalPressureLoss(teta_c);
        return p_0_sht * Math.pow(1 + (k - 1) / 2 * Math.pow(M_k, 2), -k / (k - 1));
    }

    /**
     * Отдает коэффициент давления из обычного статического
     * @param p - статическое давление
     * @return p с чертой
     */
    public static double getP_ch(double p) {
        return (p - atm.getP()) / (atm.getRo() * Math.pow(InitData.getM_inf() * atm.getA(), 2) / 2);
    }

    public static double getRo_k(double p_k, double T_k) {
        return p_k / (R * T_k);
    }

    public static double getBeta_sht(double alpha, double gamma) {
        return InitData.getBeta_k() - alpha * Math.cos(gamma) - 0.5 * Math.pow(alpha, 2) / Math.tan(InitData.getBeta_k()) * Math.pow(Math.sin(gamma), 2);
    }

    public static double getP_d() {
        return atm.getP() * (1 / InitData.getM_inf() - 0.1);
    }

    /**
     * Перевод в градусы из радиан
     * @param rad - угол в радианах
     * @return - угол в градусах
     */

    public static double toGrad(double rad) {
        return rad * 180 / Math.PI;
    }

    /**
     * Считает состояние на поверхности конуса по методу местных конусов.
     * Ищет угол СУ последовательным приближением из условия, что для данного угла СУ на поверхности конуса
     * (для получившегося угла полураствора конуса beta = beta_k) нормальная скорость V_teta должна обратиться в 0.
     * @param beta_k - угол полураствора конуса
     * @param alpha - угол атаки
     * @return - объект состояния с посчитанными скоростями
     */

    public static State getStateOnCone(double beta_k, double alpha) {
        double teta_c = 0.8 * MathModel.calcCardanoMethod(beta_k);
        if (teta_c == 0) {
            System.out.println("Decrease alpha!");
        }
        State state = new State(teta_c, beta_k, alpha);
        State old_state = new State();
        double beta = teta_c;
        while (Math.abs(beta - beta_k) > 0.001) {
            if (beta - beta_k > 0){
                teta_c -= 0.0001;
            } else {
                teta_c += 0.00001;
            }

            state = new State(teta_c, beta_k, alpha);
            while (Math.abs(state.V_teta) > 0.0001) {
                old_state.setNewState(state);
               // double V_r = state.V_r;
                state.V_r += MathModel.dV_r_dteta(state.V_teta) * state.d_teta;
                state.V_teta += MathModel.dV_teta_dteta(state.V_teta, state.V_r, state.teta) * state.d_teta;
                state.teta += state.d_teta;
                if (state.V_teta > 0) {
                    state.setNewState(old_state);
                    state.d_teta = -state.V_teta * Math.pow(MathModel.dV_teta_dteta(state.V_teta, state.V_r, state.teta), -1);
                    //V_r = state.V_r;
                    state.V_r += MathModel.dV_r_dteta(state.V_teta) * state.d_teta;
                    state.V_teta += MathModel.dV_teta_dteta(state.V_teta, state.V_r, state.teta) * state.d_teta;
                    state.teta += state.d_teta;
                }
            }
            beta = state.teta;
        }
        state.teta_c = teta_c;
        return state;
    }
}
