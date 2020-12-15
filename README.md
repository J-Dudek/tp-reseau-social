# tp-reseau-social
## How to use swagger :

## Consigne

* Rendu du projet pour le 18 decembre
* Fournir un fichier Insomnia ou Postman
* Mettre des examples permettant de valider les regles de gestions
* Fichier avec les données
* Projet sous forme de repo GIT + m'inviter 
* Un README.md pour le nom ou num du groupe + petite explication de ce que fait le projet
* Authentification via auth0.com


## Sujet

### Réseau social

| Entité       | Fonctionnalité attendu | Régle à respecter |
| ------------ | ---------------------- | ----------------- |
|     Utilisateurs         |  Ajout / Modif / Suppr / Liste / Recherche par nom                      | Une utilisateur a un ensemble d'informations sur son profile                  |
|     Amis         |     Ajout / Modif / Suppr / Liste / Recherche par nom                   |     Pour ajouter un amis il faut une validation de la part de l'amis              |
|         Post     |               Ajout / Modif / Suppr / Liste / Recherche par nom         |   Un message peut etre public                |
| Authentification |             |On peut créer un compte sans etre authentifié, mais le reste des actions doivent être réservées aux utilisateurs authentifiés          |


## Instalation 

1. Cloner le repo github : https://github.com/J-Dudek/tp-reseau-social
2. L'ouvrir comme un nouveau projet maven dans intelliJ
3. Ouvrir le fichier insomnia tp-reseau-social-insomnia.json dans insomnia

## Utilisation

### Affichage de la console h2

url : http://localhost:8088/h2-console/login.jsp?jsessionid=34920503f2033de8da3b756a315a3585
Saved Settings : Generic H2 (Embedded)
Setting name : Generic H2 (Embedded)
Driver Class : org.h2.Driver
JDBC URL: jdbc:h2:mem:testdb
User Name: admin
Password: admin

### Insomnia

1. Récupérer le token avec la requête : Get token - used before any call except login and register
2. Utilisez n'importe quelle requête présente dans le fichier

## Architecture (Appli et BDD)

Max
![](https://github.com/digeridooLeSage/stockage/blob/main/demo%20projet%20social/demo%20authenth%20full.gif)

