### Description d'une salle

##### Présentation

Chacun peut afficher le détails des réservations d'une salle ainsi que ses informations générales.

Par défaut, l'état et la réservation en cours de la salle seront affichés, mais il est possible de spécifier un intervalle de temps à étudier.

##### Utilisation

```JAVA
> describe B2-20
//Affiche les informations de la salle B2-20 ainsi que la réservation en cours si elle existe

> describe -a B2-20
Affiche les informations de la salle B2-20 ainsi que la totalité des réservations passées ou futures de cette salle

> describe -d B2-20 2020-03-10_09:00;2020-03-10_12:30
//Affiche les informations de la salle B2-20 ainsi que la totalité des réservations de cette salle entre 9h et 12h30 le 10 Mars 2020 (d pour date)
```

##### Réponse

```
Salle <nom_salle>
Salle informatique : <Oui/Non>
Localisation : <localisation> 
État actuel : <etat_actuel>
Nombre de places : <nombre_place>
<date_debut_reservation> - <date_fin_reservation> : <privé/public> - <description> (<auteur_reservation_>)
```
**La dernière ligne concerne les réservations et s'affiche autant de fois que nécessaire**

##### Exemple :

*Salle B2-20*

*Salle informatique : Non*

*Localisation : Bâtiment Blériot (2e étage)*

*État actuel : Libre*

*Nombre de places : 80*

*2020-03-10_09:00 - 2020-03-10_12:30 : privé - Cours de Génie Logiciel (Vincent BOUTOUR)*
