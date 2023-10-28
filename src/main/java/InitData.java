public class InitData {
    public static final int N = 14;
    private static final double M_inf_0 = 2.5;
    private static final double H_0 = 3.0;
    private static final double beta_k_0 = 10; // в градусах
    private static final int p = 3;
    public static double getH(){
        return (H_0 + 0.2 * N) * 1000;
    }
    public static double getM_inf(){
        return M_inf_0 + (p - 1);
    }
    public static double getBeta_k(){
        return (beta_k_0 + 2.5 * (N - 2 * p)) * Math.PI / 180;
    }
    public static double getV_inf(){
        Atmosphere atm = new Atmosphere();
        return getM_inf() * atm.getA();
    }


}
