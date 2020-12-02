* Wörter / Input:
  * `F-INPUT-40` - Anzahl Wörter - SOLL: 80+, IST: 66, DIF: 14+
  * `F-INPUT-50` - einfache Rechtschreibfehler
* Nutzerprofile:
  * `F-LOGIK-50` - Zustimmung der Speicherung von Daten (Kurs/Name) beim Start
  * `F-LOGIK-60` - Speichern der Kurszuordnung
  * `F-LOGIK-70` - Laden der Daten
  * `F-LOGIK-80` - Tauschen der Daten

Maßnahmen: 
* Finden weiterer Wörter
* Erstellen einer Vergleichsmethode (Levenshtein distance)
  ```kotlin
  fun lev(a: String, b: String): Int {
      // faster by using a buffer ? 
      return when {
          a.isEmpty -> b.length
          b.isEmpty -> a.length
          a[0] == b[0] -> lev(a.subSequence(1), b.subSequence(1))
          else -> 1 + min(
              lev(a.subSequence(1), b),
              lev(a, b.subSequence(1)),
              lev(a.subSequence(1), b.subSequence(1)))
      }
  }
  ```
* Erstellen einer Klasse `Benutzer`, in welcher Name und Kurs gespeichert sind und welche der Anfrage angehängt wird

-------------------------------------------------------------------------------------

Codepflege:

* `frageAuswahl` sollte nur bei Abbruch nichts zurückliefern, außerdem Fehlererkennung ähnlich `F-LOGIK-50`
* `frageAntwort` könnte eventuell mit frageAuswahl zusammengefasst werden? `frageAuswahl` schreibt dann nicht immer alle möglichen Antworten
* `kursbezeichnungInAnfrage` durchsucht die Wörter kann aber die Kurse nicht finden, da diese aus mehr als einem Wort bestehen