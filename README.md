# Zadanie 1 – formularz zapisu

Zbuduj formularz z rozdziału 8. Oddajesz w pięciu commitach:

1. `feat: uklad formularza w ScrollView`
2. `feat: odczyt pol i podsumowanie bez walidacji`
3. `feat: spinner z lista kursow`
4. `feat: walidacja pol z setError`
5. `feat: potwierdzenie i snackbar`

W `README.md` odpowiedz: **dlaczego przy `SeekBar` nie dało się użyć lambdy,
a przy `CheckBox` tak?**

Ponieważ, interfejs `SeekBar.OnSeekBarChangeListener` wymaga trzy metody, a lambda działa tylko gdy interfejs wymaga tylko jednej.


# Zadanie 2 – własny formularz

Wybierz jeden temat albo zaproponuj własny:

| Temat | Pola, które wymusza |
|---|---|
| **Zamówienie pizzy** | rozmiar (`RadioGroup`), dodatki (kilka `CheckBox`), ilość (`SeekBar`), adres (`EditText`) |
| **Rezerwacja wizyty** | usługa (`Spinner`), data (`DatePickerDialog`), godzina, dane kontaktowe |
| **Zgłoszenie usterki** | kategoria (`Spinner`), priorytet (`RadioGroup`), opis wielolinijkowy, zgoda na kontakt |
| **Ankieta satysfakcji** | pięć pytań na `SeekBar`, komentarz, zgoda na publikację |
| **Zapisy na wycieczkę** | kierunek (`Spinner`), liczba osób (`SeekBar`), ubezpieczenie (`Switch`), data wyjazdu |

**Wymagania – niezależnie od tematu:**

1. Minimum **pięć kontrolek różnego typu**, w tym `Spinner` zasilany z `string-array`.
2. `ScrollView` jako kontener główny.
3. Walidacja **co najmniej trzech pól**, przerywana na pierwszym błędzie,
   z `setError` tam, gdzie to możliwe.
4. **Okno dialogowe z potwierdzeniem** przed wykonaniem akcji.
5. **`Snackbar` z akcją cofnięcia** po wykonaniu.
6. Przynajmniej jedna **zależność między polami** – coś, co odblokowuje się,
   zmienia albo ukrywa w zależności od innej kontrolki.
7. `onCreate` tylko podpina i konfiguruje, logika w osobnych metodach.

**O co zapytam:** który interfejs nasłuchiwania ma więcej niż jedną metodę
i co z tego wynika dla zapisu kodu.