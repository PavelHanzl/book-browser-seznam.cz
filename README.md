# Projekt Book Browser pro Seznam.cz
Tento projekt je Android aplikace, která byla vytvořena jako úloha pro Seznam.cz. Aplikace se skládá ze dvou obrazovek: první zobrazuje seznam knih od zadaného autora a druhá poskytuje detailní informace o konkrétní knize. Data jsou získávána z Google Books API.

---

### Zadání

Napište aplikaci pro Android s libovolným UI o dvou obrazovkách, kde první bude zobrazovat výpis knih od zadaného autora a druhá obrazovka bude vázaná ke konkrétní knize a bude zobrazovat její detaily. Jako zdroj dat použijte volně dostupné vyhledávání na GoogleBooks API (https://developers.google.com/books/docs/v1/using#PerformingSearch).

Aplikace nebude nikde pracovat s daty žádného uživatele, měla by být jednoduchá, ale uživatelsky příjemná,stabilní a měla by efektivně pracovat s daty. V aplikaci použijte libovolné technologie a knihovny.


1) První obrazovka bude obsahovat možnost zadat libovolné jméno autora a výpis knih od daného autora, které vyšly v češtině. Ve výpisu bude uveden vždy název knihy,jméno autora a náhled obálky. Výpis bude obsahovat knihy s unikátním názvem (nebude se objevovat více knih stejného názvu). Kliknutím na položku ve výpisu se uživatel dostane druhé obrazovky.

2) V detailu knihy bude vidět velký náhled obálky, název knihy, jméno autora, popis knihy a rok vydání. Dále bude obrazovka obsahovat možnost prohlédnout si danou knihu v obchodě GooglePlay.

### Využité technologie/přístupy
- Kotlin
- Jetpack Compose
- MVVM Architecture
- Koin Dependency injection
- Retrofit
- Paginace
- Glide


---

**Pavel Hanzl**