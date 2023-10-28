public class State {

    public State(double teta_c, double beta_k, double alpha) {
        this.alpha = alpha;
        this.beta_k = beta_k;
        this.teta = teta_c;
        this.V_r = MathModel.getV_c_r(teta_c);
        this.V_teta = MathModel.getV_c_teta(this.V_r, teta_c);
    }

    public State(){};

    public double alpha;

    public double V_r;
    public double V_teta;
    public double teta; //в конце равна углу раствора конуса (мнимого конуса)
    public double beta_k;
    public double teta_c;
    public double d_teta = -0.01;//мб стоит сделать меньше
    public double V_k;
    public double T_k;
    public double a_k;
    public double M_k;
    public double p_k;
    public double p_k_ch;
    public double ro_k;

    public double Cxp = 0;
    public double Cx = Math.abs(MathModel.getP_ch(MathModel.getP_d()));
    public double Cy = 0;
    public double mz = 0;
    public double Cxa = 0;
    public double Cya = 0;
    public double K = 0;
    public double Xcd_ch = 0;

    /**
     * в java объекты не копируются, а присваиваются, поэтому необходимо присваивать поля.
     * @param newState
     */
    public void setNewState(State newState){
        this.teta = newState.teta;
        this.V_teta = newState.V_teta;
        this.V_r = newState.V_r;
    }


    public void countParamsOnCone() {
        this.V_k = Math.sqrt(Math.pow(this.V_teta, 2) + Math.pow(this.V_r, 2));
        this.T_k = MathModel.getT_k(this.V_k);
        this.a_k = MathModel.getA_k(this.T_k);
        this.M_k = MathModel.getM_k(this.V_k, this.a_k);
        this.p_k = MathModel.getP_k(this.teta_c, this.M_k);
        this.p_k_ch = MathModel.getP_ch(this.p_k);
        this.ro_k = MathModel.getRo_k(this.p_k, this.T_k);
    }

    public void setCoefValues(double Cx, double Cy, double mz) {
        this.Cx += Cx;
        this.Cxp = Cx;
        this.Cy = Cy;
        this.mz = mz;
        this.Cxa = this.Cx * Math.cos(this.alpha) - this.Cy * Math.sin(this.alpha);
        this.Cya = this.Cx * Math.sin(this.alpha) + this.Cy * Math.cos(this.alpha);
        this.K = this.Cy / this.Cx;
        this.Xcd_ch = -this.mz / this.Cy;
    }


}
