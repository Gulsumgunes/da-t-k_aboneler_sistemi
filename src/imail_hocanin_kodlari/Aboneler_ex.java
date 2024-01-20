 package imail_hocanin_kodlari;
// import java.util.ArrayList;
// import java.util.List;
// import java.io.Serializable;

// public class Aboneler_ex implements Serializable {
//     private static final long serialVersionUID = 1L;

//     Aboneler_ex() {
//         lastUpdatedEpochMiliSeconds = 0;
//         abonelerListesi = new ArrayList<Boolean>();
//         girisYapanlarListesi = new ArrayList<Boolean>();
//     }

//     public long getEpochMiliSeconds() {
//         return lastUpdatedEpochMiliSeconds;
//     }

//     public void setEpochMiliSeconds(long lastUpdatedEpochMiliSeconds) {
//         this.lastUpdatedEpochMiliSeconds = lastUpdatedEpochMiliSeconds;
//     }

//     long lastUpdatedEpochMiliSeconds;

//     public List<Boolean> getAboneler() {
//         return abonelerListesi;
//     }

//     public void setAboneler(List<Boolean> aboneler) {
//         abonelerListesi = aboneler;
//     }

//     List<Boolean> abonelerListesi;

//     public List<Boolean> getGirisYapanlarListesi() {
//         return girisYapanlarListesi;
//     }

//     public void setGirisYapanlarListesi(List<Boolean> girisYapanlarListesi) {
//         this.girisYapanlarListesi = girisYapanlarListesi;
//     }

//     List<Boolean> girisYapanlarListesi;
// }
import java.util.ArrayList;
import java.util.List;

public class Aboneler_ex {
     List<Boolean> aboneDurumu;
     List<Boolean> girisYapanlar;


    public Aboneler_ex(int aboneSayisi) {
        aboneDurumu = new ArrayList<>(aboneSayisi);
        girisYapanlar = new ArrayList<>(aboneSayisi);


        for (int i = 0; i < aboneSayisi; i++) {
            aboneDurumu.add(false);
            girisYapanlar.add(false);
        }
    }

    public boolean aboneOl(int istemciNo) {
        if (!aboneDurumu.get(istemciNo)) {
            aboneDurumu.set(istemciNo, true);

            return true;

        } else {
            return false; // Zaten abone olma durumu
        }
    }

    public boolean aboneligiIptalEt(int istemciNo) {
        if (aboneDurumu.get(istemciNo)) {
            aboneDurumu.set(istemciNo, false);
            return true;
        } else {
            return false; // Zaten abone değil durumu
        }
    }

    public boolean girisYap(int istemciNo) {
        if (istemciNo < getGirisYapanlar().size() && getAboneDurumu().get(istemciNo)) {
            getGirisYapanlar().set(istemciNo, true);
            return true;
        } else {
            return false; // Abone değil durumu
        }
    }

    public boolean cikisYap(int istemciNo) {
        if (girisYapanlar.get(istemciNo)) {
            girisYapanlar.set(istemciNo, false);
            return true;
        } else {
            return false; // Giriş yapmamış olma durumu
        }
    }

    public List<Boolean> getAboneDurumu() {
        return aboneDurumu;
    }

    public void setAboneDurumu(List<Boolean> aboneler) {
        this.aboneDurumu = aboneler;
    }

    public List<Boolean> getGirisYapanlar() {
        return girisYapanlar;
    }

    public void setGirisYapanlar(List<Boolean> girisYapanlar) {
        this.girisYapanlar = girisYapanlar;
    }

    public synchronized void printAbonelerListesi() {
        System.out.println("Aboneler Listesi:");
        for (int i = 0; i < aboneDurumu.size(); i++) {
            System.out.println("Abone " + (i + 1) + ": " + aboneDurumu.get(i));
        }
    }

}



