package ASUP;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Abone implements Serializable {
    private static final long serialVersionUID = 1L;

    long lastUpdatedEpochMiliSeconds;
    private List<Boolean> abonelerList;
    private List<Boolean> girisYapanlarList;

    Abone() {
        abonelerList = new ArrayList<Boolean>();
        girisYapanlarList = new ArrayList<Boolean>();
        lastUpdatedEpochMiliSeconds = 0;
    }

    public long getEpochMiliSeconds() {
        return lastUpdatedEpochMiliSeconds;
    }

    public void setEpochMiliSeconds(long lastUpdatedEpochMiliSeconds) {
        this.lastUpdatedEpochMiliSeconds = lastUpdatedEpochMiliSeconds;
    }

    public List<Boolean> getAbonelerList() {
        return abonelerList;
    }

    public List<Boolean> getGirisYapanlarList() {
        return girisYapanlarList;
    }

    public void setAboneler(List<Boolean> aboneler) {
        abonelerList = aboneler;
    }


    public void setGirisYapanlarList(List<Boolean> girisYapanlarListesi) {
        this.girisYapanlarList = girisYapanlarListesi;
    }
}

