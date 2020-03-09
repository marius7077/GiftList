### Réservation d'une salle

##### Présentation

Il est possible de réserver une salle, afin d'y accéder à l'heure spécifiée et pendant l'intervalle de temps spécifié. On peut choisir de permettre à d'autres de rejoindre la salle (elle est donc ouverte et accessible) ou de ne pas le faire (elle est donc privée).

Par défaut, la réservation est publique, et la salle est accessible à tous durant l'intervalle de la réservation, mais on ne peut plus réserver à cet intervalle.

##### Utilisation

```JAVA
> book B2-20 2020-03-10_09:00;2020-03-10_12:30
//Entamme une procédure de réservation de la salle B2-20 de 9h à 12h30 le 10 Mars 2020

> book -p B2-20 2020-03-10_09:00;2020-03-10_12:30
//Entamme une procédure de réservation privée de la salle B2-20 de 9h à 12h30 le 10 Mars 2020
```

**Si le créneau est disponible, il sera demandé à l'utilisateur de s'authentifier au moyen d'un login et d'un mot de passe et de renseigner le motif de la réservation. Il est possible que son compte utilisateur soit bloqué, dans ce cas la réservation ne peut être effectuée.**

##### Réponse

```
<date_debut_reservation> - <date_fin_reservation> : <privé/public> - <description> (<auteur_reservation_>)
```

Exemple :

*2020-03-10_09:00 - 2020-03-10_12:30 : privé - Cours de Génie Logiciel (Vincent BOUTOUR)*
