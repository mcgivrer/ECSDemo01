# **Demo01App**

Demo01App est une application simple conçue pour illustrer les fonctionnalités d'un framework orienté services.
L'objectif principal de ce projet est de démontrer l'utilisation de services modulaires pour gérer différents aspects,
comme l'affichage, la physique, la gestion des entités et les scènes.

## **Fonctionnalités**

- Architecture orientée services pour une modularité et extensibilité maximales.
- Service de configuration pour charger et gérer les paramètres de l'application.
- Prise en charge de la gestion des entrées utilisateur.
- Exemple intégrant la gestion d'une scène via la classe `PlayScene`.
- Paramètres adaptatifs via un fichier de configuration (`config.properties`) ou des arguments CLI.
- Moteur physique basique avec paramètres personnalisables (gravité, aire de jeu).

## **Installation**

### Prérequis

- **Java 17** ou version plus récente.
- Une IDE comme IntelliJ IDEA ou Maven pour la gestion du projet.
- Un terminal pour exécuter l'application via CLI (si nécessaire).

### Étapes d'installation

1. Clonez le dépôt Git :

``` bash
   git clone https://github.com/your-repo/demo01app.git
```

1. Compilez le projet avec Maven ou directement via votre IDE (IntelliJ IDEA).
2. Assurez-vous que le fichier `config.properties` est présent à la racine du projet pour charger les configurations.

## **Utilisation**

### Lancer l'application

Depuis le terminal, exécutez la commande suivante :

``` bash
java -jar demo01app.jar
```

### Surcharger les paramètres via CLI

Vous pouvez personnaliser les paramètres à l'exécution en passant des arguments sous forme `--key=value` :
Exemple :

``` bash
java -jar demo01app.jar --app.render.window.size=1280x720 --app.debug.level=4
```

## **Documentation**

### Documentation complète du projet

- [Documentation complète](docs/index.md)

### ConfigurationService

Le rôle du **`ConfigurationService`** est crucial pour personnaliser et gérer les paramètres de configuration de
l'application. Consultez la documentation détaillée ici :
➡️ [Documentation du ConfigurationService](docs/03-configuration.md)

## **Configuration**

Le fichier de configuration `config.properties` permet de définir les valeurs par défaut pour des paramètres comme :

- **Rendu graphique** :
    - Taille de la fenêtre (`app.render.window.size`)
    - Fréquence d'images (`app.render.frame.rate`)

- **Moteur physique** :
    - Gravité (`app.physic.world.gravity`)
    - Zone de jeu (`app.physic.world.play.area`)

- **Debug** :
    - Niveau de debug (`app.debug.level`)

Vous pouvez modifier ce fichier pour adapter l'application à vos besoins.

## **Contribution**

Les contributions sont les bienvenues ! Si vous souhaitez ajouter des fonctionnalités ou corriger des bugs, veuillez
forker ce dépôt et ouvrir une pull request.

## **Licence**

Ce projet est sous licence [MIT](LICENSE).

---

Cette version mise à jour intègre un lien direct vers la documentation détaillée de **`ConfigurationService`** (
`03-configuration.md`) tout en expliquant succinctement son rôle. Le lien est placé dans la section **Documentation**,
rendant la navigation intuitive pour les développeurs ou les utilisateurs intéressés.
