# Gestion des Scènes

Cette documentation explique les interactions entre les classes `Scene.java`, `AbstractScene.java`,
`SceneManagerService.java`, ainsi que leur lien avec le fichier de configuration `config.properties` et le
`ConfigurationService`.

## 1. **Présentation du système de gestion des scènes**

Dans l'architecture de l'application, une **scène** représente un état ou une vue spécifique dans le cycle de vie de
l'application. Par exemple, cela pourrait être un menu principal, un niveau de jeu, ou un écran de fin.
La gestion des scènes repose sur **trois composants principaux** :

1. **L'interface `Scene`** : définit les méthodes standard pour toutes les scènes.
2. **La classe abstraite `AbstractScene`** : fournit une base commune et des fonctionnalités par défaut pour toutes les
   implémentations de scènes.
3. **Le `SceneManagerService`** : gère la création, l'activation et la transition entre les scènes.

Le comportement des scènes est configuré via le fichier de configuration (`config.properties`), qui spécifie les scènes
disponibles, la scène par défaut, et d'autres paramètres fonctionnels.

## 2. **Composants principaux**

### 2.1 L'interface `Scene`

**Objectif** : Définir la structure fonctionnelle de toutes les scènes.
Chaque classe qui implémente `Scene` doit fournir :

- La capacité de s'initialiser (`init`) et de se créer (`create`).
- Des méthodes pour mettre à jour l'état (`update`) et libérer des ressources (`dispose`).
- Des mécanismes pour gérer les entités associées à la scène.
- Une méthode pour demander la transition vers une autre scène (`requestChange`).

### 2.2 La classe abstraite `AbstractScene`

**Objectif** : Fournir une implémentation par défaut des fonctionnalités communes à toutes les scènes.
Elle encapsule :

- **Une collection d'entités (`Map<String, Entity>`)** pour organiser et manipuler les objets constituant la scène.
- **Un nom de scène (`name`)** pour identifier la scène dans le fonctionnement global.
- **Une caméra active (`activeCamera`)** pour gérer l'affichage (rendu) de la scène.
- **Une demande de transition (`requestNextScene`)**, qui est transmise au gestionnaire de scènes pour effectuer un
  changement vers une autre scène.

Les développeurs peuvent étendre cette classe pour rapidement créer des scènes personnalisées avec un minimum de code,
en se concentrant uniquement sur les comportements spécifiques.

### 2.3 Le `SceneManagerService`

**Objectif** : Gérer le cycle de vie des scènes et les transitions entre elles.
Le `SceneManagerService` est responsable des opérations suivantes :

- Charger les scènes depuis le fichier de configuration.
- Créer une instance de chaque implémentation de scène.
- Activer une scène en désactivant la précédente (via `dispose`).
- Détecter et appliquer les transitions de scènes demandées via `requestChange`.

## 3. **Lien avec le fichier de configuration**

Le fichier `config.properties` contient des paramètres essentiels pour le gestionnaire de scènes. Les clés suivantes
sont importantes pour la gestion des scènes :

- **`app.scenes.list`** : Spécifie la liste des scènes disponibles dans l'application et leur implémentation. La clé
  suit le format suivant :

``` 
  app.scenes.list=<alias>:<FQCN (fully qualified class name)>
```

Exemple :

``` properties
  app.scenes.list=play:com.snapgames.demo.scenes.PlayScene
```

Ici, une scène est définie avec l'alias `play`, et son implémentation se trouve dans la classe
`com.snapgames.demo.scenes.PlayScene`.

- **`app.scenes.default`** : Définit la scène à charger initialement lorsque l'application démarre.
  Exemple :

``` properties
  app.scenes.default=play
```

Ces paramètres sont utilisés par le `SceneManagerService` pour initialiser et activer les scènes au lancement de
l'application.

## 4. **Lien avec le service de configuration**

Le `ConfigurationService` permet de charger et de gérer dynamiquement les paramètres spécifiés dans le fichier
`config.properties`. Il fournit :

- **Un mécanisme d'extraction des valeurs de configuration** sous la forme de `key-value`.
- Une interface pour d'autres services afin de récupérer ou d'utiliser des paramètres configurés, comme la définition
  des scènes.

### Fonctionnement :

1. **Chargement des propriétés** : Le service charge tous les paramètres définis dans le fichier `config.properties` au
   démarrage de l'application.
2. **Accès aux données de configuration** : Les autres services, comme le `SceneManagerService`, accèdent aux propriétés
   via des clés spécifiques (par ex. `app.scenes.list`).
3. **Interprétation et initialisation** :
    - Les classes associées aux scènes sont instanciées dynamiquement en utilisant **réflexion Java**.
    - La scène par défaut spécifiée dans `app.scenes.default` est activée en premier.

## 5. **Cycle de gestion des scènes**

Voici une vue d’ensemble du cycle de gestion des scènes au sein de l’application :

1. **Chargement des configurations** : Le `ConfigurationService` charge les paramètres de `config.properties`, y compris
   la liste des scènes et la scène par défaut.
2. **Initialisation des scènes** :
    - Le `SceneManagerService` lit la liste des scènes (`app.scenes.list`) et utilise la réflexion pour créer une
      instance de chaque scène.
    - La scène par défaut (`app.scenes.default`) est activée immédiatement.

3. **Transition entre les scènes** :
    - Une scène peut demander un changement via `requestChange`.
    - Le gestionnaire détecte ces demandes et effectue les transitions nécessaires en désactivant l’ancienne scène (
      appel `dispose`) et en activant la nouvelle (appel `init` et `create`).

4. **Mise à jour et rendu** :
    - Le `SceneManagerService` appelle `update` sur la scène active pour mettre à jour son état.
    - La caméra associée à la scène active est utilisée pour le rendu.

## 6. **Points clés pour la configuration et l’extension**

- **Ajout de nouvelles scènes** :
    1. Créez une classe qui implémente l’interface `Scene` ou étend `AbstractScene`.
    2. Ajoutez son alias et son chemin de classe dans `config.properties` sous `app.scenes.list`.

- **Définition de la scène par défaut** :
    - Modifiez `app.scenes.default` pour pointer vers l’alias de la scène que vous souhaitez afficher au démarrage.

- **Adaptation des paramètres de l'application** :
    - Par exemple, modifiez `app.render.window.size` ou toute autre clé pour ajuster les paramètres visuels, physiques
      ou de débogage.

## 7. **Résumé**

Le système de gestion des scènes repose sur une approche modulaire solide, où :

- Les scènes sont des modules autonomes implémentant un comportement spécifique.
- Le fichier de configuration déclare quelles scènes sont disponibles, et leur ordre ou état initial.
- Le `SceneManagerService` orchestre le cycle de vie des scènes (création, activation, mise à jour, désactivation).
- Le `ConfigurationService` centralise et simplifie la gestion des paramètres configurables.

Grâce à cette architecture, il est facile d'ajouter, de modifier ou de supprimer des scènes dans l'application, tout en
maintenant une séparation claire des responsabilités.
