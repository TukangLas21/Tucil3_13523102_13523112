# Tucil3_13523102_13523112
Penyelesaian Puzzle Rush Hour Menggunakan Algoritma Pathfinding

## Deskripsi Singkat
Puzzle Rush Hour adalah sebuah permainan puzzle berbasis grid yang melibatkan pemain untuk menggeser kendaraan-kendaraan agar kendaraan/blok utama dapat keluar dari kemacetan melalui pintu utama. Setiap kendaraan hanya dapat bergerak maju atau mundur tergantung dengan orientasinya (horizontal atau vertikal). Tujuan dari permainan ini adalah mencari cara untuk mengeluarkan kendaraan utama dari kemacetan dengan langkah yang sesedikit mungkin. <br>
Dengan tujuan mencari langkah paling sedikit, puzzle ini dapat diselesaikan melalui beberapa algoritma _pathfinding_ atau pencarian rute. Tugas kecil ini membahas penyelesaian puzzle ini dengan 4 macam algoritma pencarian rute, yaitu Greedy Best-First Search, Uniformed Cost Search, A* Search, dan Iterative Deepening A* Search, dengan dua heuristik, yakni jumlah kendaraan yang menghalangi kendaraan utama dan jarak kendaraan utama dengan pintu keluar.

## Persyaratan Sistem
Sebelum memulai, pastikan Anda sudah menginstal _dependencies_ berikut pada sistem Anda.
- [Java](https://www.oracle.com/id/java/technologies/downloads/)
- [JavaFX](https://www.oracle.com/java/technologies/install-javafx-sdk.html)
- [Maven](https://maven.apache.org/download.cgi)

## Instalasi / Memulai
Untuk memulai, silakan klon repositori ini.
```shell
git clone https://github.com/TukangLas21/Tucil3_13523102_13523112.git
cd Tucil3_13523102_13523112
```
### Menjalankan Program
Untuk meng-_compile_ dan menjalankan program, silakan masukkan perintah berikut.
```shell
mvn clean javafx:run
```

Setelah program dijalankan, akan muncul antarmuka dengan beberapa input yang perlu Anda masukkan:
- Path file konfigurasi (.txt)
- Algoritma yang ingin digunakan
- Heuristik yang ingin digunakan

Setelah memasukkan semua input yang dibutuhkan, tekan tombol "Start" untuk memulai proses pencarian rute. Setelah program selesai merumuskan rute menuju solusi, antarmuka akan menampilkan animasi yang memperlihatkan jalur penyelesaian berdasarkan algoritma dan heuristik yang dipilih. Program juga akan menyertakan waktu pencarian, jumlah gerakan yang diperiksa, dan jumlah gerakan pada solusi. Anda juga dapat menyimpan rute solusi dengan menekan tombol "Save". Program akan menyimpan solusi dalam bentuk file (.txt) pada folder `test/result`.

## Berkontribusi
Jika Anda berminat untuk mengembangkan proyek ini, silakan _fork_ repositori ini.

## Author
- Michael Alexander Angkawijaya (13523102)
- Aria Judhistira (13523112)
