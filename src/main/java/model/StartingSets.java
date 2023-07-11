package model;

import java.util.ArrayList;

public enum StartingSets {
    set1 (new double[] {272.88584111337985, 192.05673650406243, 397.35280589207696, 396.2984103636479, 263.15476510906797}, new double[] {286.6059209794237, 158.8280405761159, 128.79032137437707, 233.16593045771384, 76.35431188017495}),
    set2 (new double[] {352.908764581133 , 192.05673650406243, 397.35280589207696, 396.2984103636479, 263.15476510906797}, new double[] {83.56005687217434, 158.8280405761159, 128.79032137437707, 233.16593045771384, 76.35431188017495}),
    set3 (new double[] {272.88584111337985, 359.1029569181504 , 397.35280589207696, 396.2984103636479, 352.908764581133}, new double[] {286.6059209794237, 272.77305903941755, 128.79032137437707, 233.16593045771384, 83.56005687217434});
    private final ArrayList<Double> x = new ArrayList<>();
    private final ArrayList<Double> y = new ArrayList<>();
    StartingSets (double[] x, double[] y) {
        for (double v : x) {
            this.x.add(v);
        }
        for (double v : y) {
            this.y.add(v);
        }
    }
    
    public ArrayList<Double> getY () {
        return y;
    }
    
    public ArrayList<Double> getX () {
        return x;
    }
}
