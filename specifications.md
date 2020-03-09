# BOOK’IT – Spécifications ⚙️🛠

### Acteurs du projet

* 👨🏻‍💻 Thomas Aube : [thomasaube](https://github.com/thomasaube)
* 👨🏻‍💻 Ruben Attal : [ruben-git](https://github.com/ruben-git)
* 👨🏻‍💻 Marius Collin : [marius7077](https://github.com/marius7077)
* 👩🏼‍💻 Marine Meunier : [meuniermarine](https://github.com/ViBiOh)
* 👨🏻‍💻 Simon Weber : [simras98](https://github.com/simras98)


### Projet

L’application Book’IT permet une gestion simplifiée des salles de l’IUT de Paris Descartes. Du côté des utilisateurs, elle permet de consulter les informations et de réserver une salle en fonction de différents paramètres. 
Du côté des gestionnaires des salles de l’IUT, elle permet d’avoir une vision globale de l’utilisation des salles, d’organiser les emplois du temps en fonction des besoins des professeurs. 


### Interaction avec l'outil

Application console, via la console d'un IDE ou via l'interface de commande de son PC et l'utilisation d'un JAR


### Langage

Java


### Outils 

* JUnit : tests de l'application
* Mockito : simulation des comportements des objets pour les tests unitaires
* Maven : gestion des dépendances de l'application
* Spring : intégration continue
* SonarQube : contrôle qualité


### Cas d'utilisation

**Utilisateur** : Un étudiant ou un professeur souhaitant réserver une salle informatique de manière privative dans l'IUT le 10 Mars entre 9h et 12h30, pour un projet de groupe à 12

**Parcours :**
* Consultation des salles informatiques libres et fermées à cette date, comptant au moins 12 places
* Consultation des détails d'une salle pour s'informer sur les éventuelles réservations proches de l'horaire souhaité (afin d'envisager sereinement un dépassement d'horaire)
* Réservation de la salle impliquant une connexion

