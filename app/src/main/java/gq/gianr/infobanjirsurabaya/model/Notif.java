package gq.gianr.infobanjirsurabaya.model;

/**
 * Created by j on 18/07/2017.
 */

public class Notif {
    int _id;
    String _tgl;
    String _chj;
    String _now;

    public Notif() {
    }

    public Notif(int _id, String _tgl, String _chj, String _now) {
        this._id = _id;
        this._tgl = _tgl;
        this._chj = _chj;
        this._now = _now;
    }

    public Notif(String _tgl, String _chj, String _now) {
        this._tgl = _tgl;
        this._chj = _chj;
        this._now = _now;
    }

    public String get_now() {
        return _now;
    }

    public void set_now(String _now) {
        this._now = _now;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String get_tgl() {
        return _tgl;
    }

    public void set_tgl(String _tgl) {
        this._tgl = _tgl;
    }

    public String get_chj() {
        return _chj;
    }

    public void set_chj(String _chj) {
        this._chj = _chj;
    }
}
