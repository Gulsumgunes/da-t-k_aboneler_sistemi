import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Aboneler implements Serializable {
    private static final long serialVersionUID = 1L;

    Aboneler() {
        lastUpdatedEpochMiliSeconds = 0;
        abonelerListesi = new ArrayList<>();
        girisYapanlarListesi = new ArrayList<>();
        initializeLists();
    }

    private void initializeLists() {
        for (int i = 0; i < 3; i++) {
            abonelerListesi.add(false); // Varsayılan olarak abone değiller
            girisYapanlarListesi.add(false); // Varsayılan olarak giriş yapmamışlar
        }
    }


    private long lastUpdatedEpochMiliSeconds;
    private List<Boolean> abonelerListesi;
    private List<Boolean> girisYapanlarListesi;

    public long getEpochMiliSeconds() {
        return lastUpdatedEpochMiliSeconds;
    }

    public void setEpochMiliSeconds(long lastUpdatedEpochMiliSeconds) {
        this.lastUpdatedEpochMiliSeconds = lastUpdatedEpochMiliSeconds;
    }

    public List<Boolean> getAboneler() {
        return abonelerListesi;
    }

    public void setAboneler(List<Boolean> aboneler) {
        this.abonelerListesi = aboneler;
    }

    public List<Boolean> getGirisYapanlarListesi() {
        return girisYapanlarListesi;
    }

    public void setGirisYapanlarListesi(List<Boolean> girisYapanlarListesi) {
        this.girisYapanlarListesi = girisYapanlarListesi;
    }

    // Senkronize abone durumu güncelleme
    public void updateAboneDurumu(int aboneNum, boolean durum) {

        abonelerListesi.set(aboneNum - 1, durum);
    }

    public void updateGirisDurumu(int girisNum, boolean durum) {
        girisYapanlarListesi.set(girisNum - 1, durum);
    }



    // Yeni metod: Aboneler listesini ekrana yazdırma
    public synchronized void printAbonelerListesi() {
        System.out.println("Aboneler Listesi:");
        for (int i = 0; i < abonelerListesi.size(); i++) {
            System.out.println("Abone " + (i + 1) + ": " + abonelerListesi.get(i));
        }
    }





}    // Senkronize giriş durumu güncelleme

