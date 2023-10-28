
public class Main {

    /**
     * teta_k = 36.253200
     * M_k = 2.390781
     * CXP = 0.5493184
     */
    public static void main(String[] args) {
        double delta_alpha = 0.3 / 4 * InitData.getBeta_k(); // нужно менять в зависимости от диапазона изменения углов атаки (по дефолту до 0.8 beta_k но мне пришлось его уменьшать)
        double delta_gamma = Math.PI / 180;  //1 grad в радианах
        double delta_gamma_ch = delta_gamma / Math.PI; // дельта гамма с чертой
        ExelData exel = new ExelData();
        exel.addNewTableTitle();
        for (double alpha = 0; alpha <= 0.3 * InitData.getBeta_k(); alpha += delta_alpha) {
            State state = null;
            double Cx = 0;
            double Cy = 0;
            double mz = 0;
            for (double gamma = 0; gamma <= Math.PI; gamma += delta_gamma) {
                double beta_k = MathModel.getBeta_sht(alpha, gamma);
                state = MathModel.getStateOnCone(beta_k, alpha);
                state.countParamsOnCone();
                Cx += state.p_k_ch * delta_gamma_ch;
                Cy += -1 / Math.tan(state.beta_k) * state.p_k_ch * Math.cos(gamma) * delta_gamma_ch;
                mz += (double) 4 / 3 / Math.sin(2 * state.beta_k) * state.p_k_ch * Math.cos(gamma) * delta_gamma_ch;

            }
            // Расчет teta_c по приближенной зависимости (опционально)
            if (alpha == 0){
                double teta_c_pr = Math.asin(Math.sqrt((1 / Math.pow(InitData.getM_inf(), 2)) + 0.5 * (1.4 + 1) * Math.pow(Math.sin(InitData.getBeta_k()), 2)));
                System.out.println(String.format("teta_c приб = %s", MathModel.toGrad(teta_c_pr)));
            }

            state.setCoefValues(Cx, Cy, mz);
            exel.addDataToExel(state);
        }
        exel.writeDataToSheet();
        exel.writeExelDataFile("results for " + InitData.N + " var");
    }
}