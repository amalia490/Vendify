# 🛒 Smart Vending Machine (Sistem de Gestiune Tonomat Inteligent)

Acest proiect reprezinta o aplicatie Java pentru simularea si gestionarea unui tonomat inteligent. Arhitectura respecta principiile OOP (Incapsulare, Mostenire, Polimorfism, Abstractie) si utilizeaza sabloane de proiectare (ex: Singleton) pentru a separa logica de business de modelele de date.

## 📂 1. Arhitectura si Clasele Proiectului

Proiectul este structurat pe pachete standard (Model, Service, Exception), dupa cum urmeaza:

### `com.pao.proiect.tonomat.model` (Modele de Domeniu)
**Ierarhia de Produse:**
* `Product` (Clasa abstracta de baza, implementeaza `Comparable` si `Discountable`)
* `Eatable` (Clasa abstracta, extinde `Product` - pentru produse cu calorii si valabilitate)
* `Drink` (Extinde `Eatable` - adauga volum si arome)
* `Snack` (Extinde `Eatable` - adauga gramaj si specificatie vegana)
* `Gadget` (Extinde `Product` - pentru electronice cu garantie)

**Sistemul de Plati si Promotii:**
* `PaymentMethod` (Clasa abstracta pentru procesarea platilor)
* `CashPayment` (Extinde `PaymentMethod` - calculeaza restul de plata)
* `CardPayment` (Extinde `PaymentMethod` - valideaza datele cardului)
* `PromoCode` (Gestioneaza cupoanele de reducere aplicabile pe comenzi)

**Management si Gestiune:**
* `Compartment` (Reprezinta un slot fizic al tonomatului, stocheaza cantitatea si produsul)
* `Administrator` (Gestioneaza datele si credentialele de acces pentru mentenanta)
* `MachineReport` (Clasa `final` imutabila pentru generarea rapoartelor zilnice)

**Interfete (`interfaces`):**
* `Discountable` (Contract pentru aplicarea reducerilor pe produse/compartimente)
* `StockObserver` (Sablonul Observer pentru alertarea automata a administratorului la stoc critic)

### `com.pao.proiect.tonomat.service` (Logica de Business)
*Toate serviciile sunt implementate folosind sablonul **Singleton** pentru a asigura o instanta unica in memorie.*
* `InventoryService`: Gestioneaza operatiunile CRUD pe rafturi si stocuri.
* `PaymentService`: Gestioneaza incasarile, cupoanele promotionale si retragerile de numerar.
* `ReportService`: Se ocupa de colectarea datelor financiare si tiparirea istoricului de tranzactii.

### `com.pao.proiect.tonomat.exception` (Exceptii Custom)
* `StocEpuizatException` (Aruncata la selectarea unui produs inexistent sau cu cantitate 0)
* `FonduriInsuficienteException` (Aruncata cand banii introdusi nu acopera costul)

---

## ⚙️ 2. Actiuni si Interogari (Functionalitati)

Sistemul expune o serie de functionalitati accesibile prin clasa `Main`, delegate catre serviciile specializate:

### Actiuni (Modificari in sistem)
1. **Adaugarea unui nou tip de produs:** Alocarea unui produs nou intr-un compartiment al tonomatului (ex: alocarea Cola la slotul A1 cu stoc 10) folosind `adaugaRaft()`.
2. **Suplinirea stocului (Restock):** Actualizarea cantitatii pentru un produs deja existent de catre Administrator (`suplimenteazaStoc()`).
3. **Selectarea unui produs:** Cautarea obiectului de tip compartiment prin introducerea codului de catre client (ex: "B2") folosind `obtineRaftDupaCod()`.
4. **Procesarea platii si eliberarea produsului:** Verificarea fondurilor prin `proceseazaPlata()`, scaderea stocului cu 1 prin `elibereazaProdus()` si calcularea restului la plata cash.
5. **Autentificarea administratorului:** Validarea pe baza unui ID si cod PIN pentru a debloca functiile de mentenanta (`autentificareAdmin()`).
6. **Retragerea numerarului:** Colectarea banilor din aparat de catre Administrator si resetarea soldului intern (`retrageNumerar()`).
7. **Aplicarea unui cod promotional:** Sistemul valideaza daca obiectul `PromoCode` exista si este nefolosit, scazand pretul final inainte de plata (`aplicaCodPromo()`).

### Interogari (Cautari si returnari de date)
8. **Afisarea Meniului Complet:** Interogarea tuturor produselor disponibile in aparat care au stoc > 0 (`afiseazaProduseDisponibile()`).
9. **Afisarea produselor sortate dupa pret:** Generarea unui catalog formatat, folosind o colectie de tip `TreeSet` ce sorteaza automat obiectele implementand interfata `Comparable` (`afiseazaCatalogSortatDupaPret()`).
10. **Interogarea stocului critic:** Parcurgerea rafturilor si alertarea automata (sau manuala) pentru produsele care au cantitatea sub o anumita limita (util pentru Administrator) via `afiseazaStocCritic()`.
11. **Generarea istoricului (Raport Zilnic):** Tiparirea datelor agregate in obiectul `MachineReport` referitoare la incasari, numar de produse vandute si cel mai bine vandut produs.
12. **Filtrarea produselor dupa caracteristici specifice:** Afisarea tuturor bauturilor care sunt "sugar-free" (`filtreazaBauturiFaraZahar()`). Demonstreaza parcurgerea colectiilor, verificarea tipului cu `instanceof` (pattern matching) si utilizarea conditiilor specifice pe subclase.
