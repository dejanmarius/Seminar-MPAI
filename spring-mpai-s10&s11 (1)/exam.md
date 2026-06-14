# Subiectul 1: Gestiunea comenzilor într-un magazin virtual

**Domeniul:** Gestiunea comenzilor într-un magazin virtual
**Descriere:** Se dorește dezvoltarea unei aplicații pentru gestionarea comenzilor plasate de clienți. O comandă poate trece prin următoarele stări: plasată, procesată, expediată și livrată.

**Funcționalități:**
- Clienții pot urmări starea comenzilor.
- Administratorii pot vizualiza comenzile, filtra/căuta după criterii precum dată, client sau stare, și modifica starea comenzilor.
- Notificări trimise clienților la fiecare actualizare a stării comenzii.
- Posibilitatea de anulare a comenzilor înainte de procesare.

**Condiții minime pentru evaluare:**
- Aplicația să nu aibă erori de compilare
- Aplicația să nu aibă code autogenerat prin intermediul instrumentelor de IA (ex. ChatGPT)
- Aplicația să treacă de gradul minim de plagiat.
- Aplicația să poată fii accesată la adresa: http://localhost:8080/home

**Cerințe:**
Să se implementeze o aplicație Web folosind limbajul Java de programare respectând următoarele restricții:
1. Utilizarea modelului arhitectural de tip Model View Controller - MVC prin intermediul framework-ului Spring MVC.
2. Utilizarea template engine-ului Thymeleaf pentru a implementa interfața cu utilizatorul.
3. Configurarea unei baze de date in-memory, H2.
4. Utilizarea Data Mapper-ului Spring Data pentru asocierea datele din baza de date în memoria aplicației Web.
5. La pornirea aplicației tabelele definite sunt populate automat cu cel puțin 5 înregistrări pentru fiecare. Datele sunt preluate dintr-un fișier text.
6. Integrarea DTO-urilor pentru asigurarea comunicării între componentele MVC. Nu este permisă utilizarea modelelor direct în View-uri.

---

# Subiectul 2: Gestiunea închirierilor de echipamente

**Domeniul:** Gestiunea închirierilor de echipamente (ex: laborator, sală sport, bibliotecă de unelte)
**Descriere:** Se dorește dezvoltarea unei aplicații pentru gestionarea cererilor de închiriere. O închiriere poate avea stările: cerută, aprobată, preluată, returnată, respinsă.

**Funcționalități:**
- Utilizatorii: pot vedea cererile/închirierile proprii și stările; pot face o cerere de închiriere (echipament, perioadă, scop); pot anula cererea înainte să fie aprobată.
- Administratorii: pot gestiona lista de echipamente (minim: nume, descriere, disponibilitate); pot vizualiza toate cererile de închiriere; pot filtra/căuta după: dată, utilizator, echipament, stare; pot aproba/respinge cereri; pot marca "preluată" și "returnată".
- Notificări: utilizatorii primesc notificare la fiecare schimbare de stare;

**Condiții minime pentru evaluare:**
- Aplicația să nu aibă erori de compilare
- Aplicația să nu aibă code autogenerat prin intermediul instrumentelor de IA (ex. ChatGPT)
- Aplicația să treacă de gradul minim de plagiat.
- Aplicația să poată fii accesată la adresa: http://localhost:8080/home

**Cerințe**
Să se implementeze o aplicație Web folosind limbajul Java de programare respectând următoarele restricții:
1. Utilizarea modelului arhitectural de tip Model View Controller - MVC prin intermediul framework-ului Spring MVC.
2. Utilizarea template engine-ului Thymeleaf pentru a implementa interfața cu utilizatorul.
3. Configurarea unei baze de date in-memory, H2.
4. Utilizarea Data Mapper-ului Spring Data pentru asocierea datele din baza de date în memoria aplicației Web.
5. La pornirea aplicației tabelele definite sunt populate automat cu cel puțin 5 înregistrări pentru fiecare. Datele sunt preluate dintr-un fișier text.
6. Integrarea DTO-urilor pentru asigurarea comunicării între componentele MVC. Nu este permisă utilizarea modelelor direct în View-uri.

---

# Subiectul 3: Gestiunea programărilor într-o clinică

**Domeniul:** Gestiunea programărilor într-o clinică
**Descriere:** Se dorește dezvoltarea unei aplicații pentru gestionarea programărilor făcute de pacienți la medici. O programare poate avea stările: solicitată, confirmată, efectuată, anulată.

**Funcționalități:**
- Pacienții: pot vedea lista programărilor și starea fiecăreia; pot face o programare (aleg medic, data/ora, motiv scurt); pot anula programarea doar dacă este în starea solicitată sau confirmată (cu minim X ore înainte).
- Administratorii/Recepția: pot vizualiza toate programările; pot filtra/căuta după: dată/interval, medic, pacient, stare; pot confirma sau anula programări; pot marca o programare ca efectuată.
- Notificări: pacientul primește notificare la fiecare schimbare de stare.

**Condiții minime pentru evaluare:**
- Aplicația să nu aibă erori de compilare
- Aplicația să nu aibă code autogenerat prin intermediul instrumentelor de IA (ex. ChatGPT)
- Aplicația să treacă de gradul minim de plagiat.
- Aplicația să poată fii accesată la adresa: http://localhost:8080/home

**Cerințe**
Să se implementeze o aplicație Web folosind limbajul Java de programare respectând următoarele restricții:
1. Utilizarea modelului arhitectural de tip Model View Controller - MVC prin intermediul framework-ului Spring MVC.
2. Utilizarea template engine-ului Thymeleaf pentru a implementa interfața cu utilizatorul.
3. Configurarea unei baze de date in-memory, H2.
4. Utilizarea Data Mapper-ului Spring Data pentru asocierea datele din baza de date în memoria aplicației Web.
5. La pornirea aplicației tabelele definite sunt populate automat cu cel puțin 5 înregistrări pentru fiecare. Datele sunt preluate dintr-un fișier text.
6. Integrarea DTO-urilor pentru asigurarea comunicării între componentele MVC. Nu este permisă utilizarea modelelor direct în View-uri.
