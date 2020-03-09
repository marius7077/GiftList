# BOOK’IT – Spécifications ⚙️🛠

## Description de la solution

### Caractéristiques de la solution 

L’application Book’IT permet une gestion simplifiée des salles de l’IUT de Paris. Du côté des utilisateurs, elle permet de consulter les informations ou de réserver une salle en fonction de différents paramètres. 

Du côté des gestionnaires des salles de l’IUT, elle permet d’avoir une vision globale de l’utilisation des salles, d’organiser les emplois du temps en fonction des besoins des professeurs. 

C’est une application console. 💻

### Acteurs du projet

* 👨🏻‍💻 Thomas Aube : [thomasaube](https://github.com/thomasaube)
* 👨🏻‍💻 Ruben Attal : [ruben-git](https://github.com/ruben-git)
* 👨🏻‍💻 Marius Collin : [marius7077](https://github.com/marius7077)
* 👩🏼‍💻 Marine Meunier : [meuniermarine](https://github.com/ViBiOh)
* 👨🏻‍💻 Simon Weber : [simras98](https://github.com/simras98)

### Langage de programmation 

* Java

### Outils 

* Junit : outil de tests
* Mokito : outil de tests
* Maven : outil de gestion des dépendances
* Spring : outil d’intégration continue
* SonarQube : outil de vérification de la qualité

## Aspects métiers

### Les utilisateurs

Le persona visé par cette application englobe toutes les personnes utilisatrices de l’IUT de Paris notamment les étudiants et les professeurs mais aussi les secrétaires des départements et le personnel administratif. 

### Cas d’utilisations 

Prenons un cas d’utilisation qui fait intervenir toute les fonctionnalités de l’application.
Cas d’utilisation « Nous souhaitons réserver une salle pour un projet de groupe »

### Fonctionnalités de l'application

| Version  |Fonctionnalité |Contenu|
|:--------:|:-------------:|:-----|
| 1 |  F1 |Recherche d'une salle|
| 2 |    F2  |Consultation d'une salle|
| 3 | F3 |Réservation d'une salle|
| 4 | F4 |Connexion à l'application|

### Détail des fonctionnalités qui vont intervenir

Nous avons découpé les fonctionnalités en lots. Vous pouvez retrouver la roadmap de développement [ici](https://github.com/thomasaube).


## Lot 1
### Recherche d'une salle
La recheche d'une salle s'effecturera par filtres. Sans filtres renseignés, la liste de salles affichées sera par défaut la liste des salles disponibles ou ouvertes à l'instant T. Sinon, il sera possible de filtrer les salles selon qu'elles soient vides ou partiellement occupées, qu'elles soient ou non des salles informatiques et selon le nombre de place et selon l'horaire et la durée (en minutes) de disponibilité. 

```JAVA
> look
/* affiche la liste des salles accessibles à l'heure de la commande */
> look -fi
/* affiche la liste des salles informatiques (i) fermées (f) */
> look -hdin 2020-01-28_15:30 210 20
/* affiche la liste des salles informatiques accessibles le 28 Janvier 2020 à 15h30 pour une durée de 210 minutes (3 heures trente) et pour 20 personnes */
> look -a
/* affiche toutes les salles, quelles que soient leurs disponibilité */

/**
 * Format de réponse
 * <nom_salle> - <type_salle> - <localisation> : <etat_actuel>. Nombre de places : <nombre_place>
*/
B0-6 - Salle informatique - Bâtiment Blériot : libre. Nombre de places : 24
B2-20 - Salle de cours - Bâtiment Blériot : ouverte. Nombre de places : 86
```

## Lot 2
### Consultation d'une salle
Il peut-être intéressant de lire l'emploi du temps d'une salle, afin de savoir si elle est souvent réservée ou non. Pour un professeur, cela peut permettre de voir s'il pourra accéder lors de plusieurs séances à la même salle, pour un élève il est surtout important de savoir au bout de combien de temps la salle ne sera plus disponible. Par défaut, on affiche l'emploi du temps de la salle pour la journée, mais on peut spécifier sa recherche.
Cela permet aussi, lorsque l'on est pas passé par la recherche au prélable, d'afficher les informations plus simples d'une salle.

```JAVA
> see B2-20
/* affiche les informations de la salle B2-20 pour la journée en cours */
> see -d B2-20 2020-01-29
/* affiche les informations de la salle B2-20 pour le 29 Janvier 2020 */
> see -d B2-20 2020-01-28:2020-02-01
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

## Lot 3
### Réservation d'une salle
Après consultation, il faut bien sûr passer par l'étape de réservation. Il est nécessaire à cette étape s'être identifié lors d'une étape de connexion basique. Ainsi, on peut imaginer pouvoir désactiver un étudiant qui aurait dégradé une salle par exemple. On pourrait aussi permettre à un professeur ou à un secrétaire de passer outre une réservation en cas de manque de salle, voir d'en supprimer.
Pour réserver, il faut spécifier un horaire et une durée. On demande en plus un commentaire, permettant lors de la consultation d'une salle de connaître l'utilisation qui en est faite.

```JAVA
> book B2-20 2020-01-28_14:00 120
/* réserve la salle B2-20 de 14h à 16h le 28 Janvier 2020, pour un cours de mathématiques. */
> motif : cours de mathématiques
/* après "motif :" qui s'est affiché automatiquement, on renseigne la raison de la réservation */

/** 
 * Format de réponse
 * Réservation effectuée avec succès !
*/
```

## Lot 4
### Connexion à l'application
Pour réserver une salle, il est nécessaire de s'identifier. Pour cela, on renseignera simplement son identifiant suivi de son mot de passe. 
La déconnexion se fait ensuite au moyen d'un simple mot cle.

```JAVA
> connect ii00000 my_password
/* l'utilisateur ii00000 est connecté */
> disconnect
/* l'utilisateur se déconnecte */
```

