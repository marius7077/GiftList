# Room'IT

*Comment obtimiser la réservation de salle au sein de l'IUT ?*

### Situation
À chaque heure, des centaines d'étudiants et de professeurs vont de salle en salle pour suivre leurs différents cours. Les critères d'attribution de salles doivent prendre en compte, lors de la création des emplois du temps, de tous les besoins du cours en terme de place, d'accès aux outils informatiques et d'horaires notamment.
En plus de cela, on compte aussi un nombre d'étudiant relativement conséquent, qui nécessite des salles en dehors de leurs cours. On pense notamment aux heures de pause, aux périodes sans cours dans leurs emplois du temps ou bien aux heures en début ou fin de journée durant lesquelles ils restent à l'IUT. Dans ces trois cas, les étudiants n'auront pas besoin d'accéder à une salle pour les mêmes raisons. Ils peuvent vouloir se rendre dans une salle de cours pour manger, faire un jeu etc... dans un endroit plus au calme, ils peuvent vouloir accéder à une salle informatique pour travailler ou passer le temps sur un ordinateur, ou ils peuvent vouloir s'isoler en petit groupe afin d'avancer sereinement un projet de groupe par exemple.
On identifie donc trois types de réservations principaux : 
 - La réservation par un professeur : la salle est réservée au professeur et à ses élèves pendant une durée donnée
 - La réservation par un groupe d'étudiant : la salle est réservée au groupe d'étudiant pendant une durée donnée
 - L'ouverture d'une salle de pause : la salle est accessible à tous pendant une durée donnée

L'application sera basée sur une recherche simple :
 - Recherche d'une salle
 - Consultation des détails de la salle
 - Réservation de la salle


### Recherche d'une salle
La recheche d'une salle s'effecturera par filtres. Sans filtres renseignés, la liste de salles affichées sera par défaut la liste des salles disponibles ou ouvertes à l'instant T. Sinon, il sera possible de filtrer les salles selon qu'elles soient vides ou partiellement occupées, qu'elles soient ou non des salles informatiques et selon le nombre de place et selon l'horaire et la durée (en minutes) de disponibilité. 

```
> l_room
/* affiche la liste des salles accessibles à l'heure de la commande */
> look_for_room -fi
/* affiche la liste des salles informatiques (i) fermées (f) */
> look_for_room -hdin 2020-01-28_15:30 210 20
/* affiche la liste des salles informatiques accessibles le 28 Janvier 2020 à 15h30 pour une durée de 210 minutes (3 heures trente) et pour 20 personnes */
> look_for_room -a
/* affiche toutes les salles, quelles que soient leurs disponibilité */

/**
 * Format de réponse
 * <nom_salle> - <type_salle> - <localisation> : <etat_actuel>. Nombre de places : <nombre_place>
*/
B0-6 - Salle informatique - Bâtiment Blériot : libre. Nombre de places : 24
B2-20 - Salle de cours - Bâtiment Blériot : ouverte. Nombre de places : 86
```


### Consultation d'une salle
Il peut-être intéressant de lire l'emploi du temps d'une salle, afin de savoir si elle est souvent réservée ou non. Pour un professeur, cela peut permettre de voir s'il pourra accéder lors de plusieurs séances à la même salle, pour un élève il est surtout important de savoir au bout de combien de temps la salle ne sera plus disponible. Par défaut, on affiche l'emploi du temps de la salle pour la journée, mais on peut spécifier sa recherche.
Cela permet aussi, lorsque l'on est pas passé par la recherche au prélable, d'afficher les informations plus simples d'une salle.

```
> see_room B2-20
/* affiche les informations de la salle B2-20 pour la journée en cours */
> see_room -d B2-20 2020-01-29
/* affiche les informations de la salle B2-20 pour le 29 Janvier 2020 */
> see_room -d B2-20 2020-01-28:2020-02-01
/* affiche les informations de la salle B2-20 entre le 28 Janvier et le 1 Février 2020 */


/** 
 * Format de réponse
 * Salle <nom_salle>
 * Salle informatique : <is_informatique>
 * Localisation : <localisation>
 * Etat actuel : <etat_actuel>
 * Nombre de place : <nombre_place>
 * 
 * Planning : 
 * <date_debut_res> - <date_fin_res> : <statut> - <description>
*/

Salle B2-20
Salle informatique : Non
Localisation : Bâtiment Blériot
Etat actuel : Ouverte
Nombre de place : 86

Planning :
2020-01-28_12:30 - 2020-01-28_14:00 : ouverte (Jean Eleve)
2020-01-28_14:00 - 2020-01-28_16:00 : réservée - cours de mathématiques (Jean Professeur)
2020-01-28_17:30 - 2020-01-28_18:00 : réservée - projet de groupe (Jean Eleve)
```


### Réservation d'une salle
Après consultation, il faut bien sûr passer par l'étape de réservation. Il est nécessaire à cette étape s'être identifié lors d'une étape de connexion basique. Ainsi, on peut imaginer pouvoir désactiver un étudiant qui aurait dégradé une salle par exemple. On pourrait aussi permettre à un professeur ou à un secrétaire de passer outre une réservation en cas de manque de salle, voir d'en supprimer.
Pour réserver, il faut spécifier un horaire et une durée. On demande en plus un commentaire, permettant lors de la consultation d'une salle de connaître l'utilisation qui en est faite.

```
> take_room B2-20 2020-01-28_14:00 120
/* réserve la salle B2-20 de 14h à 16h le 28 Janvier 2020, pour un cours de mathématiques. */
> motif : cours de mathématiques
/* après "motif :" qui s'est affiché automatiquement, on renseigne la raison de la réservation */

/** 
 * Format de réponse
 * Réservation effectuée avec succès !
*/
```
