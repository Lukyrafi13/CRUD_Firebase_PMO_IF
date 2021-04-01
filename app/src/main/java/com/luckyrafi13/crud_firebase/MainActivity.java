package com.luckyrafi13.crud_firebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import static android.text.TextUtils.isEmpty;

public class MainActivity extends AppCompatActivity {

    //Deklarasi Variable
    private ProgressBar progressBar;
    private EditText NIM, Nama, Jurusan;
    private Button Simpan, ShowData;
    private String getNIM, getNama, getJurusan;

    DatabaseReference getReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Inisialisasi ID (Progresbar)
        progressBar = findViewById(R.id.progress);
        progressBar.setVisibility(View.GONE);

        //Inisialisasi ID (Button)
        Simpan = findViewById(R.id.save);
        ShowData = findViewById(R.id.showdata);

        //Inisialisasi ID (EditText)
        NIM = findViewById(R.id.nim);
        Nama = findViewById(R.id.nama);
        Jurusan = findViewById(R.id.jurusan);

        //Mendapatkan Instance dari Database
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        getReference = database.getReference(); // Mendapatkan Referensi dari Database

        // Membuat Fungsi Tombol Simpan
        Simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Menyimpan Data yang diinputkan User kedalam Variable
                getNIM = NIM.getText().toString();
                getNama = Nama.getText().toString();
                getJurusan = Jurusan.getText().toString();

                checkUser();
                progressBar.setVisibility(View.VISIBLE);

            }
        });


        ShowData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ListDataActivity.class);
                startActivity(intent);
            }
        });

    }

    private void checkUser() {

        // Mengecek apakah ada data yang kosong
        if (isEmpty(getNIM) && isEmpty(getNama) && isEmpty(getJurusan)) {
            //Jika Ada, maka akan menampilkan pesan singkan seperti berikut ini.
            Toast.makeText(MainActivity.this, "Data tidak boleh ada yang kosong", Toast.LENGTH_SHORT).show();
        } else {

            /*
            Jika Tidak, maka data dapat diproses dan meyimpannya pada Database
            Menyimpan data referensi pada Database berdasarkan User ID dari masing-masing Akun
            */

            getReference.child("Admin").child("Mahasiswa").push()
                    .setValue(new data_mahasiswa(getNIM, getNama, getJurusan))
                    .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            //Peristiwa ini terjadi saat user berhasil menyimpan datanya kedalam Database
                            NIM.setText("");
                            Nama.setText("");
                            Jurusan.setText("");
                            Toast.makeText(MainActivity.this, "Data Tersimpan", Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    });
        }
    }
}