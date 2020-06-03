package com.va181.saifulloh;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class DatabaseHandler extends SQLiteOpenHelper {

    private final static int DATABASE_VERSION = 2;
    private final static String DATABASE_NAME = "db_film";
    private final static String TABLE_FILM = "t_film";
    private final static String KEY_ID_FILM = "ID_Film";
    private final static String KEY_JUDUL = "Judul";
    private final static String KEY_TGL = "Tanggal";
    private final static String KEY_GAMBAR = "Gambar";
    private final static String KEY_AKTOR= "Aktor";
    private final static String KEY_GENRE = "Genre";
    private final static String KEY_SINOPSIS = "Sinopsis";
    private final static String KEY_LINK = "Link";
    private SimpleDateFormat sdFromat = new SimpleDateFormat("dd/MM/yyyy hh:mm", Locale.getDefault());
    private Context context;

    public DatabaseHandler(Context ctx) {
        super(ctx, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = ctx;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE_BERITA = "CREATE TABLE " + TABLE_FILM
                + "(" + KEY_ID_FILM + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + KEY_JUDUL + " TEXT, " + KEY_TGL + " DATE, "
                + KEY_GAMBAR + " TEXT, " + KEY_AKTOR + " TEXT, "
                + KEY_GENRE+ " TEXT, " + KEY_SINOPSIS + " TEXT, "
                + KEY_LINK + " TEXT);";

        db.execSQL(CREATE_TABLE_BERITA);
        inisialisasiFilmAwal(db);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_FILM;
        db.execSQL(DROP_TABLE);
        onCreate(db);

    }

    public void tambahFilm(Film dataFilm) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(KEY_JUDUL, dataFilm.getJudul());
        cv.put(KEY_TGL, sdFromat.format(dataFilm.getTanggal()));
        cv.put(KEY_GAMBAR, dataFilm.getGambar());
        cv.put(KEY_AKTOR, dataFilm.getAktor ());
        cv.put(KEY_GENRE, dataFilm.getGenre ());
        cv.put(KEY_SINOPSIS, dataFilm.getSinopsis ());
        cv.put(KEY_LINK, dataFilm.getLink());

        db.insert(TABLE_FILM, null, cv);
        db.close();
    }

    public void tambahFilm(Film dataFilm, SQLiteDatabase db) {
        ContentValues cv = new ContentValues();

        cv.put(KEY_JUDUL, dataFilm.getJudul());
        cv.put(KEY_TGL, sdFromat.format(dataFilm.getTanggal()));
        cv.put(KEY_GAMBAR, dataFilm.getGambar());
        cv.put(KEY_AKTOR, dataFilm.getAktor ());
        cv.put(KEY_GENRE, dataFilm.getGenre ());
        cv.put(KEY_SINOPSIS, dataFilm.getSinopsis ());
        cv.put(KEY_LINK, dataFilm.getLink());
        db.insert(TABLE_FILM, null, cv);
    }

    public void editFilm(Film dataFilm) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(KEY_JUDUL,  dataFilm.getJudul());
        cv.put(KEY_TGL, sdFromat.format( dataFilm.getTanggal()));
        cv.put(KEY_GAMBAR, dataFilm.getGambar());
        cv.put(KEY_AKTOR,  dataFilm.getAktor ());
        cv.put(KEY_GENRE,  dataFilm.getGenre ());
        cv.put(KEY_SINOPSIS,  dataFilm.getSinopsis ());
        cv.put(KEY_LINK,  dataFilm.getLink());

        db.update(TABLE_FILM, cv, KEY_ID_FILM + "=?", new String[]{String.valueOf(dataFilm.getIdFilm ())});

        db.close();
    }

    public void  hapusFilm(int idFilm) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_FILM, KEY_ID_FILM + "=?", new String[]{String.valueOf(idFilm)});
        db.close();
    }

    public ArrayList<Film> getAllFilm() {
        ArrayList<Film> dataFilm = new ArrayList<>();
        String query = "SELECT * FROM " + TABLE_FILM;
        SQLiteDatabase db = getReadableDatabase();
        Cursor csr = db.rawQuery(query, null);
        if (csr.moveToFirst()){
            do{
                Date tempDate = new Date();
                try {
                    tempDate = sdFromat.parse(csr.getString(2));
                } catch (ParseException er) {
                    er.printStackTrace();
                }

                Film tempFilm = new Film (
                        csr.getInt(0),
                        csr.getString(1),
                        tempDate,
                        csr.getString(3),
                        csr.getString(4),
                        csr.getString(5),
                        csr.getString(6),
                        csr.getString(7)
                );

                dataFilm.add(tempFilm);
            } while (csr.moveToNext());
        }

        return dataFilm;

    }

    private String storeImageFile(int id) {
        String location;
        Bitmap image = BitmapFactory.decodeResource(context.getResources(), id);
        location = InputActivity.saveImageToInternalStorage(image, context);
        return  location;
    }

    private void inisialisasiFilmAwal(SQLiteDatabase db) {
        int idFilm = 0;
        Date tempDate = new Date();

//Menambah data film ke-1
        try {
            tempDate = sdFromat.parse("03/07/2019");
        } catch (ParseException er) {
            er.printStackTrace();
        }



        Film film1 = new Film (
                idFilm,
                "SPIDERMAN-FAR FROM HOME",
                tempDate,
                storeImageFile(R.drawable.film1),
                "Tom Holland sebagai Peter Parker / Spider-Man\n" +
                        "Samuel L. Jackson sebagai Nick Fury\n" +
                        "Zendaya sebagai Michelle \"MJ\" Jones\n" +
                        "Cobie Smulders sebagai Maria Hill\n" +
                        "Jon Favreau sebagai Harold Happy Hogan\n" +
                        "J. B. Smoove sebagai Tn. Dell\n" +
                        "Martin Starr sebagai Tn. Harrington\n" +
                        "Marisa Tomei sebagai May Parker \n" +
                        "Jake Gyllenhaal sebagai Quentin Beck / Mysterio\n" ,
                "laga, komedi, pahlawan super, fiksi ilmiah, film fantasi, remaja, petualangan",
                "Peter Parker (Tom Holland) tengah mengunjungi Eropa untuk liburan panjang bersama temaan-temanya. Sayangnya , Parker tidak bisa dengan tenang menikmati liburannya, karena Nick Fury datang secara tiba-tiba di kamar hotelnya. Selama di Eropa pun Peter harus menghadapi banyak musuh mulai dari Hydro Man, Sandman dan Molten Man.",
                "https://www.youtube.com/watch?v=Nt9L1jCKGnE"
        );

        tambahFilm(film1, db);
        idFilm++;

        // Data film ke-2
        try {
            tempDate = sdFromat.parse("15/05/2018");
        } catch (ParseException er) {
            er.printStackTrace();
        }
        Film film2 = new Film (
                idFilm,
                "Deadpool 2",
                tempDate,
                storeImageFile(R.drawable.film2),
                "Ryan Reynolds\n" +
                        "Morena Baccarin\n" +
                        "T. J. Miller\n" +
                        "Leslie Uggams\n" +
                        "Brianna Hildebrand\n" +
                        "Stefan Kapičić\n",
                "komedi, pahlawan super, laga, fiksi ilmiah, petualangan, fantasi",
                "Wade Wilson (Ryan Reynolds) berusaha melindungi seorang mutan misterius yang diincar Cable (Josh Brolin). Untuk melindunginya, Wade lalu membentuk sebuah tim bernama X-Force yang beranggotakan Deadpool sendiri, Domino, Negasonic Teenage Warhead, Colossus, dan Bedlam." ,
                "https://www.youtube.com/watch?v=D86RtevtfrA"
        );

        tambahFilm(film2, db);
        idFilm++;

        //Data film ke-3
        try {
            tempDate = sdFromat.parse("05/04/2019");
        } catch (ParseException er) {
            er.printStackTrace();
        }
        Film film3 = new Film (
                idFilm,
                "SHAZAM",
                tempDate,
                storeImageFile(R.drawable.film3),
                "•\t\n" +
                        "Chris Hemsworth\n" +
                        "Tom Hiddleston\n" +
                        "Cate Blanchett\n" +
                        "Idris Elba\n" +
                        "Jeff Goldblum\n" +
                        "Tessa Thompson\n" +
                        "Karl Urban\n" +
                        "Mark Ruffalo\n" +
                        "Anthony Hopkins",
                "Komedii,pahlawan super, fiksi ilmiah, laga, fantasi, petualangan",
                "Billy Batson, seorang anak yatim berusia 14 tahun yang bermasalah yang tinggal di Philadelphia, diatur untuk pindah ke rumah asuh baru - yang ketujuh berturut-turut - dengan keluarga Vazquez dan lima anak asuh lainnya. Suatu hari, Billy naik mobil subway dan menemukan dirinya diangkut ke dunia yang berbeda di mana penyihir kuno memberinya kekuatan.",
                "https://www.youtube.com/watch?v=-oD7B7oiBtw"
        );

        tambahFilm(film3, db);
        idFilm++;

        // Data film ke-4
        try {
            tempDate = sdFromat.parse("25/10/2017");
        } catch (ParseException er) {
            er.printStackTrace();
        }
        Film film4 = new Film (
                idFilm,
                "THOR RAGNAROK",
                tempDate,
                storeImageFile(R.drawable.film4),
                "Chris Hemsworth\n" +
                        "Tom Hiddleston\n" +
                        "Cate Blanchett\n" +
                        "Idris Elba\n" +
                        "Jeff Goldblum\n" +
                        "Tessa Thompson\n" +
                        "Karl Urban\n" +
                        "Mark Ruffalo\n" +
                        "Anthony Hopkins\n",
                "Komedi, laga, pahlawan super, fiksi ilmiah, fantasi, persahabatan, petualangan",
                "Dipenjara, Thor yang hebat menemukan dirinya dalam sebuah kontes gladiator yang mematikan melawan Hulk, mantan sekutunya. Thor harus berjuang untuk bertahan hidup dan berpacu melawan waktu untuk mencegah Hela yang sangat kuat menghancurkan rumah dan peradaban Asgardian.",
                "https://www.youtube.com/watch?v=ue80QwXMRHg"
        );

        tambahFilm(film4, db);
        idFilm++;

        //Data film ke-5
        try {
            tempDate = sdFromat.parse ( "24/04/2019" );
        } catch (ParseException er) {
            er.printStackTrace ();
        }
        Film film5 = new Film (
                idFilm,
                "AVENGER ENDGAME",
                tempDate,
                storeImageFile ( R.drawable.film5 ),
                "Robert Downey Jr.\n" +
                        "Chris Evans\n" +
                        "Mark Ruffalo\n" +
                        "Chris Hemsworth\n" +
                        "Scarlett Johansson\n" +
                        "Jeremy Renner\n" +
                        "Don Cheadle\n" +
                        "Paul Rudd\n" +
                        "Brie Larson\n" +
                        "Karen Gillan\n" +
                        "Danai Gurira\n" +
                        "Benedict Wong\n" +
                        "Jon Favreau\n" +
                        "Bradley Cooper\n" +
                        "Gwyneth Paltrow\n" +
                        "Josh Brolin",
                "laga, pahlawan super, fiksi ilmiah, fantasi, petualangan",
                "Endgame menceritakan kejadian setelah kekalahan Avengers melawan Thanos dimana Thanos berhasil melenyapkan setengah populasi alam semesta hanya dengan jentikan jarinya. film diawalai dengan scene dimana Clint/Hawk Eye sedang berekreasi bersama keluarganya. Namun kejadian pahit menimpa Clint karena keluarganya lenyap akibat jentikan jari Thanos.\n"+
                        "Scene berlanjut ke luar angkasa dimana Tony Stark/Iron Man dan Nebula tampak dalam keadaan kritis dimana pesawat mereka terombang ambing di luar angkasa dengan persediaan makanan dan bahan bakar mulai menipis dan dalam beberapa hari akan habis. Sadar akan keadaan yang tidak memungkinkanya selamat, Tony mebuat rekaman untuk Papper Potts dan terbaring dikursinya dalam keadaan kritis. Namun dalam keadaan krisis tersebut muncul harapan untuk kembali kebumi.\n"+
                        "Adegan beralih ke markas Avengers. Di markas anggota yang tersisa masih terhuyung-huyung akan kekalahan mereka dari Thanos sembari mencoba mencari tahu siapa saja orang dibumi yang menjadi korban jentikan Thanos. mereka menyusun rencana untuk mecari keberadaan Thanos dan mengalahkannya mengembalikan kembali orang-orang yang telah dilenyapkan dengan menggunakan Infinity Stones. Rencana pun dijalankan dimana para Avengers pergi ke tempat Thanos berada.\n"+
                        "Thanos diperlihatkan sedang berada di ladang membawa makanan yang dipanen dibawa kerumahnya. Thanos yang terlihat terluka dan sedang dalam kondisi lemah sedang memasak, namun ia dikejutkan oleh serangan dari para Avengers dan Thanos berhasil ditakhlukan. Namun Avengers dikejutkan karena menemukan bahwa Thanos tidak memiliki Infinity Stones. Thanos menjelaskan bahwa ia telah melenyapkan infinity stones sehingga rencananya tidak mungkin bisa dirusak. Marah akan hal itu Thor membunuh Thanos dan Avengers dengan putus asa kembali ke bumi.\n"+
                        "Lima tahun berlanjut setelah kematian Thanos. Dimarkas Natasha/Black Widow sedang berbicara dengan anggota Avengers dan sekutu melalui alat komunikasi sedang memantau kestabilan bumi dan alam semesta. di tempat lain terjadi hal mengejutkan dimana Scott Lang/Ant Man kembali dari alam kuantum dan ia terkejut akan kejadian yang menimpa bumi. Lang pergi kemarkas Avengers dimana anggota Avengers terkejut karena mereka mengira Lang sudah lenyap.\n"+
                        "Di markas Lang menceritakan apa yang menimpanya dan memberikan sebuah ide yang memungkinkan Avengers untuk mengembalikan orang-orang yang lenyap akibat jentikan jari Thanos. Ide tersebut dijalankan dan Avengers yang berada dimarkas sedang berusahan menyusun rencana dang mengumpulkan kembali beberapa anggota yang terpencar. Setelah anggota terkumpul para Avengers pergi menjalankan rencana mereka untuk mengembalikan kembali orang-orang yang telah lenyap.",
                "https://www.youtube.com/watch?v=TcMBFSGVi1c"
        );

        tambahFilm (film5,db );

    }
}
