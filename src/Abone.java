import java.util.ArrayList;
import java.util.List;

public class Abone {
    private List<Boolean> aboneDurumu;
    private List<Boolean> girisYapanlar;

    public Abone(int aboneSayisi) {
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
        if (aboneDurumu.get(istemciNo)) {
            girisYapanlar.set(istemciNo, true);
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

