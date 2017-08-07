package gq.gianr.infobanjirsurabaya.model;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by j on 19/07/2017.
 */

public class Fuzzy {
    int id, kecamatan_id;
    String kecamatan;
    String hasil;
    LatLng[] geo;
    NilaiFuzzy banjir, chj, kp, dra;

    public Fuzzy() {
    }

    public Fuzzy(int id, int kecamatan_id, String kecamatan, String hasil, LatLng[] geo, NilaiFuzzy banjir, NilaiFuzzy chj, NilaiFuzzy kp, NilaiFuzzy dra) {
        this.id = id;
        this.kecamatan_id = kecamatan_id;
        this.kecamatan = kecamatan;
        this.hasil = hasil;
        this.geo = geo;
        this.banjir = banjir;
        this.chj = chj;
        this.kp = kp;
        this.dra = dra;
    }

    public Fuzzy(int kecamatan_id, String kecamatan, String hasil, LatLng[] geo, NilaiFuzzy banjir, NilaiFuzzy chj, NilaiFuzzy kp, NilaiFuzzy dra) {
        this.kecamatan_id = kecamatan_id;
        this.kecamatan = kecamatan;
        this.hasil = hasil;
        this.geo = geo;
        this.banjir = banjir;
        this.chj = chj;
        this.kp = kp;
        this.dra = dra;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getKecamatan_id() {
        return kecamatan_id;
    }

    public void setKecamatan_id(int kecamatan_id) {
        this.kecamatan_id = kecamatan_id;
    }

    public String getKecamatan() {
        return kecamatan;
    }

    public void setKecamatan(String kecamatan) {
        this.kecamatan = kecamatan;
    }

    public String getHasil() {
        return hasil;
    }

    public void setHasil(String hasil) {
        this.hasil = hasil;
    }

    public LatLng[] getGeo() {
        return geo;
    }

    public void setGeo(LatLng[] geo) {
        this.geo = geo;
    }

    public NilaiFuzzy getBanjir() {
        return banjir;
    }

    public void setBanjir(NilaiFuzzy banjir) {
        this.banjir = banjir;
    }

    public NilaiFuzzy getChj() {
        return chj;
    }

    public void setChj(NilaiFuzzy chj) {
        this.chj = chj;
    }

    public NilaiFuzzy getKp() {
        return kp;
    }

    public void setKp(NilaiFuzzy kp) {
        this.kp = kp;
    }

    public NilaiFuzzy getDra() {
        return dra;
    }

    public void setDra(NilaiFuzzy dra) {
        this.dra = dra;
    }

    public static class NilaiFuzzy {
        double nilai, nr, ns, nt;
        String kri;

        public NilaiFuzzy() {
        }

        public NilaiFuzzy(double nilai, double nr, double ns, double nt, String kri) {
            this.nilai = nilai;
            this.nr = nr;
            this.ns = ns;
            this.nt = nt;
            this.kri = kri;
        }

        public double getNilai() {
            return nilai;
        }

        public void setNilai(double nilai) {
            this.nilai = nilai;
        }

        public double getNr() {
            return nr;
        }

        public void setNr(double nr) {
            this.nr = nr;
        }

        public double getNs() {
            return ns;
        }

        public void setNs(double ns) {
            this.ns = ns;
        }

        public double getNt() {
            return nt;
        }

        public void setNt(double nt) {
            this.nt = nt;
        }

        public String getKri() {
            return kri;
        }

        public void setKri(String kri) {
            this.kri = kri;
        }
    }
}
