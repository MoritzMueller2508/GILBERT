* Wörter / Input:
  * `F-INPUT-40` - Anzahl Wörter - SOLL: 80+, IST: 66, DIF: 14+
  * `Readme` - Create Readme


Maßnahmen: 
* Finden weiterer Wörter

-------------------------------------------------------------------------------------

Codepflege:

* `frageAuswahl` sollte nur bei Abbruch nichts zurückliefern, außerdem Fehlererkennung ähnlich `F-LOGIK-50`
* `frageAntwort` könnte eventuell mit frageAuswahl zusammengefasst werden? `frageAuswahl` schreibt dann nicht immer alle möglichen Antworten
* `kursbezeichnungInAnfrage` durchsucht die Wörter kann aber die Kurse nicht finden, da diese aus mehr als einem Wort bestehen
