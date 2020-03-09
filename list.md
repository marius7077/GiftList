### Affichage des salles

##### Présentation

Chacun peut afficher les salles disponibles, et affiner l'affichage par l'utilisation de filtres. Il est possible de filtrer les salles selon leur nombre de places, leur disponibilité ou le fait qu'elles soient dotées de matériels informatiques.

Par défaut, les salles seront toutes affichées, à moins qu'elles soient à l'instant T occupées et privées. Il existe également une option permettant d'afficher l'intégralité des salles.

##### Utilisation

```JAVA
> list
//Affiche toutes les salles actuellement accessibles (qu'elles soient déjà occupées (mais ouvertes à tous) ou non

> list -a
//Affiche toutes les salles, y compris celles occupées et privées

> list -ic
//Affiche toutes les salles informatisées (i pour IT) actuellement inoccupées (c pour closed). Ces deux paramètres peuvent être utilisés séparément

> list -dn 2020-03-10_09:00;2020-03-10_12:30 20
//Affiche toutes les salles accessibles entre 9h et 12h30 le 10 Mars 2020 (d pour date) qui contiennent au moins 20 places (n pour number). Ces deux paramètres peuvent être utilisés séparéments, mais utilisés ensembles, l'intervalle de temps doit toujours être spécifié avant le nombre de place
```

##### Réponse

```
<nom_salle>, <localisation> (<nombre_place> places) - <type_salle> : <etat_actuel>
```

Exemple :

*B2-20, bâtiment blériot (2e étage) (80 places) - salle de cours : libre*

*B0-06, bâtiment blériot (rez-de-chaussée) (20 places) - salle informatique : libre*
