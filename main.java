import java.util.ArrayList;
import java.util.Scanner;
import java.io.*;
import java.text.NumberFormat;

// ===== Bagian 1: Template menu =====
// Kelas abstrak
abstract class Menuitem {
    private String nama;
    private double harga;
    private String kategori;

    public Menuitem(String nama, double harga, String kategori) {
        this.nama = nama;
        this.harga = harga;
        this.kategori = kategori;
    }

    public abstract void tampilMenu(int nomorMenu);

    public String getNama() {
        return nama;
    }

    public double getHarga() {
        return harga;
    }

    public String getKategori() {
        return kategori;
    }

    public double getHargaAkhir() {
        return harga;
    }

    public String getInfoStruk() {
        return "";
    }

    public String getInfoFile() {
        return getKategori() + "," + getNama() + "," + getHarga();
    }
}

// ===== Bagian 2: Jenis Menu =====
class Makanan extends Menuitem {
    private String jenisMakanan;

    public Makanan(String nama, double harga, String jenisMakanan) {
        super(nama, harga, "Makanan");
        this.jenisMakanan = jenisMakanan;
    }

    @Override
    public void tampilMenu(int nomorMenu) {
        System.out.println(nomorMenu + ". Nama: " + getNama());
        System.out.println("   Kategori: " + getKategori());
        System.out.println("   Jenis: " + jenisMakanan);
        System.out.println("   Harga: Rp" + getHarga());
    }
}

class Minuman extends Menuitem {
    private String jenisMinuman;

    public Minuman(String nama, double harga, String jenisMinuman) {
        super(nama, harga, "Minuman");
        this.jenisMinuman = jenisMinuman;
    }

    @Override
    public void tampilMenu(int nomorMenu) {
        System.out.println(nomorMenu + ". Nama: " + getNama());
        System.out.println("   Kategori: " + getKategori());
        System.out.println("   Jenis: " + jenisMinuman);
        System.out.println("   Harga: Rp" + getHarga());
    }
}

class Diskon extends Menuitem {
    private double diskon;

    public Diskon(String nama, double harga, double diskon) {
        super(nama, harga, "Diskon");
        this.diskon = diskon;
    }

    @Override
    public void tampilMenu(int nomorMenu) {
        System.out.println(nomorMenu + ". Nama promo: " + getNama());
        System.out.println("   Harga normal: Rp" + getHarga());
        System.out.println("   Diskon: " + (diskon * 100) + "%");
        System.out.println("   Harga setelah diskon: Rp" + (getHarga() * (1 - diskon)));
    }

    @Override
    public double getHargaAkhir() {
        return getHarga() * (1 - diskon);
    }

    @Override
    public String getInfoStruk() {
        return " (Promo: -" + (diskon * 100) + "%)";
    }

    @Override
    public String getInfoFile() {
        return getKategori() + "," + getNama() + "," + getHarga() + "," + (diskon * 100) + "%," + getHargaAkhir();
    }
}

// ===== Bagian 3: Pengelolaan Menu =====
class Menu {
    private ArrayList<Menuitem> daftarMenu;

    public Menu() {
        daftarMenu = new ArrayList<>();
    }

    public void tambahItem(Menuitem item) {
        daftarMenu.add(item);
    }

    public void tampilkanMenu() {
        System.out.println("\n=== MENU RESTORAN ===");
        for (int i = 0; i < daftarMenu.size(); i++) {
            daftarMenu.get(i).tampilMenu(i + 1);
            System.out.println("====================================");
        }
    }

    public Menuitem getItem(int index) throws IndexOutOfBoundsException {
        return daftarMenu.get(index);
    }

    public void simpanKeFile(String namaFile) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(namaFile))) {
            for (Menuitem item : daftarMenu) {
                writer.println(item.getInfoFile());
            }
            System.out.println("Menu berhasil disimpan ke file: " + namaFile);
        } catch (IOException e) {
            System.out.println("Error menyimpan menu: " + e.getMessage());
        }
    }
}

// ===== Bagian 4: Pengelolaan Pesanan =====
class Pesanan {
    private ArrayList<Menuitem> itemPesanan;
    private ArrayList<Integer> jumlahPesanan;
    private double totalHarga;

    public Pesanan() {
        itemPesanan = new ArrayList<>();
        jumlahPesanan = new ArrayList<>();
        totalHarga = 0;
    }

    public void tambahPesanan(Menuitem item, int jumlah) {
        itemPesanan.add(item);
        jumlahPesanan.add(jumlah);
        totalHarga += item.getHargaAkhir() * jumlah;
    }

    public void tampilkanStruk() {
        NumberFormat formatter = NumberFormat.getInstance();
        System.out.println("\n=== STRUK PESANAN ===");
        for (int i = 0; i < itemPesanan.size(); i++) {
            Menuitem item = itemPesanan.get(i);
            int jumlah = jumlahPesanan.get(i);
            System.out.println(item.getNama() + " x" + jumlah + item.getInfoStruk() + " = Rp"
                    + formatter.format(item.getHargaAkhir() * jumlah));
        }
        System.out.println("Total: Rp" + formatter.format(totalHarga));
    }

    public void simpanStruk(String namaFile) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(namaFile))) {
            writer.println("=== STRUK PESANAN ===");
            for (int i = 0; i < itemPesanan.size(); i++) {
                Menuitem item = itemPesanan.get(i);
                int jumlah = jumlahPesanan.get(i);
                writer.println(item.getNama() + " x" + jumlah + item.getInfoStruk() + " = Rp"
                        + (item.getHargaAkhir() * jumlah));
            }
            writer.println("Total: Rp" + totalHarga);
            System.out.println("Struk berhasil disimpan ke file: " + namaFile);
        } catch (IOException e) {
            System.out.println("Error menyimpan struk: " + e.getMessage());
        }
    }
}

// ===== Bagian 5: Program Utama =====
public class main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Menu menu = new Menu();

        while (true) {
            System.out.println("\n==== SISTEM MANAJEMEN RESTORAN ====");
            System.out.println("1. Tambah Item Menu");
            System.out.println("2. Tampilkan Menu");
            System.out.println("3. Buat Pesanan");
            System.out.println("4. Simpan Menu ke File");
            System.out.println("5. Keluar");
            System.out.print("Pilih menu (1-5): ");

            int pilihan = scanner.nextInt();
            scanner.nextLine();

            switch (pilihan) {
                case 1:
                    System.out.println("\nJenis item:");
                    System.out.println("1. Makanan");
                    System.out.println("2. Minuman");
                    System.out.println("3. Diskon");
                    System.out.print("Pilih jenis (1-3): ");
                    int jenis = scanner.nextInt();
                    scanner.nextLine();

                    System.out.print("Nama Item: ");
                    String nama = scanner.nextLine();
                    System.out.print("Harga: ");
                    double harga = scanner.nextDouble();
                    scanner.nextLine();

                    switch (jenis) {
                        case 1:
                            System.out.print("Jenis Makanan: ");
                            String jenisMakanan = scanner.nextLine();
                            menu.tambahItem(new Makanan(nama, harga, jenisMakanan));
                            break;
                        case 2:
                            System.out.print("Jenis Minuman: ");
                            String jenisMinuman = scanner.nextLine();
                            menu.tambahItem(new Minuman(nama, harga, jenisMinuman));
                            break;
                        case 3:
                            System.out.print("Persentase diskon (1-100): ");
                            double diskon = scanner.nextDouble() / 100.0;
                            menu.tambahItem(new Diskon(nama, harga, diskon));
                            break;
                        default:
                            System.out.println("Jenis item tidak valid!");
                    }
                    break;

                case 2:
                    menu.tampilkanMenu();
                    break;

                case 3:
                    Pesanan pesanan = new Pesanan();
                    boolean lanjutPesan = true;

                    while (lanjutPesan) {
                        menu.tampilkanMenu();
                        System.out.print("\nPilih nomor menu (0 untuk selesai): ");
                        int nomorMenu = scanner.nextInt();

                        if (nomorMenu == 0) {
                            lanjutPesan = false;
                        } else {
                            try {
                                Menuitem item = menu.getItem(nomorMenu - 1);
                                System.out.print("Masukkan jumlah pesanan: ");
                                int jumlah = scanner.nextInt();
                                pesanan.tambahPesanan(item, jumlah);
                                System.out.println("Item ditambahkan ke pesanan!");
                            } catch (IndexOutOfBoundsException e) {
                                System.out.println("Nomor menu tidak valid!");
                            }
                        }
                    }

                    pesanan.tampilkanStruk();
                    System.out.print("Simpan struk ke file? (y/n): ");
                    char simpanStruk = scanner.next().toLowerCase().charAt(0);
                    if (simpanStruk == 'y') {
                        System.out.print("Nama file: ");
                        String namaFileStruk = scanner.next();
                        pesanan.simpanStruk(namaFileStruk);
                    }
                    break;

                case 4:
                    System.out.print("Nama file untuk menyimpan menu: ");
                    String namaFileMenu = scanner.next();
                    menu.simpanKeFile(namaFileMenu);
                    break;

                case 5:
                    System.out.println("Terima kasih telah menggunakan sistem ini!");
                    return;

                default:
                    System.out.println("Pilihan tidak valid!");
            }
        }
    }
}