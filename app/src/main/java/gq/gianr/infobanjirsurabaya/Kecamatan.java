package gq.gianr.infobanjirsurabaya;

/**
 * Created by j on 09/05/2017.
 */

public class Kecamatan {
    String id, kecamatan, geo;

    public Kecamatan() {
    }

    public Kecamatan(String id, String kecamatan, String geo) {
        this.id = id;
        this.kecamatan = kecamatan;
        this.geo = geo;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getKecamatan() {
        return kecamatan;
    }

    public void setKecamatan(String kecamatan) {
        this.kecamatan = kecamatan;
    }

    public String getGeo() {
        return geo;
    }

    public void setGeo(String geo) {
        this.geo = geo;
    }
}
