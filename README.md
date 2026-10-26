# Zadanie 1 – formularz zapisu

Zbuduj formularz z rozdziału 8. Oddajesz w pięciu commitach:

1. `feat: uklad formularza w ScrollView`
2. `feat: odczyt pol i podsumowanie bez walidacji`
3. `feat: spinner z lista kursow`
4. `feat: walidacja pol z setError`
5. `feat: potwierdzenie i snackbar`

W `README.md` odpowiedz: **dlaczego przy `SeekBar` nie dało się użyć lambdy,
a przy `CheckBox` tak?**

Ponieważ, interfejst `SeekBar.OnSeekBarChangeListener` wymaga trzy metody, a lambda działa tylko gdy interfejs wymaga tylko jednej.