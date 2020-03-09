# Book'IT

*Comment obtimiser la réservation de salle au sein de l'IUT ?*

## Contexte

Application développée dans le cadre du cours de Génie Logiciel de L3 MIAGE de l’IUT de Paris. L’équipe est composée de 5 étudiants : 

* 👨🏻‍💻 Thomas Aube - [thomasaube](https://github.com/thomasaube)
* 👨🏻‍💻 Ruben Attal - [ruben-git](https://github.com/ruben-git)
* 👨🏻‍💻 Marius Collin - [marius7077](https://github.com/marius7077)
* 👩🏼‍💻 Marine Meunier - [meuniermarine](https://github.com/ViBiOh)
* 👨🏻‍💻 Simon Weber - [simras98](https://github.com/simras98)

Professeur : 

* Vincent Boutour [ViBiOh](https://github.com/ViBiOh)

## Consignes 

* [x] Utilisation de git 
* [x] Utilisation de markdown
* [x] Rédaction des besoins : user story et critère d’acceptation
* [x] Code testé unitairement 
* [x] Réalisation en Java, JavaScript ou Go
* [x] Respect de l’Inversion of Control (Spring…)
* [x] Utilisation d’un outil de gestion des dépendances (Maven…)
* [x] Mise en place d’une documentation
* [x] Refactorer le code au fur et à mesure du cours 
* [x] Bonus : ajout d’une fonctionnalité

### Pourquoi cette application ? 
Nous sommes 5 étudiants de l’IUT de Paris. Nous avons souhaité développer une application qui nous serait utile dans notre quotidien. En tant qu’étudiants, l’objectif pédagogique de cette application est de nous permettre de mettre en pratique les connaissances acquises lors du cours. 
Nous avons élaboré un cahier des charges qui détaille les fonctionnalités et les aspects métiers de notre application. Pour le consulter, cliquez [ici](https://github.com/marius7077/GiftList/blob/master/Sp%C3%A9cifications.md)


## Le projet
### Situation
À chaque heure, des centaines d'étudiants et de professeurs vont de salle en salle pour suivre leurs différents cours. En plus de cela, on compte aussi un nombre d'étudiant relativement conséquent, qui nécessitent des salles en dehors de leurs cours. On pense notamment aux heures de pause, aux périodes sans cours dans leurs emplois du temps ou bien aux heures en début ou fin de journée durant lesquelles ils restent à l'IUT. 

Dans ces trois cas, les étudiants n'auront pas besoin d'accéder à une salle pour les mêmes raisons. Ils peuvent vouloir se rendre dans une salle de cours pour manger, faire un jeu etc... dans un endroit plus au calme, ils peuvent vouloir accéder à une salle informatique pour travailler ou passer le temps sur un ordinateur, ou ils peuvent vouloir s'isoler en petit groupe afin d'avancer sereinement un projet de groupe par exemple.

On identifie donc trois types de réservations principaux : 
 - La réservation par un professeur : la salle est réservée au professeur et à ses élèves pendant une durée donnée
 - La réservation par un groupe d'étudiant : la salle est réservée au groupe d'étudiant pendant une durée donnée
 - L'ouverture d'une salle de pause : la salle est accessible à tous pendant une durée donnée
 

### Proposition
On propose donc une application permettant la gestion de cette attribution des salles. Pour réserver une salle, il faudra avoir accès à diverses informations, à savoir bien-sûr sa disponibilité, sa capacité d'accueil et son accès ou non à des outils informatiques.

L'application sera basée sur une recherche simple :
 - Recherche d'une salle
 - Consultation des détails de la salle
 - Réservation de la salle
 
 Passons maintenant aux [spécifications](https://github.com/marius7077/GiftList/blob/master/Sp%C3%A9cifications.md)
 ! 
